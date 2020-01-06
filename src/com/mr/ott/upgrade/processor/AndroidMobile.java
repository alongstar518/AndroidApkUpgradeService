package com.mr.ott.upgrade.processor;

import java.io.File;

import com.mr.ott.upgrade.utility.FilesDirs;
import com.mr.ott.upgrade.utility.Logger;

public class AndroidMobile extends Android{

	private String sessionId = null;
	
	public AndroidMobile(String sessionId) {
		super(sessionId);
		this.sessionId = sessionId;
	}
	
	@Override
	public void upgradeDevice(String ip, String apkFileName, String pkgName) {
		Logger.logStatus("InProgress", sessionId);

		if(!adbConnect(ip))
		{
			Logger.logStatus("Failed,adb connect failed, make sure you have enable tcpip adb on your device by command: adb tcpip 5555", sessionId);
			return;
		}
		if(!adbStopApp(ip, pkgName))
		{
			Logger.logStatus("Failed, stop app Failed", sessionId);
			return;
		}
		
		File apkFile = FilesDirs.getBuildCacheDir(apkFileName);
		if(apkFile == null)
		{
			Logger.log(apkFileName + " doesn`t exist in build cache", sessionId);
			return;
		}
		
		if(!adbInstall(ip, apkFile.getAbsolutePath()))
		{
			Logger.logStatus("Failed, apk install failed", sessionId);
			return;
		}
	    adbDisconnect(ip);
	    Logger.logStatus("Success", sessionId);
	}
	
}
