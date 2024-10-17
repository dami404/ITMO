
package ru.itmo.lab5.json;
import java.lang.reflect.Type;
import java.util.Date;
import static ru.itmo.lab5.utils.DateConverter.*;
import com.google.gson.*;
import ru.itmo.lab5.exceptions.InvalidDateFormatException;
/**
 * type adapter for json deserialization
 */
public class DateDeserializer implements JsonDeserializer<Date>{
    @Override
    public Date deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext ) throws JsonParseException{
        try{
            return parseDate(json.getAsJsonPrimitive().getAsString());
        }
        catch(InvalidDateFormatException e){
            throw new JsonParseException("");

        }
    }   
}

