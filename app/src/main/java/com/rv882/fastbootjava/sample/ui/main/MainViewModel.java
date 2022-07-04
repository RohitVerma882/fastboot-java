package com.rv882.fastbootjava.sample.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.rv882.fastbootjava.sample.data.FastbootDevice;
import com.rv882.fastbootjava.sample.data.FastbootDeviceDataSource;

public class MainViewModel extends ViewModel {
    public LiveData<PagedList<FastbootDevice>> fastbootDevices = new LivePagedListBuilder(FastbootDeviceDataSource.FACTORY, 10).build();
}
