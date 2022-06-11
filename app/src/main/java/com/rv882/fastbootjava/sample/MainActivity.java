package com.rv882.fastbootjava.sample;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rv882.fastbootjava.FastbootDeviceContext;
import com.rv882.fastbootjava.FastbootDeviceManagerListener;
import com.rv882.fastbootjava.FastbootDeviceManager;

public class MainActivity extends AppCompatActivity implements FastbootDeviceManagerListener {
	
	private TextView textview;
	
	private FastbootDeviceContext deviceContext;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		textview = findViewById(R.id.text);
		
		FastbootDeviceManager.getInstance().addFastbootDeviceManagerListener(this);
    }
	
	@Override
	public void onFastbootDeviceAttached(String deviceId) {
		Toast.makeText(getApplicationContext(), "device attached" + deviceId, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFastbootDeviceDetached(String deviceId) {
		closeDeviceContext();
		
		textview.setText("No device");
		Toast.makeText(getApplicationContext(), "device disconnected" + deviceId, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFastbootDeviceConnected(String deviceId, FastbootDeviceContext deviceContext) {
		this.deviceContext = deviceContext;
		
		textview.setText("Device: " + deviceId);
		Toast.makeText(getApplicationContext(), "device connected" + deviceId, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onDestroy() {
		FastbootDeviceManager.getInstance().removeFastbootDeviceManagerListener(this);
		closeDeviceContext();
		super.onDestroy();
	}
	
	private void closeDeviceContext() {
		if (deviceContext != null) {
			deviceContext.close();
			deviceContext = null;
		}
	}
}
