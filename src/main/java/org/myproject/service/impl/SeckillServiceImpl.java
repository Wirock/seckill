package org.myproject.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.myproject.dao.SeckillDao;
import org.myproject.dao.SuccessKilledDao;
import org.myproject.dao.cache.RedisDao;
import org.myproject.dto.Exposer;
import org.myproject.dto.SeckillExecution;
import org.myproject.entity.Seckill;
import org.myproject.entity.SuccessKilled;
import org.myproject.enums.SeckillStateEnum;
import org.myproject.exception.RepeatKillException;
import org.myproject.exception.SeckillCloseException;
import org.myproject.exception.SeckillException;
import org.myproject.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Service
public class SeckillServiceImpl implements SeckillService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillDao seckillDao;
	@Autowired
	private SuccessKilledDao successKilledDao;
	@Autowired
	private RedisDao redisDao;

	private final String slat = "14dscatLbtH1HNraioSAAQ4r3k?jbo&@a7t80bd";

	private String getMd5(long seckillId) {
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	public Seckill getSeckillById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	public Exposer exportSeckillUrl(long seckillId) {
		// �Ż��㣬�����Ż�
		// ��֤һ���ԣ���ʱ�Ļ�����ά��һ���ԣ����ݿ��еı���ֱ���޸ģ���Ҫ�޸������ԭ���ģ��½�һ����
		// 1.����redis
		Seckill seckill = redisDao.getSeckill(seckillId);
		// 2.���գ��������ݿ�
		if (seckill == null) {
			seckill = seckillDao.queryById(seckillId);
			if (seckill == null) {
				return new Exposer(false, seckillId);
			} else {
				// 3.����redis
				redisDao.putSeckill(seckill);
			}
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date nowTime = new Date();
		if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
			return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
		}
		String md5 = getMd5(seckillId);
		return new Exposer(true, seckillId, md5);
	}

	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long phone, String md5) {
		if (md5 == null || !md5.equals(getMd5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		// ִ����ɱ�߼��������+��¼������Ϊ
		Date nowTime = new Date();

		try {
			// ��¼������Ϊ
			int insertCount = successKilledDao.insertSuccessKilled(seckillId, phone);
			if (insertCount <= 0) {
				// �ظ���ɱʱ�׳��쳣
				throw new RepeatKillException("kill is repeated");
			} else {
				// ����棨�м������ȵ���Ʒ������
				int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
				if (updateCount <= 0) {
					// û�и��¼�¼����ɱ����
					throw new SeckillCloseException("seckill is closed");
				} else {
					// ��ɱ�ɹ�
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, phone);
					return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
				}
			}
		} catch (SeckillCloseException e1) {
			throw e1;
		} catch (RepeatKillException e2) {
			throw e2;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			// ���б������쳣ת��Ϊ�������쳣��һ���д��ܷ����ع�
			throw new SeckillException("seckill inner error:" + e.getMessage());
		}
	}

	public SeckillExecution executeSeckillByProcedure(long seckillId, long phone, String md5) {
		if (md5 == null || !md5.equals(getMd5(seckillId))) {
			return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWIRTE);
		}
		Date killTime = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seckillId", seckillId);
		map.put("phone", phone);
		map.put("killTime", killTime);
		map.put("result", null);

		try {
			seckillDao.killByProcedure(map);
			int result;
			Integer rs = (Integer) map.get("result");
			if (rs == null) {
				result = -2;
			} else {
				result = rs;
			}
			logger.info("result={}",map.get("result"));
			if (result == 1) {
				SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, phone);
				return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, sk);
			} else {
				return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
		}
	}

}
