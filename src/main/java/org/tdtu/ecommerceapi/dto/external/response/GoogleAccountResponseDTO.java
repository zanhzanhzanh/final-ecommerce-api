package org.tdtu.ecommerceapi.dto.external.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.model.GoogleAccount;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class GoogleAccountResponseDTO implements BaseDTO {
    private String sub;
    private String name;
    private String email;
    private String email_verified;
    private String family_name;
    private String given_name;
    private String picture;

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.addMappings(GoogleAccountMap(utils));
        return mapper;
    }

    public PropertyMap<GoogleAccountResponseDTO, GoogleAccount> GoogleAccountMap(MappingUtils utils) {
        return new PropertyMap<>() {
            @Override
            protected void configure() {
                Converter<GoogleAccountResponseDTO, String> mapGoogleAccountId =
                        new AbstractConverter<>() {
                            @Override
                            protected String convert(GoogleAccountResponseDTO googleAccountResponseDTO) {
                                return googleAccountResponseDTO.getSub();
                            }
                        };

                Converter<GoogleAccountResponseDTO, UUID> mapId =
                        new AbstractConverter<>() {
                            @Override
                            protected UUID convert(GoogleAccountResponseDTO googleAccountResponseDTO) {
                                return null;
                            }
                        };

                using(mapId).map(source, destination.getId());
                using(mapGoogleAccountId).map(source, destination.getSub());
            }
        };
    }
}
