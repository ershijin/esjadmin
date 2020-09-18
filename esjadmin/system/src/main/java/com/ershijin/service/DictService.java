package com.ershijin.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.dao.DictMapper;
import com.ershijin.model.PageResult;
import com.ershijin.model.entity.Dict;
import com.ershijin.model.entity.DictDetail;
import com.ershijin.model.query.DictQuery;
import com.ershijin.util.FileUtils;
import com.ershijin.util.StringUtils;
import com.ershijin.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@CacheConfig(cacheNames = "dict")
public class DictService {
    @Autowired
    private DictMapper dictMapper;

    public PageResult queryAll(DictQuery query, Page page){

        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(query.getKeyword())) {
            queryWrapper.like("name", query.getKeyword())
                    .or()
                    .like("description", query.getKeyword());
        }

        IPage<Dict> result = dictMapper.selectPage(page, queryWrapper);

        return new PageResult(result.getTotal(), result.getRecords());
    }

    public List<Dict> queryAll(DictQuery query) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(query.getKeyword())) {
            queryWrapper.like("name", query.getKeyword())
                    .or()
                    .like("description", query.getKeyword());
        }
        return dictMapper.selectList(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(Dict dict) {
        dictMapper.insert(dict);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Dict dict) {
        Dict oldDict = dictMapper.selectById(dict.getId());
        if (oldDict == null) {
            oldDict = new Dict();
        }
        ValidationUtil.isNull( oldDict.getId(),"Dict","id",dict.getId());
        dict.setId(oldDict.getId());
        dictMapper.updateById(dict);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        dictMapper.deleteBatchIds(ids);
    }

    public void download(List<Dict> dicts, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Dict dict : dicts) {
            if(CollectionUtil.isNotEmpty(dict.getDictDetails())){
                for (DictDetail dictDetail : dict.getDictDetails()) {
                    Map<String,Object> map = new LinkedHashMap<>();
                    map.put("字典名称", dict.getName());
                    map.put("字典描述", dict.getDescription());
                    map.put("字典标签", dictDetail.getLabel());
                    map.put("字典值", dictDetail.getValue());
                    map.put("创建日期", dictDetail.getCreateTime());
                    list.add(map);
                }
            } else {
                Map<String,Object> map = new LinkedHashMap<>();
                map.put("字典名称", dict.getName());
                map.put("字典描述", dict.getDescription());
                map.put("字典标签", null);
                map.put("字典值", null);
                map.put("创建日期", dict.getCreateTime());
                list.add(map);
            }
        }
        FileUtils.downloadExcel(list, response);
    }
}