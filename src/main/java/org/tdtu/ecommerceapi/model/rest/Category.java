package org.tdtu.ecommerceapi.model.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.tdtu.ecommerceapi.model.BaseModel;
import org.tdtu.ecommerceapi.utils.annotation.CascadeDelete;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseModel {
    @Indexed(unique = true)
    @NotBlank
    @Size(min = 5, message = "Category name must contain at least 5 characters")
    private String categoryName;

    @DocumentReference(lazy = true, lookup = "{ 'category' : ?#{#self._id} }")
    @ReadOnlyProperty
    @CascadeDelete
    private List<Product> products = new ArrayList<>();
}
