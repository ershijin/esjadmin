package ${package}.converter;

import com.ershijin.converter.BaseConverter;
import ${package}.model.dto.${className}DTO;
import ${package}.model.entity.${className};
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author ${author}
* @date ${date}
**/
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ${className}Converter extends BaseConverter<${className}DTO, ${className}> {
}
