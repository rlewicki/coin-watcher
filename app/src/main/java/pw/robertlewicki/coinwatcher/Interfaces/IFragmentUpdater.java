package pw.robertlewicki.coinwatcher.Interfaces;

import java.util.List;

import pw.robertlewicki.coinwatcher.Models.Coin;

public interface IFragmentUpdater {
    void updateFragments(List<Coin> coins);
}
