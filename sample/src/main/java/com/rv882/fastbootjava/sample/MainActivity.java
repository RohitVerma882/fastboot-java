package com.rv882.fastbootjava.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    @Override
	public boolean onSupportNavigateUp() {
		return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp() || super.onNavigateUp();
	}
}
