package edu.csula.cs454.ranker;

import java.util.ArrayList;

public class ExampleDocument {
	private double rank;
	private int outGoingLinks;
	private String getURL;
	private ArrayList<String> pointsToDoc;
	
	public ExampleDocument(String randomURL, double rank, int outGoingLinks, ArrayList<String> pointsToDoc){
		this.getURL = randomURL;
		this.rank = rank;
		this.outGoingLinks = outGoingLinks;
		this.pointsToDoc = pointsToDoc;
	}

	public ArrayList<String> getOutGoingLinks() {
		return pointsToDoc;
	}

	public double getRank() {
		return rank;
	}

	public String getURL() {
		return getURL;
	}

	public void setURL(String randomURL) {
		this.getURL = randomURL;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}
	
	public int getNumLinks(){
		return outGoingLinks;
	}

}
