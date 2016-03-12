package edu.csula.cs454.crawler;
import java.util.ArrayList;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.bson.types.ObjectId;

@Entity
public class DocumentMetadata {
	
	@Id private ObjectId id;
	private String url;
	private String path;
	private String[] content;
	private String ext;//file extension
	private double rank;//page rank of the document 
	private int numLinksOut;
	private ArrayList<String> links;
	
	public String[] getContent() {
		return content;
	}

	public void setURL(String docURL) {
		url = docURL;
	}
	public ObjectId getID(){
		return id;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	public void setContent(String[] content) {
		this.content = content;		
	}
	public void setFileExtestion(String extension) {
		ext = extension;
	}
	public int  getNumOutGoingLinks(){
		return numLinksOut;
	}
	public void setNumOutGoingLinks(int num){
		numLinksOut = num;
	}
	public void setLinksToThisDoc(ArrayList<String> links){
		this.links = links;
	}	
	public ArrayList<String> getLinksToThisDoc(){
		
		return links;
	}	
	public double getRank(){
		return rank;
	}
	
	public void setRank(double rank){
		this.rank = rank;
	}
}
