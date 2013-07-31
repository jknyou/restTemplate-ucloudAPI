package com.jknyou.ucloud.service;

import java.util.List;

public interface UcloudService {
	static final String API_KEY = "apiKey=yourkey";
	static final String SECRET_KEY = "yourkey";
	static final String API_URL = "https://api.ucloudbiz.olleh.com/server/v1/client/api?";
	static final String RES_TYPE = "response=json";
	
	String sendRequest(List<String> paramList);
}
