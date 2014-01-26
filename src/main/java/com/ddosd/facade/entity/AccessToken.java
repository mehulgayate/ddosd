package com.ddosd.facade.entity;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.apache.commons.lang.StringUtils;

import com.evalua.entity.support.EntityBase;

public class AccessToken extends EntityBase{

	private String accessToken;
	private User user;

	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public static String generateToken(User user){
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}


}
