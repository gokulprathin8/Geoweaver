package com.gw.workers;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.gw.jpa.ExecutionStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gw.tasks.Task;
import com.gw.utils.RandomString;

/**
 *Class Worker.java
 *@author ziheng
 */
@Service
public class Worker extends Thread{

	private String name;
	
	private Task t;
	
	private boolean status; //true: working; false: idle
	
	private boolean is_temp;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	
	public Worker() {
		
		this(false);
		
	}
	
	public Worker(boolean temporary) {
		
		is_temp = temporary;
		
		name = "Worker-" + new RandomString(5).nextString();
		
	}
	
	public Worker(Task t){
		this();
		setTask(t);
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	

	public void setTask(Task t) {

		// this.printoutCallStack();
		
		this.t = t;
		
		this.setStatus(true);

		logger.debug("task "+t.getName()+" is assigned to a worker: " + name);
		
//		this.notifyAll();
		
	}
	
	public Task getTask(){
		return t;
	}
	
	private void unloadTask() {
		
//		System.out.println("task unloaded\n notify the manager that a work is freed");
		
		this.t = null;
		
		this.setStatus(false);
		
//		wm.notifyWorkerManager(this);
		
	}
	
	@Override
	public void run() {
		//it should not be like this. The thread should not exit but wait for next task arrives 
		//so that the time cost of starting a new thread (very long) can be saved. 
//		System.out.println("A worker thread "+name+" is started.");
		try {
			
			boolean running = true;
		    
			while(running) {
				
				if(this.t!=null) {
					String executionStatus = t.execute();
					t.responseCallback();
					unloadTask();
					System.out.println(executionStatus);
                    if (Objects.equals(executionStatus, ExecutionStatus.FAILED)) {
						running = false;
						t.failureCallback(new Exception("Failed to execute process."));
					}
				}
		        
		        if (Thread.interrupted() || is_temp) {
		        	logger.debug("thread " + name + " interrupted or the worker is temporary");
		        	running = false;
		        }
		        
		        TimeUnit.SECONDS.sleep(1);
		    }
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			if(this.t!=null) {
				
				t.failureCallback(e);
				
				unloadTask();
			}
				
			
		}
		
		logger.debug("Worker "+name+" is stopped.");
	}
}
