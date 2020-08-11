package com.ershijin.esjadmin.model.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class IdForm {
    @NotNull(message = "ID不能为空")
    @Min(value = 1, message = "参数错误")
    Long id;
}
