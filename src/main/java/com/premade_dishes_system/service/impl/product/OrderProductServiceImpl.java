package com.premade_dishes_system.service.impl.product;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.premade_dishes_system.mapper.product.OrderProductMapper;
import com.premade_dishes_system.pojo.product.OrderProduct;
import com.premade_dishes_system.service.product.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shu-King
 * @description 针对表【order_product】的数据库操作Service实现
 * @createDate 2023-02-01 20:01:44
 */
@Service
public class OrderProductServiceImpl extends ServiceImpl<OrderProductMapper, OrderProduct>
        implements OrderProductService {
    @Autowired
    private OrderProductMapper orderMapper;

    @Override
    public boolean updateStatus(int oid) {
        return orderMapper.updateStatusById(oid) != 0;
    }

    @Override
    public List<OrderProduct> selectRecent(int uid) {
        return orderMapper.selectRecentById(uid);
    }

    @Override
    public List<Integer> queryTradeData(int days, int category) {
        ArrayList<Integer> trade = new ArrayList<>();
        for (int i = 1; i <= days; i++) {
            Integer sum = orderMapper.selectByDayAndSort(i, category);
            trade.add(0, sum == null ? 0 : sum);
        }
        return trade;
    }

    @Override
    public int queryMyData(int days, int uid) {
        if (days == -1) {
            Integer income = orderMapper.selectByUser(uid);
            return income == null ? 0 : income;
        }
        Integer income = orderMapper.selectByDayAndUser(days, uid);
        return income == null ? 0 : income;
    }
}




