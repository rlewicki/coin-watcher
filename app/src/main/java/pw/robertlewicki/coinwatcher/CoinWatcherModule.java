package pw.robertlewicki.coinwatcher;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import pw.robertlewicki.coinwatcher.Activities.MainActivity;
import pw.robertlewicki.coinwatcher.ChasingCoinsApi.ChasingCoins;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCap;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public abstract class CoinWatcherModule
{
    @ContributesAndroidInjector
    abstract MainActivity contributeActivityInjector();
}

@Module
class ApplicationModule
{
    @Provides
    @Named("ChasingCoins")
    Retrofit provideRetrofitChasingCoins()
    {
        return new Retrofit
                .Builder()
                .baseUrl("https://chasing-coins.com/api/v1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @Provides
    @Named("CoinMarketCap")
    Retrofit provideRetrofitCoinMarketCap()
    {
        return new Retrofit
                .Builder()
                .baseUrl("https://api.coinmarketcap.com/v1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @Provides
    ChasingCoins provideChasingCoinsApiImpl()
    {
        return new ChasingCoins();
    }

    @Provides
    CoinMarketCap provideCoinMarketCapApiImpl()
    {
        return new CoinMarketCap();
    }
}
