package org.myproject.dao;

import org.apache.ibatis.annotations.Param;
import org.myproject.entity.SuccessKilled;

public interface SuccessKilledDao {
	/**
	 * ���빺����ϸ���ɹ����ظ�
	 * @param seckillId��ɱ��Ʒ��Ϣid
	 * @param phone��ҵ绰����
	 * @return���������
	 */
	int insertSuccessKilled(@Param("seckillId")long seckillId,@Param("phone")long phone);
	
	/**
	 * ����Id��ѯ������ɱ��Ʒ��Ϣ����Ĺ�����ϸ����
	 * @param seckillId
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId,@Param("phone")long phone);
}
