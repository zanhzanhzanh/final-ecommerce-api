package org.tdtu.ecommerceapi.controller.webapp;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tdtu.ecommerceapi.enums.PromotionType;
import org.tdtu.ecommerceapi.enums.ProportionType;
import org.tdtu.ecommerceapi.model.Account;
import org.tdtu.ecommerceapi.model.AppGroup;
import org.tdtu.ecommerceapi.model.rest.*;
import org.tdtu.ecommerceapi.repository.*;

import java.time.OffsetDateTime;
import java.util.*;

@Tag(name = "Webapp.InitData")
@RestController
@RequestMapping("/v1/webapp/data")
@RequiredArgsConstructor
@Slf4j
public class InitDataController {
    private final MongoOperations mongoOperations;
    private final BCryptPasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final AccountRepository accountRepository;
    private final GoogleAccountRepository googleAccountRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PromotionRepository promotionRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @PostMapping("/delete-data")
    public ResponseEntity<?> deleteData() {
        groupRepository.deleteAll();
        accountRepository.deleteAll();
        googleAccountRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        addressRepository.deleteAll();
        cartRepository.deleteAll();
        cartItemRepository.deleteAll();
        promotionRepository.deleteAll();
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
        log.info("All data has been deleted successfully.");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/init-data")
    public ResponseEntity<?> initData() {
//        groupRepository.deleteAll();
//        accountRepository.deleteAll();
//        productRepository.deleteAll();
//        categoryRepository.deleteAll();
//        productRepository.deleteAll();
//        addressRepository.deleteAll();
//        cartRepository.deleteAll();
//        cartItemRepository.deleteAll();
//        promotionRepository.deleteAll();
//        orderRepository.deleteAll();
//        orderItemRepository.deleteAll();

        // -- Group --
        AppGroup adminGroup = groupRepository.findByName("admin").orElse(new AppGroup());
        if (adminGroup.getId() == null) {
            adminGroup.setName("admin");
            adminGroup = groupRepository.save(adminGroup);
        }

        AppGroup sellerGroup = groupRepository.findByName("seller").orElse(new AppGroup());
        if (sellerGroup.getId() == null) {
            sellerGroup.setName("seller");
            sellerGroup = groupRepository.save(sellerGroup);
        }

        AppGroup userGroup = groupRepository.findByName("user").orElse(new AppGroup());
        if (userGroup.getId() == null) {
            userGroup.setName("user");
            userGroup = groupRepository.save(userGroup);
        }

        // -- Account --
        Account admin = accountRepository.findByEmail("admin@gmail.com").orElse(new Account());
        if (admin.getId() == null) {
            admin.setUsername("Alex");
            admin.setEmail("admin@gmail.com");
            admin.setPhoneNumber("0909090909");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setGroup(adminGroup);
            admin = accountRepository.save(admin);
        }

        Account user = accountRepository.findByEmail("user@gmail.com").orElse(new Account());
        if (user.getId() == null) {
            user.setUsername("Rose");
            user.setEmail("user@gmail.com");
            user.setPhoneNumber("0808080808");
            user.setPassword(passwordEncoder.encode("password"));
            user.setGroup(userGroup);
            user = accountRepository.save(user);
        }

        // Business data
        // -- Category --
        Query electronicsQuery = new Query(Criteria.where("categoryName").is("Electronics"));
        Category electronics = mongoOperations.findOne(electronicsQuery, Category.class);
        if (electronics == null) {
            electronics = new Category();
            electronics.setCategoryName("Electronics");
            electronics = categoryRepository.save(electronics);
        }

        Query booksQuery = new Query(Criteria.where("categoryName").is("Books"));
        Category books = mongoOperations.findOne(booksQuery, Category.class);
        if (books == null) {
            books = new Category();
            books.setCategoryName("Books");
            books = categoryRepository.save(books);
        }

        // -- Product --
        // Create products for Electronics category
        Query phoneQuery = new Query(Criteria.where("productName").is("Smartphone"));
        Product phone = mongoOperations.findOne(phoneQuery, Product.class);
        if (phone == null) {
            phone = new Product();
            phone.setId(null);
            phone.setProductName("Smartphone");
            phone.setImage("smartphone.jpg");
            phone.setDescription("Latest smartphone");
            phone.setQuantity(100);
            phone.setPrice(2000000);
            phone.setCategory(electronics);
            phone = productRepository.save(phone);
        }

        Query laptopQuery = new Query(Criteria.where("productName").is("Laptop"));
        Product laptop = mongoOperations.findOne(laptopQuery, Product.class);
        if (laptop == null) {
            laptop = new Product();
            laptop.setId(null);
            laptop.setProductName("Laptop");
            laptop.setImage("laptop.jpg");
            laptop.setDescription("High-performance laptop");
            laptop.setQuantity(50);
            laptop.setPrice(5600000);
            laptop.setCategory(electronics);
            laptop = productRepository.save(laptop);
        }

        Query tabletQuery = new Query(Criteria.where("productName").is("Tablet"));
        Product tablet = mongoOperations.findOne(tabletQuery, Product.class);
        if (tablet == null) {
            tablet = new Product();
            tablet.setId(null);
            tablet.setProductName("Tablet");
            tablet.setImage("tablet.jpg");
            tablet.setDescription("Portable tablet");
            tablet.setQuantity(75);
            tablet.setPrice(1560000);
            tablet.setCategory(electronics);
            tablet = productRepository.save(tablet);
        }

        // Create product for Books category
        Query bookQuery = new Query(Criteria.where("productName").is("Programming Book"));
        Product book = mongoOperations.findOne(bookQuery, Product.class);
        if (book == null) {
            book = new Product();
            book.setId(null);
            book.setProductName("Programming Book");
            book.setImage("book.jpg");
            book.setDescription("Learn programming");
            book.setQuantity(200);
            book.setPrice(65000);
            book.setCategory(books);
            book = productRepository.save(book);
        }

        // -- Addresses --
        // Create addresses for account
        if (admin != null) {
            Address adminAddress1 = new Address();
            adminAddress1.setStreet("1600 Pennsylvania Avenue NW");
            adminAddress1.setBuildingName("The White House");
            adminAddress1.setCity("Washington");
            adminAddress1.setCountry("United States");
            adminAddress1.setState("DC");
            adminAddress1.setPincode("20500");
            adminAddress1.setAccount(admin);

            Address adminAddress2 = new Address();
            adminAddress2.setStreet("10 Downing Street");
            adminAddress2.setBuildingName("Prime Minister's Office");
            adminAddress2.setCity("London");
            adminAddress2.setCountry("United Kingdom");
            adminAddress2.setState("England");
            adminAddress2.setPincode("SW1A 2AA");
            adminAddress2.setAccount(admin);

            mongoOperations.save(adminAddress1);
            mongoOperations.save(adminAddress2);
        }


        Address userAddress1 = new Address();
        if (user != null) {
            userAddress1.setStreet("221B Baker Street");
            userAddress1.setBuildingName("Sherlock Holmes Museum");
            userAddress1.setCity("London");
            userAddress1.setCountry("United Kingdom");
            userAddress1.setState("England");
            userAddress1.setPincode("NW1 6XE");
            userAddress1.setAccount(user);

            Address userAddress2 = new Address();
            userAddress2.setStreet("1 Infinite Loop");
            userAddress2.setBuildingName("Apple Headquarters");
            userAddress2.setCity("Cupertino");
            userAddress2.setCountry("United States");
            userAddress2.setState("California");
            userAddress2.setPincode("95014");
            userAddress2.setAccount(user);

            userAddress1 = mongoOperations.save(userAddress1);
            userAddress2 = mongoOperations.save(userAddress2);
        }

        // -- Carts and CartItems --
        Cart cart = new Cart();
        if (user != null) {
            cart.setAccount(user);
            cart = cartRepository.save(cart);

            List<Product> randomProducts = new ArrayList<>();
            randomProducts.add(laptop);
            randomProducts.add(tablet);
            randomProducts.add(book);

            for (Product product : randomProducts) {
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setCart(cart);
                cartItem.setProductPrice(product.getPrice());
                cartItem.setQuantity(new Random().nextInt(9) + 3);
                cartItemRepository.save(cartItem);
            }
        }

        // -- Promotions --
        // Create ALL_PRODUCTS promotion
        Promotion allProductsPromotion = new Promotion();
        allProductsPromotion.setPromotionName("10% Off All Products");
        allProductsPromotion.setPromotionCode("ALL10");
        allProductsPromotion.setDescription("Get 10% off on all products.");
        allProductsPromotion.setStartDate(OffsetDateTime.now());
        allProductsPromotion.setEndDate(OffsetDateTime.now().plusDays(30));
        allProductsPromotion.setDiscountAmount(10.0);
        allProductsPromotion.setPromotionType(PromotionType.ALL_PRODUCTS);
        allProductsPromotion.setProportionType(ProportionType.PERCENTAGE);
        allProductsPromotion.setMinOrderValue(0.0);
        allProductsPromotion = promotionRepository.save(allProductsPromotion);

        Promotion allProductsPromotion20 = new Promotion();
        allProductsPromotion20.setPromotionName("20% Off All Products");
        allProductsPromotion20.setPromotionCode("ALL20");
        allProductsPromotion20.setDescription("Get 20% off on all products.");
        allProductsPromotion20.setStartDate(OffsetDateTime.now());
        allProductsPromotion20.setEndDate(OffsetDateTime.now().plusDays(30));
        allProductsPromotion20.setDiscountAmount(20.0);
        allProductsPromotion20.setPromotionType(PromotionType.ALL_PRODUCTS);
        allProductsPromotion20.setProportionType(ProportionType.PERCENTAGE);
        allProductsPromotion20.setMinOrderValue(0.0);
        allProductsPromotion20 = promotionRepository.save(allProductsPromotion20);

        // Create SPECIFIC_PRODUCTS promotion
        Promotion specificProductsPromotion = new Promotion();
        specificProductsPromotion.setPromotionName("20% Off Specific Products");
        specificProductsPromotion.setPromotionCode("SPECIFIC20");
        specificProductsPromotion.setDescription("Get 20% off on selected products.");
        specificProductsPromotion.setStartDate(OffsetDateTime.now());
        specificProductsPromotion.setEndDate(OffsetDateTime.now().plusDays(30));
        specificProductsPromotion.setDiscountAmount(20.0);
        specificProductsPromotion.setPromotionType(PromotionType.SPECIFIC_PRODUCTS);
        specificProductsPromotion.setProportionType(ProportionType.PERCENTAGE);
        specificProductsPromotion.setMinOrderValue(0.0);
        specificProductsPromotion.setProductIds(new HashSet<>(Arrays.asList(laptop.getId(), book.getId())));
        specificProductsPromotion = promotionRepository.save(specificProductsPromotion);

        // Create ORDER_TOTAL promotion
        Promotion orderTotalPromotion = new Promotion();
        orderTotalPromotion.setPromotionName("1000 Off Orders Over 500");
        orderTotalPromotion.setPromotionCode("ORDER1000");
        orderTotalPromotion.setDescription("Get 1000 off on orders over 500.");
        orderTotalPromotion.setStartDate(OffsetDateTime.now());
        orderTotalPromotion.setEndDate(OffsetDateTime.now().plusDays(30));
        orderTotalPromotion.setDiscountAmount(1000.0);
        orderTotalPromotion.setPromotionType(PromotionType.ORDER_TOTAL);
        orderTotalPromotion.setProportionType(ProportionType.ABSOLUTE);
        orderTotalPromotion.setMinOrderValue(500.0);
        orderTotalPromotion = promotionRepository.save(orderTotalPromotion);

        // -- Logging --
        log.info("Cart ID: " + "\"" + cart.getId().toString() + "\"");
        log.info("User/Address ID: " + "\"" + userAddress1.getId().toString() + "\"");
        log.info("Promotion ALL_PRODUCTS 10: " + "\"" + allProductsPromotion.getId().toString() + "\"");
        log.info("Promotion ALL_PRODUCTS 20: " + "\"" + allProductsPromotion20.getId().toString() + "\"");
        log.info("Promotion SPECIFIC_PRODUCTS: " + "\"" + specificProductsPromotion.getId().toString() + "\"");
        log.info("Promotion ORDER_TOTAL: " + "\"" + orderTotalPromotion.getId().toString() + "\"");
        // -- Logging --
        log.info("{\"cartId\": \"" + cart.getId().toString() + "\", " +
                "\"addressId\": \"" + userAddress1.getId().toString() + "\", " +
                "\"promotionIds\": [\"" + allProductsPromotion.getId().toString() + "\", " +
                "\"" + allProductsPromotion20.getId().toString() + "\", " +
                "\"" + specificProductsPromotion.getId().toString() + "\", " +
                "\"" + orderTotalPromotion.getId().toString() + "\"], " +
                "\"shipCOD\": false}");

        return ResponseEntity.ok().build();
    }
}
