package com.rv882.fastbootjava.sample;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;

import com.rv882.fastbootjava.FastbootDeviceContext;
import com.rv882.fastbootjava.FastbootDeviceManagerListener;
import com.rv882.fastbootjava.FastbootDeviceManager;
import com.rv882.fastbootjava.FastbootResponse;
import com.rv882.fastbootjava.FastbootCommand;

import com.rv882.fastbootjava.sample.data.FastbootDevice;

public class MainActivity extends AppCompatActivity implements FastbootDeviceManagerListener {

	private TextView deviceTextview;
	private TextView responseTextview;

	private Button rebootButton;
	
	private FastbootDevice device;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		FastbootDeviceManager.Instance.addFastbootDeviceManagerListener(this);

		deviceTextview = findViewById(R.id.deviceTextview);
		responseTextview = findViewById(R.id.responseTextview);

		rebootButton = findViewById(R.id.rebootButton);
		rebootButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					reboot();
				}
			});
    }

	private void reboot() {
		if (device == null) {
			return;
		}
		Pair<String, FastbootDeviceContext> pair = FastbootDeviceManager.Instance.getDeviceContext(device.getDeviceId());
		FastbootDeviceContext deviceContext = null;
		if (pair != null && (deviceContext = pair.second) != null) {
			FastbootResponse response = deviceContext.sendCommand(FastbootCommand.reboot(), false);
			responseTextview.setText(response.getData());
		}
	}

	private void closeDeviceContext() {
		if (device == null) {
			return;
		}
		Pair<String, FastbootDeviceContext> pair = FastbootDeviceManager.Instance.getDeviceContext(device.getDeviceId());
		FastbootDeviceContext deviceContext = null;
		if (pair != null && (deviceContext = pair.second) != null) {
			deviceContext.close();
			deviceContext = null;
		}
	}

	@Override
	public void onFastbootDeviceAttached(String deviceId) {
		FastbootDeviceManager.Instance.connectToDevice(deviceId);
	}

	@Override
	public void onFastbootDeviceDetached(String deviceId) {
		closeDeviceContext();

		deviceTextview.setText("No connected device");
	}

	@Override
	public void onFastbootDeviceConnected(String deviceId, FastbootDeviceContext deviceContext) {
		device = FastbootDevice.fromDeviceContext(deviceId, deviceContext);

		deviceTextview.setText("Connected device: " + device.getSerialNumber());
	}

	@Override
	protected void onDestroy() {
		FastbootDeviceManager.Instance.removeFastbootDeviceManagerListener(this);
		closeDeviceContext();
		super.onDestroy();
	}
}
