package pw.robertlewicki.coinwatcher.Utils;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import pw.robertlewicki.coinwatcher.Interfaces.IFragmentUpdater;
import pw.robertlewicki.coinwatcher.Interfaces.IFileStorageHandler;
import pw.robertlewicki.coinwatcher.Misc.ResponseStatus;
import pw.robertlewicki.coinwatcher.Models.Coin;
import pw.robertlewicki.coinwatcher.Models.Response;

public class CoinGetter extends AsyncTask<String, Void, Response>
{
    private IFragmentUpdater fragmentUpdater;
    private IFileStorageHandler fileStorageHandler;
    private ConnectionChecker connectionChecker;

    private final String dataStorageFilename = "coin_data";

    public CoinGetter(
            IFragmentUpdater fragmentUpdater,
            IFileStorageHandler internalStorageHandler,
            ConnectionChecker connectionChecker)
    {
        this.fragmentUpdater = fragmentUpdater;
        this.fileStorageHandler = internalStorageHandler;
        this.connectionChecker = connectionChecker;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected Response doInBackground(String[] params)
    {
        final String API_URL = "https://api.coinmarketcap.com/v1/ticker/";
        HttpGetter httpGetter = new HttpGetter();
        JsonParser jsonParser = new JsonParser();

        boolean shouldFetchNewData = Timer.shouldReloadData(fileStorageHandler);

        try
        {
            String response;
            if(shouldFetchNewData)
            {
                if(connectionChecker.isConnected())
                {
                    response = httpGetter.getResponse(API_URL);
                    fileStorageHandler.saveToFile(dataStorageFilename, response);
                    List<Coin> coins = jsonParser.stringToListOfObjects(response, Coin.class);
                    return new Response(coins, ResponseStatus.FETCHED_NEW_DATA);
                }
                else
                {
                    response = fileStorageHandler.loadFromFile(dataStorageFilename);
                    List<Coin> coins =  jsonParser.stringToListOfObjects(response, Coin.class);
                    return new Response(coins, ResponseStatus.NO_CONNECTION);
                }
            }
            else
            {
                if(fragmentUpdater.isEmpty())
                {
                    response = fileStorageHandler.loadFromFile(dataStorageFilename);
                    List<Coin> coins =  jsonParser.stringToListOfObjects(response, Coin.class);
                    return new Response(coins, ResponseStatus.FETCHED_OLD_DATA);
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return new Response(null, ResponseStatus.NOTHING);
    }

    private void loadDataFromBackup()
    {

    }

    @Override
    protected void onProgressUpdate(Void[] params)
    {
        super.onProgressUpdate(params);
    }

    @Override
    protected void onPostExecute(Response data)
    {
        super.onPostExecute(data);
        if(data != null)
        {
            fragmentUpdater.update(data);
        }
    }
}
