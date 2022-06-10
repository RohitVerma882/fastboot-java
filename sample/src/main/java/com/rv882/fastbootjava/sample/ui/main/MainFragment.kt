
package com.rv882.fastbootjava.sample.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.navigation.findNavController
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.rv882.fastbootjava.sample.R
import com.rv882.fastbootjava.sample.data.FastbootDevice
import com.rv882.fastbootjava.sample.data.FastbootDeviceAdapter
import com.rv882.fastbootjava.sample.ui.OnFastbootDeviceItemClickListener
import com.rv882.fastbootjava.sample.ui.main.MainFragmentDirections

class MainFragment : Fragment(), OnFastbootDeviceItemClickListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var rvAttachedDevices: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        rvAttachedDevices = view.findViewById(R.id.rv_attached_devices)
        rvAttachedDevices.setHasFixedSize(true)
        rvAttachedDevices.layoutManager = LinearLayoutManager(context)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val fastbootDeviceAdapter = FastbootDeviceAdapter()
        fastbootDeviceAdapter.addOnFastbootDeviceItemClickListener(this)
        rvAttachedDevices.adapter = fastbootDeviceAdapter

        viewModel.fastbootDevices.observe(this, Observer {
            fastbootDeviceAdapter.submitList(it)
        })

        return view
    }

    override fun onFastbootDeviceItemClick(device: FastbootDevice, view: View) = showDeviceDetails(device, view)

    private fun showDeviceDetails(device: FastbootDevice, view: View) {
        val connectAction = MainFragmentDirections.connectAction(device.deviceId!!)
        view.findNavController().navigate(connectAction)
    }
}
