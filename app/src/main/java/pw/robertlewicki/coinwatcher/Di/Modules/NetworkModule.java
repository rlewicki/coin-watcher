package pw.robertlewicki.coinwatcher.Di.Modules;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Logger;
import pw.robertlewicki.coinwatcher.Utils.Utils;
import timber.log.Timber;

@Module(includes = {ContextModule.class})
public class NetworkModule
{
    @Provides
    @Singleton
    @Named("CacheExcluded")
    OkHttpClient provideOkHttpClientCacheExcluded(
            HttpLoggingInterceptor loggingInterceptor)
    {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    @Named("CacheIncluded")
    OkHttpClient provideOkHttpClientCacheIncluded(
            Cache cache,
            HttpLoggingInterceptor loggingInterceptor,
            Interceptor networkInterceptor)
    {
        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(networkInterceptor)
                .build();
    }

    @Provides
    @Singleton
    Cache provideOkHttpClientCache(File file)
    {
        return new Cache(file, 10 * 1024 * 1024);
    }

    @Singleton
    @Provides
    File provideFileToCache(Context context)
    {
        return new File(context.getCacheDir(), "httpclient_cache");
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor(Logger logger)
    {
        return new HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BASIC);
    }

    @Singleton
    @Provides
    Logger provideHttpLogger()
    {
        return new Logger()
        {
            @Override
            public void log(String message)
            {
                Timber.i(message);
            }
        };
    }

    @Singleton
    @Provides
    Interceptor provideRequestInterceptor(final Context context)
    {
        return new Interceptor()
        {
            @Override
            public Response intercept(Chain chain) throws IOException
            {
                Response originalResponse = chain.proceed(chain.request());
                if(Utils.isNetworkAvailable(context))
                {
                    int maxAge = 60;
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                }
                else
                {
                    int maxStale = 60 * 60 * 24;
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };
    }
}
