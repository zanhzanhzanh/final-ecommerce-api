package org.tdtu.ecommerceapi.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiPageableResponse {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private boolean isLast;
    private boolean isFirst;
    private List<?> data;
}
