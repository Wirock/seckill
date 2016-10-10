package org.myproject.service;

import java.util.List;

import org.myproject.dto.Exposer;
import org.myproject.dto.SeckillExecution;
import org.myproject.entity.Seckill;
import org.myproject.exception.RepeatKillException;
import org.myproject.exception.SeckillCloseException;
import org.myproject.exception.SeckillException;

/**
 * 业务接口，站在使用者的角度设计
 *三个方面：方法定义粒度，参数，返回类型（return类型/异常）
 */
public interface SeckillService {
	/**
	 * 查询所有秒杀记录
	 * @return
	 */
	List<Seckill> getSeckillList();
	/**
	 * 查询单个秒杀记录
	 * @param seckillId
	 * @return
	 */
	Seckill getSeckillById(long seckillId);
	/**
	 * 开始秒杀时返回url，否则返回时间
	 * @param seckillId
	 */
	Exposer exportSeckillUrl(long seckillId);
	/**
	 * 执行秒杀操作
	 * @param seckillId
	 * @param phone
	 * @param md5
	 * @return
	 * @throws RepeatKillException
	 * @throws SeckillCloseException
	 * @throws SeckillException
	 */
	SeckillExecution executeSeckill(long seckillId,long phone,String md5)
			throws RepeatKillException,SeckillCloseException,SeckillException;
	/**
	 * 通过存储过程执行秒杀操作
	 * @param seckillId
	 * @param phone
	 * @param md5
	 * @return
	 * @throws RepeatKillException
	 * @throws SeckillCloseException
	 * @throws SeckillException
	 */
	SeckillExecution executeSeckillByProcedure(long seckillId,long phone,String md5)
			throws RepeatKillException,SeckillCloseException,SeckillException;
}
