package com.ddosd.facade.web;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.ddosd.facade.entity.AccessToken;
import com.ddosd.facade.entity.DemonEvent.DemonType;
import com.ddosd.facade.entity.FacadeRepository;
import com.ddosd.facade.entity.FileAttachment;
import com.ddosd.facade.entity.Session;
import com.ddosd.facade.entity.User;
import com.ddosd.facade.entity.UserSession;
import com.ddosd.facade.entity.User.UserRole;
import com.ddosd.facade.entity.User.UserStatus;
import com.ddosd.facade.entity.UserSession.SessionStatus;
import com.ddosd.facade.entity.support.UserForm;
import com.ddosd.facade.service.FileUploadForm;
import com.ddosd.facade.service.XMLParser;
import com.ddosd.facade.web.support.FacadeService;
import com.evalua.entity.support.DataStoreManager;

@Controller
public class FacadeResponseController {

	@Resource
	private DataStoreManager dataStoreManager;

	@Resource
	private FacadeService facadeService;

	@Resource
	private FacadeRepository facadeRepository;

	@RequestMapping("/seed-admin")
	public ModelAndView seedUsers(){
		ModelAndView mv=new ModelAndView("redirect:/user/login");
		User user=facadeRepository.findUserByEmail("admin@dgmail.com");
		if(user==null){
		user=new User();
		user.setEmail("admin@gmail.com");
		user.setPassword("1234");
		user.setName("Mehul Gayate");
		user.setStatus(UserStatus.ACTIVE);
		user.setRole(UserRole.ADMIN);
		dataStoreManager.save(user);
		}
		return mv;
	}

	@RequestMapping("/login")
	public ModelAndView login(HttpSession httpSession){
		ModelAndView mv=new ModelAndView("login");
		User userl=(User) httpSession.getAttribute("user");
		if(userl!=null){
			return new ModelAndView("redirect:/admin");
		}
		return mv;
	}
	

	@RequestMapping("/")
	public ModelAndView home(HttpSession httpSession){
		ModelAndView mv=new ModelAndView("index");
		
		return mv;
	}


	@RequestMapping("/admin-login")
	public ModelAndView showAdminLogin(){
		ModelAndView mv=new ModelAndView("in-admin-panel/login");

		return mv;
	}	


	@RequestMapping("/admin")
	public ModelAndView showAdminScreen(HttpSession httpSession){
		
		
		ModelAndView mv=new ModelAndView("in-admin-panel/index");
		List<User> users=facadeRepository.listAllUsers();

		for (User user : users) {
			Session currentSession=facadeRepository.findUserRequestCount(user);
			if(currentSession!=null){
				user.setLastSessionCount(currentSession.getRequestCount());
			}
		}
		mv.addObject("users", users);
		return mv;
	}
	
	@RequestMapping("/files")
	public ModelAndView files(HttpSession httpSession){
		
		
		ModelAndView mv=new ModelAndView("in-admin-panel/files");
		List<FileAttachment> users=facadeRepository.listAllFileAttachments();

		
		mv.addObject("users", users);
		return mv;
	}


	@RequestMapping("/blocked-users")
	public ModelAndView showBlockedUsersScreen(HttpSession httpSession){
		
		ModelAndView mv=new ModelAndView("in-admin-panel/blocked-users");
		List<User> users=facadeRepository.listAllBlockedUsers();

		for (User user : users) {
			Session currentSession=facadeRepository.findUserRequestCount(user);
			if(currentSession!=null){
				user.setLastSessionCount(currentSession.getRequestCount());
			}
		}
		mv.addObject("users", users);
		return mv;
	}

	@RequestMapping("/register")
	public ModelAndView register(){
		ModelAndView mv=new ModelAndView("signup/signup");

		return mv;
	}
	
	@RequestMapping("/signup-retry")
	public ModelAndView retry(){
		ModelAndView mv=new ModelAndView("signup/retry");

		return mv;
	}

	@RequestMapping("/register/add")
	public ModelAndView registerNewUser(HttpServletRequest request,@ModelAttribute(UserForm.key) UserForm userForm){
		ModelAndView mv=new ModelAndView("signup/complete");
		
		if(StringUtils.isNotBlank(userForm.getEmail()) && StringUtils.isNotBlank(userForm.getPassword())){
			User user=facadeRepository.findUserByEmail(userForm.getEmail());
			if(user!=null){
				return new ModelAndView("redirect:/signup-retry");
			}
			facadeService.addUser(userForm);
		}
		return mv;
	}


	@RequestMapping("/user/login")
	public ModelAndView userLogin(HttpServletRequest request){
		ModelAndView mv=new ModelAndView("service-invoke/login");
		return mv;
	}

