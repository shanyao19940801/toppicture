package com.amano.springBoot.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by jeff on 2017/11/23.
 */
public class JsonUtils {

    public static String encode(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    public static <T> T decode(String json, Class<T> target) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, target);
    }

    public static <T> T decode(String json, TypeReference target) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, target);
    }
}
