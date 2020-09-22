package com.ershijin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.dao.DictDetailMapper;
import com.ershijin.model.PageResult;
import com.ershijin.model.entity.DictDetail;
import com.ershijin.model.query.DictDetailQuery;
import com.ershijin.util.StringUtils;
import com.ershijin.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "dict")
public class DictDetailService {

    @Autowired
    private DictDetailMapper dictDetailMapper;

    public PageResult queryAll(DictDetailQuery query, Page page) {
        QueryWrapper<DictDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(query.getDictName() != null, "name", query.getDictName());
        queryWrapper.eq(query.getDictId() != null, "dict_id", query.getDictId());
        IPage<DictDetail> result = dictDetailMapper.selectPage(page, queryWrapper);

        return new PageResult(result.getTotal(), result.getRecords());
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(DictDetail dictDetail) {
        dictDetailMapper.insert(dictDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(DictDetail resources) {
        DictDetail dictDetail = dictDetailMapper.selectById(resources.getId());
        if (dictDetail == null) dictDetail = new DictDetail();
        ValidationUtil.isNull( dictDetail.getId(),"DictDetail","id",resources.getId());
        resources.setId(dictDetail.getId());
        dictDetailMapper.updateById(resources);
    }

    @Cacheable(key = "'name:' + #p0")
    public List<DictDetail> getDictByName(String name) {
        QueryWrapper<DictDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);

        return dictDetailMapper.selectList(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        dictDetailMapper.deleteById(id);
    }

}