package com.premade_dishes_system.pojo.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

//购物车

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShopCar {
    @TableId(type = IdType.AUTO)
    private Integer cid;
    private Integer pid;
    private Integer uid;
    private Integer count;
    private Boolean isChosen;
    private Integer specId;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
