package pw.robertlewicki.coinwatcher;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import pw.robertlewicki.coinwatcher.ChasingCoinsApi.ChasingCoins;
import retrofit2.Retrofit;

@Component(modules = {AndroidInjectionModule.class, CoinWatcherModule.class, ApplicationModule.class})
public interface CoinWatcherComponent extends AndroidInjector<CoinWatcherApplication>
{
    Retrofit retrofit();
    ChasingCoins chasingCoinsApiImpl();
}
