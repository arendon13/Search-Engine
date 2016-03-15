package edu.csula.cs454.indexer;

import com.mongodb.MongoClient;
import edu.csula.cs454.crawler.DocumentMetadata;
import edu.csula.cs454.ranker.ToastRanker;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by esauceda on 2/26/16.
 */
public class IndexDocuments {
	public static void main(String args[]){
        final Morphia morphia = new Morphia();
        morphia.map(Index.class);
        morphia.map(DocumentMetadata.class);        
        final Datastore ds = morphia.createDatastore(new MongoClient(), "CrawledData");
        ds.delete(ds.createQuery(Index.class));
        Query<DocumentMetadata> documents = ds.find(DocumentMetadata.class);
        int totalDocs = (int) documents.countAll();
        ToastRanker ranker = new ToastRanker();
        for (DocumentMetadata doc : documents)
        {
        	ranker.addDocument(doc);
            for (String term : doc.getContent()) 
            {
                //Check if term exists in index
                //if if does, then append class with new id
                // otherwise, create new class and add to db
                if (!termExistsInIndex(ds, term, doc.getID())) {
                    Map<ObjectId, Integer> locations = new HashMap<ObjectId, Integer>();
                    locations.put(doc.getID(), 1);
                    Index newTerm = new Index();
                    newTerm.setTerm(term);
                    newTerm.setLocations(locations);
                    ds.save(newTerm);
                }
            }
        }
        System.out.println("Done indexing");
        System.out.println("Let the ranking begin!!!!");
        DocumentMetadata[] rankedDocs = ranker.rankDocumentsUsingSecretToastMethod();
        for(DocumentMetadata d:  rankedDocs )
        {
        	ds.save(d);
        }
        System.out.print("Ranking is complete!!");
        //calculateTfIdf(ds, totalDocs);
    }

	/*
	 * private static void calculateTfIdf(Datastore ds, int totalDocs) { double
	 * tfIdf; for (Index term : ds.find(Index.class)){ tfIdf =
	 * Math.log10(totalDocs / term.docCount());
	 * //System.out.println(term.getTerm() + " TF-IDF: " + tfIdf);
	 * term.setTfIdf(tfIdf); ds.save(term); } }
	 */

	public static boolean termExistsInIndex(Datastore ds, String term, ObjectId location){
        boolean existence = false;
        Query<Index> query = ds.find(Index.class, "term ==", term);
        if (query.asList().size() > 0){
            existence = true;
            Index index = query.asList().get(0);
            index.addLocation(location);
            ds.save(index);
        }
        return existence;
    }
}