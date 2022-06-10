
package com.rv882.fastbootjava.sample.ui.devicedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.rv882.fastbootjava.sample.R
import com.rv882.fastbootjava.sample.databinding.FragmentDeviceDetailsBinding
import com.rv882.fastbootjava.sample.ui.devicedetails.DeviceDetailsFragmentArgs

class DeviceDetailsFragment : Fragment() {
    private lateinit var viewModel: DeviceDetailsViewModel
    private lateinit var binding: FragmentDeviceDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_device_details, container, false)
        binding = FragmentDeviceDetailsBinding.bind(view)

        viewModel = ViewModelProviders.of(this).get(DeviceDetailsViewModel::class.java)
        viewModel.fastbootDevice.observe(this, Observer {
            binding.device = it
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val deviceId = DeviceDetailsFragmentArgs.fromBundle(requireArguments()).deviceId!!
        viewModel.connectToDevice(deviceId)
    }
}
