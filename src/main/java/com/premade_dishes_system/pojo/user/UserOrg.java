package com.premade_dishes_system.pojo.user;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学校/企业认证信息(UserOrg)表实体类
 *
 * @author Shu-King
 * @since 2023-02-03 12:33:58
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrg {
    @TableId
    private Integer uid;

    private String orgName;

    private String orgAddress;

    private Integer orgType;

    private String licenseId;

    private String chargerName;

    private String chargerMobile;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}

