package pw.robertlewicki.coinwatcher.CoinMarketCapApi;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CoinMarketCap
{
    private CoinMarketCapService coinMarketCapService;
    private CoinMarketCapImageService coinMarketCapImageService;

    @Inject
    public CoinMarketCap(
            CoinMarketCapService coinMarketCapService,
            CoinMarketCapImageService coinMarketCapImageService)
    {
        this.coinMarketCapService = coinMarketCapService;
        this.coinMarketCapImageService = coinMarketCapImageService;
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

    public void listAllCoinsIds(final CoinMarketCapObserver observer)
    {
        okhttp3.Call request = coinMarketCapImageService.listAllCoinsIds();

        request.enqueue(new okhttp3.Callback()
        {
            @Override
            public void onFailure(okhttp3.Call call, IOException e)
            {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException
            {
                try(ResponseBody responseBody = response.body())
                {
                    final byte[] responseBytes = responseBody.bytes();
                    ObjectMapper objectMapper = new ObjectMapper();
                    final CoinMarketCapCoinsIdsModel[] coinIdsModels
                            = objectMapper.readValue(
                                    responseBytes,
                                    CoinMarketCapCoinsIdsModel[].class);

                    observer.listedCoinsIdsCallback(coinIdsModels);
                }
            }
        });
    }
}
