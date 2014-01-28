package com.ddosd.facade.web.interceptor;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ddosd.facade.entity.AccessToken;
import com.ddosd.facade.entity.FacadeRepository;
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

	@Resource
	private FacadeRepository repository;	


	public void setRepository(FacadeRepository repository) {
		this.repository = repository;
	}

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
		PrintWriter out=response.getWriter();

		System.out.println("*********** "+requestURI);
		if(requestURI.indexOf("login")>=0){
			String email=request.getParameter("email");
			String password=request.getParameter("password");
			User user=userService.validate(email, password);
			response.setContentType("application/json");

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
				return false;
			}
		}else {
			String userIdString=request.getParameter("userId");
			if(userIdString==null){
				out.print(facadeService.getErrorResponse(new Long(102), "Required Param userId Missing"));
				out.flush();
				return false;
			}
			User user=repository.findUserById(new Long(userIdString));
			if(user==null){
				out.print(facadeService.getErrorResponse(new Long(103), "User Not Found with id : "+userIdString));
				out.flush();
				return false;
			}
			if(request.getParameter("access_token")==null | user.getAccessToken().getAccessToken()==null | !user.getAccessToken().getAccessToken().equals(request.getParameter("access_token"))){
				out.print(facadeService.getErrorResponse(new Long(103), "Acess Token Not Valid : "+userIdString));
				out.flush();
				return false;
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
