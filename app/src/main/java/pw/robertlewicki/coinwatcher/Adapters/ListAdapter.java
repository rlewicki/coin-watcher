package pw.robertlewicki.coinwatcher.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import pw.robertlewicki.coinwatcher.Activities.CoinDetailsActivity;
import pw.robertlewicki.coinwatcher.CoinMarketCapApi.CoinMarketCapDetailsModel;
import pw.robertlewicki.coinwatcher.Interfaces.DialogOwner;
import pw.robertlewicki.coinwatcher.Misc.BundleKeys;
import pw.robertlewicki.coinwatcher.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
{
    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener
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

        @BindColor(R.color.gain)
        int gainColor;
        @BindColor(R.color.lose)
        int loseColor;

        ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            displayDetailsActivity(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v)
        {
            dialogOwner.displayDialog(v, getAdapterPosition());
            return false;
        }
    }

    private List<CoinMarketCapDetailsModel> listedCoins;
    private DialogOwner dialogOwner;

    public ListAdapter(List<CoinMarketCapDetailsModel> listedCoins, DialogOwner dialogOwner)
    {
        this.listedCoins = listedCoins;
        this.dialogOwner = dialogOwner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.adapter_list_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        CoinMarketCapDetailsModel coin = listedCoins.get(position);

        String logosUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/";
        String url = String.format("%s%d.png", logosUrl, coin.logoIds);
        String price = String.format("$%s", coin.priceUsd);
        String change = String.format("%s%%", coin.dailyPercentChange);

        Picasso.get()
                .load(url)
                .into(holder.coinLogo);
        holder.coinName.setText(coin.currencyName);
        holder.coinValue.setText(price);
        holder.coinPercent.setText(change);
        if(change.contains("-"))
        {
            holder.gainArrow.setImageDrawable(holder.redArrow);
            holder.coinPercent.setTextColor(holder.loseColor);
        }
        else
        {
            holder.gainArrow.setImageDrawable(holder.greenArrow);
            holder.coinPercent.setTextColor(holder.gainColor);
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemCount()
    {
        return listedCoins.size();
    }

    private void displayDetailsActivity(View v, int position)
    {
        final CoinMarketCapDetailsModel coin = listedCoins.get(position);

        Intent intent = new Intent(v.getContext(), CoinDetailsActivity.class);

        intent.putExtra(BundleKeys.RANK, coin.rank);
        intent.putExtra(BundleKeys.FULL_NAME, coin.currencyName);
        intent.putExtra(BundleKeys.PRICE_USD, coin.priceUsd);
        intent.putExtra(BundleKeys.PRICE_BTC, coin.priceBtc);
        intent.putExtra(BundleKeys.HOURLY_PERCENT_CHANGE, coin.hourlyPercentChange);
        intent.putExtra(BundleKeys.DAILY_PERCENT_CHANGE, coin.dailyPercentChange);
        intent.putExtra(BundleKeys.WEEKLY_PERCENT_CHANGE, coin.weeklyPercentChange);
        intent.putExtra(BundleKeys.DAILY_VOLUME, coin.dailyVolumeUsd);
        intent.putExtra(BundleKeys.MARKET_CAP, coin.marketCapUsd);
        intent.putExtra(BundleKeys.AVAILABLE_SUPPLY, coin.availableSupply);
        intent.putExtra(BundleKeys.TOTAL_SUPPLY, coin.totalSupply);
        intent.putExtra(BundleKeys.LAST_UPDATE_TIME, coin.lastUpdated);

        v.getContext().startActivity(intent);
    }
}
