package com.jknyou.ucloud.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jknyou.ucloud.service.UcloudDbService;

@Controller
public class UcloudDbController {
	@Inject private UcloudDbService ucloudDbService;
	
	
	@RequestMapping(value ="dbs", method = RequestMethod.GET)
	public void getServerList(ModelMap map) {
		List<String> paramList = makeParam("command=listInstances");
		map.put("data", ucloudDbService.sendRequest(paramList));
	}
	private List<String> makeParam(String param) {
		List<String> paramList = new ArrayList<String>();
		paramList.add(param);
		return paramList;
	}
}
