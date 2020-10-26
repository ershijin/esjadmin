package com.ershijin.converter;

import com.ershijin.model.dto.UserDTO;
import com.ershijin.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter extends BaseConverter<UserDTO, User> {
}