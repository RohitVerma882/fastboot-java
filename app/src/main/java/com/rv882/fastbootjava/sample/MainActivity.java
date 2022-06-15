package com.rv882.fastbootjava.sample;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.rv882.fastbootjava.FastbootDeviceContext;
import com.rv882.fastbootjava.FastbootDeviceManagerListener;
import com.rv882.fastbootjava.FastbootDeviceManager;
import com.rv882.fastbootjava.FastbootResponse;
import com.rv882.fastbootjava.FastbootCommand;

public class MainActivity extends AppCompatActivity implements FastbootDeviceManagerListener {
	
	private TextView deviceTextview;
	private TextView responseTextview;
	private EditText cmdEditText;
	
	private Button rebootButton;
	private Button runButton;
	
	private FastbootDeviceContext deviceContext;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		deviceTextview = findViewById(R.id.deviceTextview);
		responseTextview = findViewById(R.id.responseTextview);
		cmdEditText = findViewById(R.id.cmdEditText);
		
		rebootButton = findViewById(R.id.rebootButton);
		rebootButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					reboot();
				}
			});
			
		runButton = findViewById(R.id.runButton);
		runButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					run();
				}
			});
		
		FastbootDeviceManager.Instance.addFastbootDeviceManagerListener(this);
    }
	
	private void run() {
		String cmd = cmdEditText.getText().toString();
		if (!cmd.isEmpty() && deviceContext != null) {
			FastbootResponse response = deviceContext.sendCommand(FastbootCommand.command(cmd), false);
			responseTextview.setText(response.getData());
		}
	}
	
	private void reboot() {
		if (deviceContext != null) {
			FastbootResponse response = deviceContext.sendCommand(FastbootCommand.reboot(), false);
			responseTextview.setText(response.getData());
		}
	}
	
	private void closeDeviceContext() {
		if (deviceContext != null) {
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
		this.deviceContext = deviceContext;
		
		deviceTextview.setText("Connected device: " + deviceId);
	}

	@Override
	protected void onDestroy() {
		FastbootDeviceManager.Instance.removeFastbootDeviceManagerListener(this);
		closeDeviceContext();
		super.onDestroy();
	}
}
