package org.myproject.service.impl;

import java.util.Date;
import java.util.List;

import org.myproject.dao.SeckillDao;
import org.myproject.dao.SuccessKilledDao;
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
	private final String slat = "14dscatLbtH1HNraioSAAQ4r3k?jbo&@a7t80bd";
	
	private String getMd5(long seckillId){
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0,4);
	}

	public Seckill getSeckillById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	public Exposer exportSeckillUrl(long seckillId) {
		Seckill seckill = seckillDao.queryById(seckillId);
		if(seckill==null){
			return new Exposer(false,seckillId);
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date nowTime = new Date();
		if(nowTime.getTime()<startTime.getTime()
				||nowTime.getTime()>endTime.getTime()){
			return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
		}
		String md5 = getMd5(seckillId);
		return new Exposer(true,seckillId,md5);
	}
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long phone, String md5)
			throws RepeatKillException, SeckillCloseException, SeckillException {
		if(md5==null||!md5.equals(getMd5(seckillId))){
			throw new SeckillException("seckill data rewrite");
		}
		//执行秒杀逻辑，减库存+记录购买行为
		Date nowTime = new Date();
		
		try {
			//减库存
			int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
			if(updateCount <= 0){
				//没有更新记录，秒杀结束
				throw new SeckillCloseException("seckill is closed");
			}else{
				//记录购买行为
				int insertCount = successKilledDao.insertSuccessKilled(seckillId, phone);
				if(insertCount <= 0){
					//重复秒杀时抛出异常
					throw new RepeatKillException("kill is repeated");
				}else{
					//秒杀成功
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, phone);
					return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
				}
			}
		}catch(SeckillCloseException e1){
			throw e1;
		}catch(RepeatKillException e2){
			throw e2;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			//所有编译期异常转化为运行期异常，一旦有错，能发生回滚
			throw new SeckillException("seckill inner error:"+e.getMessage());
		}
	}

}
