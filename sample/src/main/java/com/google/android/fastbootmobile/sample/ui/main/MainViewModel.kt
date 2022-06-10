/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.fastbootmobile.sample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

import com.google.android.fastbootmobile.sample.data.FastbootDevice
import com.google.android.fastbootmobile.sample.data.FastbootDeviceDataSource

class MainViewModel : ViewModel() {
    val fastbootDevices: LiveData<PagedList<FastbootDevice>> = LivePagedListBuilder(
            FastbootDeviceDataSource.FACTORY, 10).build()
}
