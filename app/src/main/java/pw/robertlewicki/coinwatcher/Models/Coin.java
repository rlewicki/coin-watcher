package pw.robertlewicki.coinwatcher.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coin
{
    @JsonProperty("id")
    public String id;
    @JsonProperty("name")
    public String currencyName;
    @JsonProperty("symbol")
    public String symbol;
    @JsonProperty("rank")
    public String rank;
    @JsonProperty("price_usd")
    public String priceUsd;
    @JsonProperty("price_btc")
    public String priceBtc;
    @JsonProperty("24h_volume_usd")
    public String dailyVolumeUsd;
    @JsonProperty("market_cap_usd")
    public String marketCapUsd;
    @JsonProperty("available_supply")
    public String availableSupply;
    @JsonProperty("total_supply")
    public String totalSupply;
    @JsonProperty("percent_change_1h")
    public String hourlyPercentChange;
    @JsonProperty("percent_change_24h")
    public String dailyPercentChange;
    @JsonProperty("percent_change_7d")
    public String weeklyPercentChange;
    @JsonProperty("last_updated")
    public String lastUpdated;

    public int drawableId;
}
