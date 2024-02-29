package com.premade_dishes_system.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.premade_dishes_system.pojo.system.SysRoleMenu;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Shu-King
 * @description 针对表【sys_role_menu(前端菜单)】的数据库操作Mapper
 * @createDate 2023-02-02 17:25:46
 * @Entity com.premade_dishes_system.pojo.SysRoleMenu
 */
@Repository
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    @Select("select name from sys_role_menu where role_id=#{role}")
    public List<String> selectByRole(int role);
}