	@RequestMapping("/invoke-service")
	public ModelAndView invokePage(HttpServletRequest request,@RequestParam String accessToken,@RequestParam String userId){
		ModelAndView mv=new ModelAndView("service-invoke/invoke");
		mv.addObject("userId", userId);
		mv.addObject("accessToken", accessToken);
		return mv;
	}
	
	@RequestMapping("/invoke-xml")
	public ModelAndView invokeXMLPage(HttpServletRequest request,
			@RequestParam String accessToken, @RequestParam String userId) {
		ModelAndView mv = new ModelAndView("service-invoke/invoke-xml");
		mv.addObject("userId", userId);
		mv.addObject("accessToken", accessToken);
		return mv;
	}
	
	@RequestMapping("/admin/delete-user")
	public ModelAndView deleteUser(HttpServletRequest request,@RequestParam Long userId){
		ModelAndView mv=new ModelAndView("redirect:/admin");
		User user=facadeRepository.findUserById(userId);
		user.setStatus(UserStatus.DELETED);
		dataStoreManager.save(user);
		return mv;
	}
	
	@RequestMapping("/admin/activate-user")
	public ModelAndView unblockUser(HttpServletRequest request,@RequestParam Long userId){
		ModelAndView mv=new ModelAndView("redirect:/admin");
		User user=facadeRepository.findUserById(userId);
		user.setStatus(UserStatus.ACTIVE);
		AccessToken accessToken=user.getAccessToken();
		if(accessToken!=null){
			user.setAccessToken(null);
			UserSession userSession=facadeRepository.findActiveUserSessionByUser(user);
			userSession.setStatus(SessionStatus.INACTIVE);
			dataStoreManager.save(userSession);
			dataStoreManager.delete(accessToken);
		}
		dataStoreManager.save(user);
		return mv;
	}
	
	@RequestMapping("/admin/activate-monitor")
	public ModelAndView demonMonitor(HttpServletRequest request,HttpSession httpSession){
		
		ModelAndView mv=new ModelAndView("in-admin-panel/demon-monitor");
		mv.addObject("queueDemon",facadeRepository.findLatestDemonEvent(DemonType.BUFFERED_REQUEST_DEMON));
		mv.addObject("sessionDemon",facadeRepository.findLatestDemonEvent(DemonType.SESSION_VALIDATOR_DEMON));
//		mv.addObject("sessionDemon",facadeRepository.findLatestDemonEvent(DemonType.SESSION_VALIDATOR_DEMON));


		return mv;
	}
	
	@RequestMapping("/logout")
	public ModelAndView logout(HttpSession session){
		session.invalidate();
		return new ModelAndView("redirect:/user/login");
	}
	
	@RequestMapping("/upload-xml")
	public ModelAndView uploadFile(@ModelAttribute(FileUploadForm.key) FileUploadForm fileUploadForm)throws Exception{
		ModelAndView mv=new ModelAndView("new/upload-result");
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fileUploadForm.getXmlFile().getInputStream());
			XMLParser.parseXML(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return mv;
	}
	
	@RequestMapping("/new-attachment")
	public ModelAndView addPost(@ModelAttribute FileAttachment fileAttachment, HttpSession session,HttpServletRequest request) throws SAXException, IOException, ParserConfigurationException{
		
		boolean valid = false;

		if (request instanceof MultipartHttpServletRequest) {
			System.out.println("**** XML file is uploaded");
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");		
			
			if (file != null && !file.isEmpty()) {				
					
					//Create file attachment
					fileAttachment.setAttachment(file.getBytes());
					fileAttachment.setOri(file.getBytes());
					fileAttachment.setMimeType(file.getContentType());
					
					fileAttachment.setName(file.getOriginalFilename());
					
					String[] splited=file.getOriginalFilename().split("\\.");
					String extention= splited[splited.length-1];
					fileAttachment.setExtension(extention);					
					//Encrypt file data
					fileAttachment.setAttachment(file.getBytes());
					
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(file.getInputStream());
					int length = XMLParser.parseXML(doc);
					System.out.println("******* total nested length is "+length);
			
					if(length < facadeRepository.getXmlTagLimit()){
						System.out.println("******* total nested length is more than configured limit");
						System.out.println("******* Marking file invalid, will not be processed");
						valid = true;
						fileAttachment.setValid(true);
					}else{
						System.out.println("******* total nested length is less than configured limit");
						System.out.println("******* Marking file valid, will be processed");
					}
					dataStoreManager.save(fileAttachment);		
			}
		}
		return new ModelAndView("file-upload-done").addObject("valid", valid);
		
	}
}
