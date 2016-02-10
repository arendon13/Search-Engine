package edu.csula.cs454.crawler;

public class CrawlerApp {
	 public static void main(String[] args){
		 String seed = "http://www.calstatela.edu/";
		 int depth = 2;
		 CrawlerController controller = CrawlerController.getInstance();
		 controller.setStorageFolder("CrawlerStorge");
		 controller.setDepth(depth);
		 controller.addSeed(seed);		 
		 controller.start(); 
	  }
}
