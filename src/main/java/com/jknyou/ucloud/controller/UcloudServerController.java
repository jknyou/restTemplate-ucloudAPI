package com.jknyou.ucloud.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jknyou.ucloud.service.UcloudServerService;

@Controller
public class UcloudServerController {
	@Inject private UcloudServerService ucloudServerService;
	
	@RequestMapping(value ={"/", "/home"}, method = RequestMethod.GET)
	public String home(ModelMap map) {
		return "home";
	}
	
	@RequestMapping(value ="send", method = RequestMethod.POST)
	public void createServer(@RequestBody List<String> paramList, ModelMap map) {
		map.put("data", ucloudServerService.sendRequest(paramList));
	}
	
	@RequestMapping(value ="servers", method = RequestMethod.GET)
	public void getServerList(ModelMap map) {
		List<String> paramList = makeParam("command=listVirtualMachines");
		map.put("data", ucloudServerService.sendRequest(paramList));
	}
	
	@RequestMapping(value ="ips", method = RequestMethod.GET)
	public void getIpList(ModelMap map) {
		List<String> paramList = makeParam("command=listPublicIpAddresses");
		map.put("data", ucloudServerService.sendRequest(paramList));
	}
	
//	@RequestMapping(value ="serviceoffering", method = RequestMethod.GET)
//	public void getServiceofferingList(ModelMap map) {
//		List<String> paramList = makeParam("command=listAvailableProductTypes");
//		map.put("data", ucloudService.sendRequest(paramList));
//	}
	
	@RequestMapping(value ="products", method = RequestMethod.GET)
	public void getProductList(ModelMap map) {
		List<String> paramList = makeParam("command=listAvailableProductTypes");
		map.put("data", ucloudServerService.sendRequest(paramList));
	}
	
	private List<String> makeParam(String param) {
		List<String> paramList = new ArrayList<String>();
		paramList.add(param);
		return paramList;
	}
}
