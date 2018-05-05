package pw.robertlewicki.coinwatcher.Di.Modules;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module(includes = {NetworkModule.class})
public class RetrofitModule
{
    @Provides
    Retrofit provideRetrofitCoinMarketCap(
            @Named("CacheIncluded") OkHttpClient okHttpClient)
    {
        return new Retrofit.Builder()
                .baseUrl("https://api.coinmarketcap.com/v1/")
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
