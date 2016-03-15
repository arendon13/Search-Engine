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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by esauceda on 2/26/16.
 */
public class IndexDocuments {
    /**
     * Main function. Iterates & indexes through all documents in the DocumentMetadata collection.
     * Utilizes Clarafai API to return image tags.
     * @param args
     */
    public static void main(String args[]){
        System.out.println(args[0]);
        System.out.println(args[1]);
        final Morphia morphia = new Morphia();
        final Datastore ds = morphia.createDatastore(new MongoClient(), "CrawledData");
        String APP_ID = args[0];
        String APP_SECRET = args[1];
        ds.delete(ds.createQuery(Index.class));
        ds.delete(ds.createQuery(ImgIndex.class));
        Query<DocumentMetadata> documents = ds.find(DocumentMetadata.class);
        ClarifaiClient clarifai = new ClarifaiClient(APP_ID, APP_SECRET);
        HashSet<String> files = new HashSet<String>();
        int counter = 0;
        int size = documents.asList().size();

        for (DocumentMetadata doc : documents){
            if (doc.getContent().length == 0 && !files.contains(doc.getURL())){
                files.add(doc.getURL());
                File img = new File(doc.getPath());
                List<RecognitionResult> result = clarifai.recognize(new RecognitionRequest(img));
                    if (result.get(0).getStatusMessage().equals("OK")){
                        for (Tag tag : result.get(0).getTags()) {
                            if (!termExistsInImgIndex(ds, tag.getName(), doc.getID(), tag.getProbability())) {
                                Map<ObjectId, Double> locations = new HashMap<ObjectId, Double>();
                                locations.put(doc.getID(), tag.getProbability());
                                ImgIndex newImg = new ImgIndex();
                                newImg.setTag(tag.getName());
                                newImg.setLocations(locations);
                                ds.save(newImg);
                            }
                        }
                    }
            }
            for (String term : doc.getContent()) {
                if (!termExistsInIndex(ds, term, doc.getID())) {
                    Map<ObjectId, Integer> locations = new HashMap<ObjectId, Integer>();
                    locations.put(doc.getID(), 1);
                    Index newTerm = new Index();
                    newTerm.setTerm(term);
                    newTerm.setLocations(locations);
                    ds.save(newTerm);
                }
            }
            counter++;
            if (counter % 10 == 0){
                System.out.println("Processed " + counter + " Documents out of " + size);
            }
        }
    }

    /**
     * Checks to see if a term exists in the Index. If it does, then just append a location to that term document. Save
     * document to collection.
     * @param ds: The datastore we are saving to. In our case, it is "CrawledData".
     * @param term: The term we are searching for.
     * @param location: An ObjectId referencing the document that the term was found in.
     * @return Boolean; True if the term exists, otherwise False.
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

    /**
     * Similar to termExistsInIndex, this function checks to see if a term (tag) already exists in our Index ImgIndex.
     * If it does, append a location to that term document and save.
     * @param ds: The datastore we are saving to. "CrawledData".
     * @param tag: The term (tag) we are searching for.
     * @param id: An ObjectId referencing the document that the term was found in.
     * @return Boolean; True if the term exists, otherwise False.
     */
    public static boolean termExistsInImgIndex(Datastore ds, String tag, ObjectId id, Double prob){
        boolean existence = false;
        Query<ImgIndex> query = ds.find(ImgIndex.class, "tag ==", tag);
        if (query.asList().size() > 0){
            existence = true;
            ImgIndex index = query.asList().get(0);
            index.addLocation(id, prob);
            ds.save(index);
        }
        return existence;
    }
}