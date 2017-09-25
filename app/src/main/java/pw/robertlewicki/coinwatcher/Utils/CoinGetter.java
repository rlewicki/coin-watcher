package pw.robertlewicki.coinwatcher.Utils;

import android.os.AsyncTask;

import java.util.List;

import pw.robertlewicki.coinwatcher.Interfaces.IFragmentUpdater;
import pw.robertlewicki.coinwatcher.Interfaces.IFileStorageHandler;
import pw.robertlewicki.coinwatcher.Models.Coin;

public class CoinGetter extends AsyncTask<String, Void, List<Coin>>
{

    private IFragmentUpdater fragmentUpdater;
    private IFileStorageHandler fileStorageHandler;

    private final String dataStorageFilename = "coin_data";
    private final int updateInterval = 300;

    public CoinGetter(
            IFragmentUpdater fragmentUpdater, IFileStorageHandler internalStorageHandler)
    {
        this.fragmentUpdater = fragmentUpdater;
        this.fileStorageHandler = internalStorageHandler;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected List<Coin> doInBackground(String[] params)
    {
        HttpGetter httpGetter = new HttpGetter();
        JsonParser jsonParser = new JsonParser();

        long currentTime = System.currentTimeMillis() / 1000L;
        boolean shouldFetchNewData = false;
        long timestamp = fileStorageHandler.loadFromSharedPreferences("timestamp");
        if(timestamp == -1 ||timestamp + updateInterval < currentTime)
        {
            shouldFetchNewData = true;
        }

        try
        {
            String response;
            if(shouldFetchNewData)
            {
                response = httpGetter.getResponse("https://api.coinmarketcap.com/v1/ticker/");
                fileStorageHandler.saveToFile(dataStorageFilename, response);
                fileStorageHandler.saveToSharedPreferences("timestamp", currentTime);
            }
            else
            {
                response = fileStorageHandler.loadFromFile(dataStorageFilename);
            }
            List<Coin> coins = jsonParser.stringToListOfObjects(response, Coin.class);
            return coins;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void[] params)
    {
        super.onProgressUpdate(params);
    }

    @Override
    protected void onPostExecute(List<Coin> data)
    {
        super.onPostExecute(data);
        fragmentUpdater.update(data);
    }
}
