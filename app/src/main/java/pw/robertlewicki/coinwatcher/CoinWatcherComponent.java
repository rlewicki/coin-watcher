package pw.robertlewicki.coinwatcher;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import pw.robertlewicki.coinwatcher.ChasingCoinsApi.ChasingCoins;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCap;
import pw.robertlewicki.coinwatcher.Fragments.AllCoinsFragment;
import retrofit2.Retrofit;

@Component(modules = {AndroidInjectionModule.class, CoinWatcherModule.class, ApplicationModule.class})
public interface CoinWatcherComponent extends AndroidInjector<CoinWatcherApplication>
{
    void inject(CoinMarketCap coinMarketCap);
    void inject(ChasingCoins chasingCoins);
    void inject(AllCoinsFragment allCoinsFragment);
}
