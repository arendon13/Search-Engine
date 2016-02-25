package edu.csula.cs454.crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;


public class WebDocument {
	
	private Document doc= null;
	private Response response = null;
	private String documentId = null;
	private boolean isHtml;
	private String fileExtesion = null;
	private String path = "";
		
	//private InputStream input = null;
	
	
	public WebDocument(Document doc){
		//This assumes the document passed in is an html document 
		this.doc = doc;
		isHtml = true;
		fileExtesion="html";
	}
	
	public WebDocument(Response resp){
		//This assues that the docuemnt passed in is something other than an html document 
		this.response = resp;
		isHtml = false;
		//input = in;
	}
	
	public String getUrl(){
		if(isHtml)return doc.location();
		else return clean(response.url().toString()); 				
		//return null;
	}
	
	private String clean(String dirtyUrl){
		
		//remove query String
		for(int i = 0, length = dirtyUrl.length();i < length;i++)
		{
			if(dirtyUrl.charAt(i) == '?')return dirtyUrl.substring(0,i);	
		}
		return dirtyUrl;
	}
	
	public String[] getContent(){
		if(isHtml)return doc.select("body").text().split(" ");
		else return new String[]{};//TODO this is temparary  should check based on file type 
		
		//return null;
	}
	
	public String getExtension(){
		if(fileExtesion != null)return fileExtesion;
		//TODO find out the file extension
		fileExtesion = FilenameUtils.getExtension(getUrl());
		return fileExtesion;
	}
	
	public String getPath(){
		if(documentId == null)generateDocID();
		return path+"/"+documentId+"."+getExtension();
	}
	
	private void generateDocID(){
		documentId = MD5(getUrl()); 
	}
	
	private String MD5(String md5) {
		   try {
		        MessageDigest md = MessageDigest.getInstance("MD5");
		        byte[] array = md.digest(md5.getBytes());
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < array.length; ++i) {
		          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		       }
		        return sb.toString();
		    } catch (java.security.NoSuchAlgorithmException e) {
		    }
		    return null;
	}
	
	public void saveToDisk(String storagePath){
		path = storagePath;
		String fileName = getPath();
		System.out.println("Saving to file:"+fileName);
		try {
				new FileOutputStream(fileName, false).close();
				if(isHtml){
					PrintWriter docFile = new PrintWriter (fileName, "UTF-8");
					docFile.print(doc.toString());
					docFile.close();					
				}else{
					OutputStream out = new FileOutputStream(fileName);
					out.write(response.bodyAsBytes());
					out.close();
				}			
				
		} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}			
	}
}
