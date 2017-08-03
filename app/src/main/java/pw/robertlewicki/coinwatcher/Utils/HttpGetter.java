package pw.robertlewicki.coinwatcher.Utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pw.robertlewicki.coinwatcher.Interfaces.IHttpGetter;

public class HttpGetter implements IHttpGetter {

    @Override
    public String getResponse(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
