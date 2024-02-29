package com.premade_dishes_system.controller.trade;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.premade_dishes_system.pojo.trade.DeliveryRecord;
import com.premade_dishes_system.service.trade.DeliveryRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 长期交易发货记录(DeliveryRecord)表控制层
 *
 * @author makejava
 * @since 2023-04-19 11:52:55
 */
@RestController
@RequestMapping("/deliveryRecord")
public class DeliveryRecordController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private DeliveryRecordService deliveryRecordService;

    /**
     * 分页查询所有数据
     *
     * @param page           分页对象
     * @param deliveryRecord 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<DeliveryRecord> page, DeliveryRecord deliveryRecord) {
        return success(this.deliveryRecordService.page(page, new QueryWrapper<>(deliveryRecord)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.deliveryRecordService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param deliveryRecord 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody DeliveryRecord deliveryRecord) {
        return success(this.deliveryRecordService.save(deliveryRecord));
    }

    /**
     * 修改数据
     *
     * @param deliveryRecord 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody DeliveryRecord deliveryRecord) {
        return success(this.deliveryRecordService.updateById(deliveryRecord));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.deliveryRecordService.removeByIds(idList));
    }
}

