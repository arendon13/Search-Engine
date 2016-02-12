package edu.csula.cs454.crawler;

import java.util.ArrayList;
import java.util.Stack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
				//get document 
				Document doc = Jsoup.connect(seed).get();
				//extract all links for the document
				ArrayList<String> links = new ArrayList<String>();
				Elements elts = doc.select("a");
				//System.out.println("Links found: "+elts.size());
				for(int i = 0, length = elts.size(); i < length;i++)
				{
					links.add(elts.get(i).absUrl("href"));
				}	
				//add document to list of documents crawled
				docs.push(doc);				
				
			}catch(Exception e){
				System.out.println("Something went wrong!!!");
				System.exit(0);
			}			
		}		
	}
}
