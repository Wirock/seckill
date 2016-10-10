package org.myproject.web;

import java.util.Date;
import java.util.List;

import org.myproject.dto.Exposer;
import org.myproject.dto.SeckillExecution;
import org.myproject.dto.SeckillResult;
import org.myproject.entity.Seckill;
import org.myproject.enums.SeckillStateEnum;
import org.myproject.exception.RepeatKillException;
import org.myproject.exception.SeckillCloseException;
import org.myproject.exception.SeckillException;
import org.myproject.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping // 第一级目录url:/模块/资源/｛id｝/细分
public class SeckillController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillService seckillService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		// 获取列表页
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);
		// list.jsp+model = ModelAndView
		return "list";// WEB-INF/jsp/list.jsp,因为spring-mvc.xml的ViewResover里配置过前缀后缀
	}

	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		if (seckillId == null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getSeckillById(seckillId);
		if (seckill == null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}

	// ajax json
	@RequestMapping(value = "{seckillId}/exposer", method = RequestMethod.POST, produces = {
			"application/json;charset=utf-8" }) // contentType和charset
	// 把返回类型封装成json
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId")Long seckillId) {
		SeckillResult<Exposer> result;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {
			"application/json;charset=utf-8" })
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
			@CookieValue(value = "killPhone", required = false) Long phone, @PathVariable("md5") String md5) {
		if (phone == null) {
			return new SeckillResult<SeckillExecution>(false, "未注册");
		}
		try {
//			SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
			SeckillExecution execution = seckillService.executeSeckillByProcedure(seckillId, phone, md5);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (RepeatKillException e) {
			SeckillExecution seckillExcution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(true, seckillExcution);
		} catch (SeckillCloseException e) {
			SeckillExecution seckillExcution = new SeckillExecution(seckillId, SeckillStateEnum.END);
			return new SeckillResult<SeckillExecution>(true, seckillExcution);
		} catch (SeckillException e) {
			logger.error(e.getMessage());
			SeckillExecution seckillExcution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
			return new SeckillResult<SeckillExecution>(true, seckillExcution);
		}
	}
	
	@RequestMapping(value = "/time/now", method = RequestMethod.GET)
	@ResponseBody
	public SeckillResult<Long> time() {
		Date now = new Date();
		return new SeckillResult<Long>(true,now.getTime());
	}
}
