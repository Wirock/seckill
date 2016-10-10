package org.myproject.service;

import java.util.List;

import org.myproject.dto.Exposer;
import org.myproject.dto.SeckillExecution;
import org.myproject.entity.Seckill;
import org.myproject.exception.RepeatKillException;
import org.myproject.exception.SeckillCloseException;
import org.myproject.exception.SeckillException;

/**
 * ҵ��ӿڣ�վ��ʹ���ߵĽǶ����
 *�������棺�����������ȣ��������������ͣ�return����/�쳣��
 */
public interface SeckillService {
	/**
	 * ��ѯ������ɱ��¼
	 * @return
	 */
	List<Seckill> getSeckillList();
	/**
	 * ��ѯ������ɱ��¼
	 * @param seckillId
	 * @return
	 */
	Seckill getSeckillById(long seckillId);
	/**
	 * ��ʼ��ɱʱ����url�����򷵻�ʱ��
	 * @param seckillId
	 */
	Exposer exportSeckillUrl(long seckillId);
	/**
	 * ִ����ɱ����
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
	 * ͨ���洢����ִ����ɱ����
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
