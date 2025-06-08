package org.tdtu.ecommerceapi.dto.admin.response.extend;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.tdtu.ecommerceapi.dto.admin.response.AccountResponseDTO;
import org.tdtu.ecommerceapi.dto.admin.response.GroupResponseDTO;
import org.tdtu.ecommerceapi.model.AppGroup;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ExtGroupResponseDTO extends GroupResponseDTO {

    private List<AccountResponseDTO> accounts = new LinkedList<>();

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.addMappings(groupMap(utils));
        return mapper;
    }

    public PropertyMap<AppGroup, ExtGroupResponseDTO> groupMap(MappingUtils utils) {
        return new PropertyMap<>() {
            @Override
            protected void configure() {

                Converter<AppGroup, List<AccountResponseDTO>> mapAccounts =
                        new AbstractConverter<>() {
                            @Override
                            protected List<AccountResponseDTO> convert(AppGroup group) {
                                return utils.mapListToDTO(group.getAccounts(), AccountResponseDTO.class);
                            }
                        };

                using(mapAccounts).map(source, destination.getAccounts());
            }
        };
    }
}
