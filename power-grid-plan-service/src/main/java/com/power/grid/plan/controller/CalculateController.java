package com.power.grid.plan.controller;


import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.vo.HandleVo;
import com.power.grid.plan.service.manager.GridPlanManage;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CalculateController {

	@Resource
	private GridPlanManage gridPlanManage;

	@GetMapping(value = "/calculate")
	@ResponseBody
	public List<HandleVo> calculate(long start, long end) {
		List<HandleBo> handleBoList=gridPlanManage.calculate(start,end);
		List<HandleVo> HandleVoList=new ArrayList<>();
		handleBoList.forEach(s->{
			HandleVo vo=new HandleVo();
			vo.setSumPrice(s.getSumPrice());
			vo.setHandlePath(StringUtils.collectionToDelimitedString(s.getHandlePath(), "-"));
			HandleVoList.add(vo);
		});
		return HandleVoList;
	}

}