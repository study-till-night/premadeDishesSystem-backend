package com.premade_dishes_system.controller.trade;

import cn.dev33.satoken.util.SaResult;
import com.premade_dishes_system.service.product.OrderProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/market")
public class MarketController {
    @Resource
    private OrderProductService oService;

    /**
     * 获取近n天某类商品交易额
     * @param days 近n天
     * @param category 种类
     * @return 列表
     */
    @GetMapping("/get")
    public SaResult getMarketInfo(int days,int category){
        return SaResult.data(oService.queryTradeData(days,category));
    }
}
