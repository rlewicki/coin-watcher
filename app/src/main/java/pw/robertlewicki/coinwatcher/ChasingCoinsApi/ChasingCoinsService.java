package pw.robertlewicki.coinwatcher.ChasingCoinsApi;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChasingCoinsService
{
    @GET("coins")
    Call<List<String>> listCoins();

    @GET("std/coin/{coin}")
    Call<ChasingCoinsDetailsModel> getCoinDetails(@Path("coin") String coin);

    @GET("std/logo/{coin}")
    Call getCoinLogo(@Path("coin") String coin);
}
