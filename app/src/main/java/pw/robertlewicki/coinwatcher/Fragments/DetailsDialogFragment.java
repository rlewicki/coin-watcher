package pw.robertlewicki.coinwatcher.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pw.robertlewicki.coinwatcher.Misc.BundleKeys;
import pw.robertlewicki.coinwatcher.Models.Coin;
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
        rankField.setText(data.getString(BundleKeys.RANK));
        fullNameField.setText(data.getString(BundleKeys.FULL_NAME));
        priceUsdField.setText(data.getString(BundleKeys.PRICE_USD));
        dailyVolumeUsdField.setText(data.getString(BundleKeys.DAILY_VOLUME));
        marketCapUsdField.setText(data.getString(BundleKeys.MARKET_CAP));
        availableSupplyField.setText(data.getString(BundleKeys.AVAILABLE_SUPPLY));
        totalSupplyField.setText(data.getString(BundleKeys.TOTAL_SUPPLY));
        lastUpdateTimeField.setText(data.getString(BundleKeys.LAST_UPDATE_TIME));

        builder.setView(dialogView);

        return builder.create();
    }
}
