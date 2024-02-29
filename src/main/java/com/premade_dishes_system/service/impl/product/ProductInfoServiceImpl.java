package com.premade_dishes_system.service.impl.product;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.premade_dishes_system.mapper.product.ProductInfoMapper;
import com.premade_dishes_system.pojo.product.OrderProduct;
import com.premade_dishes_system.pojo.product.ProductInfo;
import com.premade_dishes_system.pojo.product.ProductSpecs;
import com.premade_dishes_system.service.product.OrderProductService;
import com.premade_dishes_system.service.product.ProductInfoService;
import com.premade_dishes_system.service.product.ProductSpecsService;
import com.premade_dishes_system.utils.MyComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Shu-King
 * @description 针对表【product_info】的数据库操作Service实现
 * @createDate 2023-02-01 20:01:44
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo>
        implements ProductInfoService {
    @Autowired
    private ProductInfoMapper pMapper;

    @Autowired
    private OrderProductService oService;

    @Autowired
    private ProductSpecsService sService;

    @Override
    public boolean checkNameUnique(ProductInfo prod) {
        String name = prod.getProductName();
        int uid = Integer.parseInt(StpUtil.getLoginId().toString());

        int count = this.count(new QueryWrapper<ProductInfo>()
                .eq("seller_id", uid)
                .eq("pname", name));
        return count == 0;
    }

    @Override
    public boolean updateImg(String imgPath, int pid) {
        return pMapper.updateImgById(imgPath, pid) != 0;
    }

    @Override
    public boolean updateSales(int pid, int count) {
        return pMapper.updateSalesById(pid, count) != 0;
    }

    @Override
    public List<ProductInfo> recommend(int uid, int cnt) {
        //最近10次购买的货物
        List<OrderProduct> recentProds = oService.selectRecent(uid);
        //候选推荐货物
        List<ProductInfo> recommendItems = this.list();
        if (recentProds == null) return recommendItems.subList(0,Math.min(cnt, recommendItems.size()));
        //特征值权重
        double[] weight = new double[]{0.5, 0.3, 0.2};
        double avg, sum = 0;
        //记录用户购买的货物所属类别和供应商数量
        Map<Integer, Integer> cMap = new HashMap<>(), pMap = new HashMap<>();

        //构建用户画像
        for (OrderProduct item : recentProds) {
            //获取特征字段
            Integer category = this.getById(item.getPid()).getCategoryId();
            double price = item.getTotalPrice() / item.getCount();
            Integer seller = item.getSellerId();

            //计算每个种类数量
            if (!cMap.containsKey(category))
                cMap.put(category, 1);
            else {
                Integer num = cMap.get(category);
                cMap.put(category, num + 1);
            }

            sum += price;

            //计算每个供应商数量
            if (!pMap.containsKey(seller))
                pMap.put(seller, 1);
            else {
                Integer num = pMap.get(seller);
                pMap.put(seller, num + 1);
            }
        }
        //计算平均值
        avg = sum / recentProds.size();

        //结果映射集 货物/相似度
        Map<ProductInfo, Double> rMap = new HashMap<>();

        //特征值量化
        for (ProductInfo item : recommendItems) {
            int pid = item.getPid();
            List<ProductSpecs> specs = sService.list(new QueryWrapper<ProductSpecs>().eq("pid", pid));

            //特征向量
            double[] vector = new double[3];
            //特征字段
            Integer category = item.getCategoryId();

            double price, _sum = 0;
            for (ProductSpecs spec : specs)
                _sum += spec.getPrice();
            price = _sum / specs.size();

            Integer seller = item.getSellerId();

            //量化种类字段
            if (!cMap.containsKey(category))
                vector[0] = 0;
            else {
                double per = (double) (cMap.get(category) / recentProds.size());
                if (per <= 0.3)
                    vector[0] = 0.4;
                else if (per <= 0.6)
                    vector[0] = 0.7;
                else vector[0] = 1.0;
            }
            //量化价格字段
            double abs = Math.abs(price - avg);
            if (abs <= 10)
                vector[1] = 1.0;
            else if (abs <= 30) vector[1] = 0.7;
            else if (abs <= 80) vector[1] = 0.4;
            else vector[1] = 0.1;

            //量化供应商字段
            if (!pMap.containsKey(seller))
                vector[2] = 0;
            else {
                double per = (double) (pMap.get(seller) / recentProds.size());
                if (per <= 0.3)
                    vector[2] = 0.4;
                else if (per <= 0.6)
                    vector[2] = 0.7;
                else vector[2] = 1.0;
            }
            //加权
            for (int i = 0; i < vector.length; i++)
                vector[i] *= weight[i];

            System.out.println("特征向量：" + Arrays.toString(vector));
            double top = 0, down1 = 0, down2 = 0;
            for (int i = 0; i < vector.length; i++) {
                top += vector[i] * weight[i];
                down1 += vector[i] * vector[i];
                down2 += weight[i] * weight[i];
            }
            //根据余弦函数得到相似度
            double similarity = top / (Math.sqrt(down1) * Math.sqrt(down2));
            System.out.println("货物" + pid + "的相似度是：" + similarity);

            rMap.put(item, similarity);
        }

//        对结果集排序
        List<Map.Entry<ProductInfo, Double>> entrys = new ArrayList<>(rMap.entrySet());
        entrys.sort(new MyComparator());
        //排序后的键值列表
        List<ProductInfo> resList = new ArrayList<>();

        for (Map.Entry<ProductInfo, Double> entry : entrys) {
            ProductInfo prodItem = entry.getKey();
            System.out.println("货物" + prodItem.getPid() + "的相似度是：" + entry.getValue());
            resList.add(prodItem);
        }

        return resList.subList(0, Math.min(cnt, resList.size()));
    }
}