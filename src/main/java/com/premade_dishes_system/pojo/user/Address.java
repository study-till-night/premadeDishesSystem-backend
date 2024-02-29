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

//收货/发货地址信息

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address {
    @TableId(type = IdType.AUTO)
    private Integer addressId;
    private Integer uid;
    private Boolean isDefault;
    private String userName;
    private String mobile;
    private String province;
    private String city;
    private String district;
    @TableField("detailed_address")
    private String detail;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
