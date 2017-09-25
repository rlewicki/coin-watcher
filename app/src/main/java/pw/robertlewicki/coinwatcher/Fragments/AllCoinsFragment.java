package pw.robertlewicki.coinwatcher.Fragments;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pw.robertlewicki.coinwatcher.Adapters.ListAdapter;
import pw.robertlewicki.coinwatcher.Interfaces.IDataChangedObserver;
import pw.robertlewicki.coinwatcher.Interfaces.IFragmentUpdater;
import pw.robertlewicki.coinwatcher.Interfaces.IFileStorageHandler;
import pw.robertlewicki.coinwatcher.Interfaces.ILongTapObserver;
import pw.robertlewicki.coinwatcher.Misc.BundleKeys;
import pw.robertlewicki.coinwatcher.Models.Coin;
import pw.robertlewicki.coinwatcher.R;
import pw.robertlewicki.coinwatcher.Utils.CoinGetter;

public class AllCoinsFragment extends Fragment implements IFragmentUpdater
{
    private final IFragmentUpdater self = this;
    @BindView(R.id.SwipeView) SwipeRefreshLayout swipeView;
    @BindView(R.id.CoinListView) ListView listView;

    private String title;
    private Application app;
    private List<Coin> coins;

    private List<ILongTapObserver> tapObservers;
    private List<IDataChangedObserver> dataChangedObservers;

    private IFileStorageHandler fileStorageHandler;

    public static AllCoinsFragment newInstance(
            String title, Application app, IFileStorageHandler internalStorageHandler)
    {
        AllCoinsFragment fragment = new AllCoinsFragment();

        fragment.title = title;
        fragment.app = app;
        fragment.tapObservers = new ArrayList<>();
        fragment.dataChangedObservers = new ArrayList<>();
        fragment.fileStorageHandler = internalStorageHandler;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.all_coins_fragment, container, false);
        ButterKnife.bind(this, rootView);

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {

            @Override
            public void onRefresh()
            {
                new CoinGetter(self, fileStorageHandler).execute();
            }
        });

        swipeView.setColorSchemeResources(
                R.color.circleOrange,
                R.color.circleGreen,
                R.color.circleBlue
        );

        swipeView.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipeView.setRefreshing(true);
                new CoinGetter(self, fileStorageHandler).execute();
            }
        });

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final Coin selectedCoin = coins.get(position);

                builder
                        .setMessage(String.format("Do you want to add %s to your list?", selectedCoin.currencyName))
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                for(ILongTapObserver observer : tapObservers)
                                {
                                    observer.update(selectedCoin);
                                }
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

    @Override
    public void update(List<Coin> coins)
    {
        this.coins = coins;

        listView.setAdapter(new ListAdapter(app, coins));
        swipeView.setRefreshing(false);

        for(IDataChangedObserver observer : dataChangedObservers)
        {
            observer.update(coins);
        }
    }

    public void queryCurrencies(String query)
    {
        List<Coin> queriedCoins = new ArrayList<>();
        for(Coin coin : coins)
        {
            if(coin.symbol.contains(query.toUpperCase()))
            {
                queriedCoins.add(coin);
            }
        }
        listView.setAdapter(new ListAdapter(app, queriedCoins));
    }

    public void addLongTapObserver(ILongTapObserver observer)
    {
        tapObservers.add(observer);
    }

    public void addDataChangedObserver(IDataChangedObserver observer)
    {
        dataChangedObservers.add(observer);
    }

    public String getTitle()
    {
        return title;
    }
}
