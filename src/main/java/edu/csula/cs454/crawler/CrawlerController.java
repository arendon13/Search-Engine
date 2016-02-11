package edu.csula.cs454.crawler;

import java.util.Stack;
import org.jsoup.nodes.Document;
//This will be a Singleton instance to avoid multithreaded confusion
public class CrawlerController {
	 private static CrawlerController instance = null;
	 private Stack<Document> docs;
	 private DataExtractor[] extractors;
	 private int numOfExtractors = 1;
	 WebCrawler crawler;
	 private String storageFolder;
	 private String database;
	 private boolean isCrawling;
	 private boolean poping =false;
	 private boolean shouldExtract = false;

	 private CrawlerController(){//made private to enforce singleton 
		 System.out.println("Creating Crawler Controller");
		 docs = new Stack<Document>();
		 crawler = new WebCrawler(docs);
		 isCrawling = false;
	 }
	 
	 public static CrawlerController getInstance(){
		 if(instance == null) {
	         instance = new CrawlerController();
	      }
	      return instance;		 
	}
	public void addSeed(String seed) {
		 System.out.println("Adding "+seed+" to seed list");
		 crawler.addSeed(seed);
	}
	public void setStorageFolder(String folder) {
		storageFolder = folder;
		crawler.setCrawlStorageFolder(storageFolder);
		System.out.println(folder+ " has been set as crawler storage folder");
	}
	
	public void setDepth(int depth){
		crawler.setCrawlDepth(depth);
		System.out.println("crawler depth has been set to "+depth);
	}
	
	public void start (){
		extractors = new DataExtractor[numOfExtractors];
		//set up data extractor threads 
		if(shouldExtract)
		{
			for(DataExtractor e: extractors)
			{
				e = new DataExtractor();
				e.start();
			}	
		}
			
		isCrawling = true;
		crawler.crawl();
		isCrawling = false;
	}

	public String getStorageFolder() {
		return storageFolder;
	}

	public boolean isCrawling() {
		return isCrawling;
	}

	public synchronized boolean hasDocuments() {
		while(poping);
		return !docs.isEmpty();
	}

	public synchronized Document getNextDocument() {
		poping = true;
		Document d = docs.pop();
		poping = false;
		return d;
	}

	public String getMetaDataStorageDatabase() {
		return database;
	}
	
	public void setMetaDataStorageDatabase(String db){
		database = db;
	}

	public void enableExtraction(boolean b) {
		shouldExtract = b;
	}
}
