package com.ddosd.facade.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ddosd.facade.entity.User;
import com.evalua.entity.support.DataStoreManager;

@Controller
public class FacadeResponseController {
	
	@Resource
	private DataStoreManager dataStoreManager;

	@RequestMapping("/seed-users")
	public ModelAndView seedUsers(){
		ModelAndView mv=new ModelAndView("redirect:/login");
		User user=new User();
		user.setEmail("mehulgayate21@gmail.com");
		user.setPassword("123");
		user.setFirstName("Mehul");
		user.setLastName("Gayate");
		dataStoreManager.save(user);
		return mv;
	}
	
	@RequestMapping("/login")
	public ModelAndView login(){
		ModelAndView mv=new ModelAndView("login");
		
		return mv;
	}
	
	
	@RequestMapping("/admin-login")
	public ModelAndView showAdminLogin(){
		ModelAndView mv=new ModelAndView("in-admin-panel/login");
		
		return mv;
	}
	
	
	@RequestMapping("/admin")
	public ModelAndView showAdminScreen(){
		ModelAndView mv=new ModelAndView("in-admin-panel/index");
		
		return mv;
	}
}
