package edu.csula.cs454.crawler;

import java.util.ArrayList;
public class ToastCrawler {
	
	private ArrayList<String> seeds;
	private String storageFolder;
	private int crawlDepth;
	protected ToastCrawler(){
		seeds = new ArrayList<String>();
	}
	
	
	public void addSeed(String seed) {
		// TODO Auto-generated method stub
		seeds.add(seed);
	}


	public void setCrawlStorageFolder(String storageFolder) {
		this.storageFolder = storageFolder;		
	}


	public void setCrawlDepth(int depth) {
		crawlDepth = depth;		
	}
	
	public void crawl(){
		
	}
}
