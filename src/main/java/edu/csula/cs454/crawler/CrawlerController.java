package edu.csula.cs454.crawler;

import java.util.Stack;

import org.jsoup.nodes.Document;


//This will be a Singleton instance to avoid multithreaded confusion
public class CrawlerController {
	 private static CrawlerController instance = null;
	 private Stack<Document> docs;
	// CrawlConfig config;
	 ToastCrawler crawler;
	 private String storageFolder;
	// private ArrayList<String> seeds;

	 private CrawlerController(){//made private to enforce singleton 
		 System.out.println("Creating Crawler Controller");
		 docs = new Stack<Document>();
		// config = new CrawlConfig();
		 crawler = new ToastCrawler(docs);
		// seeds = new ArrayList<String>();
	 }
	 
	 public static CrawlerController getInstance(){
		 if(instance == null) {
	         instance = new CrawlerController();
	      }
	      return instance;		 
	}
	public void addSeed(String seed) {
		//add seeds
		 System.out.println("Adding "+seed+" to seed list");
		 crawler.addSeed(seed);
		//seeds.add(seed);
	}
	public void setStorageFolder(String folder) {
		storageFolder = folder;
		//config.setCrawlStorageFolder(storageFolder);
		crawler.setCrawlStorageFolder(storageFolder);
		System.out.println(folder+ " has been set as crawler storage folder");
	}
	
	public void setDepth(int depth){
		//config.setMaxDepthOfCrawling(depth);
		crawler.setCrawlDepth(depth);
		System.out.println("crawler depth has been set to "+depth);
	}
	
	public void start (){
		//TODO start crawling and extracting meta data 
		crawler.crawl();		
		
		
		/* try {
			    System.out.println("Appling configuration for crawl controller");
			    PageFetcher pageFetcher = new PageFetcher(config);
			    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
			    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
				CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
				//add seeds 
				System.out.println("Adding seeds to crawl controller");
				for(String s: seeds)
				{
					controller.addSeed(s);
				}
				seeds = null;
				System.out.println("Let the crawling begin!!");
				controller.start(MyCrawler.class, 1);	
				
			} catch (Exception e) {
				System.out.println("Failed to create controller...");
				e.printStackTrace();
				System.exit(0);
			}*/		
	}
}
