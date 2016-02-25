package edu.csula.cs454.crawler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Stack;

import org.jsoup.Jsoup;
//import org.jsoup.*;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
public class WebCrawler {
	
	private ArrayList<String> seeds;
	private ArrayList<String> visited = new ArrayList<String>();
	private String storageFolder;
	private int crawlDepth;
	private Stack<WebDocument> docs;
	protected WebCrawler(Stack<WebDocument> docs){
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
		
	}
	
	private void recursCrawl(int curDepth, int maxDepth, ArrayList<String> links) {
		ArrayList<String> childLinks = new ArrayList<String>();
		
		for(String i: links){
			// Condition to check for blank URL's that cause errors
			if(i.trim().length() == 0 || isVisited(i)){
				continue;
			}
			addToVisited(i);
			Document doc;
			System.out.println(curDepth + ": Connecting to..." + i);
			try {
				doc = Jsoup.connect(i).get();//connect the html page
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
				//also extract any images on the page TODO
				
			} catch (IOException e) {
				//if its not html of xml what is it
				System.out.println("Document connecting is not html");
				try{
					Response response = Jsoup.connect(i).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").ignoreContentType(true).execute();
					docs.push(new WebDocument(response));
				}catch(Exception er){
					er.printStackTrace();
				}
			}
		}
		
		if(curDepth < maxDepth){
			recursCrawl(curDepth + 1, maxDepth, childLinks);
		}
		else{
			return;
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
