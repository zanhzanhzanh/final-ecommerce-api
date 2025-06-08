package org.tdtu.ecommerceapi.dto.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.tdtu.ecommerceapi.dto.BaseDTO;

@Data
public class CategoryReqDto implements BaseDTO {
    @NotBlank
    @Size(min = 5, message = "Category name must contain at least 5 characters")
    private String categoryName;
}
