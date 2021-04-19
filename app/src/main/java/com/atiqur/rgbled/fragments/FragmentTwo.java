package com.atiqur.rgbled.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.atiqur.rgbled.MainActivity;
import com.atiqur.rgbled.databinding.FragmentTwoBinding;

public class FragmentTwo extends Fragment {
    FragmentTwoBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTwoBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public FragmentTwoBinding getBinding(){
        return binding;
    }
}
