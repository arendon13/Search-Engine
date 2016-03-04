package edu.csula.cs454.crawler;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Rank {
	public static void main(String args[]){
		DecimalFormat df = new DecimalFormat("0.00");
		
		int collectionSize = 3;
		double distRank = 1.0 / collectionSize;
//		double rankDocA = round(distRank);
//		double rankDocB = round(distRank);
//		double rankDocC = round(distRank);
//		ArrayList<ExampleDocument> aLinks = new ArrayList<ExampleDocument>();
//		aLinks.add()
		ExampleDocument docA = new ExampleDocument("URL1", round(distRank), 2);
		ExampleDocument docB = new ExampleDocument("URL2", round(distRank), 1);
		ExampleDocument docC = new ExampleDocument("URL3", round(distRank), 1);
		
		System.out.println("A: " + docA.getRank() + "\nB: " + docB.getRank() + "\nC: " + docC.getRank());
		
		System.out.println("\nAfter first iteration: \n");
		
		double oldA = 0;
		double oldB = 0; 
		double oldC = 0;
		
		docRank(docA, docB, docC, oldA, oldB, oldC);
		
		
	}
	
	public static void docRank(ExampleDocument docA, ExampleDocument docB, ExampleDocument docC, double oldRankA, double oldRankB, double oldRankC){
		ArrayList<ExampleDocument> linksToA = new ArrayList<ExampleDocument>();
		ArrayList<ExampleDocument> linksToB = new ArrayList<ExampleDocument>();
		ArrayList<ExampleDocument> linksToC = new ArrayList<ExampleDocument>();
		linksToA.add(docC); 
		linksToB.add(docA);
		linksToC.add(docA); linksToC.add(docB);
		oldRankA = docA.getRank();
		oldRankB = docB.getRank();
		oldRankC = docC.getRank();
		double newRankA = docC.getRank() / docC.getOutGoingLinks();
		double newRankB = docA.getRank() / docA.getOutGoingLinks();
		double newRankC = (docA.getRank() / docA.getOutGoingLinks()) + (docB.getRank() / docB.getOutGoingLinks());
		newRankA = round(newRankA);
		newRankB = round(newRankB);
		newRankC = round(newRankC);
		
		docA.setRank(newRankA); docB.setRank(newRankB); docC.setRank(newRankC);
		
		System.out.println(oldRankA != docA.getRank());
		System.out.println(oldRankB != docB.getRank());
		System.out.println(oldRankC != docC.getRank());
		System.out.println("A: " + docA.getRank() + "\nB: " + docB.getRank() + "\nC: " + docC.getRank() + "\n");
		
		// Will call recursion until the rank converges.
		// Compares Old values with new values.
		if(oldRankA != docA.getRank() || oldRankB != docB.getRank() || oldRankC != docC.getRank()){
			docRank(docA, docB, docC, oldRankA, oldRankB, oldRankC);
		}
		else{
			System.out.println("\nRecursion finished. final converged result is: ");
			System.out.println("A: " + docA.getRank() + "\nB: " + docB.getRank() + "\nC: " + docC.getRank());
			return;
		}
	}
	
	public static double round(double x){
		return Math.round(x * 100.0) / 100.0;
	}
}


