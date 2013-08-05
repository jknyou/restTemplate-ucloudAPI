package com.jknyou.ucloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.jknyou.ucloud.service.UcloudServerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-appContext.xml"})
public class UcloudTest {
	@Inject private RestTemplate restTemplate;
	@Inject private UcloudServerService ucloudService;
	
	@Test
	public void test(){
//		getProductList();
//		deployVm();
//		queryAsyncJobResult();
//		listVirtualMachines();
//		listPublicIpAddresses();
		listTemplates();
//		getServiceoffering();
	}
	
	private void getServiceoffering() {
		List<String> paramList = new ArrayList<String>();
		paramList.add("command=listAvailableProductTypes");
		
		String jsonString = ucloudService.sendRequest(paramList);
		parsJsonString(jsonString);
	}

	private void listTemplates() {
		List<String> paramList = new ArrayList<String>();
		paramList.add("command=listTemplates");
		paramList.add("templatefilter=selfexecutable");
		
		String jsonString = ucloudService.sendRequest(paramList);
		System.out.println(jsonString);
	}

	private void listPublicIpAddresses() {
		List<String> paramList = new ArrayList<String>();
		paramList.add("command=listPublicIpAddresses");
		
		String jsonString = ucloudService.sendRequest(paramList);
		System.out.println(jsonString);
	}

	private void listVirtualMachines() {
		List<String> paramList = new ArrayList<String>();
		paramList.add("command=listVirtualMachines"); // mandatory
		
		String jsonString = ucloudService.sendRequest(paramList);
		System.out.println(jsonString);
	}

	private void queryAsyncJobResult() {
		List<String> paramList = new ArrayList<String>();
		paramList.add("command=queryAsyncJobResult"); // mandatory
		paramList.add("jobid=86b824be-a17b-472c-9431-bb3ed84f03aa"); // mandatory
		
		String jsonString = ucloudService.sendRequest(paramList);
		System.out.println(jsonString);
	}

	private void deployVm() {
		List<String> paramList = new ArrayList<String>();
		
		paramList.add("command=deployVirtualMachine"); // mandatory
		paramList.add("serviceofferingid=267734e0-9f7f-4dbe-aee7-f2bfcc9662d8"); // 1Core 1GB mandatory
		paramList.add("templateid=c60fb2cf-6589-4961-b0b4-552f7050af30"); // image
//		paramList.add("templateid=c2e4c0f2-2bc7-444c-a280-59f5b42dbf11"); // Centos 6.3 64bit mandatory
//		paramList.add("diskofferingid=0"); // disk 제공 ID  mandatory   (필수값으로 지정되어있는데 안쓸경우는??) 
//		paramList.add("diskofferingid=87c0a6f6-c684-4fbe-a393-d8412bcf788d"); // disk 제공 ID  mandatory   (필수값으로 지정되어있는데 안쓸경우는??) 
		paramList.add("zoneid=eceb5d65-6571-4696-875f-5a17949f3317"); // KOR-Central A  mandatory
//		paramList.add("usageplantype=monthly"); // oneyear(1 년약정), monthly(무약정-월 단위 요금), 	hourly(시간요금) : (* default : hourly) 
		paramList.add("runsysprep=false"); // 사용자 custom 윈도우 OS 이미지 사용시 sysprep 연동 	수행 여부. true/false (*default : true) 
		paramList.add("displayname=offeringTest11"); // VM 표시 이름 
		
		String jsonString = ucloudService.sendRequest(paramList);
		System.out.println(jsonString);
	}

	private void getProductList() {
		List<String> paramList = new ArrayList<String>();
		paramList.add("command=listAvailableProductTypes");
		
		String jsonString = ucloudService.sendRequest(paramList);
		System.out.println(jsonString);
	}
	
	private void parsJsonString(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String,Object> map = mapper.readValue(jsonString, HashMap.class);
			Map<String,Object> map2 = (Map<String, Object>) map.get("listavailableproducttypesresponse");
			List<Map<String,String>> mapList = (List<Map<String, String>>) map2.get("producttypes");
			
			Set<Map<String,String>> mapSet = new HashSet<Map<String,String>>();
			Set<Map<String,String>> mapSet2 = new HashSet<Map<String,String>>();
			
			Set<Map<String,String>> mapSet3 = new HashSet<Map<String,String>>();
			
			for (Map<String, String> map3 : mapList) {
				// spec
				Map<String, String> map4 = new HashMap<String, String>();
				map4.put(map3.get("serviceofferingid"), map3.get("serviceofferingdesc"));
				mapSet.add(map4);
				
				// spec
				Map<String, String> map5 = new HashMap<String, String>();
				Map<String, String> winmap = new HashMap<String, String>();
				String string = map3.get("templatedesc");
				if(StringUtils.startsWithIgnoreCase(string, "WIN")) {
					winmap.put(map3.get("templateid"), string);
					mapSet2.add(winmap);
				} else {
					winmap.put(map3.get("templateid"), string);
					mapSet3.add(winmap);
				}
			}
			System.out.println("###########spec##################");
			for (Map<String, String> map3 : mapSet) {
				System.out.println(map3);
			}
			
			System.out.println("###########OS##################");
			System.out.println("###########WIN##################");
			for (Map<String, String> map3 : mapSet2) {
				System.out.println(map3);
			}
			System.out.println("###########LINUX##################");
			for (Map<String, String> map3 : mapSet3) {
				System.out.println(map3);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
