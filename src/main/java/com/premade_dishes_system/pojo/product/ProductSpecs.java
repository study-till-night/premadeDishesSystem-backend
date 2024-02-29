package com.premade_dishes_system.pojo.product;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

//商品规格

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductSpecs {
    private Integer pid;
    @TableId(value = "product_spec_id", type = IdType.AUTO)
    private Integer productSpecId;
    private String specName;
    private Double price;
    private Integer inventory;
    private Boolean isEnabled;
    @Version
    private Integer version;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
