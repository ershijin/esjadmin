package com.ershijin.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ershijin.dao.ColumnInfoMapper;
import com.ershijin.exception.ApiException;
import com.ershijin.model.PageResult;
import com.ershijin.model.dto.TableInfo;
import com.ershijin.model.entity.ColumnInfo;
import com.ershijin.model.entity.GenConfig;
import com.ershijin.util.FileUtils;
import com.ershijin.utils.GenUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl extends ServiceImpl<ColumnInfoMapper,ColumnInfo> implements GeneratorService {
    private static final Logger log = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ColumnInfoMapper columnInfoMapper;

    public Object getTables() {
        // 使用预编译防止sql注入
        String sql = "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables " +
                "where table_schema = (select database()) " +
                "order by create_time desc";
        return jdbcTemplate.queryForList(sql);
    }

    public PageResult getTables(String name, int[] startEnd) {
        // 使用预编译防止sql注入
        String sql = "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables " +
                "where table_schema = (select database()) " +
                "and table_name like ? order by " +
                "table_name asc limit ?,?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, StringUtils.isNotBlank(name) ? ("%" + name + "%") : "%", startEnd[0], startEnd[1]);
        List<TableInfo> tableInfos = new ArrayList<>();
        while (rowSet.next()) {
            tableInfos.add(
                    new TableInfo(
                        rowSet.getString(1),
                        rowSet.getString(2),
                        rowSet.getString(3),
                        rowSet.getString(4),
                        ObjectUtil.isNotEmpty(rowSet.getString(5)) ? rowSet.getString(5) : "-"
                    )
            );
        }
        long total = jdbcTemplate.queryForObject("SELECT COUNT(*) from information_schema.tables where " +
                "table_schema = (select database()) and table_name like ?", Long.class, StringUtils.isNotBlank(name) ?
                ("%" + name + "%") : "%");
        return new PageResult(total, tableInfos);
    }

    public List<ColumnInfo> getColumns(String tableName) {
        QueryWrapper<ColumnInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("table_name", tableName)
                .orderByAsc("table_name");
        List<ColumnInfo> columnInfos = columnInfoMapper.selectList(queryWrapper);
        if (!CollectionUtil.isNotEmpty(columnInfos)) {
            columnInfos = query(tableName);
            saveOrUpdateBatch(columnInfos);
        }
        return columnInfos;
    }

    public List<ColumnInfo> query(String tableName) {
        // 使用预编译防止sql注入
        String sql = "select column_name, is_nullable, data_type, column_comment, column_key, extra from information_schema.columns " +
                "where table_name = ? and table_schema = (select database()) order by ordinal_position";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, tableName);

        List<ColumnInfo> columnInfos = new ArrayList<>();

        while (rowSet.next()) {
            columnInfos.add(
                    new ColumnInfo(
                            tableName,
                            rowSet.getString("column_name"),
                            "NO".equals(rowSet.getString("is_nullable")),
                            rowSet.getString("data_type"),
                            rowSet.getString("column_comment"),
                            rowSet.getString("column_key"),
                            rowSet.getString("extra")
                    )
            );
        }

        return columnInfos;
    }

    public void sync(List<ColumnInfo> columnInfos, List<ColumnInfo> columnInfoList) {
        // 第一种情况，数据库类字段改变或者新增字段
        for (ColumnInfo columnInfo : columnInfoList) {
            // 根据字段名称查找
            List<ColumnInfo> columns = columnInfos.stream().filter(c -> c.getColumnName().equals(columnInfo.getColumnName())).collect(Collectors.toList());
            // 如果能找到，就修改部分可能被字段
            if (CollectionUtil.isNotEmpty(columns)) {
                ColumnInfo column = columns.get(0);
                column.setColumnType(columnInfo.getColumnType());
                column.setExtra(columnInfo.getExtra());
                column.setKeyType(columnInfo.getKeyType());
                if (StringUtils.isBlank(column.getRemark())) {
                    column.setRemark(columnInfo.getRemark());
                }
                saveOrUpdate(column);
            } else {
                // 如果找不到，则保存新字段信息
                saveOrUpdate(columnInfo);
            }
        }
        // 第二种情况，数据库字段删除了
        for (ColumnInfo columnInfo : columnInfos) {
            // 根据字段名称查找
            List<ColumnInfo> columns = columnInfoList.stream().filter(c -> c.getColumnName().equals(columnInfo.getColumnName())).collect(Collectors.toList());
            // 如果找不到，就代表字段被删除了，则需要删除该字段
            if (CollectionUtil.isEmpty(columns)) {
                columnInfoMapper.deleteById(columnInfo);
            }
        }
    }

    public void save(List<ColumnInfo> columnInfos) {
        saveOrUpdateBatch(columnInfos);
    }

    public void generator(GenConfig genConfig, List<ColumnInfo> columns) {
        if (genConfig.getId() == null) {
            throw new ApiException("请先配置生成器");
        }
        try {
            GenUtil.generatorCode(columns, genConfig);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ApiException("生成失败，请手动处理已生成的文件");
        }
    }

    public ResponseEntity<Object> preview(GenConfig genConfig, List<ColumnInfo> columns) {
        if (genConfig.getId() == null) {
            throw new ApiException("请先配置生成器");
        }
        List<Map<String, Object>> genList = GenUtil.preview(columns, genConfig);
        return new ResponseEntity<>(genList, HttpStatus.OK);
    }

    public void download(GenConfig genConfig, List<ColumnInfo> columns, HttpServletRequest request, HttpServletResponse response) {
        if (genConfig.getId() == null) {
            throw new ApiException("请先配置生成器");
        }
        try {
            File file = new File(GenUtil.download(columns, genConfig));
            String zipPath = file.getPath() + ".zip";
            ZipUtil.zip(file.getPath(), zipPath);
            FileUtils.downloadFile(request, response, new File(zipPath), true);
        } catch (IOException e) {
            throw new ApiException("打包失败");
        }
    }
}
