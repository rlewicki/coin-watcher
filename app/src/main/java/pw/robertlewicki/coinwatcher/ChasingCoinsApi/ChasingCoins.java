package pw.robertlewicki.coinwatcher.ChasingCoinsApi;

import java.util.List;

import javax.inject.Inject;

import pw.robertlewicki.coinwatcher.DaggerCoinWatcherComponent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChasingCoins
{
    private ChasingCoinsService chasingCoinsService;
    private Retrofit retrofit;

    @Inject
    public ChasingCoins(Retrofit retrofit)
    {
        this.retrofit = retrofit;
        chasingCoinsService = retrofit.create(ChasingCoinsService.class);
    }

    public void listAllCoins(final ChasingCoinsObserver observer)
    {
        Call<List<String>> request = chasingCoinsService.listCoins();

        request.enqueue(new Callback<List<String>>()
        {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response)
            {
                List<String> listedCoins = response.body();
                observer.listedCoinsCallback(listedCoins);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t)
            {
                observer.fetchingErrorCallback(t);
            }
        });
    }

    public void getCoinDetails(final ChasingCoinsObserver observer, String coinName)
    {
        Call<ChasingCoinsDetailsModel> request = chasingCoinsService.getCoinDetails(coinName);

        request.enqueue(new Callback<ChasingCoinsDetailsModel>()
        {
            @Override
            public void onResponse(Call<ChasingCoinsDetailsModel> call, Response<ChasingCoinsDetailsModel> response)
            {
                ChasingCoinsDetailsModel chasingCoinsDetailsModel = response.body();
                observer.coinDetailsCallback(chasingCoinsDetailsModel);
            }

            @Override
            public void onFailure(Call<ChasingCoinsDetailsModel> call, Throwable t)
            {
                observer.fetchingErrorCallback(t);
            }
        });
    }

    public void getCoinLogo(final ChasingCoinsObserver observer, String coinName)
    {
        Call request = chasingCoinsService.getCoinLogo(coinName);

        request.enqueue(new Callback()
        {
            @Override
            public void onResponse(Call call, Response response)
            {
                observer.coinLogoCallback(response);
            }

            @Override
            public void onFailure(Call call, Throwable t)
            {
                observer.fetchingErrorCallback(t);
            }
        });
    }
}