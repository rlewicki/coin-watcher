package pw.robertlewicki.coinwatcher.CoinMarketCapApi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlobalMarketDataModel
{
    @JsonProperty("total_market_cap_usd")
    public long totalMarketCapUsd;
    @JsonProperty("total_24h_volume_usd")
    public long totalDailyVolumeUsd;
    @JsonProperty("bitcoin_percentage_of_market_cap")
    public float bitcoinPercentageOfMarketCap;
    @JsonProperty("active_currencies")
    public int activeCurrencies;
    @JsonProperty("active_assets")
    public int activeAssets;
    @JsonProperty("active_markets")
    public int activeMarkets;
    @JsonProperty("last_updated")
    public long lastUpdated;
}