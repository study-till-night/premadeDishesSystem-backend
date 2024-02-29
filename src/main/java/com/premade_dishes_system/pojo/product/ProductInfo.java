package com.premade_dishes_system.pojo.product;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

//商品信息

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductInfo {
    @TableId(type = IdType.AUTO)
    private Integer pid;
    private Integer categoryId;
    private Integer sellerId;
    @TableField("pname")
    private String productName;
    private String picturePath;
    private String abstruct;
    private Integer sellNum;
    private Integer leastNum;
    private String payType;
    private Double freight;
    private Boolean customizable;
    private Boolean isDeleted;
    private Boolean isOpened;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
