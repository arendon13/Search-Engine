package edu.csula.cs454.ranker;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

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
		// TODO link analyysis ranking should be done here
		
		
		
		
		
		
		//after ranking all documents, return an array of all the documents so that 
		//it can be saved to the db
		DocumentMetadata[] results = new DocumentMetadata[allDocuments.size()];	
		int index = 0;
		for(DocumentMetadata d: allDocuments){
			results[index] = d;
			index++;
		}
	}
	
	

}
