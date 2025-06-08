package org.tdtu.ecommerceapi.dto.external.response;

import org.tdtu.ecommerceapi.dto.BaseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class S3PresignedResponseDTO implements BaseDTO {
    private String url;
    private String key;
    private Instant expiration;
    private Map<String, String> signedHeaders;
}
