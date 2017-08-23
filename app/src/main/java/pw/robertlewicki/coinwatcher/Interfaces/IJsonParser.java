package pw.robertlewicki.coinwatcher.Interfaces;

import com.fasterxml.jackson.databind.JavaType;

import java.io.IOException;
import java.util.List;

public interface IJsonParser
{
    <T> T stringToObject(String data, Class<T> objectType) throws IOException;

    <T> List<T> stringToListOfObjects(String data, Class<T> objectType) throws IOException;
}
