package com.premade_dishes_system.mapper.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.premade_dishes_system.pojo.product.ProductCategory;
import org.springframework.stereotype.Repository;

/**
 * @author Shu-King
 * @description 针对表【product_category】的数据库操作Mapper
 * @createDate 2023-02-01 20:01:44
 * @Entity com.premade_dishes_system.pojo.ProductCategory
 */
@Repository

public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

}




