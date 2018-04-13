package pw.robertlewicki.coinwatcher.Interfaces;

import java.util.HashMap;
import java.util.List;

import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapDetailsModel;

public interface WatchList
{
    void newCoinAdded(CoinMarketCapDetailsModel coin);
    void coinsDataUpdated(List<CoinMarketCapDetailsModel> coins);
    void coinsIdsFetched(HashMap<String, Integer> coinsIds);
}
