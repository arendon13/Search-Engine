package edu.csula.cs454.indexer;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.bson.types.ObjectId;
import java.util.Map;
/**
 * Created by esauceda on 3/14/16.
 */
@Entity
public class ImgIndex {
    @Id private ObjectId id;
    private String term;
    private Map<ObjectId, Double> locations;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Map<ObjectId, Double> getLocations() {
        return locations;
    }

    public void setLocations(Map<ObjectId, Double> locations) {
        this.locations = locations;
    }

    public void addLocation(ObjectId id, Double prob){
        if (!locations.containsKey(id)){
            locations.put(id, prob);
        }
    }
}
