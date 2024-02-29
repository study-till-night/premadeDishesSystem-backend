package com.premade_dishes_system.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.premade_dishes_system.pojo.user.ShopCar;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author Shu-King
 * @description 针对表【shop_car】的数据库操作Mapper
 * @createDate 2023-02-01 20:01:44
 * @Entity com.premade_dishes_system.pojo.ShopCar
 */

@Repository
public interface ShopCarMapper extends BaseMapper<ShopCar> {
    @Update("update shop_car set is_chosen=is_chosen^1 where cid=#{id}")
    public int updateById(int id);

    /**
     * 选中所有
     * @param id
     * @return
     */
    @Update("update shop_car set is_chosen=1,update_time = now() where uid=#{id} and is_chosen=0")
    public int updateByChosen1(int id);

    /**
     * 取消所有
     * @param id
     * @return
     */
    @Update("update shop_car set is_chosen=0,update_time = now() where uid=#{id}")
    public int updateByChosen2(int id);
}




