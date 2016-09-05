package com.floreantpos.entrytool;


/**
 * @author Vinay Gupta 
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.floreantpos.POSConstants;

// Program to monitor the folder . If any file is created in folder then event will trigger.

public class ReceiptsMonitor extends Thread {
	
	private static Logger logger = Logger.getLogger(ReceiptsMonitor.class);
	private static ReceiptsMonitor mainMonitor = new ReceiptsMonitor();
	
	private ReceiptsMonitor(){
		super();
	}
	
	

	@Override
	public void run() {
		
		startProces();
	}



	@Override
	public void interrupt() {
		
		logger.debug("My Receipts Monitor thread interrupted!!!");
	}



	private void startProces() {

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		String folderPath = POSConstants.ENTRYTOOL_FOLDERPATH_TOREADRECEIPTS;
		String movePath = POSConstants.ENTRYTOOL_MOVEPATH_AFTERREAD;
		Path dir = Paths.get(folderPath);

		WatchService watchService=null;
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e2) {
			e2.printStackTrace(pw);
			logger.error("Error : "+sw.toString());
		}
		try {
			dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e1) {
			e1.printStackTrace(pw);
			logger.error("Error : "+sw.toString());
		}

		boolean valid = true;
		do {
			WatchKey watchKey=null;
			try {
				watchKey = watchService.take();
			} catch (InterruptedException e) {
				e.printStackTrace(pw);
				logger.error("Error : "+sw.toString());
			}

			for (WatchEvent<?> event : watchKey.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
					String fileName = event.context().toString();
					logger.debug("File Created:" + fileName);
					
					try{
						Calendar currentTime = Calendar.getInstance();
						String folderDate = String.valueOf(currentTime.get(Calendar.YEAR))+"-"+String.valueOf(currentTime.get(Calendar.MONTH))+"-"+String.valueOf(currentTime.get(Calendar.DATE))+"/";
						String folderTime = String.valueOf(currentTime.get(Calendar.HOUR_OF_DAY))+"-"+String.valueOf(currentTime.get(Calendar.MINUTE))+"/";
						ParseReceipt.ReadFile(folderPath+fileName);

						/*
						 * After Successful read the file will move to other folder
						 */
						try{
							Path newDirDate = Paths.get(movePath + folderDate);
							Path newDirTime = Paths.get(newDirDate.toAbsolutePath() + "/" + folderTime);
							Path oldPath = Paths.get(folderPath + fileName);
							Path newPath = Paths.get(newDirTime.toAbsolutePath() + "/" + fileName);
							if(!Files.exists(newDirDate))
								Files.createDirectory(newDirDate);
							if(!Files.exists(newDirTime))
								Files.createDirectory(newDirTime);
					    	Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
						}catch (Exception e){
							e.printStackTrace();
							e.printStackTrace(pw);
							logger.error("Error during moving file : "+e);
						}
					}catch(Exception e){
						e.printStackTrace();
						e.printStackTrace(pw);
						logger.error("Error during reading files or insert into DB : "+sw);
					}
					
				} else if (StandardWatchEventKinds.ENTRY_DELETE.equals(event.kind())) {
					
					String fileName = event.context().toString();
					logger.debug("File Deleted:" + fileName);
					
				} else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(event.kind())) {
					
					String fileName = event.context().toString();
					logger.debug("File Modified:" + fileName);
					
				}
			}
			valid = watchKey.reset();
			
		} while (valid);

	}
	
	public static ReceiptsMonitor getInstance(){
		return mainMonitor;
	}
}