package com.premade_dishes_system.service.impl.product;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.premade_dishes_system.pojo.product.ProductRecipe;
import com.premade_dishes_system.service.product.ProductRecipeService;
import com.premade_dishes_system.mapper.product.ProductRecipeMapper;
import org.springframework.stereotype.Service;

/**
* @author HP
* @description 针对表【product_recipe(公开制作配方)】的数据库操作Service实现
* @createDate 2023-04-17 21:31:25
*/
@Service
public class ProductRecipeServiceImpl extends ServiceImpl<ProductRecipeMapper, ProductRecipe>
    implements ProductRecipeService{

}




