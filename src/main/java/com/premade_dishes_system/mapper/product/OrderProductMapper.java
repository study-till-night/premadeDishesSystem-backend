package com.premade_dishes_system.mapper.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.premade_dishes_system.pojo.product.OrderProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Shu-King
 * @description 针对表【order_product】的数据库操作Mapper
 * @createDate 2023-02-01 20:01:44
 * @Entity com.premade_dishes_system.pojo.OrderProduct
 */
@Repository
public interface OrderProductMapper extends BaseMapper<OrderProduct> {

    @Update("update order_product set status=status+1,update_time = now() where oid=#{oid} ")
    int updateStatusById(int oid);

    @Select("select * from order_product where uid=#{uid} order by create_time limit 10")
    public List<OrderProduct> selectRecentById(int uid);

    @Select("select sum(total_price)  from order_product where DATEDIFF(create_time,NOW()) <= -(#{param1}-1) AND DATEDIFF(create_time,NOW()) > -#{param1} "
            + "and pid in (select pid from product_info where category_id = #{param2})")
    public Integer selectByDayAndSort(int day, int category);

    /**
     * 根据天数和用户查收益
     *
     * @param day
     * @param uid
     * @return
     */
    @Select("select sum(total_price)  from order_product where DATEDIFF(create_time,NOW()) >= -#{param1} "
            + "and seller_id = #{param2}")
    public Integer selectByDayAndUser( int day, int uid);

    /**
     * 根据用户查收益
     *
     * @param uid
     * @return
     */
    @Select("select sum(total_price)  from order_product where seller_id = #{uid}")
    public Integer selectByUser(int uid);

    /**
     * 根据天数和用户查数量
     * @param uid
     * @return
     */
    @Select("select count(*) from order_product where DATEDIFF(create_time,NOW()) >= -#{param1} "
            + "and TRIM(seller_id) = #{param2}")
    public int selectCountByDay(int day, int uid);
}




