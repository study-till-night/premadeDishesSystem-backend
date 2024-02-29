package com.premade_dishes_system.mapper.trade;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.premade_dishes_system.pojo.trade.Contract;
import org.springframework.stereotype.Repository;

/**
* @author Shu-King
* @description 针对表【contract(长期批发单)】的数据库操作Mapper
* @createDate 2023-02-02 13:44:47
* @Entity com.premade_dishes_system.pojo.Contract
*/
@Repository
public interface ContractMapper extends BaseMapper<Contract> {

}




