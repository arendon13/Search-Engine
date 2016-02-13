package edu.csula.cs454.data_dump;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.csula.cs454.crawler.DocumentMetadata;
import org.bson.BSON;
import org.json.*;
import org.mongodb.morphia.*;
import org.mongodb.morphia.query.Query;

import java.io.FileWriter;
import java.io.IOException;

import static com.mongodb.client.model.Projections.*;

public class DataDump {

	private static MongoDatabase db;
	private static MongoClient mongo;
	private static MongoCollection collection;
	private static int iterator = 1;
	private static Datastore dataStore = null;

	public static void main(String[] args) {

		Morphia morphia = new Morphia();
		morphia.map(DocumentMetadata.class);

		mongo = new MongoClient();
		dataStore = morphia.createDatastore(mongo, "CrawledData");

		//Data dump code goes here lol as if you couldn't tell
		db = mongo.getDatabase("CrawledData");
		collection = db.getCollection("DocumentMetadata");

		JSONObject dump = new JSONObject();


		for (Object val : collection.find().projection(fields(exclude("_id")))) {
			dump.append(String.valueOf(iterator), val);
		}

		try {
			FileWriter jsonWriter = new FileWriter("dump.json");
			jsonWriter.write(dump.toString(2));
			jsonWriter.close();
			System.out.println("Concluded Data Dump");
		} catch (IOException e) {
			e.printStackTrace();
		}
		mongo.close();
	}
}
