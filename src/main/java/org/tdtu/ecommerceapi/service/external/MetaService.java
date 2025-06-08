//package org.tdtu.ecommerceapi.service.external;
//
//import org.tdtu.ecommerceapi.dto.external.response.MetaAccountResponseDTO;
//
//public class MetaService extends ApiBinding {
//    private static final String GRAPH_API_BASE_URL = "https://graph.facebook.com";
//
//    public MetaService(String accessToken) {
//        super(accessToken);
//    }
//
//    public MetaAccountResponseDTO getProfile() {
//        return restTemplate.getForObject(
//                GRAPH_API_BASE_URL + "/me?fields=id,last_name,link,email,first_name,name",
//                MetaAccountResponseDTO.class);
//    }
//}
