package com.premade_dishes_system.mapper.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.premade_dishes_system.pojo.product.ProductSpecs;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author Shu-King
 * @description 针对表【product_specs】的数据库操作Mapper
 * @createDate 2023-02-01 20:01:44
 * @Entity com.premade_dishes_system.pojo.ProductSpecs
 */
@Repository
public interface ProductSpecsMapper extends BaseMapper<ProductSpecs> {
    @Update("update product_specs set is_enabled=is_enabled^1 where product_spec_id=#{id}")
    public int UpdateEnabledById(int id);

    @Update("update product_specs set inventory=inventory-#{count} where product_spec_id=#{sid} and inventory >= #{count}")
    public int UpdateStockById(int sid,int count);
}




