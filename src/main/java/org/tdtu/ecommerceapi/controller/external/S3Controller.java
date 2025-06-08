package org.tdtu.ecommerceapi.controller.external;

import org.tdtu.ecommerceapi.controller.SecuredRestController;
import org.tdtu.ecommerceapi.dto.external.response.S3PresignedResponseDTO;
import org.tdtu.ecommerceapi.enums.ContentDisposition;
import org.tdtu.ecommerceapi.service.external.S3Service;
import org.tdtu.ecommerceapi.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;

import java.util.Map;

@Tag(name = "S3")
@RestController
@RequestMapping("/s3/presigned-url")
@RequiredArgsConstructor
public class S3Controller implements SecuredRestController {

    private final S3Service s3Service;

    /**
     * @apiNote postman cannot download, use browser to receive correct behavior
     * @apiNote to add signedHeaders into header request
     */
    @GetMapping
    public ResponseEntity<S3PresignedResponseDTO> getPresignedGetUrl(
            @RequestParam String key, @RequestParam ContentDisposition contentDisposition) {
        return ResponseEntity.ok(s3Service.getPresignedUrl(key, contentDisposition));
    }

    /**
     * @param params user-defined metadata
     * @apiNote select binary tab in Body section to upload file while using with postman
     * @apiNote to add signedHeaders into header request
     */
    @PutMapping
    public ResponseEntity<S3PresignedResponseDTO> getPresignedPutUrl(
            @Parameter(schema = @Schema(pattern = RegexUtils.SIMPLE_FILENAME_PATTERN)) @RequestParam
            String filename,
            @RequestParam ObjectCannedACL acl,
            @RequestBody Map<String, String> params) {
        return ResponseEntity.ok(s3Service.getPresignedUrl(filename, acl, params));
    }
}
