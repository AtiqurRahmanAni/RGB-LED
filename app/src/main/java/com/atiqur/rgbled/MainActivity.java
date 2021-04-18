package com.atiqur.rgbled;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.atiqur.rgbled.databinding.ActivityMainBinding;
import com.atiqur.rgbled.utils.HelperUtils;
import com.atiqur.rgbled.utils.ToolbarHelper;
import com.google.android.material.slider.Slider;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private boolean menuCreated = false;
    private BluetoothAdapter mBluetoothAdapter = null;
    public Bluetooth mBluetooth = null;
    private String deviceAddress = null;
    private static final int REQUEST_ENABLE_BT = 222;
    private static final int ENABLED = 111;
    private boolean isConnected = false;
    private Menu menu;
    private Thread mRGBThread;
    private final int[] rgb ={0,0,0};
    private final boolean[] allow ={false,false,false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ToolbarHelper.create(binding.toolbar,null,this,"RGB LED");
        setSliderListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.textViewR.setText(String.format("R : %.3s",(int)binding.sliderR.getValue()));
        binding.textViewG.setText(String.format("G : %.3s",(int)binding.sliderG.getValue()));
        binding.textViewB.setText(String.format("B : %.3s",(int)binding.sliderB.getValue()));
        checkBluetooth();
        if (mBluetooth == null) {
            mBluetooth = new Bluetooth(mHandler);
        }
        if (mBluetooth.getState() == 0) {
            mBluetooth.start();
        }
        mRGBThread = new Thread(sendValue);
        mRGBThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRGBThread.interrupt();
    }

    public void onDestroy() {
        super.onDestroy();
        mRGBThread.interrupt();
        if (this.mBluetooth != null) {
            this.mBluetooth.stop();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setSliderListener() {
        binding.sliderR.setOnTouchListener((v, event) -> {
            rgb[0]=(int)binding.sliderR.getValue();
            setBackground();
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                if (!allow[0] && mBluetooth.getState() == 2) {
                    allow[0] = true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                allow[0] = false;
            }
            binding.textViewR.setText(String.format("R : %.3s", rgb[0]));
            if (event.getAction() == MotionEvent.ACTION_DOWN && mBluetooth.getState() != 2) {
                Toast.makeText(MainActivity.this, "You are not connected to a device", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        binding.sliderG.setOnTouchListener((v, event) -> {
            rgb[1]=(int)binding.sliderG.getValue();
            setBackground();
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                if (!allow[1] && mBluetooth.getState() == 2) {
                    allow[1] = true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                allow[1] = false;
            }
            binding.textViewG.setText(String.format("G : %.3s", rgb[1]));
            if (event.getAction() == MotionEvent.ACTION_DOWN && mBluetooth.getState() != 2) {
                Toast.makeText(MainActivity.this, "You are not connected to a device", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        binding.sliderB.setOnTouchListener((v, event) -> {
            rgb[2]=(int)binding.sliderB.getValue();
            setBackground();
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                if (!allow[2] && mBluetooth.getState() == 2) {
                    allow[2] = true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                allow[2] = false;
            }
            binding.textViewB.setText(String.format("B : %.3s", rgb[2]));
            if (event.getAction() == MotionEvent.ACTION_DOWN && mBluetooth.getState() != 2) {
                Toast.makeText(MainActivity.this, "You are not connected to a device", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
    }

    private final Runnable sendValue = new Runnable() {
        public void run() {
            while (true) {
                if (allow[0]) {
//                    float r = binding.sliderR.getValue();
                    mBluetooth.write(HelperUtils.toBytesFloat('R', rgb[0], 3));
                }
                if (allow[1]) {
//                    float g = binding.sliderG.getValue();
                    mBluetooth.write(HelperUtils.toBytesFloat('G', rgb[1], 3));
                }
                if (allow[2]) {
//                    float b = binding.sliderB.getValue();
                    mBluetooth.write(HelperUtils.toBytesFloat('B', rgb[2], 3));
                }
                synchronized (this) {
                    try {
                        wait(25);
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        }
    };

    void setBackground() {
        binding.colorBackground.getBackground().setTint(Color.rgb(rgb[0],rgb[1],rgb[2]));
    }

    private void checkBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        this.menu = menu;
        menuCreated = true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.connect_scan) {
            if (!mBluetoothAdapter.isEnabled()) {
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
            } else if (!isConnected) {
                startActivityForResult(new Intent(this, PairedActivity.class), ENABLED);
            } else {
                Toast.makeText(this, "You are already connected!", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == R.id.menu_disconnect) {
            mBluetooth.disconnect();
        }
        return super.onOptionsItemSelected(item);
    }

    private void connectDevice(Intent data) {
        if (mBluetooth != null) {
            mBluetooth.stop();
        }
        String address = data.getExtras().getString(PairedActivity.EXTRA_DEVICE_ADDRESS);
        deviceAddress = address;
        mBluetooth.connect(mBluetoothAdapter.getRemoteDevice(address));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ENABLED && resultCode == RESULT_OK) {
            if (mBluetooth.getState() != 0) {
                if (data != null && !deviceAddress.equals(data.getExtras().getString(PairedActivity.EXTRA_DEVICE_ADDRESS))) {
                    mBluetooth.stop();
                    mBluetooth = new Bluetooth(mHandler);
                    try {
                        wait(10);
                    } catch (Exception e) {
                        Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
                    }
                    connectDevice(data);
                    return;
                }
                return;
            }
            mBluetooth.stop();
            mBluetooth = new Bluetooth(mHandler);
            connectDevice(data);
        } else if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), "Bluetooth enabled", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Bluetooth not enabled. Leaving", Toast.LENGTH_SHORT).show();
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private final Handler mHandler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (msg.arg1 == 0 && menuCreated) {
                    isConnected = false;
                    menu.getItem(0).setShowAsAction(5);
                    menu.getItem(0).setTitle("Connect");
                } else if (msg.arg1 == 1 && menuCreated) {
                    isConnected = false;
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_bluetooth_connecting));
                    menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                } else if (msg.arg1 == 2 && menuCreated) {
                    isConnected = true;
                    menu.getItem(0).setIcon(null);
                    menu.getItem(0).setShowAsAction(5);
                    menu.getItem(0).setTitle("Connected");
                }
            } else if (msg.what == 2) {
                Toast.makeText(MainActivity.this, "Connected to " + msg.getData().getString("device_name"), Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                menu.getItem(0).setIcon(null);
                menu.getItem(0).setShowAsAction(5);
                menu.getItem(0).setTitle("Connect");
                Toast.makeText(MainActivity.this, msg.getData().getString("toast"), Toast.LENGTH_SHORT).show();
            }
        }
    };
}