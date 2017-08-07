package pw.robertlewicki.coinwatcher.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import pw.robertlewicki.coinwatcher.Fragments.BaseFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(BaseFragment.newInstance("All"));
        fragments.add(BaseFragment.newInstance("Chosen"));
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
