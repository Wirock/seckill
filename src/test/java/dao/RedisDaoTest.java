package dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.myproject.dao.SeckillDao;
import org.myproject.dao.cache.RedisDao;
import org.myproject.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class RedisDaoTest {
	@Autowired
	private RedisDao redisDao;
	@Autowired
	private SeckillDao seckillDao;
	
	private long id = 1001;
	@Test
	public void testSeckill(){
		Seckill seckill = redisDao.getSeckill(id);
		if(seckill == null){
			seckill = seckillDao.queryById(id);
		}
		if(seckill!=null){
			String result = redisDao.putSeckill(seckill);
			System.out.println(result);
			seckill = redisDao.getSeckill(id);
			System.out.println(seckill);
		}
	}
}
