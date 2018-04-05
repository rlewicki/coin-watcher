package pw.robertlewicki.coinwatcher.Di.Modules;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCap;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapImageService;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapService;
import retrofit2.Retrofit;

@Module(includes = {RetrofitModule.class})
public class CoinMarketCapModule
{
    @Provides
    @Singleton
    CoinMarketCapService provideCoinMarketCapService(Retrofit retrofit)
    {
        return retrofit.create(CoinMarketCapService.class);
    }

    @Provides
    @Singleton
    CoinMarketCapImageService provideCoinMarketCapImageService(
            @Named("CacheExcluded") OkHttpClient okHttpClient)
    {
        return new CoinMarketCapImageService(okHttpClient);
    }

    @Provides
    CoinMarketCap provideCoinMarketCap(
            CoinMarketCapService coinMarketCapService,
            CoinMarketCapImageService coinMarketCapImageService)
    {
        return new CoinMarketCap(coinMarketCapService, coinMarketCapImageService);
    }
}
