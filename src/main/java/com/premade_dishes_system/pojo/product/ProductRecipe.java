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
import java.io.Serializable;

/**
 * 公开制作配方(ProductRecipe)实体类
 *
 * @author makejava
 * @since 2023-04-17 21:35:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductRecipe {
    @TableId(type = IdType.AUTO)
    private Integer rid;

    private Integer pid;
    /**
     * 由哪家供应商提出
     */
    private Integer sellerId;
    /**
     * 调料
     */
    private String seasoning;
    /**
     * 制作步骤
     */
    private String steps;
    /**
     * 注意事项
     */
    private String tips;
    /**
     * 食材
     */
    private String ingredients;
    /**
     * 点赞数
     */
    private Integer love;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}

