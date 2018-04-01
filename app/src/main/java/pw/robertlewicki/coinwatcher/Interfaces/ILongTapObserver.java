package pw.robertlewicki.coinwatcher.Interfaces;

import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapDetailsModel;

public interface ILongTapObserver
{
    void update(CoinMarketCapDetailsModel coin);
}
