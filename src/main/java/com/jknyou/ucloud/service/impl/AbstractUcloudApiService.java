package com.jknyou.ucloud.service.impl;

import java.util.Collections;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractUcloudApiService{
	private static final String API_KEY = "apiKey=yourKey";
	private static final String SECRET_KEY = "yourKey";
	private static final String RES_TYPE = "response=json";
	
	@Inject private RestTemplate restTemplate;

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

	private String createSignature(String secretkey, String commandString){
		try {
			Mac mac = Mac.getInstance ( "HmacSHA1" );
			mac.init(new SecretKeySpec(secretkey.getBytes(), "HmacSHA1"));
			mac.update (commandString.toLowerCase().getBytes() );
			return Base64.encodeBase64String(mac.doFinal());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected String getData(List<String> paramList) {
		StringBuilder paramsStr = makeParamsStr(paramList);
		paramsStr.append("&signature="+createSignature(SECRET_KEY, paramsStr.toString()));
		return restTemplate.getForObject(createRequestUrl(paramsStr.toString()), String.class);
	}

	protected abstract String createRequestUrl(String paramsStr);
}
