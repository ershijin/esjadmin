package com.ershijin.esjadmin.model.entity;

import com.ershijin.esjadmin.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class File extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -1650933924291524865L;

    private String title;

    private String postfix;

    private String path;

    private String type;

    private long size;

    private String hashCode;

    private Long userId;
}
