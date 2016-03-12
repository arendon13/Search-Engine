package edu.csula.cs454.ranker;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;

import edu.csula.cs454.crawler.DocumentMetadata;
import edu.csula.cs454.indexer.Index;

public class Ranker {
	
	public static void rankAllDocuments(){
		Morphia morphia = new Morphia();
        morphia.map(DocumentMetadata.class);
        morphia.map(Index.class);
        Datastore ds = morphia.createDatastore(new MongoClient(), "CrawledData");
        //calculate the page rank for each document
        List<DocumentMetadata> documents = ds.find(DocumentMetadata.class).asList();
        collectionRank(documents); 
	}
	
	private static void collectionRank(List<DocumentMetadata> collection){
		//TODO: adrian implement your link anaylis here 
		
		
	}
}
