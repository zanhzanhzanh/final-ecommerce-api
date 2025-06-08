package org.tdtu.ecommerceapi.dto.admin.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;
import org.tdtu.ecommerceapi.dto.BaseDTO;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class GroupRequestDTO implements BaseDTO {
    @NotBlank
    @Indexed(unique = true)
    private String name;

    // @Override
    // public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils)
    // {
    // mapper.addMappings(accountMapping(utils));
    // return mapper;
    // }

    // public PropertyMap<GroupRequestDTO, AppGroup> accountMapping(MappingUtils utils)
    // {
    // return new PropertyMap<GroupRequestDTO, AppGroup>() {
    // @Override
    // protected void configure() {
    //
    // Converter<GroupRequestDTO, List<Account>> mapAccounts = new
    // AbstractConverter<GroupRequestDTO, List<Account>>() {
    // @Override
    // protected List<Account> convert(GroupRequestDTO groupRequestDTO) {
    // return utils.mapListFromDTO(groupRequestDTO.getAccounts(), Account.class);
    // }
    // };
    //
    // using(mapAccounts).map(source, destination.getAccounts());
    // }
    // };
    // }

}
