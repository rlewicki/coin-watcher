package pw.robertlewicki.coinwatcher.Models;

import java.util.List;

import pw.robertlewicki.coinwatcher.Misc.ResponseStatus;

public class Response
{
    public List<Coin> coins;
    public ResponseStatus status;

    public Response(List<Coin> coins, ResponseStatus status)
    {
        this.coins = coins;
        this.status = status;
    }
}
