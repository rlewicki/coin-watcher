package pw.robertlewicki.coinwatcher.Utils;

import pw.robertlewicki.coinwatcher.Interfaces.IFileStorageHandler;

public class Timer
{
    public static boolean shouldReloadData(IFileStorageHandler fileStorageHandler)
    {
        final int updateInterval = 300;

        long currentTime = System.currentTimeMillis() / 1000L;
        long timestamp = fileStorageHandler.loadFromSharedPreferences("timestamp");
        return timestamp == -1 ||timestamp + updateInterval < currentTime;
    }
}
