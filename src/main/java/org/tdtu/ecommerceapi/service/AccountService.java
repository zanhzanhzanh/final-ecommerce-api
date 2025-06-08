package org.tdtu.ecommerceapi.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tdtu.ecommerceapi.dto.admin.request.AccountRequestDTO;
import org.tdtu.ecommerceapi.dto.admin.request.GoogleAuthRequestDTO;
import org.tdtu.ecommerceapi.dto.admin.response.AccountResponseDTO;
import org.tdtu.ecommerceapi.dto.external.response.GoogleAccountResponseDTO;
import org.tdtu.ecommerceapi.enums.Source;
import org.tdtu.ecommerceapi.exception.ConflictException;
import org.tdtu.ecommerceapi.exception.NotFoundException;
import org.tdtu.ecommerceapi.model.Account;
import org.tdtu.ecommerceapi.model.AppGroup;
import org.tdtu.ecommerceapi.model.GoogleAccount;
import org.tdtu.ecommerceapi.repository.AccountRepository;
import org.tdtu.ecommerceapi.repository.GoogleAccountRepository;
import org.tdtu.ecommerceapi.service.external.GoogleService;
//import org.tdtu.ecommerceapi.service.external.SendgridService;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.util.Objects;
import java.util.UUID;

@Service
// TODO: Needing Cache?
//@CacheConfig(cacheNames = "account", keyGenerator = "redisKeyGenerator")
public class AccountService
        extends BaseService<Account, AccountRequestDTO, AccountResponseDTO, AccountRepository> {

    //  @Value("${server.uri}")
    //  private String SERVER_URI;

    private final PasswordEncoder passwordEncoder;
    private final GroupService groupService;
    //    private final SendgridService sendgridService;
    private final GoogleAccountRepository googleAccountRepository;
    private final GoogleService googleService;
    //  private final MetaAccountRepository metaAccountRepository;

    public AccountService(
            AccountRepository repository,
            MappingUtils mappingUtils,
            PasswordEncoder passwordEncoder,
            GroupService groupService,
//            SendgridService sendgridService,
            GoogleService googleService,
            GoogleAccountRepository googleAccountRepository
            //      MetaAccountRepository metaAccountRepository,
    ) {
        super(repository, mappingUtils);
        this.passwordEncoder = passwordEncoder;
        this.groupService = groupService;
//        this.sendgridService = sendgridService;
        this.googleAccountRepository = googleAccountRepository;
        this.googleService = googleService;
        //    this.metaAccountRepository = metaAccountRepository;
    }

//    @Override
//    @Cacheable
//    public ExtAccountResponseDTO getById(UUID id, boolean noException) {
//        Account model =
//                repository.findById(id, AccountEntityGraph.____().group().____.____()).orElse(null);
//        if (Objects.isNull(model) && !noException) {
//            throw new NotFoundException(modelClass, "id", id.toString());
//        }
//        return mappingUtils.mapToDTO(model, ExtAccountResponseDTO.class);
//    }

    @Override
    protected void postprocessModel(Account model, AccountRequestDTO requestDTO) {
        // TODO: This is a temporary solution
        if (requestDTO.getGroupId() == null) {
            try {
                AppGroup group = mappingUtils.mapFromDTO(groupService.getByName("user"), AppGroup.class);
                requestDTO.setGroupId(group.getId());
                // TODO: Fix the Catch
            } catch (NotFoundException e) {
                requestDTO.setGroupId(null);
            }
        }

        model.setGroup(
                mappingUtils.mapFromDTO(
                        groupService.getById(requestDTO.getGroupId(), false), AppGroup.class));
    }

    @Override
    protected void preprocessUpdateModel(Account model, AccountRequestDTO requestDTO) {
        if (repository.findByEmailAndIdNot(requestDTO.getEmail(), model.getId()).isPresent()) {
            throw new ConflictException(Account.class, "email", requestDTO.getEmail());
        }
        if (repository.findByPhoneNumberAndIdNot(requestDTO.getPhoneNumber(), model.getId()).isPresent()) {
            throw new ConflictException(Account.class, "phoneNumber", requestDTO.getPhoneNumber());
        }
    }

    @Override
    protected void postprocessUpdateModel(Account model, AccountRequestDTO requestDTO) {
        // TODO: This is a temporary solution
        if (requestDTO.getGroupId() != null) {
            model.setGroup(groupService.find(requestDTO.getGroupId(), false));
        }
        if (requestDTO.getPassword() != null) {
            model.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }
    }

    @Override
    public AccountResponseDTO create(AccountRequestDTO requestDTO) {
        if (repository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new ConflictException(Account.class, "email", requestDTO.getEmail());
        }
        if (repository.findByPhoneNumber(requestDTO.getPhoneNumber()).isPresent()) {
            throw new ConflictException(Account.class, "phoneNumber", requestDTO.getPhoneNumber());
        }
        requestDTO.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        return super.create(requestDTO);
    }

    public AccountResponseDTO signup(AccountRequestDTO requestDTO) {
        return this.create(requestDTO);
    }

    @Transactional
    public AccountResponseDTO signupWithGoogle(GoogleAuthRequestDTO requestDTO) {
        GoogleIdToken.Payload payload = googleService.verifyToken(requestDTO.getIdToken());
        GoogleAccountResponseDTO googleAccountResponseDTO =
                mappingUtils.mapToDTO(payload, GoogleAccountResponseDTO.class);

        GoogleAccount googleAccount =
                googleAccountRepository.findBySub(googleAccountResponseDTO.getSub()).orElse(null);
        if (googleAccount != null) {
            throw new ConflictException(GoogleAccount.class, "email",
                    googleAccount.getEmail());
        }
        googleAccount = mappingUtils.mapFromDTO(googleAccountResponseDTO,
                GoogleAccount.class);

        Account account =
                repository.findByEmail(googleAccount.getEmail()).orElse(null);
        if (Objects.isNull(account)) {
            account = new Account();
            account.setEmail(googleAccount.getEmail());
            account.setUsername(googleAccount.getName());
            account.setPassword(passwordEncoder.encode(googleAccount.getSub()));
            account.setGoogleAccount(googleAccount);
            // TODO: This is a temporary solution
            try {
                AppGroup group = mappingUtils.mapFromDTO(groupService.getByName("user"), AppGroup.class);
                account.setGroup(group);
                // TODO: Fix the Catch
            } catch (NotFoundException e) {
                account.setGroup(null);
            }

            String token = UUID.randomUUID().toString(); // Need alternative approach
//            sendgridService.sendRegistrationConfirmation(account.getEmail(),
//                    account.getUsername(),
//                    googleAccount.getSub(), Source.GOOGLE.getLabel(), token);
            return super.create(account);
        }
        String token = UUID.randomUUID().toString(); // Need alternative approach
//        sendgridService.sendRegistrationConfirmation(account.getEmail(),
//                account.getUsername(),
//                googleAccount.getSub(), Source.GOOGLE.getLabel(), token);
        account.setGoogleAccount(googleAccount);
        return mappingUtils.mapToDTO(account, AccountResponseDTO.class);
    }

    //  @Transactional
    //  public AccountResponseDTO signupWithMeta(MetaAuthRequestDTO requestDTO) {
    //    MetaService metaService = new MetaService(requestDTO.getAccessToken());
    //    MetaAccountResponseDTO metaAccountResponseDTO = metaService.getProfile();
    //
    //    MetaAccount metaAccount =
    //        metaAccountRepository.findByMetaAccountId(metaAccountResponseDTO.getId()).orElse(null);
    //    if (metaAccount != null) {
    //      throw new ConflictException(MetaAccount.class, "email", metaAccount.getEmail());
    //    }
    //    metaAccount = mappingUtils.mapFromDTO(metaAccountResponseDTO, MetaAccount.class);
    //
    //    Account account = repository.findByEmail(metaAccount.getEmail()).orElse(null);
    //    if (Objects.isNull(account)) {
    //      account = new Account();
    //      account.setEmail(metaAccount.getEmail());
    //      account.setUsername(metaAccount.getFirst_name().concat(metaAccount.getLast_name()));
    //      account.setPassword(passwordEncoder.encode(metaAccount.getMetaAccountId()));
    //      account.setMetaAccount(metaAccount);
    //      String token = UUID.randomUUID().toString(); // Need alternative approach
    //      sendgridService.sendRegistrationConfirmation(
    //          account.getEmail(),
    //          account.getUsername(),
    //          metaAccount.getMetaAccountId(),
    //          Source.META.getLabel(),
    //          token);
    //      return super.create(account);
    //    }
    //    account.setMetaAccount(metaAccount);
    //    return mappingUtils.mapToDTO(account, AccountResponseDTO.class);
    //  }
}
