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

    public ObjectId getID() {
        return id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }


    public void addLocation(ObjectId location) {
        if (locations.containsKey(location)) {
            int count = locations.get(location) + 1;
            locations.put(location, count);
        } else {
            locations.put(location, 1);
        }
    }
    public int docCount(){
        return locations.size();
    }

    public void setLocations(Map<ObjectId, Integer> locations) {
        this.locations = locations;
    }

    /*public void setTfIdf(double tfIdf) {
        this.tfIdf = tfIdf;
    }*/
}
