package com.ershijin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ershijin.exception.NotFoundException;
import com.ershijin.model.PageResult;
import com.ershijin.util.FileUtils;
import com.ershijin.util.QueryHelp;
import com.ershijin.model.entity.Demo;

import com.ershijin.exception.ApiException;
import com.ershijin.constant.ResultCode;
import com.ershijin.model.dto.DemoDTO;
import com.ershijin.model.query.DemoQuery;
import com.ershijin.converter.DemoConverter;
import com.ershijin.dao.DemoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @description 服务实现
* @author ershijin
* @date 2020-10-26
**/
@Service
public class DemoService {
    
    @Autowired
    private DemoMapper demoMapper;

    @Autowired
    private DemoConverter demoConverter;

    /**
    * 查询数据分页
    * @param query 条件
    * @param page 分页参数
    * @return Map<String,Object>
    */
    public PageResult list(DemoQuery query, IPage<Demo> page){
        QueryWrapper<Demo> queryWrapper = QueryHelp.buildQueryWrapper(query);
        queryWrapper.orderByDesc("id");
        IPage<Demo> result = demoMapper.selectPage(page,  queryWrapper);
        return new PageResult(result.getTotal(), demoConverter.toDto(result.getRecords()));
    }

    /**
    * 查询所有数据不分页
    * @param query 条件参数
    * @return List<DemoDTO>
    */
    public List<DemoDTO> list(DemoQuery query){
        QueryWrapper<Demo> queryWrapper = QueryHelp.buildQueryWrapper(query);
        queryWrapper.orderByDesc("id");
        return demoConverter.toDto(demoMapper.selectList(queryWrapper));
    }

    /**
       * 根据ID查询
       * @param id ID
       * @return DemoVO
       */
    @Transactional
    public DemoDTO get(Long id) {
        Demo demo = demoMapper.selectById(id);
        if (demo == null) {
            throw new NotFoundException("Demo 不存在");
        }
        return demoConverter.toDto(demo);
    }

    /**
    * 创建
    * @param resources /
    * @return DemoDTO
    */
    @Transactional(rollbackFor = Exception.class)
    public DemoDTO save(Demo resources) {
        if(demoMapper.selectOne(Wrappers.<Demo>lambdaQuery().eq(Demo::getUni, resources.getUni())) !=
        null){
            throw new ApiException("Demo 列 uni 值 " + resources.getUni() + " 已经存在", ResultCode.ENTITY_EXISTS);
        }
        demoMapper.insert(resources);
        return demoConverter.toDto(resources);
    }

    /**
    * 编辑
    * @param demo /
    */
    @Transactional(rollbackFor = Exception.class)
    public void update(Demo demo) {
        demoMapper.updateById(demo);
    }

    /**
    * 多选删除
    * @param ids /
    */
    public void removeAll(Long[] ids) {
        for (Long id : ids) {
            demoMapper.deleteById(id);
        }
    }

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    public void download(List<DemoDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DemoDTO demo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" id",  demo.getId());
            map.put("名称", demo.getTitle());
            map.put("分类id", demo.getCategoryId());
            map.put("链接地址", demo.getLink());
            map.put("状态：-1,删除；0，待审核；1，正常", demo.getStatus());
            map.put(" createTime",  demo.getCreateTime());
            map.put(" updateTime",  demo.getUpdateTime());
            map.put(" uni",  demo.getUni());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}