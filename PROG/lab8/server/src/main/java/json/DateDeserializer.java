package json;

import java.lang.reflect.Type;
import java.util.Date;

import static common.utils.DateConverter.*;

import com.google.gson.*;

import common.exceptions.InvalidDateFormatException;

/**
 * type adapter for json deserialization
 */
public class DateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return parseDate(json.getAsJsonPrimitive().getAsString());
        } catch (InvalidDateFormatException e) {
            throw new JsonParseException("");
        }
    }
}