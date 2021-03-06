package pw.robertlewicki.coinwatcher.Di.Components;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import pw.robertlewicki.coinwatcher.CoinWatcherApplication;
import pw.robertlewicki.coinwatcher.Di.Modules.CoinMarketCapModule;
import pw.robertlewicki.coinwatcher.Di.Modules.CoinWatcherModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, CoinWatcherModule.class, CoinMarketCapModule.class})
public interface CoinWatcherComponent extends AndroidInjector<CoinWatcherApplication>
{
}
