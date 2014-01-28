package com.ddosd.facade.web.support;

import com.ddosd.facade.BuffredThread;
import com.ddosd.facade.BuffredThreadQueue;
import com.ddosd.facade.FacadeConfig;
import com.ddosd.facade.entity.AccessToken;
import com.ddosd.facade.entity.FacadeRepository;
import com.ddosd.facade.entity.Session;
import com.ddosd.facade.entity.User;
import com.ddosd.facade.entity.User.UserStatus;
import com.evalua.entity.support.DataStoreManager;

import net.sf.json.JSONObject;

public class FacadeService {

	private FacadeConfig config;	

	private FacadeRepository repository;	
	
	private DataStoreManager dataStoreManager;	

	public void setDataStoreManager(DataStoreManager dataStoreManager) {
		this.dataStoreManager = dataStoreManager;
	}

	public void setRepository(FacadeRepository repository) {
		this.repository = repository;
	}

	public void setConfig(FacadeConfig config) {
		this.config = config;
	}

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

	public boolean checkUserForDdosAttack(User user){

		Session session=repository.findActiveSessionByUser(user);
		if((user.getTrustScore()+config.getThreshod())>session.getRequestCount()){
			if((user.getTrustScore()+config.getThreshod()+config.getBufferSize())<=session.getRequestCount()){
				Thread currentThread=Thread.currentThread();
				BuffredThread buffredThread=new BuffredThread();
				buffredThread.setThread(currentThread);
				buffredThread.setPriority(1);
				buffredThread.setUser(user);
				BuffredThreadQueue.buffredThreads.offer(buffredThread);
				try {
					currentThread.wait();
					System.out.println("Thread in Buffer  for User Id ****** "+user.getId());
					System.out.println("Thread Size is now : "+ BuffredThreadQueue.buffredThreads.size());
				} catch (InterruptedException e) {
					System.out.println("Unable to wait thread ******");
					e.printStackTrace();
				}
			}else{
				user.setStatus(UserStatus.BLOCKED);
				dataStoreManager.save(user);
				return false;
			}
		}

		return true;
	}

}
