package edu.csula.cs454.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
public class WebCrawler {
	
	private ArrayList<String> seeds;
	private ArrayList<String> visited = new ArrayList<String>();
	private int crawlDepth;
	private Stack<WebDocument> docs;
	protected WebCrawler(Stack<WebDocument> docs){
		seeds = new ArrayList<String>();
		this.docs = docs;
	}	
	
	public void addSeed(String seed) {
		seeds.add(seed);
	}

	public void setCrawlDepth(int depth) {
		crawlDepth = depth;		
	}
	
	public void crawl(){
		recursCrawl(0, crawlDepth, seeds);
		visited = null;//free up some memory 
	}
	
	private void recursCrawl(int curDepth, int maxDepth, ArrayList<String> links) {
		ArrayList<String> childLinks = new ArrayList<String>();
		for(String i: links){
			// Condition to check for blank URL's that cause errors
			if(i.trim().length() == 0 || isVisited(i))continue;
			addToVisited(i);
			try {
				System.out.println(curDepth + ": Connecting to..." + i);
				Document doc = Jsoup.connect(i).get();//connect the html page
				System.out.println("Documnet Received! ");
				//get all links on the page 
				Elements elts = doc.select("a");
				for(int j = 0, length = elts.size(); j < length; j++)
				{
					//Adding links to child list
					childLinks.add(elts.get(j).absUrl("href"));
				}
				//add document to list of documents crawled
				docs.push(new WebDocument(doc));
				/*
				//TODO get all images on the page 
				Elements images = doc.select("img");
				System.out.println("Extracting Images...");
				for(int j = 0, length = images.size(); j < length; j++)
				{
					String link = images.get(j).attr("abs:src").trim();
					if(link.length() == 0)continue;
					System.out.println("Extracting from: "+link);
					crawlNonHtml(link);					
				}*/
				
			} catch (IOException e) {
				//if its not html or xml what is it
				System.out.println("Document is not Html! ");
				crawlNonHtml(i);
			}
		}
		
		if(curDepth < maxDepth){
			recursCrawl(curDepth + 1, maxDepth, childLinks);
		}
		else{
			System.out.println("Done Crawling!!");
			return;
		}
		
	}
	
	private void crawlNonHtml(String link){
		try{
			Response response = Jsoup.connect(link).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").ignoreContentType(true).execute();
			docs.push(new WebDocument(response));
			System.out.println("Documnet Received! ");
		}catch(Exception er){
			System.err.println("Document could not be retreived: "+er.getMessage());
		}		
	}
	
	// Methoods are used to make sure we do not repeat 
	private boolean isVisited(String link){
		for(String i: visited)
		{
			if(i.equalsIgnoreCase(link))return true;
		}		
		return false;
	}
	
	private void addToVisited(String link){
		visited.add(link);
	}
}
