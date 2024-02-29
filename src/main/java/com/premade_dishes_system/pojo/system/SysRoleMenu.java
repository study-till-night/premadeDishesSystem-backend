package com.premade_dishes_system.pojo.system;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 前端菜单(SysRoleMenu)表实体类
 *
 * @author makejava
 * @since 2023-02-19 21:32:16
 */
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysRoleMenu {
    @TableId(type = IdType.AUTO)
    private Integer menuId;

    private Integer roleId;

    private String name;

    private String path;

    private String component;

    private String title;

    private String hidden;
}



