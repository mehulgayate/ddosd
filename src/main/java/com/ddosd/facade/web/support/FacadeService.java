package com.ddosd.facade.web.support;

import com.ddosd.facade.entity.AccessToken;
import com.ddosd.facade.entity.User;

import net.sf.json.JSONObject;

public class FacadeService {
	
	public JSONObject getErrorResponse(Long errorCode,String errorString){
		JSONObject jsonObject=new JSONObject();
		JSONObject innerJsonObject=new JSONObject();
		innerJsonObject.put("code", errorCode);
		innerJsonObject.put("msg", errorString);
		jsonObject.put("error", innerJsonObject);
		return jsonObject;
	}
	
	public JSONObject getAccessTokenResponse(AccessToken accessToken,User user){
		JSONObject jsonObject=new JSONObject();
		JSONObject innerJsonObject=new JSONObject();
		innerJsonObject.put("user_id", user.getId());
		innerJsonObject.put("access_Token", accessToken.getAccessToken());
		jsonObject.put("access_Token", innerJsonObject);
		return jsonObject;
	}

}
