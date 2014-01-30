package com.ddosd.facade.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ddosd.facade.entity.User;
import com.ddosd.facade.entity.User.UserRole;
import com.ddosd.facade.entity.User.UserStatus;
import com.ddosd.facade.entity.support.UserForm;
import com.ddosd.facade.web.support.FacadeService;
import com.evalua.entity.support.DataStoreManager;

@Controller
public class FacadeResponseController {
	
	@Resource
	private DataStoreManager dataStoreManager;
	
	@Resource
	private FacadeService facadeService;

	@RequestMapping("/seed-admin")
	public ModelAndView seedUsers(){
		ModelAndView mv=new ModelAndView("redirect:/login");
		User user=new User();
		user.setEmail("mehulgayate21@gmail.com");
		user.setPassword("123");
		user.setName("Mehul Gayate");
		user.setStatus(UserStatus.ACTIVE);
		user.setRole(UserRole.ADMIN);
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
	
	@RequestMapping("/register")
	public ModelAndView register(){
		ModelAndView mv=new ModelAndView("signup/signup");
		
		return mv;
	}
	
	@RequestMapping("/register/add")
	public ModelAndView registerNewUser(HttpServletRequest request,@ModelAttribute(UserForm.key) UserForm userForm){
		ModelAndView mv=new ModelAndView("signup/complete");
		facadeService.addUser(userForm);
		return mv;
	}
}
