package edu.csula.cs454.indexer;

import com.mongodb.MongoClient;
import edu.csula.cs454.crawler.DocumentMetadata;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by esauceda on 2/26/16.
 */
public class IndexDocuments {
    public static void main(String args[]){
        final Morphia morphia = new Morphia();
        final Datastore ds = morphia.createDatastore(new MongoClient(), "CrawledData");
        ds.delete(ds.createQuery(Index.class));

        for (DocumentMetadata doc : ds.find(DocumentMetadata.class)){
            for (String term : doc.getContent()){
                //Check if term exists in index
                //if if does, then append class with new id
                // otherwise, create new class and add to db
                if (!termExistsInIndex(ds, term, doc.getID())){
                    ArrayList<ObjectId> locations = new ArrayList<ObjectId>();
                    locations.add(doc.getID());
                    Index newTerm = new Index();
                    newTerm.setTerm(term);
                    newTerm.setLocations(locations);
                    ds.save(newTerm);

                }
            }
        }
    }

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
