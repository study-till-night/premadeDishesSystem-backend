package com.premade_dishes_system.service.product;

import com.baomidou.mybatisplus.extension.service.IService;
import com.premade_dishes_system.pojo.product.ProductSpecs;

/**
 * @author Shu-King
 * @description 针对表【product_specs】的数据库操作Service
 * @createDate 2023-02-01 20:01:44
 */
public interface ProductSpecsService extends IService<ProductSpecs> {
    /**
     * 修改启用状态
     * @param sid
     * @return
     */
    public boolean UpdateEnabled(int sid);

    /**
     * 检测规格名称是否重复
     * @param spec
     * @return
     */
    public boolean checkNameUnique(ProductSpecs spec);

    /**
     * 更新库存
     * @param sid
     * @param count
     * @return
     */
    public boolean updateStocks(int sid,int count);
}
