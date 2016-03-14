package edu.csula.cs454.crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.mongodb.morphia.Datastore;
import  org.mongodb.morphia.Morphia;
import org.jsoup.nodes.Document;
import com.mongodb.MongoClient;

public class DataExtractor extends Thread implements Runnable {
	
	private static CrawlerController controller = null;
	private static String storageFolder = null;
	private static String database = null;
	private static Datastore dataStore = null;
	public void run(){
		if(controller == null){
			controller = CrawlerController.getInstance();
		}	
		
		if(dataStore == null)
		{
			storageFolder = controller.getStorageFolder();
			database = controller.getMetaDataStorageDatabase();
			//check to see if the storage folder exsists 
			File storageDir = new File(storageFolder);
			if(!storageDir.exists())storageDir.mkdir();
			//create morphia instace 
			MongoClient mongo = new MongoClient();
			Morphia morphia = new Morphia();
			morphia.map(DocumentMetadata.class);
			dataStore = morphia.createDatastore(mongo, database);
		}
		
		
				
		while(controller.isCrawling() || controller.hasDocuments())
		{			
			while(controller.hasDocuments())
			{
				WebDocument doc = controller.getNextDocument();
				//extract url
				//String docURL = doc.getUrl();	
				DocumentMetadata docMetadata = new DocumentMetadata();
				//need to set url before writing to disk to get appropreate file name 
				docMetadata.setURL(doc.getUrl());
				//must save to disk before trying to retreive content from non html files 
				doc.saveToDisk(storageFolder);
				docMetadata.setContent(doc.getContent());
				docMetadata.setPath(doc.getPath());	
				docMetadata.setFileExtestion(doc.getExtension());
				docMetadata.setOutGoingLinks(doc.getOutGoingLinks());
				dataStore.save(docMetadata);	
			}			
		}	
	}
}
