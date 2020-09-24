package com.ershijin.converter;

import com.ershijin.converter.BaseConverter;
import com.ershijin.model.dto.DemoDTO;
import com.ershijin.model.entity.Demo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author ershijin
* @date 2020-09-24
**/
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DemoConverter extends BaseConverter<DemoDTO, Demo> {
}
