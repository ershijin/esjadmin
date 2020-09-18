package com.ershijin.service;

import com.ershijin.exception.NotFoundException;
import com.ershijin.util.MyBeanUtils;
import com.ershijin.model.PageResult;
import com.ershijin.util.FileUtils;
import com.ershijin.model.entity.Demo;
import com.ershijin.model.vo.DemoVO;
import com.ershijin.model.query.DemoQuery;
import com.ershijin.dao.DemoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @description 服务实现
* @author ershijin
* @date 2020-09-17
**/
@Service
public class DemoService {
    
    @Autowired
    private DemoMapper demoMapper;

    /**
    * 查询数据分页
    * @param query 条件
    * @param page 分页参数
    * @return Map<String,Object>
    */
    public PageResult list(DemoQuery query, Page<Demo> page){
        QueryWrapper<Demo> queryWrapper = new QueryWrapper<>();
        IPage<Demo> result = demoMapper.selectPage(page, queryWrapper);
        return new PageResult(result.getTotal(), MyBeanUtils.convert(result.getRecords(), DemoVO.class));
    }

    /**
    * 查询所有数据不分页
    * @param query 条件参数
    * @return List<DemoVO>
    */
    public List<DemoVO> list(DemoQuery query){
        QueryWrapper<Demo> queryWrapper = new QueryWrapper<>();
        return MyBeanUtils.convert(demoMapper.selectList(queryWrapper), DemoVO.class);
    }

    /**
       * 根据ID查询
       * @param id ID
       * @return DemoVO
       */
    @Transactional
    public DemoVO findById(Long id) {
        Demo demo = demoMapper.selectById(id);
        if (demo == null) {
            throw new NotFoundException("Demo 不存在");
        }
        DemoVO demoVO = new DemoVO();
        return (DemoVO) MyBeanUtils.convert(demo, DemoVO.class);
    }

    /**
    * 创建
    * @param resources /
    * @return DemoVO
    */
    @Transactional(rollbackFor = Exception.class)
    public Demo save(Demo resources) {
        demoMapper.insert(resources);
        return resources;
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
    public void download(List<DemoVO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DemoVO demo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", demo.getTitle());
            map.put("分类id", demo.getCategoryId());
            map.put("链接地址", demo.getLink());
            map.put("状态：-1,删除；0，待审核；1，正常", demo.getStatus());
            map.put(" createTime",  demo.getCreateTime());
            map.put(" updateTime",  demo.getUpdateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}