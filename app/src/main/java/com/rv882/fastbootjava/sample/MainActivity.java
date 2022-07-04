package com.rv882.fastbootjava.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rv882.fastbootjava.sample.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		if (getSupportFragmentManager().findFragmentByTag(MainFragment.TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MainFragment(), MainFragment.TAG)
                .commit();
        }
    }
}
