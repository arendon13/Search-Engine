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
		
		DocRankFirstIter(docA, docB, docC);
		
		
	}
	
	public static void DocRankFirstIter(ExampleDocument docA, ExampleDocument docB, ExampleDocument docC){
		ArrayList<ExampleDocument> aLinks = new ArrayList<ExampleDocument>();
		ArrayList<ExampleDocument> bLinks = new ArrayList<ExampleDocument>();
		ArrayList<ExampleDocument> cLinks = new ArrayList<ExampleDocument>();
		aLinks.add(docB); aLinks.add(docC);
		bLinks.add(docC);
		cLinks.add(docA);
		double newRankA = docC.getRank() / docC.getOutGoingLinks();
		double newRankB = docA.getRank() / docA.getOutGoingLinks();
		double newRankC = (docA.getRank() / docA.getOutGoingLinks()) + (docB.getRank() / docB.getOutGoingLinks());
		newRankA = round(newRankA);
		newRankB = round(newRankB);
		newRankC = round(newRankC);
		
		docA.setRank(newRankA); docB.setRank(newRankB); docC.setRank(newRankC);
		
		System.out.println("A: " + docA.getRank() + "\nB: " + docB.getRank() + "\nC: " + docC.getRank());
	}
	
	public static double round(double x){
		return Math.round(x * 100.0) / 100.0;
	}
}


