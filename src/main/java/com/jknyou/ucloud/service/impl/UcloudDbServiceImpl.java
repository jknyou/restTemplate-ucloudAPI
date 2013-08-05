package com.jknyou.ucloud.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jknyou.ucloud.service.UcloudDbService;

@Service 
public class UcloudDbServiceImpl extends AbstractUcloudApiService implements UcloudDbService {
	private static final String API_URL = "https://api.ucloudbiz.olleh.com/db/v1/client/api?";

	@Override
	public String sendRequest(List<String> paramList) {
		return getData(paramList);
	}

	@Override
	protected String createRequestUrl(String paramsStr) {
		return API_URL+paramsStr;
	}
}
