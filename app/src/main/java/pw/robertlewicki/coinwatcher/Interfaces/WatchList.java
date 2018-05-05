package pw.robertlewicki.coinwatcher.Interfaces;

import java.util.List;

import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapDetailsModel;

public interface WatchList
{
    void newCoinAdded(CoinMarketCapDetailsModel coin);
    void dataUpdated(List<CoinMarketCapDetailsModel> coins);
}
