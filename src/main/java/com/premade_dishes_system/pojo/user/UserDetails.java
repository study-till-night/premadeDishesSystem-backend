package com.premade_dishes_system.pojo.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

//用户详细信息

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDetails {
    @TableId
    private Integer uid;
    private Integer gender;
    private String mobile;
    private String email;
    private String realName;
    private String nickName;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
