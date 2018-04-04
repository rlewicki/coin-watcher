package pw.robertlewicki.coinwatcher.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;
import pw.robertlewicki.coinwatcher.Activities.CoinDetailsActivity;
import pw.robertlewicki.coinwatcher.Adapters.ListAdapter;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCap;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapDetailsModel;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapObserver;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.GlobalMarketDataModel;
import pw.robertlewicki.coinwatcher.Interfaces.IDataChangedObserver;
import pw.robertlewicki.coinwatcher.Interfaces.ILongTapObserver;
import pw.robertlewicki.coinwatcher.Misc.BundleKeys;
import pw.robertlewicki.coinwatcher.R;
import pw.robertlewicki.coinwatcher.Utils.Utils;

public class AllCoinsFragment extends Fragment implements CoinMarketCapObserver
{
    private final AllCoinsFragment self = this;
    @BindView(R.id.SwipeView) SwipeRefreshLayout swipeView;
    @BindView(R.id.CoinListView) ListView listView;

    @Inject
    CoinMarketCap coinMarketCap;

    private String title;
    private Application app;
    private List<CoinMarketCapDetailsModel> listedCoins;

    private List<ILongTapObserver> tapObservers;
    private List<IDataChangedObserver> dataChangedObservers;

    public static AllCoinsFragment newInstance(String title, Application app)
    {
        AllCoinsFragment fragment = new AllCoinsFragment();

        fragment.title = title;
        fragment.app = app;
        fragment.listedCoins = new ArrayList<>();
        fragment.tapObservers = new ArrayList<>();
        fragment.dataChangedObservers = new ArrayList<>();

        return fragment;
    }

    @Override
    public void onAttach(Activity activity)
    {
        AndroidSupportInjection.inject(this);
        super.onAttach(activity);
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
                coinMarketCap.listAllCoins(self);
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
                coinMarketCap.listAllCoins(self);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Bundle data = new Bundle();
                CoinMarketCapDetailsModel coin = listedCoins.get(position);

                Intent intent = new Intent(getContext(), CoinDetailsActivity.class);

                intent.putExtra(BundleKeys.RANK, coin.rank);
                intent.putExtra(BundleKeys.FULL_NAME, coin.currencyName);
                intent.putExtra(BundleKeys.PRICE_USD, coin.priceUsd);
                intent.putExtra(BundleKeys.DAILY_VOLUME, coin.dailyVolumeUsd);
                intent.putExtra(BundleKeys.MARKET_CAP, coin.marketCapUsd);
                intent.putExtra(BundleKeys.AVAILABLE_SUPPLY, coin.availableSupply);
                intent.putExtra(BundleKeys.TOTAL_SUPPLY, coin.totalSupply);
                intent.putExtra(BundleKeys.LAST_UPDATE_TIME, coin.lastUpdated);

                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final CoinMarketCapDetailsModel selectedCoin = listedCoins.get(position);

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

    public void queryCurrencies(String query)
    {
        List<CoinMarketCapDetailsModel> queriedCoins = new ArrayList<>();
        for(CoinMarketCapDetailsModel coin : listedCoins)
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

    @Override
    public void listedCoinsCallback(List<CoinMarketCapDetailsModel> listedCoins)
    {
        DisplayPostRequestToast();
        swipeView.setRefreshing(false);
        listView.setAdapter(new ListAdapter(app, listedCoins));

        this.listedCoins = listedCoins;

        for(IDataChangedObserver observer : dataChangedObservers)
        {
            observer.update(listedCoins);
        }
    }

    @Override
    public void listedLimitedAmountOfCoinsCallback(List<CoinMarketCapDetailsModel> listedCoins)
    {
        swipeView.setRefreshing(false);
        listView.setAdapter(new ListAdapter(app, listedCoins));
    }

    @Override
    public void specificCoinDetailsCallback(CoinMarketCapDetailsModel coinDetails)
    {

    }

    @Override
    public void globalMarketDataCallback(GlobalMarketDataModel marketData)
    {

    }

    @Override
    public void fetchingErrorCallback(Throwable t)
    {

    }

    private void DisplayPostRequestToast()
    {
        if(Utils.isNetworkAvailable(getContext()))
        {
            Toasty.success(getContext(), "New data fetched", Toast.LENGTH_SHORT, true).show();
        }
        else
        {
            Toasty.warning(getContext(), "You are in offline mode", Toast.LENGTH_SHORT, true).show();
        }
    }
}
