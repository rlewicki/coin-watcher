package pw.robertlewicki.coinwatcher;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import okhttp3.OkHttpClient;
import pw.robertlewicki.coinwatcher.Activities.MainActivity;

@Module
public abstract class CoinWatcherModule
{
    @ContributesAndroidInjector
    abstract MainActivity contributeActivityInjector();
}

@Module
class NetworkModule
{
    @Provides
    OkHttpClient contributeHttpClientInjector()
    {
        return new OkHttpClient();
    }
}