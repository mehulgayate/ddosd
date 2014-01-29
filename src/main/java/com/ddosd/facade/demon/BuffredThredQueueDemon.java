package com.ddosd.facade.demon;

import java.util.Timer;
import java.util.TimerTask;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ddosd.facade.BuffredThread;
import com.ddosd.facade.BuffredThreadQueue;

public class BuffredThredQueueDemon extends TimerTask{


	public static Logger logger = LoggerFactory.getLogger(SessionValidatorDemon.class);

	public static Timer sessionTimer = new Timer("Buffered Thread Schudular", true);   

	private int initialDelay; 
	private int period;
	
	
	public BuffredThredQueueDemon(int initialDelay,int period){
		this.initialDelay=initialDelay;
		this.period=period;
	}


	public void init() {
		logger.info("Scedulaing Buffered Thread Schudular *******");
		sessionTimer.schedule(this, initialDelay,period);
	}

	@Override
	public void run() {
		
		logger.info("Running Buffered Thread Schudular *******");
		
		for(int i=0;i<BuffredThreadQueue.buffredThreads.size();i++){
			BuffredThread buffredThread=BuffredThreadQueue.buffredThreads.poll();
			buffredThread.getThread().notify();
		}		
	}

}
