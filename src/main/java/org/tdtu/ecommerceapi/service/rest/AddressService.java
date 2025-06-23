package org.tdtu.ecommerceapi.service.rest;

import org.springframework.stereotype.Service;
import org.tdtu.ecommerceapi.dto.rest.request.AddressReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.AddressResDto;
import org.tdtu.ecommerceapi.model.rest.Address;
import org.tdtu.ecommerceapi.repository.AddressRepository;
import org.tdtu.ecommerceapi.service.AccountService;
import org.tdtu.ecommerceapi.service.BaseService;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService extends BaseService<Address, AddressReqDto, AddressResDto, AddressRepository> {
    private final AccountService accountService;
    public AddressService(AddressRepository repository,
                          MappingUtils mappingUtils,
                          AccountService accountService) {
        super(repository, mappingUtils);
        this.accountService = accountService;
    }

    @Override
    protected void postprocessModel(Address model, AddressReqDto requestDTO) {
        model.setAccount(accountService.find(requestDTO.getAccountId(), false));
    }

    @Override
    protected void postprocessUpdateModel(Address model, AddressReqDto requestDTO) {
        if(requestDTO.getAccountId() != null) {
            model.setAccount(accountService.find(requestDTO.getAccountId(), false));
        }
    }

    public List<AddressResDto> findByAccount(UUID accountId) {
        return mappingUtils.mapListToDTO(
                repository.findByAccount(accountId), AddressResDto.class);
    }
}
