package com.mr.ott.upgrade.processor;

import com.mr.ott.upgrade.utility.Logger;

public class GetVersionThread implements Runnable {
	private String deviceIp;
	private String deviceType;
	private String pkgName;
	private String sessionId;
	
	public GetVersionThread(String deviceIp, String deviceType, String pkgName,String sessionId)
	{
		this.deviceIp=deviceIp;
		this.deviceType = deviceType;
		this.pkgName= pkgName;
		this.sessionId = sessionId;
	}
	
	public void run()
	{
		if(deviceType.contains("android"))
		{
			Android a = new Android(sessionId);
			String version = deviceIp + "=" +a.adbGetVersions(deviceIp, pkgName);
			
			Logger.WriteFileVersion(version, sessionId);
		}
	}
}
