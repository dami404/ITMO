package json;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.Vector;
import common.data.*;
import com.google.gson.*;
import log.Log;


/**
 * type adapter for json deserialization
 */
public class CollectionDeserializer implements JsonDeserializer<Vector<MusicBand>>{
    private Set<Integer> uniqueIds;

    /**
     * constructor
     * @param uniqueIds set of ids. useful for generating new id
     */
    public CollectionDeserializer(Set<Integer> uniqueIds){
        this.uniqueIds = uniqueIds;
    }
    @Override
    public Vector<MusicBand> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        Vector<MusicBand> collection = new Vector<>();
        JsonArray workers = json.getAsJsonArray();
        int damagedElements = 0;
        for (JsonElement jsonWorker: workers){
            MusicBand worker = null;
            try{
                if(jsonWorker.getAsJsonObject().entrySet().isEmpty()){
                    Log.logger.error("empty music band found");
                    throw new JsonParseException("empty music band");
                }
                if(!jsonWorker.getAsJsonObject().has("id")) {
                    Log.logger.error("found music band without id");
                    throw new JsonParseException("no id");
                }
                worker = (MusicBand) context.deserialize(jsonWorker, MusicBand.class);
                
                Integer id = worker.getId();
                
                if(uniqueIds.contains(id)) {
                    Log.logger.error("database already contains music band with id #" + Integer.toString(id));
                    throw new JsonParseException("id isnt unique");
                }
                if(!worker.validate()) {
                    Log.logger.error("music band #"+Integer.toString(id) + " doesnt match specific conditions");
                    throw new JsonParseException("invalid music band data");
                }      
                uniqueIds.add(id);        
                collection.add(worker);
            } catch (JsonParseException e){
                damagedElements += 1;
            }
        }   
        if(collection.size()==0){
            if(damagedElements == 0) Log.logger.error("database is empty");
            else  Log.logger.error("all elements in database are damaged");
            throw new JsonParseException("no data");
        }
        if (damagedElements != 0) Log.logger.error(Integer.toString(damagedElements) + " elements in database are damaged");
        return collection;
    }
}
