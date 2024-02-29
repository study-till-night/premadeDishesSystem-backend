package com.premade_dishes_system.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.premade_dishes_system.mapper.user.UserDetailsMapper;
import com.premade_dishes_system.mapper.user.UserRoleMapper;
import com.premade_dishes_system.pojo.user.UserDetails;
import com.premade_dishes_system.pojo.user.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
public class UserRoleMapperTest {
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserDetailsMapper userDetailsMapper;
    @Test
    public void testQuery() {
        List<UserRole> userRoles = userRoleMapper.selectList(null);
        if (userRoles.isEmpty()) System.out.println("null");
        else {
            userRoles.forEach(System.out::println);
            log.info("query completed");
        }
    }

    @Test
    public void testQuery2() {
        List<UserRole> userRoles = userRoleMapper.selectBatchIds(Arrays.asList(1, 2));
        System.out.println(userRoles);
    }

    @Test
    public void testInsert() {
        for (int i = 0; i < 10; i++) {
            UserRole userRole = new UserRole();
            userRole.setUserName("18013383582");
            userRole.setUserRole(0);
//            userRoleMapper.insert(userRole);
        }

        testQuery2();
    }

    @Test
    @Transactional()
    public void testUpdate() {
        UserRole userRole = new UserRole();
        userRole.setUserName("18013383582");
        userRole.setUserRole(0);
        userRole.setUid(3);
//        int res = userRoleMapper.updateById(userRole);
//        System.out.println(res);
    }

    @Test
    public void testPageQuery() {
        Page<UserRole> userRolePage = new Page<>(2, 5);
        userRoleMapper.selectPage(userRolePage, null);
        userRolePage.getRecords().forEach(System.out::println);
        System.out.println(userRolePage.getCurrent());
    }

    @Test
    public void insertDetail(){
//        int insert = userDetailsMapper.insert(new UserDetails());
//        System.out.println(insert);
    }
}