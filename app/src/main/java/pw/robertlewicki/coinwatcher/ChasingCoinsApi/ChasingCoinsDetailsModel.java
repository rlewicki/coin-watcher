package pw.robertlewicki.coinwatcher.ChasingCoinsApi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChasingCoinsDetailsModel
{
    @JsonProperty("change")
    public Change changeValues;
    @JsonProperty("price")
    public float price;
    @JsonProperty("coinheat")
    public int coinheat;
}

class Change
{
    @JsonProperty("hour")
    public float hourChange;
    @JsonProperty("day")
    public float dayChange;
}