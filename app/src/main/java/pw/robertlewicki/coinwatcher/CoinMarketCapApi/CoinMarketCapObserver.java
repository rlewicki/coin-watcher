package pw.robertlewicki.coinwatcher.CoinMarketCapApi;

import java.util.List;

public interface CoinMarketCapObserver
{
    void listedCoinsCallback(List<CoinMarketCapDetailsModel> listedCoins);
    void listedLimitedAmountOfCoinsCallback(List<CoinMarketCapDetailsModel> listedCoins);
    void specificCoinDetailsCallback(CoinMarketCapDetailsModel coinDetails);
    void globalMarketDataCallback(GlobalMarketDataModel marketData);
    void fetchingErrorCallback(Throwable t);
}
