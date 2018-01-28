package ru.alexandrkotovfrombutovo.destrictpassengerapp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by vasap87 on 28.01.18.
 */

public class JsonUtil {

    private ObjectMapper objectMapper;
    private static JsonUtil jsonUtil;
    private JsonUtil(){
        objectMapper = new ObjectMapper();
    }

    public static JsonUtil getInstance(){
        if(jsonUtil==null){
            jsonUtil = new JsonUtil();
        }
        return jsonUtil;
    }

    public String serialization(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    public <T> T deserialization (String string, Class<T> clazz) throws IOException {
        return objectMapper.readValue(string, clazz);
    }
}
