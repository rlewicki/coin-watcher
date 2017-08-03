package pw.robertlewicki.coinwatcher;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

import pw.robertlewicki.coinwatcher.Model.Coin;
import pw.robertlewicki.coinwatcher.Util.JsonParser;

public class JsonParserTest {

    private String singleObjectJsonData =
            "    {\n" +
            "        \"id\": \"bitcoin\", \n" +
            "        \"name\": \"Bitcoin\", \n" +
            "        \"symbol\": \"BTC\", \n" +
            "        \"rank\": \"1\", \n" +
            "        \"price_usd\": \"2761.54\", \n" +
            "        \"price_btc\": \"1.0\", \n" +
            "        \"24h_volume_usd\": \"738021000.0\", \n" +
            "        \"market_cap_usd\": \"45526817479.0\", \n" +
            "        \"available_supply\": \"16486025.0\", \n" +
            "        \"total_supply\": \"16486025.0\", \n" +
            "        \"percent_change_1h\": \"0.73\", \n" +
            "        \"percent_change_24h\": \"1.44\", \n" +
            "        \"percent_change_7d\": \"6.21\", \n" +
            "        \"last_updated\": \"1501783490\"\n" +
            "    }";

    private String arrayOfObjectsJsonData = "[\n" +
            "    {\n" +
            "        \"id\": \"bitcoin\", \n" +
            "        \"name\": \"Bitcoin\", \n" +
            "        \"symbol\": \"BTC\", \n" +
            "        \"rank\": \"1\", \n" +
            "        \"price_usd\": \"2761.54\", \n" +
            "        \"price_btc\": \"1.0\", \n" +
            "        \"24h_volume_usd\": \"738021000.0\", \n" +
            "        \"market_cap_usd\": \"45526817479.0\", \n" +
            "        \"available_supply\": \"16486025.0\", \n" +
            "        \"total_supply\": \"16486025.0\", \n" +
            "        \"percent_change_1h\": \"0.73\", \n" +
            "        \"percent_change_24h\": \"1.44\", \n" +
            "        \"percent_change_7d\": \"6.21\", \n" +
            "        \"last_updated\": \"1501783490\"\n" +
            "    }" +
            "]";

    @Test
    public void ParseSingleObjectJsonToPojoShouldReturnNonNullObject() throws Exception {
        JsonParser jsonParser = new JsonParser();
        Coin coin = jsonParser.stringToObject(singleObjectJsonData, Coin.class);
        assertNotEquals(coin, null);
        assertEquals(coin.id, "bitcoin");
        assertEquals(coin.currencyName, "Bitcoin");
        assertEquals(coin.dailyPercentChange, "1.44");
        assertEquals(coin.lastUpdated, "1501783490");
    }

    @Test
    public void ParseArrayOfObjectsJsonToPojoShouldReturnNonNullObjects() throws Exception {
        JsonParser jsonParser = new JsonParser();
        List<Coin> coins = jsonParser.stringToListOfObjects(arrayOfObjectsJsonData, Coin.class);
        assertNotEquals(coins, null);
        for(Coin coin : coins) {
            assertEquals(coin.currencyName, "Bitcoin");
        }
    }
}
