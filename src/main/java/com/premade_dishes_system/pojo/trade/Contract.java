package com.premade_dishes_system.pojo.trade;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 长期批发单(Contract)表实体类
 *
 * @author Shu-King
 * @since 2023-02-02 13:47:46
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contract {
    @TableId(type = IdType.AUTO)
    private Integer contractId;

    private Integer uid1; //需求方

    private Integer uid2; //供应商
    @TableField("product_id")
    private Integer pid;

    private Integer validityTime;

    private Integer frequency;

    private Integer status;

    private Double price;

    private Integer productNum;

    private String productName;

    private String custom; //个性化配比

    private Integer refuseReason;

    private Integer deliverTimes;

    private Date startTime;

    private Integer customGrade;

    private String customComment;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}