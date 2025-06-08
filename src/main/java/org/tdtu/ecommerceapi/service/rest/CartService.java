package org.tdtu.ecommerceapi.service.rest;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tdtu.ecommerceapi.dto.rest.request.CartQuantityReqDto;
import org.tdtu.ecommerceapi.dto.rest.request.CartReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.CartResDto;
import org.tdtu.ecommerceapi.exception.BadRequestException;
import org.tdtu.ecommerceapi.exception.NotFoundException;
import org.tdtu.ecommerceapi.model.Account;
import org.tdtu.ecommerceapi.model.rest.Cart;
import org.tdtu.ecommerceapi.model.rest.CartItem;
import org.tdtu.ecommerceapi.model.rest.Product;
import org.tdtu.ecommerceapi.repository.CartItemRepository;
import org.tdtu.ecommerceapi.repository.CartRepository;
import org.tdtu.ecommerceapi.service.AccountService;
import org.tdtu.ecommerceapi.service.BaseService;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartService extends BaseService<Cart, CartReqDto, CartResDto, CartRepository> {
    private final MongoTemplate mongoTemplate;
    private final AccountService accountService;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository repository,
                       MappingUtils mappingUtils,
                       MongoTemplate mongoTemplate,
                       AccountService accountService,
                       ProductService productService,
                       CartItemRepository cartItemRepository) {
        super(repository, mappingUtils);
        this.mongoTemplate = mongoTemplate;
        this.accountService = accountService;
        this.productService = productService;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    protected void postprocessModel(Cart model, CartReqDto requestDTO) {
        model.setAccount(accountService.find(
                UUID.fromString(requestDTO.getAccountId()), false));
    }

    @Override
    protected void postprocessUpdateModel(Cart model, CartReqDto requestDTO) {
        model.setAccount(accountService.find(
                UUID.fromString(requestDTO.getAccountId()), false));
    }

    public CartResDto getByToken(UUID accountId) {
        Cart cart = repository.findAllByAccountId(accountId).stream().findFirst()
                .orElseThrow(() -> new NotFoundException(Account.class, "accountId", accountId.toString()));
        return mappingUtils.mapToDTO(cart, CartResDto.class);
    }

    @Transactional
    public CartResDto updateCartItemQuantity(CartQuantityReqDto requestDTO) {
        Cart cart = repository.findById(requestDTO.getCartId())
                .orElseGet(Cart::new);

        Product product = productService.find(requestDTO.getProductId(), false);
        if (product.getQuantity() == 0) throw new BadRequestException("Product is out of stock");

        CartItem cartItem = cart.getId() == null ? null : mongoTemplate.findOne(new Query().addCriteria(
                Criteria.where("product._id").is(product.getId())
                        .and("cart._id").is(cart.getId())), CartItem.class);

        int delta = requestDTO.getDelta();
        if (cartItem == null) {
            if (delta <= 0) throw new BadRequestException("Invalid delta for new CartItem");
            if (product.getQuantity() < delta) throw new BadRequestException("Not enough product");
            // ONLY CASE: Cart is null
            if (cart.getId() == null) cart = repository.save(cart);

            cartItem = new CartItem();
            cartItem.setQuantity(delta);
            cartItem.setProductPrice(product.getPrice());
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
            cartItemRepository.save(cartItem);
        } else {
            int newQuantity = cartItem.getQuantity() + delta;
            if (newQuantity <= 0) {
                cart.getCartItems().remove(cartItem);
                cartItemRepository.delete(cartItem);
            } else {
                if (product.getQuantity() < newQuantity) throw new BadRequestException("Not enough product");
                cartItem.setQuantity(newQuantity);
                cartItemRepository.save(cartItem);
            }
        }

        repository.save(cart);
        return mappingUtils.mapToDTO(find(cart.getId(), false), CartResDto.class);
    }

    public CartResDto refreshCart(UUID cartId) {
        Cart cart = repository.findById(cartId)
                .orElseThrow(() -> new NotFoundException(Cart.class, "id", cartId.toString()));

        List<CartItem> itemsToRemove = new ArrayList<>();
        List<CartItem> itemsToUpdate = new ArrayList<>();

        cart.getCartItems().forEach(cartItem -> {
            Product product = cartItem.getProduct().getId() == null ? null :
                    productService.find(cartItem.getProduct().getId(), true);

            if (product == null || product.getQuantity() == 0) {
                itemsToRemove.add(cartItem);
            } else if (cartItem.getQuantity() > product.getQuantity()) {
                cartItem.setQuantity(product.getQuantity());
                itemsToUpdate.add(cartItem);
            }
        });

        if (!itemsToRemove.isEmpty()) {
            cart.getCartItems().removeAll(itemsToRemove);
            cartItemRepository.deleteAll(itemsToRemove);
        }

        cartItemRepository.saveAll(itemsToUpdate);

        repository.save(cart);

        return mappingUtils.mapToDTO(cart, CartResDto.class);
    }
}