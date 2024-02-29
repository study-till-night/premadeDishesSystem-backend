package com.premade_dishes_system.mapper.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.premade_dishes_system.pojo.product.ProductRecipe;
import org.springframework.stereotype.Repository;

/**
* @author HP
* @description 针对表【product_recipe(公开制作配方)】的数据库操作Mapper
* @createDate 2023-04-17 21:31:25
* @Entity com.premade_dishes_system.pojo.ProductRecipe
*/
@Repository
public interface ProductRecipeMapper extends BaseMapper<ProductRecipe> {

}




