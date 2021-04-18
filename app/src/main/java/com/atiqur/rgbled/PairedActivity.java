package com.atiqur.rgbled;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.atiqur.rgbled.adapter.PairedDeviceAdapter;
import com.atiqur.rgbled.databinding.ActivityPairedBinding;
import com.atiqur.rgbled.utils.ToolbarHelper;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class PairedActivity extends AppCompatActivity implements PairedDeviceAdapter.OnItemClick{

    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    ActivityPairedBinding binding;
    PairedDeviceAdapter adapter;
    ArrayList<ArrayList<String>> objects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPairedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setResult(0);
        init();
    }

    private void init() {

        ToolbarHelper.create(binding.toolbar, null, this, "Paired Devices");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemViewCacheSize(1);

        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        int i = 0;
        if (pairedDevices.size() > 0) {
            binding.noDeviceTextView.setVisibility(View.GONE);
            for (BluetoothDevice device : pairedDevices) {
                objects.add(new ArrayList<>());
                objects.get(i).add(device.getName());
                objects.get(i).add(device.getAddress());
                i++;
            }
            adapter = new PairedDeviceAdapter(objects, this);
            binding.recyclerView.setAdapter(adapter);
        } else {
            binding.noDeviceTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(int position, ArrayList<ArrayList<String>> objects) {
        String address = objects.get(position).get(1);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
        setResult(-1, intent);
        finish();
    }
}