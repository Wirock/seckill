package org.myproject.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.myproject.entity.Seckill;

/**
 *秒杀业务相关数据操作 
 *
 */
public interface SeckillDao {
	/**
	 * 减库存
	 * @param seckillId秒杀商品id
	 * @param createTime秒杀商品信息创建时间
	 * @return 如果影响的行数>0，表示更新的行数
	 */
	int reduceNumber(@Param("seckillId")long seckillId,@Param("killTime")Date killTime);
	/**
	 * 通过id查询秒杀商品信息
	 * @param seckillId秒杀商品id
	 * @return对应秒杀上屏
	 */
	Seckill queryById(long seckillId);
	/**
	 * 根据偏移量查询秒杀商品信息
	 * @param offset起始坐标
	 * @param limit个数
	 * @return 秒杀商品集合
	 */
	List<Seckill> queryAll(@Param("offset")int offset,@Param("limit")int limit);
	/**
	 * 使用存储过程执行秒杀
	 * @param map
	 */
	void killByProcedure(Map<String,Object> map);
}
