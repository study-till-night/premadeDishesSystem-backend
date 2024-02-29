package com.premade_dishes_system.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.premade_dishes_system.pojo.user.UserOrg;
import org.springframework.stereotype.Repository;

/**
* @author Shu-King
* @description 针对表【user_org(学校/企业认证信息)】的数据库操作Mapper
* @createDate 2023-02-03 12:33:08
* @Entity com.premade_dishes_system.pojo.UserOrg
*/
@Repository
public interface UserOrgMapper extends BaseMapper<UserOrg> {

}




