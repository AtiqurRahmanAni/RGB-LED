package com.atiqur.rgbled.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.atiqur.rgbled.databinding.FragmentOneBinding;

public class FragmentOne extends Fragment {
    FragmentOneBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOneBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public FragmentOneBinding getBinding(){
        return binding;
    }
}
