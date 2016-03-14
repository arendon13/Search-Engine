package edu.csula.cs454.ranker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.csula.cs454.crawler.DocumentMetadata;

// Ranking Take 2

public class RankTest {

	public static void main(String[] args) {
		ArrayList<Integer> empty = new ArrayList<Integer>();
		DocumentMetadata docA = new DocumentMetadata(1, 0.0);
		DocumentMetadata docB = new DocumentMetadata(2, 0.0);
		DocumentMetadata docC = new DocumentMetadata(3, 0.0);
		
		HashMap<Integer, HashSet<Integer>> linksFromMe;
		HashMap<Integer, HashSet<Integer>> linkToMe;
		
		ArrayList<DocumentMetadata> collection = new ArrayList<DocumentMetadata>();
		collection.add(docA); collection.add(docB); collection.add(docC);
		
		initializeRank(collection);
	}
	
	
	public static void initializeRank(ArrayList<DocumentMetadata> collection){
		double initialRank = 1.0 / collection.size();
		initialRank = round(initialRank);
		
		for(DocumentMetadata doc: collection){
			doc.setRank(initialRank);
			System.out.println(doc.getRank());
		}
	}
	
	public static double round(double x){
		return Math.round(x * 100.0) / 100.0;
	}

}
