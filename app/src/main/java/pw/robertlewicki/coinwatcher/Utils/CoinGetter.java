package pw.robertlewicki.coinwatcher.Utils;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import pw.robertlewicki.coinwatcher.Interfaces.IFragmentUpdater;
import pw.robertlewicki.coinwatcher.Interfaces.IFileStorageHandler;
import pw.robertlewicki.coinwatcher.Models.Coin;

public class CoinGetter extends AsyncTask<String, Void, List<Coin>>
{

    private IFragmentUpdater fragmentUpdater;
    private IFileStorageHandler fileStorageHandler;

    private final String dataStorageFilename = "coin_data";

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

        boolean shouldFetchNewData = Timer.shouldReloadData(fileStorageHandler);

        try
        {
            String response;
            if(shouldFetchNewData)
            {
                response = httpGetter.getResponse("https://api.coinmarketcap.com/v1/ticker/");
                fileStorageHandler.saveToFile(dataStorageFilename, response);
                return jsonParser.stringToListOfObjects(response, Coin.class);
            }
            else
            {
                if(fragmentUpdater.isEmpty())
                {
                    response = fileStorageHandler.loadFromFile(dataStorageFilename);
                    return jsonParser.stringToListOfObjects(response, Coin.class);
                }
                else
                {
                    return null;
                }
            }

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
    protected void onPostExecute(@Nullable List<Coin> data)
    {
        super.onPostExecute(data);
        fragmentUpdater.stopSpinAnimation();
        if(data != null)
        {
            fragmentUpdater.update(data);
        }
    }
}
