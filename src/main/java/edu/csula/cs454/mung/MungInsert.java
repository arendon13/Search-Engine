package edu.csula.cs454.mung;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

import edu.csula.cs454.crawler.DocumentMetadata;
import edu.csula.cs454.crawler.WebDocument;

public class MungInsert {
	
	public static void main(String[] args)throws Exception{
		
		//String path = 
		System.out.println("mung");
		String path = "";
		String baseUrl = "";
		File dataset = new File(path);
		MongoClient mongo = new MongoClient();
		Morphia morphia = new Morphia();
		morphia.map(DocumentMetadata.class);
		Datastore dataStore = morphia.createDatastore(mongo, "CrawledData");
		File[] allFiles = dataset.listFiles();
		for(File file: allFiles)
		{
			Document currentDocument = Jsoup.parse(file, "UTF-8",baseUrl);
			String[] content = WebDocument.extractMeaningfulContent(currentDocument.select("body").text());
			DocumentMetadata docData = new DocumentMetadata();
			docData.setContent(content);
			docData.setURL(currentDocument.location().toLowerCase().trim());
			docData.setFileExtestion("html");
			docData.setPath(path+"/"+file.getName());
			dataStore.save(docData);
		}		
		
	}

}
