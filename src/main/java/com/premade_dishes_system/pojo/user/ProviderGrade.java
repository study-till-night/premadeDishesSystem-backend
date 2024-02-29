package com.premade_dishes_system.pojo.user;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商数值评估(ProviderGrade)表实体类
 *
 * @author makejava
 * @since 2023-04-17 21:34:23
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProviderGrade {
    @TableId
    private Integer uid;
    //诚信度 未按时发货次数
    private Integer credit;
    //公开制作工艺次数
    private Integer transparency;
    //个性化配比受到好评次数
    private Integer creativity;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}

