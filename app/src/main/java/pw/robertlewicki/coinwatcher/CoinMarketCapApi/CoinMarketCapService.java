package pw.robertlewicki.coinwatcher.CoinMarketCapApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CoinMarketCapService
{
    @GET("ticker/?limit=0")
    Call<List<CoinMarketCapDetailsModel>> listAllCoins();

    @GET("ticker/")
    Call<List<CoinMarketCapDetailsModel>> listLimitedAmountOfCoins(@Query("limit") int number);

    @GET("ticker/{name}")
    Call<List<CoinMarketCapDetailsModel>> getSpecificCoinDetails(@Path("name") String name);

    @GET("global")
    Call<GlobalMarketDataModel> getGlobalMarketData();
}
