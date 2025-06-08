package org.tdtu.ecommerceapi.converter;

import org.springframework.core.convert.converter.Converter;
import org.tdtu.ecommerceapi.enums.ContentDisposition;
import org.tdtu.ecommerceapi.exception.BadRequestException;

public class ContentDispositionConverter implements Converter<String, ContentDisposition> {
    @Override
    public ContentDisposition convert(String source) {
        try {
            return ContentDisposition.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid Content-Disposition");
        }
    }
}
