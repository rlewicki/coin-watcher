package pw.robertlewicki.coinwatcher.Interfaces;

import pw.robertlewicki.coinwatcher.Models.Response;

public interface IFragmentUpdater
{
    void update(Response data);
    boolean isEmpty();
}
