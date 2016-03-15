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
    private String tag;
    private Map<ObjectId, Double> locations;


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /** Layer of obfuscation for adding a location.
     * @param id: (ObjectId) DocumentId of the Image
     * @param prob: (Double) Probability that the tag is correct.
     */
    public void addLocation(ObjectId id, Double prob){
        if (!locations.containsKey(id)){
            locations.put(id, prob);
        }
    }

    public void setLocations(Map<ObjectId,Double> locations) {
        this.locations = locations;
    }
}
