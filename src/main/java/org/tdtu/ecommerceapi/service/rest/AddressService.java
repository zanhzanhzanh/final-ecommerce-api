package org.tdtu.ecommerceapi.service.rest;

import org.springframework.stereotype.Service;
import org.tdtu.ecommerceapi.dto.rest.request.AddressReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.AddressResDto;
import org.tdtu.ecommerceapi.model.rest.Address;
import org.tdtu.ecommerceapi.repository.AddressRepository;
import org.tdtu.ecommerceapi.service.BaseService;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService extends BaseService<Address, AddressReqDto, AddressResDto, AddressRepository> {
    public AddressService(AddressRepository repository, MappingUtils mappingUtils) {
        super(repository, mappingUtils);
    }

    public List<AddressResDto> findByAccount(UUID accountId) {
        return mappingUtils.mapListToDTO(
                repository.findByAccount(accountId), AddressResDto.class);
    }
}
