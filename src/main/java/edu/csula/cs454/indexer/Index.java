package edu.csula.cs454.indexer;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.bson.types.ObjectId;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Entity
public class Index {
    @Id private ObjectId id;
    private String term;
    private ArrayList<ObjectId> locations;

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
        locations.add(location);
    }

    public void setLocations(ArrayList<ObjectId> locations) {
        this.locations = locations;
    }
}
