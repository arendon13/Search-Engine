package edu.csula.cs454.indexer;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.bson.types.ObjectId;
import java.util.Map;

@Entity
public class Index {
    @Id private ObjectId id;
    private String term;
    private Map<ObjectId, Integer> locations;
    private double tfIdf;


    /**
     * If the locations map contains the key, increment the occurrence count.
     * Otherwise, put the location (ObjectId) & set the occurrence count to 1.
     * @param location: (ObjectId) The document where the term was seen.
     */
    public void addLocation(ObjectId location) {
        if (locations.containsKey(location)) {
            int count = locations.get(location) + 1;
            locations.put(location, count);
        } else {
            locations.put(location, 1);
        }
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setLocations(Map<ObjectId, Integer> locations) {
        this.locations = locations;
    }
}
