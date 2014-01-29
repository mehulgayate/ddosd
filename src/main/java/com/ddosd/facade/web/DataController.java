package com.ddosd.facade.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ddosd.facade.entity.User;
import com.ddosd.facade.entity.User.UserStatus;
import com.evalua.entity.support.DataStoreManager;

@Controller
public class DataController {
	
	@Resource
	private DataStoreManager  dataStoreManager;

	@RequestMapping("/seedData")
	public ModelAndView seedUser(){
		ModelAndView mv= new ModelAndView("json-string");
		
		User user=new User();
		user.setEmail("mehul@gmail.com");
		user.setFirstName("Mehul");
		user.setLastName("Gayate");
		user.setPassword("123");
		user.setStatus(UserStatus.ACTIVE);
		user.setTrustScore(20);
		dataStoreManager.save(user);
		return mv;
	}
}
