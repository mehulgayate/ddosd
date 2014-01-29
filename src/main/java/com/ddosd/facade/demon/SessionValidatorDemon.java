package com.ddosd.facade.demon;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ddosd.facade.BuffredThread;
import com.ddosd.facade.BuffredThreadQueue;
import com.ddosd.facade.entity.UserSession;
import com.ddosd.facade.entity.UserSession.SessionStatus;

public class SessionValidatorDemon extends TimerTask{
	
	public static Logger logger = LoggerFactory.getLogger(SessionValidatorDemon.class);


	private SessionFactory sessionFactory;

	public static Timer sessionTimer = new Timer("Session Validator", true);    
	private int initialDelay; 
	private int period;


	public SessionValidatorDemon(SessionFactory sessionFactory,int initialDelay,int period){
		this.sessionFactory=sessionFactory;
		this.initialDelay=initialDelay;
		this.period=period;
	}


	public void init() {
		logger.info("Scedulaing Session Validator *******");
		sessionTimer.schedule(this, initialDelay,period);
	}


	@Override
	public void run() {
		logger.info("Starting Session Validator *******");
		Session hibernateSession=sessionFactory.openSession();
		Query query=hibernateSession.createQuery("select session From "+UserSession.class.getName()+" us where us.status=:status");
		query.setParameter("status", SessionStatus.ACTIVE);
		List<com.ddosd.facade.entity.Session> userSessions=query.list();
		
		for (com.ddosd.facade.entity.Session session : userSessions) {
			if(session.getStartTime().before(getChangedDateByMins(new Date(),-10))){
				session.setEndTime(new Date());
				Query queryBlock=hibernateSession.createQuery("From "+UserSession.class.getName()+" us where session=:session");
				query.setParameter("session", session);
				UserSession userSession=(UserSession) queryBlock.uniqueResult();
				userSession.setStatus(SessionStatus.INACTIVE);
				hibernateSession.update(session);
				hibernateSession.update(userSession);
			}
		}
	}
	
	private Date getChangedDateByMins(Date date,int change){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, change);
		return calendar.getTime();
	}

}
