package com.premade_dishes_system.service.product;

import com.baomidou.mybatisplus.extension.service.IService;
import com.premade_dishes_system.pojo.product.ProductInfo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Shu-King
 * @description 针对表【product_info】的数据库操作Service
 * @createDate 2023-02-01 20:01:44
 */
public interface ProductInfoService extends IService<ProductInfo> {
    /**
     * 检测货物名称是否重复
     * @param prod 货物对象
     * @return 是否唯一
     */
    public boolean checkNameUnique( ProductInfo prod);

    /**
     * 修改图片路径
     * @param imgPath 图片在本地的存储路径
     * @return 是否成功
     */
    public boolean updateImg(String imgPath,int pid);


    /**
     * 更新销量
     * @param pid 货物id
     * @param count 单词销售数量
     * @return 是否成功
     */
    public boolean updateSales(int pid,int count);

    /**
     * 推荐算法
     * @param uid 用户id
     * @return 推荐货物列表
     */
    public List<ProductInfo> recommend(int uid,int num);
}
