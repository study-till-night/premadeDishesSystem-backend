package com.premade_dishes_system.pojo.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

//用户角色信息

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("user_account")
public class UserRole {
    @TableId(type = IdType.AUTO)
    private Integer uid;
    private String userName;
    private String password;
    @TableField("role")
    private Integer userRole;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
