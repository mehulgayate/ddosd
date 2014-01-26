package com.ddosd.api.web;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApiController {

	@RequestMapping("/user/{userId}")
	public ModelAndView showUserData(HttpServletRequest request,@PathVariable("userId") Long userId){
		ModelAndView mv=new ModelAndView("json-string");
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("userId","User id : "+userId);
		mv.addObject("userId",jsonObject);
		return mv;
	
	}
}
