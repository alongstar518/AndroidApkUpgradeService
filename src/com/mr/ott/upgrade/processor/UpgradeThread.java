package com.mr.ott.upgrade.processor;

public class UpgradeThread implements Runnable{
	
	private String deviceIp;
	private String fileName;
	private String deviceType;
	private String pkgName;
	private String sessionId;
	
	public UpgradeThread(String deviceIp, String fileName, String deviceType, String pkgName,String sessionId)
	{
		this.deviceIp=deviceIp;
		this.fileName=fileName;
		this.deviceType = deviceType;
		this.pkgName= pkgName;
		this.sessionId = sessionId;
	}
	
	public void run()
	{
		Android a = null;
		if(deviceType.equals("androidmobile"))
			a = new AndroidMobile(sessionId);
		else if(deviceType.equals("androidsys"))
			a = new AndroidSysApp(sessionId);
		else
		{
			return;
		}
		a.upgradeDevice(deviceIp, fileName, pkgName);
	}
}
