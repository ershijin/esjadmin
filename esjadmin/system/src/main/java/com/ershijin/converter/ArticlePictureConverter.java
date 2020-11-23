package com.ershijin.converter;

import com.ershijin.model.dto.ArticlePictureDTO;
import com.ershijin.model.entity.ArticlePicture;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ArticlePictureConverter extends BaseConverter<ArticlePictureDTO, ArticlePicture> {
}
