package pw.robertlewicki.coinwatcher.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.lb.auto_fit_textview.AutoResizeTextView;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pw.robertlewicki.coinwatcher.Misc.BundleKeys;
import pw.robertlewicki.coinwatcher.R;

public class CoinDetailsActivity extends AppCompatActivity
{
    @BindView(R.id.rankField)
    AutoResizeTextView rankField;
    @BindView(R.id.fullNameField)
    AutoResizeTextView fullNameField;
    @BindView(R.id.priceUsdField)
    AutoResizeTextView priceUsdField;
    @BindView(R.id.priceBtcField)
    AutoResizeTextView priceBtcField;
    @BindView(R.id.hourlyPercentChangeField)
    AutoResizeTextView hourlyPercentChangeField;
    @BindView(R.id.dailyPercentChangeField)
    AutoResizeTextView dailyPercentChangeField;
    @BindView(R.id.weeklyPercentChangeField)
    AutoResizeTextView weeklyPercentChangeField;
    @BindView(R.id.dailyVolumeUsdField)
    AutoResizeTextView dailyVolumeUsdField;
    @BindView(R.id.marketCapUsdField)
    AutoResizeTextView marketCapUsdField;
    @BindView(R.id.availableSupplyField)
    AutoResizeTextView availableSupplyField;
    @BindView(R.id.totalSupplyField)
    AutoResizeTextView totalSupplyField;
    @BindView(R.id.lastUpdateTimeField)
    AutoResizeTextView lastUpdateTimeField;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat.getCurrencyInstance().setMaximumFractionDigits(8);

        String rank = getIntent().getStringExtra(BundleKeys.RANK);
        String fullName = getIntent().getStringExtra(BundleKeys.FULL_NAME);
        String priceUsd = getIntent().getStringExtra(BundleKeys.PRICE_USD);
        String priceBtc = getIntent().getStringExtra(BundleKeys.PRICE_BTC);
        String hourlyChange = getIntent().getStringExtra(BundleKeys.HOURLY_PERCENT_CHANGE);
        String dailyChange = getIntent().getStringExtra(BundleKeys.DAILY_PERCENT_CHANGE);
        String weeklyChange = getIntent().getStringExtra(BundleKeys.WEEKLY_PERCENT_CHANGE);
        String dailyVolume = getIntent().getStringExtra(BundleKeys.DAILY_VOLUME);
        String marketCap = getIntent().getStringExtra(BundleKeys.MARKET_CAP);
        String availableSupply = getIntent().getStringExtra(BundleKeys.AVAILABLE_SUPPLY);
        String totalSupply = getIntent().getStringExtra(BundleKeys.TOTAL_SUPPLY);

        String timeStampString = getIntent().getStringExtra(BundleKeys.LAST_UPDATE_TIME);
        Long timeStamp = Long.parseLong(timeStampString);
        timeStamp *= 1000L;
        Date currentDate = new Date(timeStamp);

        rankField.setText(rank);
        fullNameField.setText(fullName);

        currencyFormat.setMaximumFractionDigits(8);
        priceUsdField.setText(currencyFormat.format(Float.parseFloat(priceUsd)));
        priceBtcField.setText(priceBtc);

        hourlyPercentChangeField.setText(String.format("%s%%", hourlyChange));
        dailyPercentChangeField.setText(String.format("%s%%", dailyChange));
        weeklyPercentChangeField.setText(String.format("%s%%", weeklyChange));

        currencyFormat.setMaximumFractionDigits(2);
        dailyVolumeUsdField.setText(currencyFormat.format(Float.parseFloat(dailyVolume)));
        marketCapUsdField.setText(currencyFormat.format(Float.parseFloat(marketCap)));
        availableSupplyField.setText(currencyFormat.format(Float.parseFloat(availableSupply)));
        totalSupplyField.setText(currencyFormat.format(Float.parseFloat(totalSupply)));
        lastUpdateTimeField.setText(currentDate.toString());
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
