
package com.rv882.fastbootjava.sample.data

import androidx.paging.DataSource
import androidx.paging.PositionalDataSource

import com.google.android.fastbootmobile.DeviceId
import com.google.android.fastbootmobile.FastbootDeviceContext
import com.google.android.fastbootmobile.FastbootDeviceManager
import com.google.android.fastbootmobile.FastbootDeviceManagerListener

import com.rv882.fastbootjava.sample.data.FastbootDevice.Companion.fromDeviceId

class FastbootDeviceDataSource: PositionalDataSource<FastbootDevice>(), FastbootDeviceManagerListener {
    init {
        FastbootDeviceManager.addFastbootDeviceManagerListener(this)
    }

    override fun onFastbootDeviceAttached(deviceId: DeviceId) {
        invalidate()
    }

    override fun onFastbootDeviceDetached(deviceId: DeviceId) {
        invalidate()
    }

    override fun onFastbootDeviceConnected(deviceId: DeviceId, deviceContext: FastbootDeviceContext) {}

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<FastbootDevice>) {
        val items = FastbootDeviceManager.getAttachedDeviceIds()
                .drop(params.startPosition)
                .take(params.loadSize)
                .map(::fromDeviceId)
        callback.onResult(items)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<FastbootDevice>) {
        val allItems = FastbootDeviceManager.getAttachedDeviceIds()
        val items = allItems
                .drop(params.requestedStartPosition)
                .take(params.pageSize)
                .map(::fromDeviceId)

        if (params.placeholdersEnabled)
            callback.onResult(items, params.requestedStartPosition, allItems.size)
        else
            callback.onResult(items, params.requestedStartPosition)
    }

    companion object {
        @JvmStatic
        val FACTORY: Factory<Int, FastbootDevice> = object: Factory<Int, FastbootDevice>() {
            private var lastSource : FastbootDeviceDataSource? = null

            override fun create(): DataSource<Int, FastbootDevice> {
                if (lastSource != null) {
                    FastbootDeviceManager.removeFastbootDeviceManagerListener(lastSource!!)
                }

                lastSource = FastbootDeviceDataSource()
                return lastSource!!
            }
        }
    }

}
