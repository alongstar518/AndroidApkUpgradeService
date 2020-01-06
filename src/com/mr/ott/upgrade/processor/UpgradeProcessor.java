package com.mr.ott.upgrade.processor;

import com.mr.ott.upgrade.utility.FilesDirs;
import com.mr.ott.upgrade.utility.Logger;

public class UpgradeProcessor implements Runnable{
	
	String deviceIpString;
	String fileName;
	String deviceType;
	String pkgName;
	String sessionId;
	
	public UpgradeProcessor(String deviceIpString, String fileName, String deviceType, String pkgName, String sessionId)
	{
		this.deviceIpString = deviceIpString;
		this.fileName = fileName;
		this.deviceType = deviceType;
		this.pkgName = pkgName;
		this.sessionId = sessionId;
		
//		Logger.SaveParmForSession("deviceIpString="+deviceIpString + "\n" + "deviceType=" + deviceType + "\n" + "pkgName="+pkgName, sessionId);
	}
	
//	public void upgrade()
//	{
//		String[] Ips = deviceIpString.split(",");
//		
//		if(deviceType.equals("androidsys"))
//			FilesDirs.prepareBuild(sessionId, fileName);
//
//		for(String ip : Ips)
//		{
//			Runnable r = new UpgradeThread(ip, fileName, deviceType, pkgName, sessionId);
//			new Thread(r).start();
//		}
//
//	}

	@Override
	public void run() {
		
		Logger.SaveParmForSession("deviceIpString="+deviceIpString + "\n" + "deviceType=" + deviceType + "\n" + "pkgName="+pkgName, sessionId);
		String[] Ips = deviceIpString.split(",");
		
		if(deviceType.equals("androidsys"))
			if(!FilesDirs.prepareBuild(sessionId, fileName))
				return;

		for(String ip : Ips)
		{
			Runnable r = new UpgradeThread(ip, fileName, deviceType, pkgName, sessionId);
			new Thread(r).start();
		}
		
	}
	
}
