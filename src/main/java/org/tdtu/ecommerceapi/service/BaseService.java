package org.tdtu.ecommerceapi.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.api.ApiPageableResponse;
import org.tdtu.ecommerceapi.exception.NotFoundException;
import org.tdtu.ecommerceapi.model.BaseModel;
import org.tdtu.ecommerceapi.repository.BaseRepository;
import org.tdtu.ecommerceapi.utils.HelperUtils;
import org.tdtu.ecommerceapi.utils.MappingUtils;
import org.tdtu.ecommerceapi.utils.PredicateUtils;
import org.tdtu.ecommerceapi.utils.SearchCriteria;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public abstract class BaseService<
        M extends BaseModel,
        REQ extends BaseDTO,
        RES extends BaseDTO,
        R extends BaseRepository<M, UUID>> {

    protected final Class<M> modelClass = (Class<M>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    protected final Class<RES> responseDtoClass = (Class<RES>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[2];
    protected final R repository;
    protected final MappingUtils mappingUtils;

    public RES create(REQ requestDTO) {
        M model = mappingUtils.mapFromDTO(requestDTO, modelClass);
        postprocessModel(model, requestDTO);
        return mappingUtils.mapToDTO(repository.save(model), responseDtoClass);
    }

    protected void postprocessModel(M model, REQ requestDTO) {
    }

    public RES create(M model) {
        return mappingUtils.mapToDTO(repository.save(model), responseDtoClass);
    }

    public RES getById(UUID id, boolean noException) {
        return mappingUtils.mapToDTO(find(id, noException), responseDtoClass);
    }

    public RES updateById(UUID id, REQ requestDTO) {
        M model = find(id, false);
        preprocessUpdateModel(model, requestDTO);
        M payload = mappingUtils.mapFromDTO(requestDTO, modelClass);
        ModelMapper modelMapper = mappingUtils.getSimpleMapper();
        modelMapper.map(payload, model);
        postprocessUpdateModel(model, requestDTO);
        return mappingUtils.mapToDTO(repository.save(model), responseDtoClass);
    }

    protected void preprocessUpdateModel(M model, REQ requestDTO) {
    }

    protected void postprocessUpdateModel(M model, REQ requestDTO) {
    }

    public void deleteById(UUID id) {
        repository.delete(find(id, false));
    }

    public M find(UUID id, boolean noException) {
        M model = repository.findById(id).orElse(null);
        if (Objects.isNull(model) && !noException) {
            throw new NotFoundException(modelClass, "id", id.toString());
        }
        return model;
    }

    public ApiPageableResponse getList(String[] filter, Pageable pageable) {
        List<SearchCriteria> criteria = HelperUtils.formatSearchCriteria(filter);
        BooleanExpression expression = PredicateUtils.getBooleanExpression(criteria, modelClass);
        Page<M> pagingModel = expression != null ? repository.findAll(expression, pageable) : repository.findAll(pageable);
        return formatPagingResponse(pagingModel);
    }

    public ApiPageableResponse formatPagingResponse(Page<M> page) {
        return ApiPageableResponse.builder()
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .data(mappingUtils.mapListToDTO(page.getContent(), responseDtoClass))
                .build();
    }
}
