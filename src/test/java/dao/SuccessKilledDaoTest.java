package dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.myproject.dao.SuccessKilledDao;
import org.myproject.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {
	@Resource
	private SuccessKilledDao successKilledDao;
	@Test
	public void testInsertSuccessKilled(){
		int insertCount = successKilledDao.insertSuccessKilled(1001L,12345678901L);
		System.out.println(insertCount);
	}
	@Test
	public void testQueryByIdWithSeckill(){
	SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1001L,12345678901L);
	System.out.println(successKilled);
	System.out.println(successKilled.getSeckill());
	}
}
