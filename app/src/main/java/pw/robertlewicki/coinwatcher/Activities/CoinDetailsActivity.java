package pw.robertlewicki.coinwatcher.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;
import pw.robertlewicki.coinwatcher.Misc.BundleKeys;
import pw.robertlewicki.coinwatcher.R;

public class CoinDetailsActivity extends AppCompatActivity
{
    @BindView(R.id.rankField)
    AutofitTextView rankField;
    @BindView(R.id.fullNameField)
    AutofitTextView fullNameField;
    @BindView(R.id.priceUsdField)
    AutofitTextView priceUsdField;
    @BindView(R.id.priceBtcField)
    AutofitTextView priceBtcField;
    @BindView(R.id.hourlyPercentChangeField)
    AutofitTextView hourlyPercentChangeField;
    @BindView(R.id.dailyPercentChangeField)
    AutofitTextView dailyPercentChangeField;
    @BindView(R.id.weeklyPercentChangeField)
    AutofitTextView weeklyPercentChangeField;
    @BindView(R.id.dailyVolumeUsdField)
    AutofitTextView dailyVolumeUsdField;
    @BindView(R.id.marketCapUsdField)
    AutofitTextView marketCapUsdField;
    @BindView(R.id.availableSupplyField)
    AutofitTextView availableSupplyField;
    @BindView(R.id.totalSupplyField)
    AutofitTextView totalSupplyField;
    @BindView(R.id.lastUpdateTimeField)
    AutofitTextView lastUpdateTimeField;

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
