package pw.robertlewicki.coinwatcher.Interfaces;

import java.util.List;

import pw.robertlewicki.coinwatcher.Models.Coin;

public interface IFragmentUpdater {
    void update(List<Coin> coins);
}
