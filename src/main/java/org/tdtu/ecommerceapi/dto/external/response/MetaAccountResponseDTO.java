//package org.tdtu.ecommerceapi.dto.external.response;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.experimental.Accessors;
//import org.modelmapper.AbstractConverter;
//import org.modelmapper.Converter;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.PropertyMap;
//import org.tdtu.ecommerceapi.dto.BaseDTO;
//import org.tdtu.ecommerceapi.model.MetaAccount;
//import org.tdtu.ecommerceapi.utils.MappingUtils;
//
//import java.util.UUID;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@Accessors(chain = true)
//public class MetaAccountResponseDTO implements BaseDTO {
//    private String id;
//    private String name;
//    private String email;
//    private String first_name;
//    private String last_name;
//
//    @Override
//    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
//        mapper.addMappings(MetaAccountMap(utils));
//        return mapper;
//    }
//
//    public PropertyMap<MetaAccountResponseDTO, MetaAccount> MetaAccountMap(MappingUtils utils) {
//        return new PropertyMap<MetaAccountResponseDTO, MetaAccount>() {
//            @Override
//            protected void configure() {
//                Converter<MetaAccountResponseDTO, String> mapMetaAccountId =
//                        new AbstractConverter<MetaAccountResponseDTO, String>() {
//                            @Override
//                            protected String convert(MetaAccountResponseDTO metaAccountResponseDTO) {
//                                return metaAccountResponseDTO.getId();
//                            }
//                        };
//
//                Converter<MetaAccountResponseDTO, UUID> mapId =
//                        new AbstractConverter<MetaAccountResponseDTO, UUID>() {
//                            @Override
//                            protected UUID convert(MetaAccountResponseDTO metaAccountResponseDTO) {
//                                return null;
//                            }
//                        };
//
//                using(mapId).map(source, destination.getId());
//                using(mapMetaAccountId).map(source, destination.getMetaAccountId());
//            }
//        };
//    }
//}
