package com.htoja.mifik.htoja.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.htoja.mifik.htoja.R;

public class SetupSettingsFragment extends Fragment {
    private static final int MIN_WORDS = 10;
    private static final int MIN_SECONDS = 20;
    private TextView tvWords;
    private SeekBar sbWords;
    private TextView tvSeconds;
    private SeekBar sbSeconds;
    private Switch swFine;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        return inflater.inflate(R.layout.fragment_setup_settings, container, false);
    }

    public int getTargetWords(){
        return Integer.valueOf(tvWords.getText().toString());
    }


    public int getSeconds(){
        return Integer.valueOf(tvSeconds.getText().toString());
    }

    public boolean getFine(){
        return swFine.isChecked();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvWords = (TextView) getActivity().findViewById(R.id.tvWords);
        sbWords = (SeekBar) getActivity().findViewById(R.id.sbWords);
        sbWords.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvWords.setText(String.valueOf(i + MIN_WORDS));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        tvSeconds = (TextView) getActivity().findViewById(R.id.tvSeconds);
        sbSeconds = (SeekBar) getActivity().findViewById(R.id.sbSeconds);
        sbSeconds.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvSeconds.setText(String.valueOf(i + MIN_SECONDS));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        swFine = (Switch) getActivity().findViewById(R.id.swFine);
    }
}
