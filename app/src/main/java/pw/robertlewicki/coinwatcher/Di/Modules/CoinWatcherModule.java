package pw.robertlewicki.coinwatcher.Di.Modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pw.robertlewicki.coinwatcher.Activities.MainActivity;
import pw.robertlewicki.coinwatcher.Fragments.AllCoinsFragment;

@Module
public abstract class CoinWatcherModule
{
    @ContributesAndroidInjector
    abstract MainActivity contributeActivityInjector();

    @ContributesAndroidInjector
    abstract AllCoinsFragment contributeAllCoinsFragmentInjector();
}
