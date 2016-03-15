package edu.csula.cs454.ranker;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import edu.csula.cs454.crawler.DocumentMetadata;

public class ToastRanker {
	
	HashMap <String, HashSet<String>> outgoing;
	HashMap <String, HashSet<String>> incoming;
	HashMap <String, Double> allRanks;
	HashMap <String, DocumentMetadata> allDocuments;
	
	public ToastRanker(){
		outgoing = new HashMap<String, HashSet<String>>();
		incoming = new HashMap<String, HashSet<String>>();
		allRanks = new HashMap<String,Double>();
		allDocuments = new HashMap <String, DocumentMetadata>();
	}
	
	public void addDocument(DocumentMetadata doc) {
		try{
			String linkId = doc.getURL();
			allRanks.put(linkId, new Double(doc.getRank()));
			allDocuments.put(linkId, doc);
			//calculate the outgoing links of this page
			File input = new File(doc.getPath());
			Document currentDocument = Jsoup.parse(input, "UTF-8",doc.getURL());
			Elements linkElements = currentDocument.select("a");
			HashSet<String> outLinks = new HashSet<String>();
			outgoing.put(linkId, outLinks);
			for(int i = 0, length = linkElements.size(); i < length; i++)
			{
				String link = linkElements.get(i).absUrl("href").toLowerCase().trim();
				if(link.length() == 0)continue;
				outLinks.add(link);
			}
			//for each link use as a key and store this documents key 
			for(String link: outLinks)
			{
				HashSet<String> inLinks  = incoming.get(link);
				if(inLinks == null)
				{
					inLinks =new HashSet<String>();
					incoming.put(link, inLinks);
				}
				inLinks.add(linkId);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public DocumentMetadata[] rankDocumentsUsingSecretToastMethod() {
		// TODO link analysis ranking should be done here
		double sum;
		double oldRank;
		boolean recurs = false;
		
		Map<String, Double> oldRanks = getRanks(allDocuments);
		
		for (Entry<String, DocumentMetadata> entry : allDocuments.entrySet()) {
		    String key = entry.getKey();
		    DocumentMetadata value = entry.getValue();
		    
		    sum = 0.0;
			oldRank = 0.0;
			
			DocumentMetadata[] incomingDocs = getIncomingDocs(value);
			
			for(DocumentMetadata d: incomingDocs){
				int numOutgoing = outgoing.get(d.getURL()).size();
				oldRank = oldRanks.get(d.getURL());
				
				sum += oldRank / numOutgoing;
			}
			
			double newRank = round(sum);
			
//			System.out.println(value.getURL() + ": newrank = " + newRank + "; oldrank = " + value.getRank());
			
			if(!recurs){
				oldRank = value.getRank();
				double change = newRank - oldRank;
				if(change != 0){
					recurs = true;
				}
			}
			
			value.setRank(newRank);
		}
		
		if(recurs){
			rankDocumentsUsingSecretToastMethod();
		}
		else{
			//after ranking all documents, return an array of all the documents so that 
			//it can be saved to the db
			DocumentMetadata[] array = getFinalArray();
			return array;
		}
		
		// benji, double check this eclipse made me add this return statement
		return null;
	}
	
	public DocumentMetadata[] getFinalArray(){
		int size = allDocuments.size();
		DocumentMetadata[] array = new DocumentMetadata[size];
		
		int index = 0;
		for (Entry<String, DocumentMetadata> entry : allDocuments.entrySet()) {
		    DocumentMetadata value = entry.getValue();
		    array[index] = value;
		}
		
		return array;
	}
	
	public DocumentMetadata[] getIncomingDocs(DocumentMetadata curDoc){
		int size = incoming.get(curDoc.getURL()).size();
		DocumentMetadata[] doc = new DocumentMetadata[size];
		
		for(int i = 0; i < doc.length; i++){
			doc[i] = allDocuments.get(incoming.get(curDoc.getURL()).toArray()[i]);
		}
		
		return null;
	}
	
	public Map<String, Double> getRanks(HashMap <String, DocumentMetadata> allDocs){
		/*
		 * Returns a map 
		 */
		Map<String, Double> oldRanks = new HashMap<String, Double>();
		for (Entry<String, DocumentMetadata> entry : allDocs.entrySet()) {
		    DocumentMetadata value = entry.getValue();
		    oldRanks.put(value.getURL(), value.getRank());
		}
		
		return oldRanks;
	}
	
	public double round(double x){
		/*
		 * Rounds to the nearest two decimals
		 */
		return Math.round(x * 100.0) / 100.0;
	}

}
