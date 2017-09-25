package pw.robertlewicki.coinwatcher.Interfaces;

public interface IFileStorageHandler
{
    void saveToFile(String filepath, String data);
    String loadFromFile(String filepath);
    void saveToSharedPreferences(String key, long value);
    long loadFromSharedPreferences(String key);
}
