package dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.myproject.dao.SeckillDao;
import org.myproject.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	//注入dao实现类依赖
	@Resource
	private SeckillDao seckillDao;
	@Test
	public void testQueryById(){
		long id = 1000;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);
	}
	@Test
	public void testQueryAll(){
		List<Seckill> list = seckillDao.queryAll(0, 4);
		for(Seckill seckill:list){
			System.out.println(seckill);
		}
	}
	@Test
	public void testReduceNumber(){
		Date killTime = new Date();
		int updateCount = seckillDao.reduceNumber(1000L,killTime);
		System.out.println(updateCount);
	}
	@Test
	public void teestKillByProcedure(){
		long seckillId = 1001L;
		long phone = 13826248990L;
		Date killTime = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seckillId", seckillId);
		map.put("phone", phone);
		map.put("killTime", killTime);
		map.put("result", null);
		seckillDao.killByProcedure(map);
		System.out.println(map.get("result"));
	}
}
