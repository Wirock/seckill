package org.myproject.dao;

import org.apache.ibatis.annotations.Param;
import org.myproject.entity.SuccessKilled;

public interface SuccessKilledDao {
	/**
	 * 插入购买明细，可过滤重复
	 * @param seckillId秒杀商品信息id
	 * @param phone买家电话号码
	 * @return插入的行数
	 */
	int insertSuccessKilled(@Param("seckillId")long seckillId,@Param("phone")long phone);
	
	/**
	 * 根据Id查询带有秒杀商品信息对象的购买明细对象
	 * @param seckillId
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId,@Param("phone")long phone);
}
