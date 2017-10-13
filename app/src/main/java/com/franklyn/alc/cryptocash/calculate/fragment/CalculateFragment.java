package com.franklyn.alc.cryptocash.calculate.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.franklyn.alc.cryptocash.R;

import butterknife.ButterKnife;

/**
 * Created by AGBOMA franklyn on 10/13/17.
 */

public class CalculateFragment extends Fragment{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View calculate = inflater.inflate(R.layout.fragment_calcaulate, container, false);
        ButterKnife.bind(this, calculate);
        return calculate;
    }
}
