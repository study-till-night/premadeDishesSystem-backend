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
 * 长期交易发货记录(DeliveryRecord)表实体类
 *
 * @author makejava
 * @since 2023-04-19 11:52:55
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRecord {

    @TableId(type = IdType.AUTO, value = "record_id")
    private Integer rid;
    //哪个交易单
    private Integer cid;
    //第几次发货
    private Integer times;

    private Boolean isLate;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}

