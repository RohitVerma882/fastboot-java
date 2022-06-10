package com.rv882.fastbootjava.sample.ui;

import android.view.View;

import com.rv882.fastbootjava.sample.data.FastbootDevice;

public interface OnFastbootDeviceItemClickListener {
    void onFastbootDeviceItemClick(FastbootDevice device, View view);
}
