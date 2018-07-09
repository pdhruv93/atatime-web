package com.heroku.dhruv.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DBJob implements Job
{
	public void execute(JobExecutionContext context)
	throws JobExecutionException {
		
		System.out.println("Starting Cron Job for clearing DB Tables");	
		DBWork.startCron();
		
	}
	
}