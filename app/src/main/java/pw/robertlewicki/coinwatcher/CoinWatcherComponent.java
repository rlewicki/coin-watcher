package pw.robertlewicki.coinwatcher;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Component(modules = {AndroidInjectionModule.class, CoinWatcherModule.class, NetworkModule.class})
public interface CoinWatcherComponent extends AndroidInjector<CoinWatcherApplication>
{
}
