package edu.csula.cs454.crawler;

public class CrawlerApp {
	 public static void main(String[] args){
		 String seed = "";
		 int depth = 2;
		 CrawlerController controller = CrawlerController.getInstance();
		 controller.addSeed(seed);
		 controller.setStorageFolder("CrawlerStorge");
		 controller.setDepth(depth);
		 controller.start(); 
	  }
}
