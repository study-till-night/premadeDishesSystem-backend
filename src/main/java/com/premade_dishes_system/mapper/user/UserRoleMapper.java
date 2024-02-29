package com.premade_dishes_system.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.premade_dishes_system.pojo.user.UserRole;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author Shu-King
 * @description 针对表【user_account】的数据库操作Mapper
 * @createDate 2023-02-01 20:01:44
 * @Entity com.premade_dishes_system.pojo.UserAccount
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Update("update user_account set role=1,update_time = now() where uid=#{uid}")
    public int updateRoleById(int uid);
}




