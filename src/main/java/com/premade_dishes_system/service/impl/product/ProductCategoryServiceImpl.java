package com.premade_dishes_system.service.impl.product;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.premade_dishes_system.mapper.product.ProductCategoryMapper;
import com.premade_dishes_system.pojo.product.ProductCategory;
import com.premade_dishes_system.service.product.ProductCategoryService;
import org.springframework.stereotype.Service;

/**
 * @author Shu-King
 * @description 针对表【product_category】的数据库操作Service实现
 * @createDate 2023-02-01 20:01:44
 */
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory>
        implements ProductCategoryService {

}




