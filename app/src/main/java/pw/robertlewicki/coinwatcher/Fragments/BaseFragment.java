package pw.robertlewicki.coinwatcher.Fragments;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pw.robertlewicki.coinwatcher.Adapters.ListAdapter;
import pw.robertlewicki.coinwatcher.Models.Coin;
import pw.robertlewicki.coinwatcher.R;

public class BaseFragment extends Fragment {

    @BindView(R.id.CoinListView) ListView listView;

    private String title;
    private Application app;

    public BaseFragment() {}

    public static BaseFragment newInstance(String title, Application app) {
        BaseFragment fragment = new BaseFragment();

        fragment.title  = title;
        fragment.app    = app;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void update(List<Coin> coins) {
        listView.setAdapter(new ListAdapter(app, coins));
    }

    public String getTitle() {
        return title;
    }
}
