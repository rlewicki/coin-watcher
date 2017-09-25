package pw.robertlewicki.coinwatcher.Adapters;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pw.robertlewicki.coinwatcher.Fragments.AllCoinsFragment;
import pw.robertlewicki.coinwatcher.Fragments.MyCoinsFragment;
import pw.robertlewicki.coinwatcher.Interfaces.IFileStorageHandler;

public class SectionsPagerAdapter extends FragmentPagerAdapter
{
    private AllCoinsFragment allCoinsFragment;
    private MyCoinsFragment myCoinsFragment;

    public SectionsPagerAdapter(
            FragmentManager fm, Application app, IFileStorageHandler internalStorageHandler)
    {
        super(fm);

        allCoinsFragment = AllCoinsFragment.newInstance("All", app, internalStorageHandler);
        myCoinsFragment = MyCoinsFragment.newInstance("My", app);
        allCoinsFragment.addLongTapObserver(myCoinsFragment);
        allCoinsFragment.addDataChangedObserver(myCoinsFragment);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return allCoinsFragment;
            case 1:
                return myCoinsFragment;
        }
        return null;
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch(position)
        {
            case 0:
                return allCoinsFragment.getTitle();
            case 1:
                return myCoinsFragment.getTitle();
        }
        return "";
    }

    public void queryCurrencies(String query)
    {
        allCoinsFragment.queryCurrencies(query);
    }
}
