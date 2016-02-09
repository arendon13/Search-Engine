package edu.csula.cs454.crawler;

//This will be a Singleton instance to avoid multithreaded confusion
public class CrawlerController {
	 private static CrawlerController instance = null;
	 MyCrawler crawler;
	 private String storageFolder;
	 private CrawlerController(){}//to enforce singleton 
	 public static CrawlerController getInstance(){
		 if(instance == null) {
	         instance = new CrawlerController();
	      }
	      return instance;		 
	 }
	public void addSeed(String seed) {
		// TODO add see to the crawler
		  
	}
	public void setStorageFolder(String folder) {
		storageFolder = folder;
	}
	
	public void setDepth(int depth){
		// TODO add depth to crawler 
	}
	
	public void start (){
		//TODO start crawling and extracting meta data 
	}
}
