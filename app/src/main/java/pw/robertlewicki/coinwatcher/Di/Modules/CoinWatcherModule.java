package pw.robertlewicki.coinwatcher.Di.Modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pw.robertlewicki.coinwatcher.Activities.MainActivity;

@Module
public abstract class CoinWatcherModule
{
    @ContributesAndroidInjector
    abstract MainActivity contributeActivityInjector();
}
