package edu.csula.cs454.crawler;

public class CrawlerApp {
	 public static void main(String[] args){

		 if (args.length >= 2)
		 {
             int depth = Integer.parseInt(args[0]);
			 String seed = args[1];

			 CrawlerController controller = CrawlerController.getInstance();
			 controller.setStorageFolder("CrawlerStorage");
			 controller.setDepth(depth);
			 controller.addSeed(seed);
			 controller.setMetaDataStorageDatabase("CrawledData");

			 if (args.length == 3){
				 controller.enableExtraction(true);
				 controller.setNumberOfExtractors(Integer.parseInt(args[2]));
			 } else {
                 controller.enableExtraction(false);
             }

			 controller.start(); 			 
			 
		 } else {
			 System.out.println("Not Enough Command Line arguments. Or incorrect arguments passed in...");
			 for (String s : args){
				 System.out.print(s);
			 }
		 }
		
	  }
}
