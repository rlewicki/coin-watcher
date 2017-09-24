package pw.robertlewicki.coinwatcher.Fragments;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import pw.robertlewicki.coinwatcher.Adapters.ListAdapter;
import pw.robertlewicki.coinwatcher.Interfaces.IDataChangedObserver;
import pw.robertlewicki.coinwatcher.Interfaces.ILongTapObserver;
import pw.robertlewicki.coinwatcher.Misc.BundleKeys;
import pw.robertlewicki.coinwatcher.Models.Coin;
import pw.robertlewicki.coinwatcher.R;

public class MyCoinsFragment
        extends Fragment
        implements ILongTapObserver, IDataChangedObserver
{
    @BindView(R.id.CoinListView) ListView listView;

    private String title;
    private Application app;
    private List<Coin> coins;

    public static MyCoinsFragment newInstance(String title, Application app)
    {
        MyCoinsFragment fragment = new MyCoinsFragment();

        fragment.title = title;
        fragment.app = app;
        fragment.coins = new ArrayList<>();

        return fragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.my_coins_fragment, container, false);
        ButterKnife.bind(this, rootView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Bundle data = new Bundle();
                Coin coin = coins.get(position);

                data.putString(BundleKeys.RANK, coin.rank);
                data.putString(BundleKeys.FULL_NAME, coin.currencyName);
                data.putString(BundleKeys.PRICE_USD, coin.priceUsd);
                data.putString(BundleKeys.DAILY_VOLUME, coin.dailyVolumeUsd);
                data.putString(BundleKeys.MARKET_CAP, coin.marketCapUsd);
                data.putString(BundleKeys.AVAILABLE_SUPPLY, coin.availableSupply);
                data.putString(BundleKeys.TOTAL_SUPPLY, coin.totalSupply);
                data.putString(BundleKeys.LAST_UPDATE_TIME, coin.lastUpdated);

                DetailsDialogFragment dialog = new DetailsDialogFragment();
                dialog.setArguments(data);
                dialog.show(getActivity().getFragmentManager(), "details_dialog");
            }
        });

        return rootView;
    }

    public String getTitle()
    {
        return title;
    }

    @Override
    public void update(Coin coin)
    {
        coins.add(coin);
        addCoinToPreferences(coin);
        updateView();
    }

    @Override
    public void update(List<Coin> coins)
    {
        this.coins = getCoinsFromPreferences(coins);
        updateView();
    }

    private void updateView()
    {
        listView.setAdapter(new ListAdapter(app, coins));
    }

    private void addCoinToPreferences(Coin coin)
    {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(coin.id, coin.id);
        editor.apply();
    }

    private List<Coin> getCoinsFromPreferences(List<Coin> allCoins)
    {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_APPEND);
        List<Coin> selectedCoins = new ArrayList<>();
        String defaultValue = "not_found";
        for(Coin coin : allCoins)
        {
            String id = preferences.getString(coin.id, defaultValue);
            if(!Objects.equals(id, defaultValue))
            {
                selectedCoins.add(coin);
            }
        }
        return selectedCoins;
    }
}
