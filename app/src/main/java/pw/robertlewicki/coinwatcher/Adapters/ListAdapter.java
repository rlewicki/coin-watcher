package pw.robertlewicki.coinwatcher.Adapters;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapDetailsModel;
import pw.robertlewicki.coinwatcher.R;

public class ListAdapter extends BaseAdapter
{

    @BindView(R.id.CoinName)
    TextView coinName;
    @BindView(R.id.CoinPercent)
    TextView coinPercent;
    @BindView(R.id.CoinValue)
    TextView coinValue;
    @BindView(R.id.GainArrow)
    ImageView gainArrow;
    @BindView(R.id.CoinIcon)
    ImageView coinIcon;

    @BindDrawable(R.drawable.ic_arrow_gain_green_24dp)
    Drawable greenArrow;
    @BindDrawable(R.drawable.ic_arrow_lose_red_24dp)
    Drawable redArrow;

    @BindColor(R.color.gain)
    int gainColor;
    @BindColor(R.color.lose)
    int loseColor;

    private List<CoinMarketCapDetailsModel> listedCoins;
    private Application app;

    private String baseUrl = "https://chasing-coins.com/api/v1/std/logo/";

    public ListAdapter(Application application, List<CoinMarketCapDetailsModel> listedCoins)
    {
        this.app = application;
        this.listedCoins = listedCoins;
    }

    @Override
    public int getCount()
    {
        return listedCoins.size();
    }

    @Override
    public Object getItem(int position)
    {
        return listedCoins.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(app.getApplicationContext());
        View newView = convertView;
        if(newView == null)
        {
            newView = inflater.inflate(R.layout.adapter_list_row, parent, false);
        }

        ButterKnife.bind(this, newView);

        CoinMarketCapDetailsModel coin = listedCoins.get(position);

        coinName.setText(coin.symbol);
        coinValue.setText(String.format("$%s", coin.priceUsd));

        if(coin.dailyPercentChange != null)
        {
            if(coin.dailyPercentChange.contains("-"))
            {
                gainArrow.setImageDrawable(redArrow);
                coinPercent.setText(String.format("%s%%", coin.dailyPercentChange));
                coinPercent.setTextColor(loseColor);
            }
            else
            {
                gainArrow.setImageDrawable(greenArrow);
                coinPercent.setText(String.format("+%s%%", coin.dailyPercentChange));
                coinPercent.setTextColor(gainColor);
            }
        }


        Picasso.get()
                .load(baseUrl + coin.symbol)
                .into(coinIcon);

        return newView;
    }
}
