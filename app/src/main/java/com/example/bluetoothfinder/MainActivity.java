package com.example.bluetoothfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnSearch;
    TextView tvStatus;
    ListView listView;
    BluetoothAdapter bluetoothAdapter;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.i("Action: ",action);
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {   arrayList.clear();
                arrayAdapter.notifyDataSetChanged();
                btnSearch.setEnabled(true);
                tvStatus.setText("Finished");
            }
            else if(BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE));
                Log.i("Action: device name",name);
                Log.i("Action: device address",address);
                Log.i("Action: device rssi",rssi);
                if(name == null || name.equals(""))
                {
                    arrayList.add(address + " - "+ rssi+"dBm");
                }
                else
                {
                    arrayList.add(name +" - "+ rssi+"dBm");
                }
                arrayAdapter.notifyDataSetChanged();


            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        btnSearch = findViewById(R.id.btnSearch);
        tvStatus = findViewById(R.id.tvStatus);
        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);


         bluetoothAdapter =BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);

        registerReceiver(broadcastReceiver, intentFilter);

    }

    public void beginSearch(View view)
    {   arrayList.clear();
        tvStatus.setText("Searching...");
        btnSearch.setEnabled(false);
        bluetoothAdapter.startDiscovery();



    }


}