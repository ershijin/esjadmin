package com.ershijin.esjadmin.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MenuMeta implements Serializable {

    private String title;

    private String icon;

    private boolean noCache;

}
