package com.mr.ott.upgrade.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.apache.commons.io.IOUtils;

public class Logger {
	
	public static void log(Object message,String sessionId)
	{
		 if(message == null) return;
		 File logFile = new File(FilesDirs.getOpsDir(sessionId), sessionId + ".log");
		 try
		 {
			  if(!logFile.exists())
			  {
				  logFile.createNewFile();
			  }
			  Writer fileWriter = new FileWriter(logFile,true);	
			  try
			  {
				  if(message.getClass() == String.class)
				  {		  
					  fileWriter.write((String)message  + '\n');  
				  }
				  else
				  {
					  String logToWrite = IOUtils.toString((InputStream)message)  + '\n';
					  fileWriter.write(logToWrite);
				  }
			  }
			  catch(IOException e)
			  {
				  
			  }
			  fileWriter.close();
		 }
		 catch(IOException e)
		 {
			 
		 }
		  
	}
	
	public static void logStatus(String status, String sessionId)
	{
		 File statusFile = new File(FilesDirs.getOpsDir(sessionId), sessionId + ".stat");
		 try
		 {
			  if(!statusFile.exists())
			  {
				  statusFile.createNewFile();
			  }
			  Writer fileWriter = new FileWriter(statusFile,false);
			  try {
				  fileWriter.write(status);
			  }
			  catch(IOException e)
			  {
				  log("Failed Log Status for " + sessionId, sessionId);
			  }
			  fileWriter.close();
		 }
		 catch(IOException e)
		 {
			 log("Failed Log Status for " + sessionId, sessionId);
		 }
	}

	public static String getStatus(String sessionId)
	{
		File statusFile = new File(FilesDirs.getOpsDir(sessionId), sessionId + ".stat");

		return readFileContent(statusFile, sessionId);
	}
	
	public static String getLog(String sessionId)
	{
		File logFile = new File(FilesDirs.getOpsDir(sessionId), sessionId + ".log");

		return readFileContent(logFile, sessionId);
	}
	
	public static String getVersions(String sessionId)
	{
		File verFile = new File(FilesDirs.getOpsDir(sessionId), sessionId + ".ver");

		return readFileContent(verFile, sessionId);
	}
	
	public static String getParamFromFile(String sessionId)
	{
		File paramFile = new File(FilesDirs.getOpsDir(sessionId), sessionId + ".param");

		return readFileContent(paramFile, sessionId);
	}
	
	private static String readFileContent(File file,String sessionId)
	{
		String status = null;
		try {
			FileInputStream fi = new FileInputStream(file);
			status = IOUtils.toString(fi);
		} catch (Exception e) {
			log("Error when read file :" + file.getAbsolutePath() + " " + e.getMessage(),sessionId);
		}
		
		return status;
	}
	
	public static void WriteFileVersion(String version, String sessionId)
	{
		 File verFile = new File(FilesDirs.getOpsDir(sessionId), sessionId + ".ver");
		 try
		 {
			  if(!verFile.exists())
			  {
				  verFile.createNewFile();
			  }
			  Writer fileWriter = new FileWriter(verFile,true);
			  try {
				  fileWriter.write(version + '\n');
			  }
			  catch(IOException e)
			  {
				  log("Failed write file version for " + sessionId, sessionId);
			  }
			  fileWriter.close();
		 }
		 catch(IOException e)
		 {
			 log("Failed write file version for " + sessionId, sessionId);
		 }
	}
	
	public static void SaveParmForSession(String param, String sessionId)
	{
		 File paramFile = new File(FilesDirs.getOpsDir(sessionId), sessionId + ".param");
		 try
		 {
			  if(!paramFile.exists())
			  {
				  paramFile.createNewFile();
			  }
			  Writer fileWriter = new FileWriter(paramFile,true);
			  try {
				  fileWriter.write(param + '\n');
			  }
			  catch(IOException e)
			  {
				  log("Failed write param file for " + sessionId, sessionId);
			  }
			  fileWriter.close();
		 }
		 catch(IOException e)
		 {
			 log("Failed write param file for " + sessionId, sessionId);
		 }
	}
}
