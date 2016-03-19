package edu.csula.cs454.ranker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import edu.csula.cs454.crawler.DocumentMetadata;

// Ranking Take 2

public class RankTest {
	
	static HashMap<Integer, HashSet<Integer>> linksFromMe = new HashMap<Integer, HashSet<Integer>>();
	static HashMap<Integer, HashSet<Integer>> linksToMe = new HashMap<Integer, HashSet<Integer>>();
	static HashMap<Integer, DocumentMetadata> docs = new HashMap<Integer, DocumentMetadata>();
	static ArrayList<DocumentMetadata> collection;

	public static void main(String[] args) {
		DocumentMetadata docA = new DocumentMetadata(1, 0.0, "URLA");
		DocumentMetadata docB = new DocumentMetadata(2, 0.0, "URLB");
		DocumentMetadata docC = new DocumentMetadata(3, 0.0, "URLC");
		
		collection = new ArrayList<DocumentMetadata>();
		collection.add(docA); collection.add(docB); collection.add(docC);
		
		initializeRank(collection);
		
		populateDocs();
		
		getLinksFromMe();
		
		determineLinksToMe();
		
		rankCollection(collection);
		
//		System.out.println(linksFromMe.toString());
//		System.out.println(linksToMe.toString());
	}
	
	public static void populateDocs(){
		for(DocumentMetadata doc: collection){
			docs.put(doc.getIdInt(), doc);
		}
	}
	
	public static void rankCollection(ArrayList<DocumentMetadata> allDocs){
		double sum;
		double oldRank;
		boolean recurs = false;
		Map<Integer, Double> oldRanks = getRanks(collection);
		
		for(DocumentMetadata doc: allDocs){
			sum = 0.0;
			oldRank = 0.0;
			
			linksToMe.get(doc.getIdInt()).toArray();
			DocumentMetadata[] incomingDocs = getIncomingDocs(doc);
			
			for(DocumentMetadata d: incomingDocs){
				int numOutgoing = linksFromMe.get(d.getIdInt()).size();
				oldRank = oldRanks.get(d.getIdInt());
						
				sum += oldRank / numOutgoing;
			}
			
			double newRank = round(sum);
			
			System.out.println(doc.getURL() + ": newrank = " + newRank + "; oldrank = " + doc.getRank());
			
			if(!recurs){
				oldRank = doc.getRank();
				double change = newRank - oldRank;
				if(change != 0){
					recurs = true;
				}
			}
			
			doc.setRank(newRank);
		}
		
		if(recurs){
			rankCollection(allDocs);
		}
		else{
			System.out.println("\nRecursion finished. final converged result is: ");
			System.out.println("A: " + collection.get(0).getRank() + "\nB: " + collection.get(1).getRank() + "\nC: " + collection.get(2).getRank());
			return;
		}
	}
	
	public static DocumentMetadata[] getIncomingDocs(DocumentMetadata curDoc){
		DocumentMetadata[] doc = new DocumentMetadata[linksToMe.get(curDoc.getIdInt()).size()];
		
		for(int i = 0; i < doc.length; i++){
			doc[i] = docs.get(linksToMe.get(curDoc.getIdInt()).toArray()[i]);
		}
		
		return doc;
	}
	
	public static Map<Integer, Double> getRanks(ArrayList<DocumentMetadata> allDocs){
		/*
		 * Returns a map 
		 */
		Map<Integer, Double> oldRanks = new HashMap<Integer, Double>();
		for(DocumentMetadata doc: allDocs){
			oldRanks.put(doc.getIdInt(), doc.getRank());
		}
		return oldRanks;
	}
	
	public static void determineLinksToMe(){
	    /*
	     *  iterate through outgoing and reverse link for incoming
	     *  keys and values for out going links need to be switched when getting incoming links
	     */
		HashSet<Integer> value;
		HashSet<Integer> hset; // used to insert into map
		
		for (Entry<Integer, HashSet<Integer>> entry : linksFromMe.entrySet()) {
		    int key = entry.getKey();
		    value = entry.getValue();
		    for(int i = 0; i < value.toArray().length; i++){
		    	hset = new HashSet<Integer>();
		    	hset.add(key);
		    	int curValue = (Integer) value.toArray()[i];
		    	if(linksToMe.keySet().contains(curValue)){
					linksToMe.get(curValue).add(key);
				}
				else{
					linksToMe.put(curValue, hset);
				}
		    }
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void printHashMap(HashMap<Integer, HashSet<Integer>> hm){		
	    /*
	     *  Method for printing maps
	     *  Taken and modified from StackOverflow
	     */
		Iterator<Entry<Integer, HashSet<Integer>>> it = hm.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	public static void getLinksFromMe(){
		/*
		 * Method will loop through each doc in the collection
		 * Will receive the out going links per doc
		 * Populate the hash map using the current doc and the list of links
		 */
		for(DocumentMetadata doc: collection){
			ArrayList<String> outGoingLinks = getOutGoingLinks(doc);
			populateLinksFromMe(doc, outGoingLinks);
		}
	}
	
	public static ArrayList<String> getOutGoingLinks(DocumentMetadata doc){
		/*
		 * Method returns a list of all the outgoing links pertaining to a document.
		 * Hard coded for now.
		 * Should be replaced with Benji's method
		 */
		ArrayList<String> outGoingLinks = new ArrayList<String>();

		switch (doc.getIdInt()){
		case 1:
			outGoingLinks.add("URLB"); 
			outGoingLinks.add("URLC");
			return outGoingLinks;
		case 2:
			outGoingLinks.add("URLC");
			return outGoingLinks;
		case 3:
			outGoingLinks.add("URLA");
			return outGoingLinks;
		default:
			return null;
		}

	}
	
	public static void populateLinksFromMe(DocumentMetadata doc, ArrayList<String> outgoingLinks){
		/*
		 * Method implements the populating of the hash map
		 * creates a hash set that will contain the id's of the doc that contains the out going link
		 * puts all of this in the hash map 
		 */
		HashSet<Integer> hset = new HashSet<Integer>();
		
		for(String url: outgoingLinks){
			int urlId = getDocIdByUrl(url);
			hset.add(urlId);
		}
		
		linksFromMe.put(doc.getIdInt(), hset);
	}
	
	public static int getDocIdByUrl(String url){
		/*
		 * Based on the url/link given, method will return the doc id by iterating through the collection
		 */
		for(DocumentMetadata doc: collection){
			if(doc.getURL().equals(url)){
				return doc.getIdInt();
			}
		}
		return -1;
	}
	
	public static void initializeRank(ArrayList<DocumentMetadata> collection){
		/*
		 * Method sets initial rank for all documents in the collection
		 */
		double initialRank = 1.0 / collection.size();
		initialRank = round(initialRank);
		
		for(DocumentMetadata doc: collection){
			doc.setRank(initialRank);
			System.out.println(doc.getRank());
		}
	}
	
	public static double round(double x){
		/*
		 * Rounds to the nearest two decimals
		 */
		return Math.round(x * 100.0) / 100.0;
	}

}
