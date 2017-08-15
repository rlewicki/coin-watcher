package pw.robertlewicki.coinwatcher.Utils;

import android.os.AsyncTask;

import java.util.List;

import pw.robertlewicki.coinwatcher.Interfaces.IFragmentUpdater;
import pw.robertlewicki.coinwatcher.Models.Coin;

public class CoinGetter extends AsyncTask<String, Void, List<Coin>> {

    private IFragmentUpdater fragmentUpdater;

    public CoinGetter(IFragmentUpdater fragmentUpdater) {
        this.fragmentUpdater = fragmentUpdater;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Coin> doInBackground(String[] params) {
        HttpGetter httpGetter = new HttpGetter();
        JsonParser jsonParser = new JsonParser();
        try {
            String response = httpGetter.getResponse("https://api.coinmarketcap.com/v1/ticker/");
            List<Coin> coins = jsonParser.stringToListOfObjects(response, Coin.class);
            return coins;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void[] params) {
        super.onProgressUpdate(params);
    }

    @Override
    protected void onPostExecute(List<Coin> data) {
        super.onPostExecute(data);
        fragmentUpdater.updateFragments(data);
    }
}
