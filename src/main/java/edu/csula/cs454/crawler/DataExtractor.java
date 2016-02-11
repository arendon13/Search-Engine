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
		if(controller == null)
		{
			controller = CrawlerController.getInstance();
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
				Document doc = controller.getNextDocument();
				String docURL = doc.location();
				System.out.println("Absolute URL: "+docURL);	
				DocumentMetadata docMetadata = new DocumentMetadata();
				docMetadata.setURL(docURL);
				dataStore.save(docMetadata);
				System.out.println("Object ID " + docMetadata.getID());
				//collection.insertOne(docMetaData);
				//create a string reference file (<storagefolder>/<mongoid>.html)
				String fileName = storageFolder+"/"+docMetadata.getID().toHexString()+".html";
				//write document to disk 
				PrintWriter docFile;
				try {
					new FileOutputStream(fileName, false).close();
					docFile = new PrintWriter (fileName, "UTF-8");
					docFile.print(doc.toString());
					docFile.close();					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.exit(0);
				} catch (IOException e) {
					
					e.printStackTrace();
					System.exit(0);
				}
				
				
				//add path on drive
				docMetadata.setPath((new File(fileName)).getAbsolutePath());		
				dataStore.save(docMetadata);
				
			}			
		}	
	}
}
