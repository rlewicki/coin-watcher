package pw.robertlewicki.coinwatcher.Fragments;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pw.robertlewicki.coinwatcher.Adapters.ListAdapter;
import pw.robertlewicki.coinwatcher.Interfaces.IFragmentUpdater;
import pw.robertlewicki.coinwatcher.Models.Coin;
import pw.robertlewicki.coinwatcher.R;
import pw.robertlewicki.coinwatcher.Utils.CoinGetter;

public class BaseFragment extends Fragment implements IFragmentUpdater {

    @BindView(R.id.SwipeView)
    SwipeRefreshLayout swipeView;

    @BindView(R.id.CoinListView)
    ListView listView;

    private String title;
    private Application app;

    private final IFragmentUpdater self = this;

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

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new CoinGetter(self).execute();
            }
        });

        swipeView.setColorSchemeResources(R.color.circleOrange,
                                          R.color.circleGreen,
                                          R.color.circleBlue);

        swipeView.post(new Runnable() {
            @Override
            public void run() {
                swipeView.setRefreshing(true);
                new CoinGetter(self).execute();
            }
        });

        return rootView;
    }

    @Override
    public void update(List<Coin> coins) {
        listView.setAdapter(new ListAdapter(app, coins));
        swipeView.setRefreshing(false);
    }

    public String getTitle() {
        return title;
    }
}
