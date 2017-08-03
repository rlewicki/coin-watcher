package pw.robertlewicki.coinwatcher.Interfaces;

import java.io.IOException;

public interface IHttpGetter {
    String getResponse(String url) throws IOException;
}
