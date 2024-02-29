package com.premade_dishes_system.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.premade_dishes_system.pojo.user.ShopCar;

/**
 * @author Shu-King
 * @description 针对表【shop_car】的数据库操作Service
 * @createDate 2023-02-01 20:01:44
 */
public interface ShopCarService extends IService<ShopCar> {
     /**
      * 修改选中状态
      * @return
      */
     boolean updateStatus(int id);

     /**
      * 全选
      * @return
      */
     boolean updateAllStatus(int id);
}
