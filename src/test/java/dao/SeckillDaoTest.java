package dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.myproject.dao.SeckillDao;
import org.myproject.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * ����spring��junit���ϣ�junit����ʱ����springIOC����
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//����junit spring�������ļ�
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	//ע��daoʵ��������
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
}
