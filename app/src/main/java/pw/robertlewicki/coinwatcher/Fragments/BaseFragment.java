package pw.robertlewicki.coinwatcher.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pw.robertlewicki.coinwatcher.R;

public class BaseFragment extends Fragment {

    private String title;


    public BaseFragment() {}

    public static BaseFragment newInstance(String title) {
        BaseFragment fragment = new BaseFragment();
        fragment.title = title;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(title);
        return rootView;
    }

    public String getTitle() {
        return title;
    }
}
