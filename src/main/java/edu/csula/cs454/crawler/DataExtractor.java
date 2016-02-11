package edu.csula.cs454.crawler;

import org.jsoup.nodes.Document;

public class DataExtractor extends Thread implements Runnable {
	
	private static CrawlerController controller = null;
	private static String storageFolder = null;
	public void run(){
		if(controller == null)
		{
			controller = CrawlerController.getInstance();
			storageFolder = controller.getStorageFolder();
		}
		
		while(controller.isCrawling())
		{			
			while(controller.hasDocuments())
			{
				//get document 
				Document doc = controller.getNextDocument();
				//TODO Start Extracting the data and storing it in storage folder
				
			}			
		}		
	}
}
