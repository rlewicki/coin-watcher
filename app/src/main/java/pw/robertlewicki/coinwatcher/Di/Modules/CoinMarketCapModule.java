package pw.robertlewicki.coinwatcher.Di.Modules;

import dagger.Module;
import dagger.Provides;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCap;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapService;
import retrofit2.Retrofit;

@Module(includes = {RetrofitModule.class})
public class CoinMarketCapModule
{
    @Provides
    CoinMarketCapService provideCoinMarketCapService(Retrofit retrofit)
    {
        return retrofit.create(CoinMarketCapService.class);
    }

    @Provides
    CoinMarketCap provideCoinMarketCap(CoinMarketCapService coinMarketCapService)
    {
        return new CoinMarketCap(coinMarketCapService);
    }
}
