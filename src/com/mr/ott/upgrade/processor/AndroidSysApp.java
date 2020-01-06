package com.mr.ott.upgrade.processor;

import java.io.File;
import java.io.FileFilter;

import com.mr.ott.upgrade.utility.FilesDirs;
import com.mr.ott.upgrade.utility.Logger;

public class AndroidSysApp extends Android{

	private String sessionId;
	private final String libPath = "/system/lib";
	
	public AndroidSysApp(String sessionId) {
		super(sessionId);
		this.sessionId = sessionId;
	}
	
	@Override
	public void upgradeDevice(String ip, String apkFileName, String pkgName) {
		Logger.logStatus("InProgress", sessionId);
		
		File opsDir = FilesDirs.getOpsDir(sessionId);
		File bldFile = FilesDirs.getBuildCacheDir(apkFileName);
		
		File tmpDir = new File(opsDir, "tmp");
		File libDir = new File(new File(tmpDir, "lib"),"armeabi");
				
		File[] libFiles = libDir.listFiles();
		if(libFiles == null)
		{
			libFiles = tmpDir.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().toLowerCase().endsWith(".so");
				}
			});
		}
		adbDisconnect(ip);
		
		if(!adbConnect(ip))
		{
			Logger.logStatus("Failed,adb connect failed, make sure you have enable tcpip adb on your device by command: adb tcpip 5555", sessionId);
			Logger.log("adb connect failed for Ip:" + ip, sessionId);
			tmpDir.delete();
			return;
		}
		
		if(System.getProperty("os.name").toLowerCase().contains("win"))
		{

			adbRoot(ip, 3000);
		}
		else
		{
			adbRoot(ip,defaultProcTimeOut);
			issueSysCommand(String.format("adb -s %s:5555 remount", ip),defaultProcTimeOut,null);
			adbDisconnect(ip);
		}
		if(!adbConnect(ip))
		{
			Logger.logStatus("Failed,adb connect failed, make sure you have enable tcpip adb on your device by command: adb tcpip 5555", sessionId);
			Logger.log("adb connect failed for Ip:" + ip, sessionId);
			tmpDir.delete();
			return;
		}
		adbRoot(ip, defaultProcTimeOut);
		issueSysCommand(String.format("adb -s %s:5555 remount", ip),defaultProcTimeOut,null);
		
		if(!adbStopApp(ip, pkgName))
		{
			Logger.logStatus("Failed, Stop APP " + pkgName, sessionId);
			tmpDir.delete();
			return;
		}
		
		issueSysCommand(String.format("adb -s %s:5555 shell rm -f /system/priv-app/EricssonTVApp/EricssonTVApp.apk", ip), defaultProcTimeOut,null);
		issueSysCommand(String.format("adb -s %s:5555 shell mv /system/priv-app/Netflix/Netflix.apk /sdcard/Netflix.apk", ip), defaultProcTimeOut,null);
		issueSysCommand(String.format("adb -s %s:5555 shell mv /system/priv-app/TV/TV.apk /sdcard/TV.apk", ip), defaultProcTimeOut,null);
		issueSysCommand(String.format("adb -s %s:5555 shell sync", ip), defaultProcTimeOut,null);
		
		if(!adbPush(ip, bldFile.getAbsolutePath(), "/system/priv-app/EricssonTVApp/EricssonTVApp.apk"))
		{
			Logger.log("Failed, push File: " + bldFile.getAbsolutePath(), sessionId);
			Logger.logStatus("Failed, push build Failed", sessionId);
			tmpDir.delete();
			return;
		}
		
		if(libFiles == null || libFiles.length <= 0)
		{
			Logger.log("Can not find lib file", sessionId);
			Logger.logStatus("Failed, no lib file", sessionId);
			tmpDir.delete();
			return;
		}
		
		for(File f : libFiles)
		{
			if(!adbPush(ip, f.getAbsolutePath(), libPath))
			{
				Logger.logStatus("Failed, push File: " + f.getName(), sessionId);
				tmpDir.delete();
				return;
			}
		}
		issueSysCommand(String.format("adb -s %s:5555 reboot", ip),defaultProcTimeOut,null);
		Logger.logStatus("Success", sessionId);
		adbDisconnect(ip);
		tmpDir.delete();
	}

}
