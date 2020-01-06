package com.mr.ott.upgrade.rest;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.mr.ott.upgrade.processor.GetVersionProcessor;
import com.mr.ott.upgrade.processor.UpgradeProcessor;
import com.mr.ott.upgrade.utility.Logger;

@Path("Upgrade")
public class UpgradeApi {
	
	@GET
	@Path("UpgradeDevice")
	@Produces(MediaType.APPLICATION_JSON)
	public SessionID UpgradeDevice(@QueryParam("DeviceIps") String deviceIps, @QueryParam("FileName") String fileName, @QueryParam("DeviceType") String deviceType,@QueryParam("PackageName") String packageName)
	{
		String sessionId = UUID.randomUUID().toString(); 
		Runnable r = new UpgradeProcessor(deviceIps, fileName, deviceType, packageName, sessionId);
		new Thread(r).start();
		return new SessionID(sessionId);
	}
	
	@GET
	@Path("GetUpgradeStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public UpgradeStatus GetUpgradeStatus(@QueryParam("sessionID") String sessionID)
	{
		return new UpgradeStatus(sessionID,Logger.getStatus(sessionID));
	}
	
	@GET
	@Path("GetUpgradeLog")
	@Produces(MediaType.TEXT_PLAIN)
	public String GetUpgradeLog(@QueryParam("sessionID") String sessionID)
	{
		return Logger.getLog(sessionID);
	}
	
	@GET
	@Path("GetCurrentAppVer")
	@Produces(MediaType.TEXT_PLAIN)
	public String GetCurrentAppVer(@QueryParam("PackageName")String pkgName, @QueryParam("sessionID")String sessionId)
	{
		new GetVersionProcessor(sessionId).getVersion();
		return Logger.getVersions(sessionId);
	}
}
