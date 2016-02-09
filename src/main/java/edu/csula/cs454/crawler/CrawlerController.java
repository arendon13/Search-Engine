package edu.csula.cs454.crawler;

//This will be a Singleton instance to avoid multithreaded confusion
public class CrawlerController {
	 private static CrawlerController instance = null;
	 private CrawlerController(){}
	 public static CrawlerController getInstance(){
		 if(instance == null) {
	         instance = new CrawlerController();
	      }
	      return instance;		 
	 } 
}
