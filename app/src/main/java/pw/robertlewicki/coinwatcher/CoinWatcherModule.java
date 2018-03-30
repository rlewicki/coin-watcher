package pw.robertlewicki.coinwatcher;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import pw.robertlewicki.coinwatcher.Activities.MainActivity;
import pw.robertlewicki.coinwatcher.ChasingCoinsApi.ChasingCoins;
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
    Retrofit provideRetrofit()
    {
        return new Retrofit
                .Builder()
                .baseUrl("https://chasing-coins.com/api/v1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @Provides
    ChasingCoins provideChasingCoinsApiImpl(Retrofit retrofit)
    {
        return new ChasingCoins(retrofit);
    }
}
