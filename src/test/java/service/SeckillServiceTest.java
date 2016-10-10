package service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.myproject.dto.Exposer;
import org.myproject.dto.SeckillExecution;
import org.myproject.entity.Seckill;
import org.myproject.exception.RepeatKillException;
import org.myproject.exception.SeckillCloseException;
import org.myproject.exception.SeckillException;
import org.myproject.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillService seckillService;
	@Test
	public void testGetSeckillList(){
		List<Seckill> list = seckillService.getSeckillList();
		logger.info("list={}",list);
	}	
	@Test
	public void testGetBySeckillId(){
		 long seckillId = 1000L;
		 Seckill seckill = seckillService.getSeckillById(seckillId);
		 logger.info("seckill={}",seckill);
	}
	@Test
	public void testExportseckillUrl(){
		long seckillId = 1000L;
		Exposer exposer = seckillService.exportSeckillUrl(seckillId);
		logger.info("exposer={}",exposer);
	}
	@Test
	public void testExecuteSeckill(){
		 long seckillId = 1000L;
		 long phone = 1234567890L;
		 String md5 = "c4a9f102141abfea4c0f032ec946fe35";
		 try {
			SeckillExecution seckillExection = seckillService.executeSeckill(seckillId, phone, md5);
			 logger.info("seckillExection={}",seckillExection);
		} catch (RepeatKillException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (SeckillCloseException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (SeckillException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}
	@Test
	public void testSeckillLogic(){
		long seckillId = 1000L;
		Exposer exposer = seckillService.exportSeckillUrl(seckillId);
		if(exposer.isExposed()){
			logger.info("exposer={}",exposer);
			long phone = 1234567890L;
			String md5 = exposer.getMd5();
			try {
				SeckillExecution seckillExection = seckillService.executeSeckill(seckillId, phone, md5);
				 logger.info("seckillExection={}",seckillExection);
			} catch (RepeatKillException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			} catch (SeckillCloseException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			} catch (SeckillException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
		}else{
			logger.warn("exposer={}",exposer);
		}
	}
	@Test
	public void testExecuteSeckillByProcedure(){
		long seckillId = 1001L;
		long phone = 13826248990L;
		Exposer exposer = seckillService.exportSeckillUrl(seckillId);
		if(exposer.isExposed()){
			String md5 = exposer.getMd5();
			SeckillExecution seckillExecution = seckillService.executeSeckillByProcedure(seckillId, phone, md5);
			logger.info(seckillExecution.getStateInfo());
		}
	} 
}
