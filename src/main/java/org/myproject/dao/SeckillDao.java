package org.myproject.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.myproject.entity.Seckill;

/**
 *��ɱҵ��������ݲ��� 
 *
 */
public interface SeckillDao {
	/**
	 * �����
	 * @param seckillId��ɱ��Ʒid
	 * @param createTime��ɱ��Ʒ��Ϣ����ʱ��
	 * @return ���Ӱ�������>0����ʾ���µ�����
	 */
	int reduceNumber(@Param("seckillId")long seckillId,@Param("killTime")Date killTime);
	/**
	 * ͨ��id��ѯ��ɱ��Ʒ��Ϣ
	 * @param seckillId��ɱ��Ʒid
	 * @return��Ӧ��ɱ����
	 */
	Seckill queryById(long seckillId);
	/**
	 * ����ƫ������ѯ��ɱ��Ʒ��Ϣ
	 * @param offset��ʼ����
	 * @param limit����
	 * @return ��ɱ��Ʒ����
	 */
	List<Seckill> queryAll(@Param("offset")int offset,@Param("limit")int limit);
	/**
	 * ʹ�ô洢����ִ����ɱ
	 * @param map
	 */
	void killByProcedure(Map<String,Object> map);
}
