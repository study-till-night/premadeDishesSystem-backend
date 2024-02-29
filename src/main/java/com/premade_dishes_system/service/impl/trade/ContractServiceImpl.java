package com.premade_dishes_system.service.impl.trade;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.premade_dishes_system.pojo.trade.Contract;
import com.premade_dishes_system.service.trade.ContractService;
import com.premade_dishes_system.mapper.trade.ContractMapper;
import org.springframework.stereotype.Service;

/**
* @author Shu-King
* @description 针对表【contract(长期批发单)】的数据库操作Service实现
* @createDate 2023-02-02 13:44:47
*/
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract>
    implements ContractService{

}




