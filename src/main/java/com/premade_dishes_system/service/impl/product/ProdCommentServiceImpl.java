package com.premade_dishes_system.service.impl.product;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.premade_dishes_system.pojo.product.ProdComment;
import com.premade_dishes_system.service.product.ProdCommentService;
import com.premade_dishes_system.mapper.product.ProdCommentMapper;
import org.springframework.stereotype.Service;

/**
* @author HP
* @description 针对表【prod_comment(货物评价)】的数据库操作Service实现
* @createDate 2023-04-19 16:54:04
*/
@Service
public class ProdCommentServiceImpl extends ServiceImpl<ProdCommentMapper, ProdComment>
    implements ProdCommentService{

}




