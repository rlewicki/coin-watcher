package pw.robertlewicki.coinwatcher.CoinMarketCapApi;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import pw.robertlewicki.coinwatcher.CoinWatcherComponent;
import pw.robertlewicki.coinwatcher.CoinWatcherModule;
import pw.robertlewicki.coinwatcher.DaggerCoinWatcherComponent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CoinMarketCap
{
    private CoinMarketCapService coinMarketCapService;

    @Inject
    @Named("CoinMarketCap")
    Retrofit retrofit;

    @Inject
    public CoinMarketCap()
    {
        DaggerCoinWatcherComponent.create().inject(this);
        coinMarketCapService = retrofit.create(CoinMarketCapService.class);
    }

    public void listAllCoins(final CoinMarketCapObserver observer)
    {
        Call<List<CoinMarketCapDetailsModel>> request = coinMarketCapService.listAllCoins();

        request.enqueue(new Callback<List<CoinMarketCapDetailsModel>>()
        {
            @Override
            public void onResponse(Call<List<CoinMarketCapDetailsModel>> call, Response<List<CoinMarketCapDetailsModel>> response)
            {
                List<CoinMarketCapDetailsModel> listedCoins = response.body();
                observer.listedCoinsCallback(listedCoins);
            }

            @Override
            public void onFailure(Call<List<CoinMarketCapDetailsModel>> call, Throwable t)
            {
                observer.fetchingErrorCallback(t);
            }
        });
    }

    public void listLimitedAmountOfCoins(final CoinMarketCapObserver observer, int numberOfCoins)
    {
        Call<List<CoinMarketCapDetailsModel>> request = coinMarketCapService.listLimitedAmountOfCoins(numberOfCoins);

        request.enqueue(new Callback<List<CoinMarketCapDetailsModel>>()
        {
            @Override
            public void onResponse(Call<List<CoinMarketCapDetailsModel>> call, Response<List<CoinMarketCapDetailsModel>> response)
            {
                List<CoinMarketCapDetailsModel> listedCoins = response.body();
                observer.listedLimitedAmountOfCoinsCallback(listedCoins);
            }

            @Override
            public void onFailure(Call<List<CoinMarketCapDetailsModel>> call, Throwable t)
            {
                observer.fetchingErrorCallback(t);
            }
        });
    }

    public void getSpecificCoinDetails(final CoinMarketCapObserver observer, String coinName)
    {
        Call<List<CoinMarketCapDetailsModel>> request = coinMarketCapService.getSpecificCoinDetails(coinName);

        request.enqueue(new Callback<List<CoinMarketCapDetailsModel>>()
        {
            @Override
            public void onResponse(Call<List<CoinMarketCapDetailsModel>> call, Response<List<CoinMarketCapDetailsModel>> response)
            {
                List<CoinMarketCapDetailsModel> coinData = response.body();
                observer.specificCoinDetailsCallback(coinData.get(0));
            }

            @Override
            public void onFailure(Call<List<CoinMarketCapDetailsModel>> call, Throwable t)
            {
                observer.fetchingErrorCallback(t);
            }
        });
    }

    public void getGlobalMarketData(final CoinMarketCapObserver observer)
    {
        Call<GlobalMarketDataModel> request = coinMarketCapService.getGlobalMarketData();

        request.enqueue(new Callback<GlobalMarketDataModel>()
        {
            @Override
            public void onResponse(Call<GlobalMarketDataModel> call, Response<GlobalMarketDataModel> response)
            {
                GlobalMarketDataModel marketData = response.body();
                observer.globalMarketDataCallback(marketData);
            }

            @Override
            public void onFailure(Call<GlobalMarketDataModel> call, Throwable t)
            {
                observer.fetchingErrorCallback(t);
            }
        });
    }
}
