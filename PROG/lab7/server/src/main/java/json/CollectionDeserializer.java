package json;

import java.lang.reflect.Type;
import java.util.Deque;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import common.data.*;
import com.google.gson.*;
import log.Log;


/**
 * type adapter for json deserialization
 */
public class CollectionDeserializer implements JsonDeserializer<Deque<Worker>> {
    private final Set<Integer> uniqueIds;

    /**
     * constructor
     *
     * @param uniqueIds set of ids. useful for generating new id
     */
    public CollectionDeserializer(Set<Integer> uniqueIds) {
        this.uniqueIds = uniqueIds;
    }

    @Override
    public Deque<Worker> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        Deque<Worker> collection = new ConcurrentLinkedDeque<>();
        JsonArray workers = json.getAsJsonArray();
        int damagedElements = 0;
        for (JsonElement jsonWorker : workers) {
            Worker worker = null;
            try {
                if (jsonWorker.getAsJsonObject().entrySet().isEmpty()) {
                    Log.logger.error("empty worker found");
                    throw new JsonParseException("empty worker");
                }
                if (!jsonWorker.getAsJsonObject().has("id")) {
                    Log.logger.error("found worker without id");
                    throw new JsonParseException("no id");
                }
                worker = context.deserialize(jsonWorker, Worker.class);

                Integer id = worker.getId();

                if (uniqueIds.contains(id)) {
                    Log.logger.error("database already contains worker with id #" + id);
                    throw new JsonParseException("id isnt unique");
                }
                if (!worker.validate()) {
                    Log.logger.error("worker #" + id + " doesnt match specific conditions");
                    throw new JsonParseException("invalid worker data");
                }
                uniqueIds.add(id);
                collection.add(worker);
            } catch (JsonParseException e) {
                damagedElements += 1;
            }
        }
        if (collection.size() == 0) {
            if (damagedElements == 0) Log.logger.error("database is empty");
            else Log.logger.error("all elements in database are damaged");
            throw new JsonParseException("no data");
        }
        if (damagedElements != 0)
            Log.logger.error(damagedElements + " elements in database are damaged");
        return collection;
    }
}
