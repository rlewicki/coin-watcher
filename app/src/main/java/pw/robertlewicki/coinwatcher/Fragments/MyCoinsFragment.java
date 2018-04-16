package pw.robertlewicki.coinwatcher.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import pw.robertlewicki.coinwatcher.Adapters.ListAdapter;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapDetailsModel;
import pw.robertlewicki.coinwatcher.Interfaces.DialogOwner;
import pw.robertlewicki.coinwatcher.Interfaces.WatchList;
import pw.robertlewicki.coinwatcher.R;

public class MyCoinsFragment extends Fragment implements WatchList, DialogOwner
{
    private final MyCoinsFragment self = this;

    private String title;
    private List<CoinMarketCapDetailsModel> coins;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public static MyCoinsFragment newInstance(String title)
    {
        MyCoinsFragment fragment = new MyCoinsFragment();

        fragment.title = title;
        fragment.coins = new ArrayList<>();

        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.my_coins_fragment, container, false);
        ButterKnife.bind(this, rootView);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.CoinListView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

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

    private void updateView()
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                recyclerView.setAdapter(new ListAdapter(coins, self));
            }
        });
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

    @Override
    public void newCoinAdded(CoinMarketCapDetailsModel coin)
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
    public void dataUpdated(List<CoinMarketCapDetailsModel> coins)
    {
        this.coins = getCoinsFromPreferences(coins);
        updateView();
    }

    @Override
    public void displayDialog(View view, int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final CoinMarketCapDetailsModel selectedCoin = coins.get(position);

        builder
                .setMessage(String.format("Do you want to remove %s from watch list?", selectedCoin.currencyName))
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        removeCoinFromPreferences(selectedCoin);
                        coins.remove(selectedCoin);
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
    }
}
