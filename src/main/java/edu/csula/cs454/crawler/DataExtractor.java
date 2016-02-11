package edu.csula.cs454.crawler;

import org.bouncycastle.jcajce.provider.digest.MD5;
import org.jsoup.nodes.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DataExtractor extends Thread implements Runnable {
	
	private static CrawlerController controller = null;
	private static String storageFolder = null;
	private static String database = null;
	public void run(){
		if(controller == null)
		{
			controller = CrawlerController.getInstance();
			storageFolder = controller.getStorageFolder();
			database = controller.getMetaDataStorageDatabase();
		}
		
				
		while(controller.isCrawling())
		{			
			while(controller.hasDocuments())
			{
				//get document 
				Document doc = controller.getNextDocument();
				//TODO Start Extracting the data and storing it in storage folder
				MongoClient mongoClient = new MongoClient();
				MongoDatabase db = mongoClient.getDatabase(database);
				
			}			
		}		
	}
}
