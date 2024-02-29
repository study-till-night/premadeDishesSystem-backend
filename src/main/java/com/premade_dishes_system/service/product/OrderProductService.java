package com.premade_dishes_system.service.product;

import com.baomidou.mybatisplus.extension.service.IService;
import com.premade_dishes_system.pojo.product.OrderProduct;

import java.util.List;

/**
 * @author Shu-King
 * @description 针对表【order_product】的数据库操作Service
 * @createDate 2023-02-01 20:01:44
 */
public interface OrderProductService extends IService<OrderProduct> {
    /**
     * 更新订单状态
     *
     * @param oid 订单id
     * @return 是否成功
     */
    boolean updateStatus(int oid);

    /**
     * 查询用户最近订单
     *
     * @param uid 用户id
     * @return 货物列表
     */
    public List<OrderProduct> selectRecent(int uid);

    /**
     * 获取近n天某类商品交易额
     *
     * @param days     近n天
     * @param category 种类
     * @return 列表
     */
    public List<Integer> queryTradeData(int days, int category);

    /**
     *
     * @param days 近n天
     * @param uid 用户id
     * @return 收益总和
     */
    public int queryMyData(int days,int uid);
}
