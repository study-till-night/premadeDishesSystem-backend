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

//商品订单信息

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderProduct {
    @TableId(type = IdType.AUTO)
    private Integer oid;
    private Integer uid;
    private Integer sellerId;
    private Integer aid;
    private Integer pid;
    private Integer specId;
    private Double totalPrice;
    private Integer status;
    private Integer count;
    private Date deliverTime;
    private Date reachTime;
    private Date confirmTime;
    private Boolean isCommented;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE,update = "now()")
    private Date updateTime;

}
