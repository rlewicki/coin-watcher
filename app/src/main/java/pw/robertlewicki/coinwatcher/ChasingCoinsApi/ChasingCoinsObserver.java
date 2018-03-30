package pw.robertlewicki.coinwatcher.ChasingCoinsApi;

import java.util.List;

import retrofit2.Response;

public interface ChasingCoinsObserver
{
    void listedCoinsCallback(List<String> listedCoins);
    void coinDetailsCallback(ChasingCoinsDetailsModel chasingCoinsDetailsModel);
    void coinLogoCallback(Response logoResponse);
    void fetchingErrorCallback(Throwable t);
}
