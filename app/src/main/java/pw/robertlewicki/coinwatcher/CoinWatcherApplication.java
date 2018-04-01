package pw.robertlewicki.coinwatcher;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import pw.robertlewicki.coinwatcher.Di.Components.DaggerCoinWatcherComponent;
import pw.robertlewicki.coinwatcher.Di.Modules.ContextModule;

public class CoinWatcherApplication extends Application implements HasActivityInjector
{
    @Inject
    public DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate()
    {
        super.onCreate();
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
}
