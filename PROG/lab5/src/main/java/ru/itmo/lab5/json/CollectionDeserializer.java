package ru.itmo.lab5.json;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Vector;
import ru.itmo.lab5.data.*;
import com.google.gson.*;
import static ru.itmo.lab5.io.OutputManager.*;

/**
 * type adapter for json deserialization
 */
public class CollectionDeserializer implements JsonDeserializer<Vector<MusicBand>>{
    private HashSet<Integer> uniqueIds;

    /**
     * constructor
     * @param uniqueIds set of ids. useful for generating new id
     */
    public CollectionDeserializer(HashSet<Integer> uniqueIds){
        this.uniqueIds = uniqueIds;
    }
    @Override
    public Vector<MusicBand> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        Vector<MusicBand> collection = new Vector<>();
        JsonArray mBands = json.getAsJsonArray();
        int damagedElements = 0;
        for (JsonElement jsonWorker: mBands){
            MusicBand mband = null;
            try{
                if(jsonWorker.getAsJsonObject().entrySet().isEmpty()){
                    printErr("empty music band found");
                    throw new JsonParseException("empty worker");
                }
                if(!jsonWorker.getAsJsonObject().has("id")) {
                    printErr("found music band without id");
                    throw new JsonParseException("no id");
                }
                mband = (MusicBand) context.deserialize(jsonWorker, MusicBand.class);
                
                Integer id = mband.getId();
                
                if(uniqueIds.contains(id)) {
                    printErr("database already contains worker with id #" + Integer.toString(id));
                    throw new JsonParseException("id isnt unique");
                }
                if(!mband.validate()) {
                    printErr("worker #"+Integer.toString(id) + " doesnt match specific conditions");
                    throw new JsonParseException("invalid worker data"); 
                }      
                uniqueIds.add(id);        
                collection.add(mband);
            } catch (JsonParseException e){
                damagedElements += 1;
            }
        }   
        if(collection.size()==0){
            if(damagedElements == 0) printErr("database is empty");
            else  printErr("all elements in database are damaged");
            throw new JsonParseException("no data");
        }
        if (damagedElements != 0) printErr(Integer.toString(damagedElements) + " elements in database are damaged"); 
        return collection;
    }
}