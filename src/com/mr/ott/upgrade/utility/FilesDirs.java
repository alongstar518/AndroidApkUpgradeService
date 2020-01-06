package com.mr.ott.upgrade.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FilesDirs {
	
	private static String OpFolder =null;
	
	public static File getOpsDir(String sessionId)
	{
		File opDir1 = new File(GetOpFolder(), "Ops");
		File opDir = new File(opDir1, sessionId);
		if(opDir.exists())
			return opDir;
		
		opDir.mkdir();
		
		return opDir;
	}
	
	public static File getBuildCacheDir(String buildFileName)
	{
		File bldc = new File(GetOpFolder(), "BuildCache");
		File bldFile = new File(bldc, buildFileName);
		if(bldFile.exists())
			return bldFile;
		return null;
	}
	
	public static String GetOpFolder()
	{
		if(OpFolder == null)
		{
	        Map<String, String> env = System.getenv();
	        for (String envName : env.keySet()) {
	            if(envName.equals("UPGRADE_OPS"))
	            {
	            	OpFolder = env.get(envName);
	            	break;
	            }
	        }
		}
		return OpFolder;
	}

	public static boolean prepareBuild(String sessionId, String apkFileName)
	{
		File opsDir = FilesDirs.getOpsDir(sessionId);
		File bldFile = FilesDirs.getBuildCacheDir(apkFileName);
		
		File tmpDir = new File(opsDir, "tmp");
		File targetBldFile = new File(tmpDir, apkFileName.replace(".apk", ".zip"));
		
		tmpDir.mkdir();
		try {
			Files.copy(bldFile.toPath(), targetBldFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			Logger.log("Error when copy src file to dst error:" + e.getMessage(), sessionId);
			Logger.logStatus("Failed, srcbuild copy Failed", sessionId);
			return false;
		}
		
		try {
			Process  proc = Runtime.getRuntime().exec("7z e -aoa " + targetBldFile.getAbsolutePath(),null,tmpDir);
			proc.waitFor(5, TimeUnit.MINUTES);
		} catch (Exception e) {
			Logger.log("Error when try to unzip file, Error message: " + e.getMessage(), sessionId);
			Logger.logStatus("Failed, srcbuild unzip Failed", sessionId);
			return false;
		}
		
		return true;
	}
}
