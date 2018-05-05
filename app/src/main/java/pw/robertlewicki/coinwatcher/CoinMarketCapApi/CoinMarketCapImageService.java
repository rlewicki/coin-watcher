package pw.robertlewicki.coinwatcher.CoinMarketCapApi;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class CoinMarketCapImageService
{
    private OkHttpClient okHttpClient;

    private String idsUrl = "https://s2.coinmarketcap.com/generated/search/quick_search.json";

    public CoinMarketCapImageService(OkHttpClient okHttpClient)
    {
        this.okHttpClient = okHttpClient;
    }

    public Call listAllCoinsIds()
    {
        Request request = new Request.Builder()
                .url(idsUrl)
                .build();

        return okHttpClient.newCall(request);
    }
}
