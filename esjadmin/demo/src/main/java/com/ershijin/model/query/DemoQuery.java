package com.ershijin.model.query;

import com.ershijin.annotation.Query;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
* @author ershijin
* @date 2020-10-15
**/
@Data
public class DemoQuery{

    /** 精确 */
    @Query(tableField = "title")
    private String title;

    /** 精确 */
    @Query(tableField = "category_id")
    private Integer categoryId;

    /** 模糊 */
    @Query(type = Query.Type.LIKE, tableField = "link")
    private String link;

    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN, tableField = "create_time")
    private List<LocalDateTime> createTime;
}