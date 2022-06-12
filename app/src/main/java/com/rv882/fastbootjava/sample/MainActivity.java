package com.rv882.fastbootjava.sample;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.rv882.fastbootjava.FastbootDeviceContext;
import com.rv882.fastbootjava.FastbootDeviceManagerListener;
import com.rv882.fastbootjava.FastbootDeviceManager;
import com.rv882.fastbootjava.FastbootResponse;
import com.rv882.fastbootjava.FastbootCommand;

public class MainActivity extends AppCompatActivity implements FastbootDeviceManagerListener {
	
	private TextView textview;
	private TextView textview2;
	private Button button;
	
	private FastbootDeviceContext deviceContext;
	private String deviceId;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		textview = findViewById(R.id.textview);
		textview2 = findViewById(R.id.textview2);
		button = findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					if (deviceContext != null) {
						FastbootResponse response = deviceContext.sendCommand(FastbootCommand.reboot());
						textview2.setText(response.getData());
					}
				}
			});
		
		FastbootDeviceManager.Instance.addFastbootDeviceManagerListener(this);
    }
	
	@Override
	public void onFastbootDeviceAttached(String deviceId) {
		this.deviceId = deviceId;
		FastbootDeviceManager.Instance.connectToDevice(deviceId);
		
		Toast.makeText(getApplicationContext(), "device attached " + deviceId, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFastbootDeviceDetached(String deviceId) {
		closeDeviceContext();
		
		textview.setText("No device connected");
		Toast.makeText(getApplicationContext(), "device disconnected " + deviceId, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFastbootDeviceConnected(String deviceId, FastbootDeviceContext deviceContext) {
		this.deviceContext = deviceContext;
		
		textview.setText("Device: " + deviceId);
		Toast.makeText(getApplicationContext(), "device connected " + deviceId, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onDestroy() {
		FastbootDeviceManager.Instance.removeFastbootDeviceManagerListener(this);
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
