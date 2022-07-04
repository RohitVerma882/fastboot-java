package com.rv882.fastbootjava.sample.ui.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.annotation.NonNull;

import com.rv882.fastbootjava.sample.R;
import com.rv882.fastbootjava.sample.ui.OnFastbootDeviceItemClickListener;
import com.rv882.fastbootjava.sample.data.FastbootDeviceAdapter;
import com.rv882.fastbootjava.sample.data.FastbootDevice;
import com.rv882.fastbootjava.sample.ui.devicedetails.DeviceDetailsFragment;

public class MainFragment extends Fragment implements OnFastbootDeviceItemClickListener {
    public static final String TAG = "MainFragment";

    private MainViewModel viewModel;
    private RecyclerView rvAttachedDevices;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        rvAttachedDevices = view.findViewById(R.id.rv_attached_devices);
        rvAttachedDevices.setHasFixedSize(true);
        rvAttachedDevices.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        final FastbootDeviceAdapter fastbootDeviceAdapter = new FastbootDeviceAdapter();
        fastbootDeviceAdapter.addOnFastbootDeviceItemClickListener(this);
        rvAttachedDevices.setAdapter(fastbootDeviceAdapter);

        viewModel.fastbootDevices.observe(getViewLifecycleOwner(), new Observer<PagedList<FastbootDevice>>() {
                @Override
                public void onChanged(@NonNull PagedList<FastbootDevice> p1) {
                    fastbootDeviceAdapter.submitList(p1);
                }
            });
        return view;
    }
    
    @Override
    public void onFastbootDeviceItemClick(@NonNull FastbootDevice device, @NonNull View view) {
        showDeviceDetails(device, view);
    }

    private void showDeviceDetails(FastbootDevice device, View view) {
        DeviceDetailsFragment fragment = DeviceDetailsFragment.newInstance(device.getDeviceId());
        getParentFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit();
    }
}
