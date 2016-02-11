package edu.csula.cs454.crawler;

import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.jcajce.provider.digest.MD5;
import org.jsoup.nodes.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.*;

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
		MongoClient mongoClient =  new MongoClient();
		MongoCollection collection = mongoClient.getDatabase(database).getCollection("Docs");		
		while(controller.isCrawling() || controller.hasDocuments())
		{			
			while(controller.hasDocuments())
			{
				Document doc = controller.getNextDocument();
				//TODO Start Extracting the data and storing it in storage folder
				//create empty object to store doceuments meta data
				Map<String, Object> docMetaData = new HashMap<String, Object>();
				//docMetaData.put();
				//String urlHash = 
				System.out.println("Absolute URL: "+doc.location());				
				
				//		
			}			
		}	
		mongoClient.close();
		//collection.close();
	}
}
