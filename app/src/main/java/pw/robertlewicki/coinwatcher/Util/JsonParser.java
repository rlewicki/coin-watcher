package pw.robertlewicki.coinwatcher.Util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pw.robertlewicki.coinwatcher.Interfaces.IJsonParser;

public class JsonParser implements IJsonParser {

    @Override
    public <T> T stringToObject(String data, Class<T> objectType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(data, objectType);
    }

    @Override
    public <T> List<T> stringToListOfObjects(String data, Class<T> objectType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(data);
        List<T> objects = new ArrayList<>();

        for (JsonNode child : rootNode) {
            objects.add(objectMapper.readValue(child.toString(), objectType));
        }
        return objects;
    }
}
