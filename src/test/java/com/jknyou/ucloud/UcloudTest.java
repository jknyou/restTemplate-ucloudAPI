package com.jknyou.ucloud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-appContext.xml"})
public class UcloudTest {
	@Inject private RestTemplate restTemplate;
	
	private static final String API_KEY = "apiKey=yourkey";
	private static final String SECRET_KEY = "yourkey";
	private static final String API_URL = "https://api.ucloudbiz.olleh.com/server/v1/client/api?";
	private static final String RES_TYPE = "response=json";
	
	@Test
	public void test(){
//		getProductList();
//		deployVm();
//		queryAsyncJobResult();
//		listVirtualMachines();
		listPublicIpAddresses();
	}
	
	private void listPublicIpAddresses() {
		List<String> paramList = new ArrayList<String>();
		paramList.add("command=listPublicIpAddresses");
		
		addSignatureAndGetData(makeParamsStr(paramList));
	}

	private void listVirtualMachines() {
		List<String> paramList = new ArrayList<String>();
		paramList.add("command=listVirtualMachines"); // mandatory
		
		addSignatureAndGetData(makeParamsStr(paramList));
	}

	private void queryAsyncJobResult() {
		List<String> paramList = new ArrayList<String>();
		paramList.add("command=queryAsyncJobResult"); // mandatory
		paramList.add("jobid=34a9d656-5dd9-4145-8048-fa334ec9fb25"); // mandatory
		
		addSignatureAndGetData(makeParamsStr(paramList));
	}

	private void deployVm() {
		List<String> paramList = new ArrayList<String>();
		
		paramList.add("command=deployVirtualMachine"); // mandatory
		paramList.add("serviceofferingid=267734e0-9f7f-4dbe-aee7-f2bfcc9662d8"); // 1Core 1GB mandatory
		paramList.add("templateid=c2e4c0f2-2bc7-444c-a280-59f5b42dbf11"); // Centos 6.3 64bit mandatory
		paramList.add("diskofferingid=87c0a6f6-c684-4fbe-a393-d8412bcf788d"); // disk 제공 ID  mandatory   (필수값으로 지정되어있는데 안쓸경우는??) 
		paramList.add("zoneid=eceb5d65-6571-4696-875f-5a17949f3317"); // KOR-Central A  mandatory
		paramList.add("usageplantype=monthly"); // oneyear(1 년약정), monthly(무약정-월 단위 요금), 	hourly(시간요금) : (* default : hourly) 
		paramList.add("runsysprep=false"); // 사용자 custom 윈도우 OS 이미지 사용시 sysprep 연동 	수행 여부. true/false (*default : true) 
		paramList.add("displayname=testtest11"); // VM 표시 이름 
		
		addSignatureAndGetData(makeParamsStr(paramList));
	}

	private void getProductList() {
		List<String> paramList = new ArrayList<String>();
		paramList.add("command=listAvailableProductTypes");
		
		addSignatureAndGetData(makeParamsStr(paramList));
	}
	
	private StringBuilder makeParamsStr(List<String> paramList) {
		StringBuilder paramsStr = new StringBuilder();
		paramList.add(API_KEY);
		paramList.add(RES_TYPE);
		Collections.sort(paramList);
		for (int i=0; i < paramList.size(); i++) {
			paramsStr.append(paramList.get(i));
			if (i+1 != paramList.size()) paramsStr.append("&");
		}
		return paramsStr;
	}

	
	private String createRequestUrl(String paramsStr) {
		return API_URL + paramsStr;
	}

	private String createSignature(String secretkey, String commandString){
		Mac mac;
		try {
			mac = Mac.getInstance ( "HmacSHA1" );
			SecretKeySpec keySpec = new SecretKeySpec(secretkey.getBytes(), "HmacSHA1");
			mac.init( keySpec );
			mac.update (commandString.toLowerCase().getBytes() );
			byte[] encryptedBytes = mac.doFinal();
			return Base64.encodeBase64String(encryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void addSignatureAndGetData(StringBuilder paramsStr) {
		// signature
		String signature = createSignature(SECRET_KEY, paramsStr.toString());
		paramsStr.append("&signature="+signature);
		
		String createRequestUrl = createRequestUrl(paramsStr.toString());
		System.out.println(createRequestUrl);
		
		System.out.println("######### RESULT ##########");
		String jsonString = restTemplate.getForObject(createRequestUrl, String.class);
		System.out.println(jsonString); 
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String,Object> map = mapper.readValue(jsonString, HashMap.class);
			System.out.println(map);
			Map<String,Object> map2 = (Map<String, Object>) map.get("listpublicipaddressesresponse");
			System.out.println(map2);
			System.out.println(map2.get("count"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
