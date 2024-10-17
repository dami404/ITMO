package ru.itmo.lab5.json;
import java.lang.reflect.Type;
import java.util.Date;
import com.google.gson.*;
import static ru.itmo.lab5.utils.DateConverter.*;
public class DateSerializer implements JsonSerializer<Date>{
    @Override
    public JsonPrimitive serialize (Date date, Type srcType, JsonSerializationContext context){
        return new JsonPrimitive(dateToString(date));
    }
    
}
