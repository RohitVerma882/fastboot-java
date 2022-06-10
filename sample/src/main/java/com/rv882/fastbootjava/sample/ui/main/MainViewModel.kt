
package com.rv882.fastbootjava.sample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

import com.rv882.fastbootjava.sample.data.FastbootDevice
import com.rv882.fastbootjava.sample.data.FastbootDeviceDataSource

class MainViewModel : ViewModel() {
    val fastbootDevices: LiveData<PagedList<FastbootDevice>> = LivePagedListBuilder(
            FastbootDeviceDataSource.FACTORY, 10).build()
}
