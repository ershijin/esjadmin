package com.ershijin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ershijin.converter.ArticleConverter;
import com.ershijin.converter.ArticlePictureConverter;
import com.ershijin.dao.ArticleMapper;
import com.ershijin.model.PageResult;
import com.ershijin.model.dto.ArticleDTO;
import com.ershijin.model.entity.Article;
import com.ershijin.model.entity.ArticlePicture;
import com.ershijin.model.query.ArticleQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticlePictureService pictureService;

    @Autowired
    private ArticleConverter converter;

    @Autowired
    private ArticlePictureConverter pictureConverter;

    public PageResult list(ArticleQuery query, IPage<Article> page) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(query.getCategoryId() != null, "category_id", query.getCategoryId());
        queryWrapper.and(!StringUtils.isEmpty(query.getKeyword()), i -> {
            i.like("title", query.getKeyword())
                    .or().like("summary", query.getKeyword());
        });

        queryWrapper.orderByDesc("id");

        IPage<Article> result = articleMapper.selectPage(page, queryWrapper);
        return new PageResult(result.getTotal(), converter.toDto(result.getRecords()));
    }

    public ArticleDTO get(long id) {
        Article article = articleMapper.selectById(id);
        ArticleDTO articleDTO = converter.toDto(article);

        // 查询图片墙
        List<ArticlePicture> pictures = pictureService.list(
                Wrappers.<ArticlePicture>lambdaQuery()
                        .eq(ArticlePicture::getArticleId, article.getId())
                        .select(ArticlePicture::getId, ArticlePicture::getUrl, ArticlePicture::getSortId)
                .orderByAsc(ArticlePicture::getSortId)
        );
        articleDTO.setPictures(pictureConverter.toDto(pictures));

        return articleDTO;
    }

    @Transactional
    public void save(ArticleDTO articleDTO) {
        // 保存文章信息
        Article article = converter.toEntity(articleDTO);
        articleMapper.insert(article);
        // 保存图片墙
        for (int i = 0; i < articleDTO.getPictures().size(); i++) {
            articleDTO.getPictures().get(i).setSortId(i);
            articleDTO.getPictures().get(i).setArticleId(article.getId());
        }
        List<ArticlePicture> pictures = pictureConverter.toEntity(articleDTO.getPictures());
        pictureService.saveBatch(pictures);
    }

    @Transactional
    public void update(ArticleDTO articleDTO) {
        Article article = converter.toEntity(articleDTO);
        articleMapper.updateById(article);

        // 更新图片墙
        for (int i = 0; i < articleDTO.getPictures().size(); i++) {
            articleDTO.getPictures().get(i).setSortId(i);
            articleDTO.getPictures().get(i).setArticleId(articleDTO.getId());
        }
        // 删除图片
        pictureService.remove(Wrappers.<ArticlePicture>lambdaQuery().eq(ArticlePicture::getArticleId, article.getId()));
        // 重新插入图片
        List<ArticlePicture> pictures = pictureConverter.toEntity(articleDTO.getPictures());
        pictureService.saveBatch(pictures);
    }

    public void remove(Long id) {
        articleMapper.deleteById(id);
    }
}
