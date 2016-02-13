package edu.csula.cs454.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
public class WebCrawler {
	
	private ArrayList<String> seeds;
	private ArrayList<String> visited = new ArrayList<String>();
	private String storageFolder;
	private int crawlDepth;
	private Stack<Document> docs;
	protected WebCrawler(Stack<Document> docs){
		seeds = new ArrayList<String>();
		this.docs = docs;
	}	
	
	public void addSeed(String seed) {
		seeds.add(seed);
	}

	public void setCrawlStorageFolder(String storageFolder) {
		this.storageFolder = storageFolder;		
	}

	public void setCrawlDepth(int depth) {
		crawlDepth = depth;		
	}
	
	public void crawl(){
		recursCrawl(0, crawlDepth, seeds);
		
		visited = null;
		
//		for(String seed: seeds)
//		{
//			try{
//				//get document 
//				Document doc = Jsoup.connect(seed).get();
//				//extract all links for the document
//				ArrayList<String> links = new ArrayList<String>();
//				Elements elts = doc.select("a");
//				//System.out.println("Links found: "+elts.size());
//				for(int i = 0, length = elts.size(); i < length;i++)
//				{
//					links.add(elts.get(i).absUrl("href"));
//				}	
//				//add document to list of documents crawled
//				docs.push(doc);				
//				
//			}catch(Exception e){
//				System.out.println("Something went wrong!!!");
//				System.exit(0);
//			}			
//		}		
	}
	
	private void recursCrawl(int curDepth, int maxDepth, ArrayList<String> links) {
		ArrayList<String> childLinks = new ArrayList<String>();
		
		for(String i: links){
			if(i.trim().length() == 0 || isVisited(i)){
				continue;
			}
			addToVisited(i);
			Document doc;
			System.out.println(curDepth + ": Connecting to..." + i);
			try {
				doc = Jsoup.connect(i).get();

				Elements elts = doc.select("a");
				//System.out.println("Links found: "+elts.size());
				for(int j = 0, length = elts.size(); j < length; j++)
				{
					childLinks.add(elts.get(j).absUrl("href"));
//					System.out.println(elts.get(j).absUrl("href"));
				}
				//add document to list of documents crawled
				docs.push(doc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
		
		if(curDepth < maxDepth){
			recursCrawl(curDepth + 1, maxDepth, childLinks);
		}
		else{
			return;
		}
		
	}
	
	private boolean isVisited(String link){
		
		for(String i: visited){
			if(i.equalsIgnoreCase(link)){
				return true;
			}
		}
		
		return false;
	}
	
	private void addToVisited(String link){
		visited.add(link);
	}
}
