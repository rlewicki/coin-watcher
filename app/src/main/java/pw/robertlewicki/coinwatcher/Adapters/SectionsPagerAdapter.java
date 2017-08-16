package pw.robertlewicki.coinwatcher.Adapters;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import pw.robertlewicki.coinwatcher.Fragments.BaseFragment;
import pw.robertlewicki.coinwatcher.Interfaces.IFragmentUpdater;
import pw.robertlewicki.coinwatcher.Models.Coin;
import pw.robertlewicki.coinwatcher.Utils.CoinGetter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager fm, Application app) {
        super(fm);

        fragments.add(BaseFragment.newInstance("All", app));
        fragments.add(BaseFragment.newInstance("Favorites", app));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }
}
