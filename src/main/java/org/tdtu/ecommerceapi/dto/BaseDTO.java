package org.tdtu.ecommerceapi.dto;

import org.modelmapper.ModelMapper;
import org.tdtu.ecommerceapi.utils.MappingUtils;

public interface BaseDTO {

    default ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        return mapper;
    }
}
