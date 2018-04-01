package pw.robertlewicki.coinwatcher.Di.Modules;

import android.content.Context;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import pw.robertlewicki.coinwatcher.Activities.MainActivity;

@Module(includes = {ContextModule.class})
public class NetworkModule
{
    @Provides
    OkHttpClient provideOkHttpClient(Cache cache)
    {
        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    @Provides
    Cache provideOkHttpClientCache(File file)
    {
        return new Cache(file, 10 * 1024 * 1024);
    }

    @Provides
    File provideFileToCache(Context context)
    {
        return new File(context.getCacheDir(), "httpclient_cache");
    }
}
