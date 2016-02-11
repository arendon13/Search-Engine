package edu.csula.cs454.crawler;

import java.util.ArrayList;
import java.util.Stack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
public class WebCrawler {
	
	private ArrayList<String> seeds;
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
		/*TODO 
		 implement so that it recursively crawls (without circulation)
		 (breadth first) till the depth specified
		*
		*/
		
		for(String seed: seeds)
		{
			try{
				Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
				docs.push(doc);
			    System.out.print(doc.toString());
			}catch(Exception e){
				System.out.println("Something went wrong!!!");
				System.exit(0);
			}			
		}		
	}
}
