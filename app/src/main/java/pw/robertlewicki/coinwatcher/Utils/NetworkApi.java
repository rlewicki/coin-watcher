package pw.robertlewicki.coinwatcher.Utils;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkApi
{
    @Inject
    OkHttpClient httpClient;

    @Inject
    public NetworkApi()
    {
    }

    public String getResponseFrom(String url) throws IOException, RuntimeException
    {
        Request request = new Request
                .Builder()
                .url(url)
                .build();

        Response response = httpClient
                .newCall(request)
                .execute();

        if(response.code() != 200)
        {
            throw new RuntimeException(String.format("Http response code %d.", response.code()));
        }

        return response.body().string();
    }
}
