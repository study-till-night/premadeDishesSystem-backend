package com.premade_dishes_system.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.premade_dishes_system.pojo.user.Address;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author Shu-King
 * @description 针对表【addresses】的数据库操作Mapper
 * @createDate 2023-02-01 19:48:50
 * @Entity com.premade_dishes_system.pojo.Addresses
 */
@Repository
public interface AddressesMapper extends BaseMapper<Address> {
    /**
     * 将原有默认消除
     * @param uid
     * @return
     */
    @Update("update address set is_default=0 where is_default=1 and uid=#{uid} and address_id!=#{aid}")
    int updateByDefault1(int uid,int aid);

    /**
     * 修改当前选择地址状态
     * @param aid
     * @return
     */
    @Update("update address set is_default=is_default^1 where address_id=#{aid}")
    int updateByDefault2(int aid);
}




