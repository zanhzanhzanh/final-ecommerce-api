package org.tdtu.ecommerceapi.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.mapping.MappingException;
import org.springframework.stereotype.Component;
import org.tdtu.ecommerceapi.dto.BaseDTO;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MappingUtils {

    public ModelMapper getSimpleMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
                // Not for MongoDB
//                .setPropertyCondition(
//                        context -> !(context.getSource() instanceof PersistentCollection) // exclude
//                        // properties of
//                        // type
//                        // PersistentCollection
//                        // (List,
//                        // Set,..) (@OnetoMany)
//                );
        return modelMapper;
    }

    public <DTO extends BaseDTO> ModelMapper getMapper(Class<DTO> target) {
        ModelMapper modelMapper = getSimpleMapper();
        return updateMapping(modelMapper, target);
    }

    public <DTO extends BaseDTO> ModelMapper updateMapping(ModelMapper mapper, Class<DTO> dto) {
        try {
            Constructor<DTO> constructor = dto.getConstructor();
            DTO instance = constructor.newInstance();
            return instance.updateModelMapper(mapper, this);
        } catch (NoSuchMethodException
                 | InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException e) {
            throw new MappingException(dto.getName());
        }
    }

    public <T, DTO extends BaseDTO> List<DTO> mapListToDTO(List<T> source, Class<DTO> target) {
        ModelMapper modelMapper = getMapper(target);
        return source.stream()
                .map(element -> modelMapper.map(element, target))
                .collect(Collectors.toList());
    }

    public <T, DTO extends BaseDTO> List<T> mapListFromDTO(List<DTO> source, Class<T> target) {
        ModelMapper modelMapper = getMapper(source.get(0).getClass());
        return source.stream()
                .map(element -> modelMapper.map(element, target))
                .collect(Collectors.toList());
    }

    public <T, DTO extends BaseDTO> DTO mapToDTO(T source, Class<DTO> target) {
        ModelMapper modelMapper = getMapper(target);
        return modelMapper.map(source, target);
    }

    public <T, DTO extends BaseDTO> T mapFromDTO(DTO source, Class<T> target) {
        ModelMapper modelMapper = getMapper(source.getClass());
        return modelMapper.map(source, target);
    }

    public <T, DTO extends BaseDTO> void mapFields(T source, DTO target) {
        ModelMapper modelMapper = getMapper(target.getClass());
        modelMapper.map(source, target);
    }
}
