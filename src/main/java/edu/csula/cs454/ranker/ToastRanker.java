package edu.csula.cs454.ranker;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import edu.csula.cs454.crawler.DocumentMetadata;

public class ToastRanker {

	HashMap <String, HashSet<String>> outgoing;
	HashMap <String, HashSet<String>> incoming;
	HashMap <String, Double> allRanks;
	HashMap <String, DocumentMetadata> allDocuments;
	//double maxRank = 0;

	public ToastRanker(){
		outgoing = new HashMap<String, HashSet<String>>();
		incoming = new HashMap<String, HashSet<String>>();
		allRanks = new HashMap<String,Double>();
		allDocuments = new HashMap <String, DocumentMetadata>();
	}

	public void addDocument(DocumentMetadata doc) {

		String linkId = doc.getURL();
		allRanks.put(linkId, new Double(doc.getRank()));
		allDocuments.put(linkId, doc);
		//calculate the outgoing links of this page
		HashSet<String> outLinks = new HashSet<String>();
		outgoing.put(linkId, outLinks);
		if(doc.isHtml())
		{
			try{
				File input = new File(doc.getPath());
				Document currentDocument = Jsoup.parse(input, "UTF-8",doc.getURL());
				Elements linkElements = currentDocument.select("a");
				for(int i = 0, length = linkElements.size(); i < length; i++)
				{
					String link = linkElements.get(i).absUrl("href").toLowerCase().trim();
					if(link.length() == 0)continue;
					//System.out.println("Link Added : "+link);
					outLinks.add(link);
				}

			}catch(Exception e){
				e.printStackTrace();
			}
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
	}

	public DocumentMetadata[] rankDocumentsUsingSecretToastMethod() {
		// TODO link analysis ranking should be done here
		/*double sum;
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
		}*/
		//initialize the rank for the documents 
		/*
		 * It only makes sense to rank the html documents
		 * */
		int count  = 0;
		for(Entry<String,DocumentMetadata> d: allDocuments.entrySet())
		{
			count++;
		}
		//maxRank = 1.0/count;
		double initRank = 1.0/count;
		System.out.print("initial Rank: "+initRank+" html collection Size: "+ count);
		//initialize rank!! :P
		for(Entry<String,DocumentMetadata> d: allDocuments.entrySet())
		{
			//DocumentMetadata doc = d.getValue();
			//doc.setRank(initRank);
			allRanks.put(d.getKey(),new Double(initRank));
			//d.getValue().setRank(initRank);

		}
		rankAllDocs();
		//return that documents as an array so they can be saved to the database 
		int size = allDocuments.size();
		DocumentMetadata[] array = new DocumentMetadata[size];
		int index = 0;
		for(Entry<String,DocumentMetadata> d: allDocuments.entrySet())
		{
			array[index] = d.getValue();
			index++;
		}
		return array;
	}


	public void rankAllDocs(){
		boolean notDoneRanking = true;
		//rank documents
		int counter = 0;
		//clone initial ranks 
		HashMap <String, Double>previousRank;
		//while(!isDone())
		do{
			previousRank = (HashMap<String, Double>) allRanks.clone();
			for(Entry<String,DocumentMetadata> d: allDocuments.entrySet())
			{
				DocumentMetadata doc = d.getValue(); 
				/*if(doc.isHtml())*/rankDoc(doc);
			}
			counter++;
			if (counter % 100 == 0){
				System.out.println("Iteration: " + counter);
				System.out.println(previousRank.values().toArray()[0]);
			}
		}while(!isDone(previousRank));

		//copy new ranks into documents
		for(Entry<String,DocumentMetadata> d: allDocuments.entrySet())
		{
			DocumentMetadata doc = d.getValue();
			doc.setRank(allRanks.get(d.getKey()).doubleValue());
		}
	}

	public boolean isDone(HashMap<String, Double> prevRanks){

		//TODO calculate an appropriate range
		double range  = .00001;
		for(Entry<String,Double> d: allRanks.entrySet())
		{
			String key = d.getKey();
			double newVal = allRanks.get(key).doubleValue();
			double oldVal = prevRanks.get(key).doubleValue();
			if(newVal <= oldVal+range && newVal >= oldVal-range)return false;
			//if(.equals(prevRanks.get(key)))return false;
		}

		return true;
	}



	public void rankDoc(DocumentMetadata doc){
		String url  = doc.getURL();
		allRanks.put(url,getRank(url));
	}

	public Double getRank(String url){
		HashSet<String> linksToThisURL = incoming.get(url);
		//HashSet<String> linksFromThisURL = outgoing.get(url);
		/*must becarefull with this because either sets may be of size 0;
		 * or null if not link points to it (ex. seed with nothing refing it )
		 * */
		if(linksToThisURL ==null ||linksToThisURL.isEmpty())return allRanks.get(url);
		double newRank = 0.0;
		for(String link: linksToThisURL)
		{
			Double currentRank = allRanks.get(link);
			/*check to see if this is a document that has been crawled
			if it isn't we can counted because we don't know the number 
			of out going links it has*/
			if(currentRank == null )continue;
			//if the docuement doens't have out going links dissregard calculation
			HashSet<String> linksFromURL = outgoing.get(link);
			if(linksFromURL == null || linksFromURL.isEmpty())continue;
			newRank+= (currentRank.doubleValue()/linksFromURL.size());
			//System.out.print("Calculating New Rank");
		}

		if(newRank == 0.0)return allRanks.get(url);
		else{
			//check against max
			//if(newRank > maxRank)maxRank = newRank;
		}

		return new Double(newRank);
	}
	
	/*public DocumentMetadata[] getFinalArray(){
		int size = allDocuments.size();
		DocumentMetadata[] array = new DocumentMetadata[size];
		
		int index = 0;
		for (Entry<String, DocumentMetadata> entry : allDocuments.entrySet()) {
		    DocumentMetadata value = entry.getValue();
		    array[index] = value;
		}
		
		return array;
	}*/
	
	/*public DocumentMetadata[] getIncomingDocs(DocumentMetadata curDoc){
		int size = incoming.get(curDoc.getURL()).size();
		DocumentMetadata[] doc = new DocumentMetadata[size];
		
		for(int i = 0; i < doc.length; i++){
			doc[i] = allDocuments.get(incoming.get(curDoc.getURL()).toArray()[i]);
		}
		
		return doc;
	}*/	
	/*public Map<String, Double> getRanks(HashMap <String, DocumentMetadata> allDocs){
		// Returns a map 
		Map<String, Double> oldRanks = new HashMap<String, Double>();
		for (Entry<String, DocumentMetadata> entry : allDocs.entrySet()) {
		    DocumentMetadata value = entry.getValue();
		    oldRanks.put(value.getURL(), value.getRank());
		}
		
		return oldRanks;
	}
	
	public double round(double x){
		// Rounds to the nearest two decimals
		return Math.round(x * 100.0) / 100.0;
	}*/

}
