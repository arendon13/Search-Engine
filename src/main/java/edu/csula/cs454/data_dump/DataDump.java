package edu.csula.cs454.data_dump;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.json.*;
import org.mongodb.morphia.Datastore;

import java.io.FileWriter;
import java.io.IOException;

public class DataDump {
	private static MongoDatabase db;
	private static MongoClient mongo;
	private static MongoCollection collection;
	private static int iterator = 1;
	public static void main(String[] args) {
		//Data dump code goes here lol as if you couldn't tell
		mongo = new MongoClient();
		db = mongo.getDatabase("CrawledData");
		collection = db.getCollection("DocumentMetadata");

		JSONObject dump = new JSONObject();

		for (Object val : collection.find()) {
			dump.append(String.valueOf(iterator), val);
		}

		try {
			FileWriter jsonWriter = new FileWriter("dump.json");
			jsonWriter.write(dump.toString(2));
			jsonWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
