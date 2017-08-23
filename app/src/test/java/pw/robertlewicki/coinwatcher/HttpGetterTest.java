package pw.robertlewicki.coinwatcher;

import org.junit.Test;

import static org.junit.Assert.*;

import pw.robertlewicki.coinwatcher.Utils.HttpGetter;

public class HttpGetterTest
{

    @Test
    public void getHttpRequestShouldReturnNonEmptyString() throws Exception
    {
        HttpGetter httpGetter = new HttpGetter();
        String url = "http://httpbin.org/ip";
        assertNotEquals(httpGetter.getResponse(url), "");
    }
}
