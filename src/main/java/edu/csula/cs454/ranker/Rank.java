package edu.csula.cs454.ranker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;


public class Rank {
	static Map<String, ExampleDocument> docs = new HashMap<String, ExampleDocument>();
	
	public static void main(String args[]){
		DecimalFormat df = new DecimalFormat("0.00");
		
		int collectionSize = 3;
		double distRank = 1.0 / collectionSize;
		ArrayList<String> pointsToA = new ArrayList<String>();
		ArrayList<String> pointsToB = new ArrayList<String>();
		ArrayList<String> pointsToC = new ArrayList<String>();
		ExampleDocument docA = new ExampleDocument("URLA", round(distRank), 2, pointsToA);
		ExampleDocument docB = new ExampleDocument("URLB", round(distRank), 1, pointsToB);
		ExampleDocument docC = new ExampleDocument("URLC", round(distRank), 1, pointsToC);
		docs.put(docA.getRandomURL(), docA);
		docs.put(docB.getRandomURL(), docB);
		docs.put(docC.getRandomURL(), docC);
		
		System.out.println("A: " + docA.getRank() + "\nB: " + docB.getRank() + "\nC: " + docC.getRank());
		
		System.out.println("\nAfter first iteration: \n");
		
		double oldA = 0;
		double oldB = 0; 
		double oldC = 0;
		
		pointsToC.add("URLA"); pointsToC.add("URLB");
		pointsToB.add("URLA");
		pointsToA.add("URLC");
		
		ArrayList<ExampleDocument> collection = new ArrayList<ExampleDocument>();
		collection.add(docA); collection.add(docB); collection.add(docC);	
		collectionRank(collection);	
	}
	
	public static void collectionRank(ArrayList<ExampleDocument> collection){
		double sum;
		double oldRank;
		boolean recurs = false;
		System.out.println("Before loop: " + recurs);
		Map<String, Double> oldRanks = getRanks(collection);
		for(ExampleDocument doc: collection){
			System.out.print(doc.getRandomURL() + ": ");
			sum = 0.0;
			oldRank = 0.0;
			
			for(String path: doc.getPointsToDoc()){
				ExampleDocument v = docs.get(path);
				oldRank = oldRanks.get(path);
				
				System.out.print("(Current V: " + oldRank + "/" + v.getOutGoingLinks() + ")");
				System.out.print("(Sum Before: " + sum + ") ");
				System.out.print(path + " ");
				sum += oldRank / v.getOutGoingLinks();
				System.out.print("(Sum: " + sum + ") ");
			}
			System.out.println("");
			double newRank = round(sum);
			
			if(!recurs){
				oldRank = oldRanks.get(doc.getRandomURL());
				double change = newRank - oldRank;
				System.out.println(newRank + " - " + oldRank + " = " + change);
				if(change != 0){
					recurs = true;
				}
			}
			
			doc.setRank(newRank);
		}
		
		if(recurs){
			collectionRank(collection);
		}
		else{
			System.out.println("\nRecursion finished. final converged result is: ");
			System.out.println("A: " + collection.get(0).getRank() + "\nB: " + collection.get(1).getRank() + "\nC: " + collection.get(2).getRank());
			return;
		}
	}
	
	public static void docRank(ExampleDocument docA, ExampleDocument docB, ExampleDocument docC, double oldRankA, double oldRankB, double oldRankC){
		// following commented lines of code were written with the thought of making a loop easier. Not currently used.
//		ArrayList<ExampleDocument> linksToA = new ArrayList<ExampleDocument>();
//		ArrayList<ExampleDocument> linksToB = new ArrayList<ExampleDocument>();
//		ArrayList<ExampleDocument> linksToC = new ArrayList<ExampleDocument>();
//		linksToA.add(docC); 
//		linksToB.add(docA);
//		linksToC.add(docA); linksToC.add(docB);
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
	
	public static Map<String, Double> getRanks(ArrayList<ExampleDocument> collection){
		Map<String, Double> oldRanks = new HashMap<String, Double>();
		for(ExampleDocument doc: collection){
			oldRanks.put(doc.getRandomURL(), doc.getRank());
		}
		return oldRanks;
	}
}


