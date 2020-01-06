package com.mr.ott.upgrade.processor;

import java.util.ArrayList;
import java.util.List;

import com.mr.ott.upgrade.utility.Logger;

public class GetVersionProcessor {
	String deviceIpString;
	String deviceType;
	String pkgName;
	String sessionId;
		
	public GetVersionProcessor(String sessionId)
	{
		String param = Logger.getParamFromFile(sessionId);
		String[] params = param.split("\n");
		try
		{
			for(String p : params)
			{
				if(p.contains("deviceIpString"))
				{
					this.deviceIpString = p.split("=")[1];
					continue;
				}
				if(p.contains("deviceType"))
				{
					this.deviceType = p.split("=")[1];
					continue;
				}
				if(p.contains("pkgName"))
				{
					this.pkgName = p.split("=")[1];
					continue;
				}
			}
			this.sessionId = sessionId;
		}
		catch(Exception e)
		{
			Logger.log("Error when init GetVersionProcessor, error: " + e.getMessage(), sessionId);
		}
	}
	
	public void getVersion()
	{
		String[] Ips = deviceIpString.split(",");
		List<Thread> threadList = new ArrayList<>();
		for(String ip : Ips)
		{
			Runnable r = new GetVersionThread(ip, deviceType, pkgName, sessionId);
			new Thread(r).start();
		}
		
		int count = Ips.length;
		long tick1 = System.currentTimeMillis();
		long tick2 = 0;
		while(count > 0)
		{
			tick2 = System.currentTimeMillis();
			if(tick2 - tick1 > 30000)
			{
				Logger.log("Time Out when try to get versions", sessionId);
			}
			
			for(Thread t : threadList)
			{
				if(!t.isAlive())
					count--;
			}
		}
	}
}
