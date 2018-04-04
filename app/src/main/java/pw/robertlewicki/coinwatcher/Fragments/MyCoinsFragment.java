package pw.robertlewicki.coinwatcher.Fragments;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
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
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapDetailsModel;
import pw.robertlewicki.coinwatcher.Interfaces.IDataChangedObserver;
import pw.robertlewicki.coinwatcher.Interfaces.ILongTapObserver;
import pw.robertlewicki.coinwatcher.Misc.BundleKeys;
import pw.robertlewicki.coinwatcher.R;

public class MyCoinsFragment
        extends Fragment
        implements ILongTapObserver, IDataChangedObserver
{
    @BindView(R.id.CoinListView) ListView listView;

    private String title;
    private Application app;
    private List<CoinMarketCapDetailsModel> coins;

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final CoinMarketCapDetailsModel selectedCoin = coins.get(position);

                builder
                        .setMessage(String.format(
                                "Do you want to remove %s from your list?", selectedCoin.currencyName))
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                coins.remove(selectedCoin);
                                removeCoinFromPreferences(selectedCoin);
                                updateView();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();

                return true;
            }
        });

        return rootView;
    }

    public void clearWatchList()
    {
        removeCoinsFromPreferences(coins);
        coins.clear();
        updateView();
    }

    public String getTitle()
    {
        return title;
    }

    @Override
    public void update(CoinMarketCapDetailsModel coin)
    {
        if(coins.contains(coin))
        {
            return;
        }

        coins.add(coin);
        addCoinToPreferences(coin);
        updateView();
    }

    @Override
    public void update(List<CoinMarketCapDetailsModel> coins)
    {
        this.coins = getCoinsFromPreferences(coins);
        updateView();
    }

    private void updateView()
    {
        listView.setAdapter(new ListAdapter(app, coins));
    }

    private void addCoinToPreferences(CoinMarketCapDetailsModel coin)
    {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(coin.id, coin.id);
        editor.apply();
    }

    private void removeCoinFromPreferences(CoinMarketCapDetailsModel coin)
    {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(coin.id);
        editor.apply();
    }

    private void removeCoinsFromPreferences(List<CoinMarketCapDetailsModel> coins)
    {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for(CoinMarketCapDetailsModel coin : coins)
        {
            editor.remove(coin.id);
        }
        editor.apply();
    }

    private List<CoinMarketCapDetailsModel> getCoinsFromPreferences(List<CoinMarketCapDetailsModel> allCoins)
    {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        List<CoinMarketCapDetailsModel> selectedCoins = new ArrayList<>();
        String defaultValue = "not_found";
        for(CoinMarketCapDetailsModel coin : allCoins)
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
