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

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCap;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapDetailsModel;
import pw.robertlewicki.coinwatcher.R;
import timber.log.Timber;

public class ListAdapter extends BaseAdapter
{
    @BindView(R.id.CoinIcon)
    ImageView coinLogo;
    @BindView(R.id.CoinName)
    TextView coinName;
    @BindView(R.id.CoinPercent)
    TextView coinPercent;
    @BindView(R.id.CoinValue)
    TextView coinValue;
    @BindView(R.id.GainArrow)
    ImageView gainArrow;

    @BindDrawable(R.drawable.ic_arrow_gain_green_24dp)
    Drawable greenArrow;
    @BindDrawable(R.drawable.ic_arrow_lose_red_24dp)
    Drawable redArrow;
    @BindDrawable(R.drawable.ic_image_black_24dp)
    Drawable logoPlaceholder;

    @BindColor(R.color.gain)
    int gainColor;
    @BindColor(R.color.lose)
    int loseColor;

    private List<CoinMarketCapDetailsModel> listedCoins;
    private HashMap<String, Integer> coinsIds;
    private Application app;

    private String logosUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/";

    public ListAdapter(Application application)
    {
        this.app = application;
        this.listedCoins = new ArrayList<>();
    }

    public void updateListedCoins(List<CoinMarketCapDetailsModel> listedCoins)
    {
        this.listedCoins = listedCoins;
    }

    public void updateCoinsIds(HashMap<String, Integer> coinsIds)
    {
        this.coinsIds = coinsIds;
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

        CoinMarketCapDetailsModel coin = null;
        if(listedCoins != null)
        {
            coin = listedCoins.get(position);
        }

        fetchIcon(coin);
        fillRowData(coin);
        return newView;
    }

    private void fetchIcon(CoinMarketCapDetailsModel coin)
    {
        if(coin == null || coinsIds == null)
        {
            return;
        }

        if (coinsIds.containsKey(coin.currencyName))
        {
            final Integer logoId = coinsIds.get(coin.currencyName);
            final String logoUrl = logosUrl + logoId + ".png";

            Picasso.get()
                    .load(logoUrl)
                    .placeholder(logoPlaceholder)
                    .fetch(new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {
                            Picasso.get()
                                    .load(logoUrl)
                                    .placeholder(logoPlaceholder)
                                    .noFade()
                                    .into(coinLogo);
                        }

                        @Override
                        public void onError(Exception e)
                        {
                        }
                    });
        }
    }

    private void fillRowData(CoinMarketCapDetailsModel coin)
    {
        if(coin != null)
        {
            coinName.setText(coin.currencyName);
            coinValue.setText(String.format("$%s", coin.priceUsd));
            coinPercent.setText(String.format("%s%%", coin.dailyPercentChange));
            if(coin.dailyPercentChange != null)
            {
                if(coin.dailyPercentChange.contains("-"))
                {
                    gainArrow.setImageDrawable(redArrow);
                    coinPercent.setTextColor(loseColor);
                }
                else
                {
                    gainArrow.setImageDrawable(greenArrow);
                    coinPercent.setTextColor(gainColor);
                }
            }
        }
        else
        {
            coinName.setText("");
            coinValue.setText("");
            coinPercent.setText("");
            gainArrow.setImageDrawable(null);
        }
    }
}
