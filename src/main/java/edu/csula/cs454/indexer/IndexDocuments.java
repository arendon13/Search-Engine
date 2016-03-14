package edu.csula.cs454.indexer;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.mongodb.MongoClient;
import edu.csula.cs454.crawler.DocumentMetadata;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.io.File;
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
        final Datastore ds = morphia.createDatastore(new MongoClient(), "CrawledData");
        String APP_ID = "";
        String APP_SECRET = "";
        ds.delete(ds.createQuery(Index.class));
        Query<DocumentMetadata> documents = ds.find(DocumentMetadata.class);
        int totalDocs = (int) documents.countAll();
        ClarifaiClient clarifai = new ClarifaiClient(APP_ID, APP_SECRET);
        int i = 0;


        for (DocumentMetadata doc : documents){
            if (doc.getContent().length == 0){
                    File img = new File(doc.getPath());
                    RecognitionResult result = clarifai.recognize(new RecognitionRequest(img)).get(0);
                    for (Tag tag : result.getTags()){
                        if (!termExistsInImgIndex(ds, tag.getName(), doc.getID(), tag.getProbability())) {
                            Map<ObjectId, Double> locations = new HashMap<ObjectId, Double>();
                            locations.put(doc.getID(), tag.getProbability());
                            ImgIndex newImg = new ImgIndex();
                            newImg.setTerm(tag.getName());
                            newImg.setLocations(locations);
                            ds.save(newImg);
                        }
                }
            }
            for (String term : doc.getContent()) {
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
    }

   /* private static void calculateTfIdf(Datastore ds, int totalDocs) {
        double tfIdf;
        for (Index term : ds.find(Index.class)){
            tfIdf = Math.log10(totalDocs / term.docCount());
            //System.out.println(term.getTerm() + " TF-IDF: " + tfIdf);
            term.setTfIdf(tfIdf);
            ds.save(term);
        }
    }*/

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

    public static boolean termExistsInImgIndex(Datastore ds, String term, ObjectId id, Double prob){
        boolean existence = false;
        Query<ImgIndex> query = ds.find(ImgIndex.class, "term ==", term);
        if (query.asList().size() > 0){
            existence = true;
            ImgIndex index = query.asList().get(0);
            index.addLocation(id, prob);
            ds.save(index);
        }
        return existence;
    }
}