package com.premade_dishes_system.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.premade_dishes_system.pojo.user.UserDetails;
import org.springframework.stereotype.Repository;

/**
 * @author Shu-King
 * @description 针对表【user_details】的数据库操作Mapper
 * @createDate 2023-02-01 20:01:44
 * @Entity com.premade_dishes_system.pojo.UserDetails
 */
@Repository
public interface UserDetailsMapper extends BaseMapper<UserDetails> {

}




