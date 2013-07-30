package com.jknyou.ucloud.service.impl;

import java.util.Collections;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jknyou.ucloud.service.UcloudService;

@Service
public class UcloudServiceImpl implements UcloudService {
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

	
	private String createRequestUrl(String paramsStr) {
		return API_URL + paramsStr;
	}

	private String createSignature(String secretkey, String commandString){
		try {
			Mac mac = Mac.getInstance ( "HmacSHA1" );;
			mac.init(new SecretKeySpec(secretkey.getBytes(), "HmacSHA1"));
			mac.update (commandString.toLowerCase().getBytes() );
			return Base64.encodeBase64String(mac.doFinal());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String addSignatureAndGetData(StringBuilder paramsStr) {
		paramsStr.append("&signature="+createSignature(SECRET_KEY, paramsStr.toString()));
		String jsonString = restTemplate.getForObject(createRequestUrl(paramsStr.toString()), String.class);
		return jsonString;
	}

	@Override
	public String sendRequest(List<String> paramList) {
		return addSignatureAndGetData(makeParamsStr(paramList));
	}
}
