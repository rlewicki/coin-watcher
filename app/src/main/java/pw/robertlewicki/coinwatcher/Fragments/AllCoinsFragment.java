package pw.robertlewicki.coinwatcher.Fragments;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pw.robertlewicki.coinwatcher.Adapters.ListAdapter;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCap;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapDetailsModel;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapObserver;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.GlobalMarketDataModel;
import pw.robertlewicki.coinwatcher.DaggerCoinWatcherComponent;
import pw.robertlewicki.coinwatcher.Interfaces.IDataChangedObserver;
import pw.robertlewicki.coinwatcher.Interfaces.ILongTapObserver;
import pw.robertlewicki.coinwatcher.Misc.BundleKeys;
import pw.robertlewicki.coinwatcher.R;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        DaggerCoinWatcherComponent.create().inject(this);

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

    private void displayNoConnectionSnackbar()
    {
        Snackbar.make(getView(), "No Internet connection", Snackbar.LENGTH_LONG)
                .show();
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
}
