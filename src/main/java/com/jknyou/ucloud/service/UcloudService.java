package com.jknyou.ucloud.service;

import java.util.List;

public interface UcloudService {
	static final String API_KEY = "apiKey=jlhr1am7QgJjECqhsyDrrwmLeP9IA_LSlHaDZWqqgncoFrzu0goKupviFTM1xNwIRTvJR6nphtcJh_SOUBKtXg";
	static final String SECRET_KEY = "WbyrssRwkNvgIKFw_VX_pv4lyWG1Fx7FVqg8Fm1ShAKXPmAml3jDdm1eqtBEMLqPjh9dPTTIbcDE2k0yYEwTpA";
	static final String API_URL = "https://api.ucloudbiz.olleh.com/server/v1/client/api?";
	static final String RES_TYPE = "response=json";
	
	String sendRequest(List<String> paramList);
}
