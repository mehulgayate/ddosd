package com.ddosd.facade.entity;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.ddosd.facade.entity.UserSession.SessionStatus;

@Transactional
public class FacadeRepository {

	@Resource
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	Session getSession(){
		return sessionFactory.getCurrentSession();
	}

	public User findUserByEmail(String email){
		return (User) getSession().createQuery("From "+User.class.getName()+" where email=:email")
				.setParameter("email",email).uniqueResult();
	}

	public User findUserById(Long id){
		return (User) getSession().createQuery("From "+User.class.getName()+" where id=:id")
				.setParameter("id",id).uniqueResult();	
	}
	
	public com.ddosd.facade.entity.Session findActiveSessionByUser(User user){
		return (com.ddosd.facade.entity.Session) getSession().createQuery("From "+UserSession.class.getName()+" where user=:user AND status=:status")
				.setParameter("user",user)
				.setParameter("status", SessionStatus.ACTIVE).uniqueResult();	
	}

}
