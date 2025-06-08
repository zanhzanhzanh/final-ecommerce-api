package org.tdtu.ecommerceapi.service;

import org.springframework.stereotype.Service;
import org.tdtu.ecommerceapi.dto.admin.request.GroupRequestDTO;
import org.tdtu.ecommerceapi.dto.admin.response.GroupResponseDTO;
import org.tdtu.ecommerceapi.dto.admin.response.extend.ExtGroupResponseDTO;
import org.tdtu.ecommerceapi.exception.NotFoundException;
import org.tdtu.ecommerceapi.model.AppGroup;
import org.tdtu.ecommerceapi.repository.GroupRepository;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class GroupService
        extends BaseService<AppGroup, GroupRequestDTO, GroupResponseDTO, GroupRepository> {

    public GroupService(GroupRepository repository, MappingUtils mappingUtils) {
        super(repository, mappingUtils);
    }

    public GroupResponseDTO getByName(String name) {
        return mappingUtils.mapToDTO(repository.findByName(name).orElseThrow(
                () -> new NotFoundException(modelClass, "name", name)
        ), GroupResponseDTO.class);
    }

    @Override
    public ExtGroupResponseDTO getById(UUID id, boolean noException) {
        AppGroup model = repository.findById(id).orElse(null);
        if (Objects.isNull(model) && !noException) {
            throw new NotFoundException(modelClass, "id", id.toString());
        }

        return mappingUtils.mapToDTO(model, ExtGroupResponseDTO.class);
    }
}
