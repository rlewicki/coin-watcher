package pw.robertlewicki.coinwatcher.Utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionChecker
{
    private Application app;

    public ConnectionChecker(Application app)
    {
        this.app = app;
    }

    public boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)app.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
