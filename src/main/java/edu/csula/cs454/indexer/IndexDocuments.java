package edu.csula.cs454.indexer;

import com.mongodb.MongoClient;
import edu.csula.cs454.crawler.DocumentMetadata;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.Datastore;

/**
 * Created by esauceda on 2/26/16.
 */
public class IndexDocuments {
    public static void main(String args[]){
        final Morphia morphia = new Morphia();
        final Datastore ds = morphia.createDatastore(new MongoClient(), "CrawledData");
        final Datastore in = morphia.createDatastore(new MongoClient(), "DataIndex");
        for (DocumentMetadata doc : ds.find(DocumentMetadata.class)){
            for (String term : doc.getContent()){
                System.out.println(term);
            }
        }
    }
}
