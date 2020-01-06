package com.mr.ott.upgrade.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import com.mr.ott.upgrade.utility.Logger;

public class Android {
	
	public int defaultProcTimeOut = 300000;
	public String sessionId = null;
	
	public Android(String sessionId)
	{
		this.sessionId = sessionId;
	}
	
	public boolean adbConnect(String ip)
	{
		return issueSysCommand(String.format("adb connect %s", ip),defaultProcTimeOut,Error_adbConnect);
	}
	
	public boolean adbRoot(String ip, int waitTime)
	{
		return issueSysCommand(String.format("adb -s %s:5555 root", ip),2000,null);
	}
	
	
	public boolean adbPush(String ip, String src, String dst)
	{
		return issueSysCommand(String.format("adb -s %s:5555 push %s %s", ip, src, dst),defaultProcTimeOut,Error_adbPush);
	}
	
	public boolean adbInstall(String ip, String apkPath)
	{
		return issueSysCommand(String.format("adb -s %s:5555 install -r %s", ip, apkPath),defaultProcTimeOut,Error_adbInstall);
	}
	
	public boolean adbStopApp(String ip, String pkgName)
	{
		return issueSysCommand(String.format("adb -s %s:5555 shell am force-stop %s", ip,pkgName), defaultProcTimeOut,Error_adbStopApp);
	}
	
	public boolean adbDisconnect(String ip)
	{
		return issueSysCommand(String.format("adb -s %s:5555 disconnect", ip), defaultProcTimeOut,null);
	}
	
	public String adbGetVersions(String ip, String pkgName)
	{
		return issueSysCommandGetOutput(String.format("adb -s %s:5555 shell dumpsys package %s | grep versionName", ip,pkgName), defaultProcTimeOut,null);
	}
	
	public void upgradeDevice(String ip, String apkPath, String pkgName)
	{
		// This will be override by AndroidMobile and AndroidSysApp class
	}
	
	public boolean issueSysCommand(String cmd,int timeOutKill,String errorMsg)
	{
		boolean ret = true;
		Logger.log("Issuing Command " + cmd+ " timeout:" + timeOutKill, sessionId);
		Runtime rt = Runtime.getRuntime();
		Process proc = null;
		try {
			proc = rt.exec(cmd);
			InputStream input = proc.getInputStream();
			BufferedReader reader =
					new BufferedReader(new InputStreamReader(proc.getInputStream()));
			
			String output = reader.readLine();
			while(output != null)
			{
				Logger.log(output,sessionId);
				ret = checkCmdSuccess(input, errorMsg);
				output = reader.readLine();
			}
			
			proc.waitFor(timeOutKill, TimeUnit.MILLISECONDS);
			
			if(proc.isAlive())
			{
				proc.destroy();
				if(timeOutKill == defaultProcTimeOut)
				{
					Logger.log("Operation TimeOut for Session " + sessionId ,sessionId);
					return false;
				}
			}

		} catch (Exception e) {
			Logger.log(e.getMessage(), sessionId);
		}

		return ret;
		
	}
	
	public String issueSysCommandGetOutput(String cmd,int timeOutKill,String errorMsg)
	{
		String ret = null;
		Logger.log("Issuing Command " + cmd+ " timeout:" + timeOutKill, sessionId);
		Runtime rt = Runtime.getRuntime();
		Process proc = null;
		try {
			proc = rt.exec(cmd);
			
			ret = IOUtils.toString(proc.getInputStream());
			
			proc.waitFor(timeOutKill, TimeUnit.MILLISECONDS);
			
			if(proc.isAlive())
			{
				proc.destroy();
				if(timeOutKill == defaultProcTimeOut)
				{
					Logger.log("Operation TimeOut for Session " + sessionId ,sessionId);
					return null;
				}
			}

		} catch (Exception e) {
			Logger.log(e.getMessage(), sessionId);
		}

		return ret;
		
	}
	
	public boolean checkCmdSuccess(InputStream input, String errorMsg)
	{
		if(errorMsg == null)
		{
			return true;
		}
		String toChk = null;
		try {
			toChk = IOUtils.toString(input).toLowerCase();
		} catch (IOException e) {
			Logger.log("Error When CheckingErrorMessage", sessionId);
			Logger.log(e.getMessage(), sessionId);
			return false;
		}
		if(toChk.contains(errorMsg))
			return false;
		return true;
	}
	
	private String Error_adbConnect = "unable to connect to";
	private String Error_adbPush = "error";
	private String Error_adbStopApp = "fail";
	private String Error_adbInstall = "fail";
}
