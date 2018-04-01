package pw.robertlewicki.coinwatcher.Interfaces;

import java.util.List;

import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapDetailsModel;

public interface IDataChangedObserver
{
    void update(List<CoinMarketCapDetailsModel> coins);
}
