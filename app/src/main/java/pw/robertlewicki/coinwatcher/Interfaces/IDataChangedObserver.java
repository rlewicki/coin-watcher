package pw.robertlewicki.coinwatcher.Interfaces;

import java.util.List;

import pw.robertlewicki.coinwatcher.Models.Coin;

public interface IDataChangedObserver
{
    void update(List<Coin> coins);
}
