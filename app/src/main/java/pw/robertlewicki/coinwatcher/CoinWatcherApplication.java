package pw.robertlewicki.coinwatcher;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import pw.robertlewicki.coinwatcher.Di.Components.DaggerCoinWatcherComponent;
import pw.robertlewicki.coinwatcher.Di.Modules.ContextModule;
import timber.log.Timber;

public class CoinWatcherApplication extends Application
        implements HasActivityInjector, HasSupportFragmentInjector
{
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

    @Override
    public void onCreate()
    {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        DaggerCoinWatcherComponent.builder()
                .contextModule(new ContextModule(this))
                .build()
                .inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector()
    {
        return dispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector()
    {
        return dispatchingFragmentInjector;
    }
}
