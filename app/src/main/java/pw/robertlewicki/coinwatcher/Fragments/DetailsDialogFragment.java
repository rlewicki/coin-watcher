package pw.robertlewicki.coinwatcher.Fragments;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pw.robertlewicki.coinwatcher.Misc.BundleKeys;
import pw.robertlewicki.coinwatcher.R;

public class DetailsDialogFragment extends DialogFragment
{
    @BindView(R.id.rankField)
    TextView rankField;
    @BindView(R.id.fullNameField)
    TextView fullNameField;
    @BindView(R.id.priceUsdField)
    TextView priceUsdField;
    @BindView(R.id.dailyVolumeUsdField)
    TextView dailyVolumeUsdField;
    @BindView(R.id.marketCapUsdField)
    TextView marketCapUsdField;
    @BindView(R.id.availableSupplyField)
    TextView availableSupplyField;
    @BindView(R.id.totalSupplyField)
    TextView totalSupplyField;
    @BindView(R.id.lastUpdateTimeField)
    TextView lastUpdateTimeField;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.details_dialog, null);
        ButterKnife.bind(this, dialogView);

        Bundle data = getArguments();

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String priceUsd = currencyFormat.format(
                Float.parseFloat(data.getString(BundleKeys.PRICE_USD)));
        String dailyVolume = currencyFormat.format(
                Float.parseFloat(data.getString(BundleKeys.DAILY_VOLUME)));
        String marketCap = currencyFormat.format(
                Float.parseFloat(data.getString(BundleKeys.MARKET_CAP)));
        String availableSupply = currencyFormat.format(
                Float.parseFloat(data.getString(BundleKeys.AVAILABLE_SUPPLY)));
        String totalSupply = currencyFormat.format(
                Float.parseFloat(data.getString(BundleKeys.TOTAL_SUPPLY)));
        Date timestamp = new Date(
                (long)Integer.parseInt(data.getString(BundleKeys.LAST_UPDATE_TIME)) * 1000);


        rankField.setText(data.getString(BundleKeys.RANK));
        fullNameField.setText(data.getString(BundleKeys.FULL_NAME));
        priceUsdField.setText(priceUsd);
        dailyVolumeUsdField.setText(dailyVolume);
        marketCapUsdField.setText(marketCap);
        availableSupplyField.setText(availableSupply);
        totalSupplyField.setText(totalSupply);
        lastUpdateTimeField.setText(timestamp.toString());

        builder.setView(dialogView);

        return builder.create();
    }
}
