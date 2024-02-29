package com.premade_dishes_system.mapper.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.premade_dishes_system.pojo.product.ProductInfo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Shu-King
 * @description 针对表【product_info】的数据库操作Mapper
 * @createDate 2023-02-01 20:01:44
 * @Entity com.premade_dishes_system.pojo.ProductInfo
 */
@Repository

public interface ProductInfoMapper extends BaseMapper<ProductInfo> {

    @Update("update product_info set picture_path=#{imgPath},update_time = now() where pid=#{pid}")
    public int updateImgById(String imgPath,int pid);

    @Update("update product_info set sell_num=sell_num+#{count},update_time = now() where pid=#{pid}")
    public int updateSalesById(int pid,int count);
}




