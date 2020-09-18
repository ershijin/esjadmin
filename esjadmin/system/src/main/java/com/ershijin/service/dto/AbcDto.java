/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package com.ershijin.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author ershijin
* @date 2020-09-15
**/
@Data
public class AbcDto implements Serializable {

    private Long id;

    /** 名称 */
    private String title;

    /** 分类id */
    private Integer categoryId;

    /** 链接地址 */
    private String link;

    /** 状态：-1,删除；0，待审核；1，正常 */
    private Integer status;

    private Timestamp createTime;

    private Timestamp updateTime;
}