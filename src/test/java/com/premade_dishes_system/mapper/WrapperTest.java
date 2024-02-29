package com.premade_dishes_system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.premade_dishes_system.mapper.product.OrderProductMapper;
import com.premade_dishes_system.mapper.user.UserRoleMapper;
import com.premade_dishes_system.pojo.user.UserDetails;
import com.premade_dishes_system.pojo.user.UserRole;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
public class WrapperTest {
    @Autowired
    private UserRoleMapper roleMapper;
    @Autowired
    private OrderProductMapper orderMapper;

    @Test
    public void testQuery1() {
        QueryWrapper<UserRole> userWrapper = new QueryWrapper<>();
        userWrapper.eq("role", 1);
        roleMapper.selectList(userWrapper).forEach(System.out::println);
    }

    @Test
    public void testQuery2() {
        QueryWrapper<UserDetails> userWrapper = new QueryWrapper<>();
        userWrapper
                .like("nick_name", "shu-king")
                .between("uid", 5, 12);
//        roleMapper.selectList(userWrapper).forEach(System.out::println);
    }

    @Test
    public void testUpdate1() {
        UpdateWrapper<UserDetails> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .notLike("nick_name", "zhu-king")
                .notBetween("uid", 6, 12);
        UserRole userRole = new UserRole();
//        roleMapper.update(userRole, updateWrapper);
//        roleMapper.selectList(null).forEach(System.out::println);
    }

    @Test
    public void test111() {
        for (int i = 1; i <= 7; i++) {
            Integer v = orderMapper.selectByDayAndSort(i, 4);
            System.out.println(v);
        }
    }

    @SneakyThrows
    @Test
    public void testDate() {

        Calendar calendar = Calendar.getInstance();
//        calendar.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date1 = formatter.parse("2023-4-18");
        Date date2 = formatter.parse("2023-4-19");
        System.out.println(formatter.format(date1).equals(formatter.format(date2)));
    }

}
