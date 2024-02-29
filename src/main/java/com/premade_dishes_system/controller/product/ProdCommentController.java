package com.premade_dishes_system.controller.product;


import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.premade_dishes_system.pojo.product.OrderProduct;
import com.premade_dishes_system.pojo.product.ProdComment;
import com.premade_dishes_system.service.product.OrderProductService;
import com.premade_dishes_system.service.product.ProdCommentService;
import com.premade_dishes_system.service.user.UserAccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 货物评价(ProdComment)表控制层
 *
 * @author makejava
 * @since 2023-04-19 16:57:00
 */
@RestController
@RequestMapping("/api/prodComment")
public class ProdCommentController {
    /**
     * 服务对象
     */
    @Resource
    private ProdCommentService commentService;

    @Resource
    private UserAccountService userService;

    @Resource
    private OrderProductService orderService;

    private List<Integer> loveList = new ArrayList<>();

    /**
     * 分页查询所有数据
     *
     * @param pid 货物id
     * @return 所有数据
     */
    @GetMapping("/getProd")
    public SaResult selectByProd(Integer pid,
                                 int type,
                                 int pageId,
                                 @RequestParam("pNum") int pageSize) {
        QueryWrapper<ProdComment> queryWrapper = new QueryWrapper<ProdComment>().eq("pid", pid);
        Page<ProdComment> page = new Page<>(pageId, pageSize);

        if (type == 0) queryWrapper.orderByDesc("love");
        else queryWrapper.orderByDesc("update_time");

        Page<ProdComment> comments = commentService.page(page, queryWrapper);
        ArrayList<String> userNames = new ArrayList<>(); //评论用户名
        ArrayList<Boolean> isLoved = new ArrayList<>(); //是否点赞

        comments.getRecords().forEach(item -> {
            userNames.add(userService.getById(item.getUid()).getUserName());
            isLoved.add(loveList.contains(item.getCid()));
        });
        return SaResult.ok().set("comments", comments.getRecords())
                .set("userNames", userNames)
                .set("isLoved", isLoved)
                .set("currentPage", pageId)
                .set("pageSize", pageSize)
                .set("total", commentService.count(queryWrapper));
    }

    /**
     * 新增数据
     *
     * @param comment 实体对象
     * @return 新增结果
     */
    @PostMapping("/add")
    @SaCheckRole(mode = SaMode.OR, value = {"common_user", "organization"})
    public SaResult add(@RequestBody ProdComment comment) {
        if (commentService.save(comment)) {
            orderService.update(new UpdateWrapper<OrderProduct>()
                    .set("is_commented", true).eq("oid", comment.getOid()));
            return SaResult.ok("发表成功");
        } else return SaResult.error("发表失败");
    }

    /**
     * 点赞
     *
     * @param cid 评论序号
     * @return 结果
     */
    @PostMapping("/love")
    @SaCheckRole(mode = SaMode.OR, value = {"common_user", "organization"})
    public SaResult addLove(int cid) {
        if (loveList.contains(cid)) return SaResult.error("已经点赞");

        ProdComment comment = commentService.getById(cid);
        if (commentService.update(new UpdateWrapper<ProdComment>()
                .set("love", comment.getLove() + 1).eq("cid", cid))) {
            loveList.add(cid);
            return SaResult.ok("点赞成功");
        }
        return SaResult.error("点赞失败");
    }
}

