package com.ershijin.converter;

import com.ershijin.model.dto.ArticleDTO;
import com.ershijin.model.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleConverter extends BaseConverter<ArticleDTO, Article> {
}
