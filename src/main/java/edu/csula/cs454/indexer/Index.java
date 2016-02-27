package edu.csula.cs454.indexer;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.bson.types.ObjectId;

@Entity
public class Index {
    @Id private ObjectId id;
    private String term;
    private String[][] locations;

    public ObjectId getID() {
        return id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String[][] getLocations() {
        return locations;
    }

    public void setLocations(String[][] locations) {
        this.locations = locations;
    }

}
