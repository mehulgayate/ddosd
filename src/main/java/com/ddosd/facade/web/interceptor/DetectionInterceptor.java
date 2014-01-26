package com.ddosd.facade.web.interceptor;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ddosd.facade.entity.AccessToken;
import com.ddosd.facade.entity.User;
import com.ddosd.facade.service.UserService;
import com.ddosd.facade.web.support.FacadeService;
import com.evalua.entity.support.DataStoreManager;

public class DetectionInterceptor implements HandlerInterceptor {

	@Resource
	private UserService userService;	

	@Resource
	private FacadeService facadeService;
	
	@Resource
	private DataStoreManager dataStoreManager;	

	public void setDataStoreManager(DataStoreManager dataStoreManager) {
		this.dataStoreManager = dataStoreManager;
	}

	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		System.out.println("*********** "+requestURI);
		if(requestURI.indexOf("login")>=0){
			String email=request.getParameter("email");
			String password=request.getParameter("password");
			User user=userService.validate(email, password);
			response.setContentType("application/json");
			PrintWriter out=response.getWriter();

			if(user!=null){
				AccessToken accessToken=user.getAccessToken();
				if(accessToken==null){
					accessToken=AccessToken.generateToken(user);
					dataStoreManager.save(accessToken);
					user.setAccessToken(accessToken);
					dataStoreManager.save(user);					
				}
				out.print(facadeService.getAccessTokenResponse(accessToken, user));
				out.flush();
			}else{
				out.print(facadeService.getErrorResponse(new Long(101), "email / password wrong."));
				out.flush();
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
					throws Exception {
		// TODO Auto-generated method stub

	}

}
