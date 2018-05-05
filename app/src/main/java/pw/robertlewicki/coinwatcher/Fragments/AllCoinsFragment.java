package pw.robertlewicki.coinwatcher.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;
import outlander.showcaseview.ShowcaseViewBuilder;
import pw.robertlewicki.coinwatcher.Adapters.ListAdapter;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCap;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapCoinsIdsModel;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapDetailsModel;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapObserver;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.GlobalMarketDataModel;
import pw.robertlewicki.coinwatcher.Interfaces.DialogOwner;
import pw.robertlewicki.coinwatcher.Interfaces.WatchList;
import pw.robertlewicki.coinwatcher.R;
import pw.robertlewicki.coinwatcher.Utils.Utils;

public class AllCoinsFragment extends Fragment implements CoinMarketCapObserver, DialogOwner
{
    private final AllCoinsFragment self = this;
    @BindView(R.id.SwipeView) SwipeRefreshLayout swipeView;

    @Inject
    CoinMarketCap coinMarketCap;

    private String title;
    private Application app;
    private List<CoinMarketCapDetailsModel> listedCoins;
    private CoinMarketCapCoinsIdsModel[] listedIds;


    private ListAdapter listAdapter;

    private WatchList watchList;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private int dataSources = 2;
    private int callbacksReceived = 0;

    private boolean shouldShowTutorial = false;
    private int tutorialStep = 0;

    public static AllCoinsFragment newInstance(String title, Application app, WatchList watchList)
    {
        AllCoinsFragment fragment = new AllCoinsFragment();

        fragment.title = title;
        fragment.app = app;
        fragment.listedCoins = new ArrayList<>();
        fragment.watchList = watchList;

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

        checkForTutorialPassed();

        recyclerView = (RecyclerView)rootView.findViewById(R.id.CoinListView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                if(recyclerView.getChildAt(0) != null && shouldShowTutorial == false)
                {
                    shouldShowTutorial = true;
                    startTutorial();
                }
            }
        });

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                coinMarketCap.listAllCoins(self);
                coinMarketCap.listAllCoinsIds(self);
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
                coinMarketCap.listAllCoinsIds(self);
            }
        });

        return rootView;
    }

    public void queryCurrencies(String query)
    {
        List<CoinMarketCapDetailsModel> queriedCoins = new ArrayList<>();
        for(CoinMarketCapDetailsModel coin : listedCoins)
        {
            String coinName = coin.currencyName;
            if(coinName.toLowerCase().contains(query.toLowerCase()))
            {
                queriedCoins.add(coin);
            }
        }
        listAdapter = new ListAdapter(queriedCoins, self);
        recyclerView.setAdapter(listAdapter);
    }

    public String getTitle()
    {
        return title;
    }

    @Override
    public void displayDialog(View view, int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final CoinMarketCapDetailsModel selectedCoin = listedCoins.get(position);

        builder
                .setMessage(String.format("Do you want to add %s to watch list?", selectedCoin.currencyName))
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        watchList.newCoinAdded(selectedCoin);
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

    private void updateAdapter()
    {
        callbacksReceived++;
        if(callbacksReceived == dataSources)
        {
            callbacksReceived = 0;

            for(int i = 0; i < listedCoins.size(); i++)
            {
                listedCoins.get(i).logoIds = listedIds[i].id;
            }

            watchList.dataUpdated(listedCoins);

            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    listAdapter = new ListAdapter(listedCoins, self);
                    recyclerView.setAdapter(listAdapter);
                }
            });
        }
    }

    private void startTutorial()
    {
        if(tutorialStep == 0)
        {
            View firstItemView = recyclerView.getChildAt(0).findViewById(R.id.CoinIcon);
            final ShowcaseViewBuilder showcaseViewBuilder;

            showcaseViewBuilder = ShowcaseViewBuilder.init(getActivity())
                    .setTargetView(firstItemView)
                    .setBackgroundOverlayColor(0xdd4d4d4d)
                    .setRingColor(0xcc8e8e8e)
                    .addCustomView(R.layout.tutorial_description, Gravity.BOTTOM)
                    .setCustomViewMargin((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()));

            showcaseViewBuilder.setClickListenerOnView(R.id.dismissButton, new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    passTutorial();
                    showcaseViewBuilder.hide();
                }
            });

            showcaseViewBuilder.show();
        }
    }

    @Override
    public void listedCoinsCallback(List<CoinMarketCapDetailsModel> listedCoins)
    {
        swipeView.setRefreshing(false);
        if(Utils.isNetworkAvailable(getContext()))
        {
            Toasty.success(getContext(), "New data fetched", Toast.LENGTH_SHORT, true).show();
            this.listedCoins = listedCoins;
            updateAdapter();
        }
        else
        {
            Toasty.error(getContext(), "You are in offline mode", Toast.LENGTH_SHORT, true).show();
        }
    }

    @Override
    public void listedLimitedAmountOfCoinsCallback(List<CoinMarketCapDetailsModel> listedCoins)
    {
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
    public void listedCoinsIdsCallback(CoinMarketCapCoinsIdsModel[] listedCoins)
    {
        listedIds = listedCoins;
        updateAdapter();
    }

    @Override
    public void fetchingErrorCallback(Throwable t)
    {
        swipeView.setRefreshing(false);
        Toasty.error(getContext(), "You are in offline mode!", Toast.LENGTH_SHORT, true).show();
    }

    private void checkForTutorialPassed()
    {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        shouldShowTutorial = preferences.getBoolean("tutorial", false);
    }

    private void passTutorial()
    {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("tutorial", true);
        editor.apply();
    }
}
