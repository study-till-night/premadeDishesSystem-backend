package com.premade_dishes_system.service.impl.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.premade_dishes_system.mapper.user.UserOrgMapper;
import com.premade_dishes_system.pojo.user.UserOrg;
import com.premade_dishes_system.service.user.UserOrgService;
import org.springframework.stereotype.Service;

@Service
public class UserOrgServiceImpl extends ServiceImpl<UserOrgMapper, UserOrg> implements UserOrgService {

}
