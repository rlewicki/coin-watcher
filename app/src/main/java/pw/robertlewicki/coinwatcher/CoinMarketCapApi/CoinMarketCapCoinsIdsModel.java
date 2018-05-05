package pw.robertlewicki.coinwatcher.CoinMarketCapApi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CoinMarketCapCoinsIdsModel
{
    @JsonProperty("name")
    public String name;
    @JsonProperty("symbol")
    public String symbol;
    @JsonProperty("rank")
    public int rank;
    @JsonProperty("slug")
    public String slug;
    @JsonProperty("tokens")
    public List<String> tokens;
    @JsonProperty("id")
    public int id;
}
