package com.premade_dishes_system.mapper.trade;

import com.premade_dishes_system.pojo.trade.DeliveryRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author HP
* @description 针对表【delivery_record(长期交易发货记录)】的数据库操作Mapper
* @createDate 2023-04-19 11:52:02
* @Entity com.premade_dishes_system.pojo.trade.DeliveryRecord
*/
@Repository
public interface DeliveryRecordMapper extends BaseMapper<DeliveryRecord> {
    @Select("select * from delivery_record where cid=#{cid}")
    public List<DeliveryRecord> selectByCid(int cid);

//    @Insert("INSERT INTO delivery_record  ( cid, times, create_time, update_time )  VALUES(#{cid},#{index},now(),now())")
//    public int insert(int cid,int index);
}




