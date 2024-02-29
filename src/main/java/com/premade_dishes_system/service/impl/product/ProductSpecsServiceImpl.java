package com.premade_dishes_system.service.impl.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.premade_dishes_system.mapper.product.ProductSpecsMapper;
import com.premade_dishes_system.pojo.product.ProductSpecs;
import com.premade_dishes_system.service.product.ProductSpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Shu-King
 * @description 针对表【product_specs】的数据库操作Service实现
 * @createDate 2023-02-01 20:01:44
 */
@Service
public class ProductSpecsServiceImpl extends ServiceImpl<ProductSpecsMapper, ProductSpecs>
        implements ProductSpecsService {
    @Autowired
    private ProductSpecsMapper specsMapper;

    @Override
    public boolean UpdateEnabled(int sid) {
        return specsMapper.UpdateEnabledById(sid) != 0;
    }

    @Override
    public boolean checkNameUnique(ProductSpecs spec) {
        Integer pid = spec.getPid();
        String name = spec.getSpecName();
        int count = this.count(new QueryWrapper<ProductSpecs>()
                .eq("pid", pid)
                .eq("spec_name", name));
        return count == 0;
    }

    @Override
    public boolean updateStocks(int sid, int count) {
        return specsMapper.UpdateStockById(sid, count) != 0;
    }
}




