package edu.csula.cs454.ranker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;

import edu.csula.cs454.crawler.DocumentMetadata;
import edu.csula.cs454.indexer.Index;

public class Ranker {
	
	static Map<String, DocumentMetadata> docs = new HashMap<String, DocumentMetadata>();
	
	public static void rankAllDocuments(){
		Morphia morphia = new Morphia();
        morphia.map(DocumentMetadata.class);
        morphia.map(Index.class);
        Datastore ds = morphia.createDatastore(new MongoClient(), "CrawledData");
        //calculate the page rank for each document
        List<DocumentMetadata> documents = ds.find(DocumentMetadata.class).asList();
        
        // `doc`s needs to be filled out with the URL and its own Document. check rank class for reference
        
        collectionRank(documents); 
	}
	
	private static void collectionRank(List<DocumentMetadata> collection){
		//TODO: adrian implement your link anaylis here 
		double sum;
		double oldRank;
		boolean recurs = false;
		Map<String, Double> oldRanks = getRanks(collection);
		for(DocumentMetadata doc: collection){
			sum = 0.0;
			oldRank = 0.0;
			
			for(String path: doc.getLinksToThisDoc()){
				DocumentMetadata v = docs.get(path);
				oldRank = oldRanks.get(path);

				sum += oldRank / v.getNumOutGoingLinks();
			}
			double newRank = round(sum);
			
			if(!recurs){
				oldRank = oldRanks.get(doc.getURL());
				double change = newRank - oldRank;
				if(change != 0){
					recurs = true;
				}
			}
			
			doc.setRank(newRank);
		}
		System.out.println("A: " + collection.get(0).getRank() + "\nB: " + collection.get(1).getRank() + "\nC: " + collection.get(2).getRank());
		
		if(recurs){
			collectionRank(collection);
		}
		else{
			System.out.println("\nRecursion finished. final converged result is: ");
			System.out.println("A: " + collection.get(0).getRank() + "\nB: " + collection.get(1).getRank() + "\nC: " + collection.get(2).getRank());
			return;
		}
	}
	
	public static double round(double x){
		return Math.round(x * 100.0) / 100.0;
	}
	
	public static Map<String, Double> getRanks(List<DocumentMetadata> collection){
		Map<String, Double> oldRanks = new HashMap<String, Double>();
		for(DocumentMetadata doc: collection){
			oldRanks.put(doc.getURL(), doc.getRank());
		}
		return oldRanks;
	}
}
