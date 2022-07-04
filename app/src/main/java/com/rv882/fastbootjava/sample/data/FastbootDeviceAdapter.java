package com.rv882.fastbootjava.sample.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rv882.fastbootjava.sample.ui.OnFastbootDeviceItemClickListener;
import com.rv882.fastbootjava.sample.R;

public class FastbootDeviceAdapter extends PagedListAdapter<FastbootDevice, FastbootDeviceAdapter.ViewHolder> implements OnFastbootDeviceItemClickListener {

    private static DiffUtil.ItemCallback<FastbootDevice> FASTBOOT_DEVICE_COMPARATOR = new DiffUtil.ItemCallback<FastbootDevice>() {
        @Override
        public boolean areItemsTheSame(@NonNull FastbootDevice p1, @NonNull FastbootDevice p2) {
            return p1.getDeviceId() ==  p2.getDeviceId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull FastbootDevice p1, @NonNull FastbootDevice p2) {
            return p1.equals(p2);
        }
    };

    private List<OnFastbootDeviceItemClickListener> listeners = new ArrayList<>();

    public FastbootDeviceAdapter() {
        super(FASTBOOT_DEVICE_COMPARATOR);
    }

    @Override
    public void onFastbootDeviceItemClick(FastbootDevice device, View view) {
        for (OnFastbootDeviceItemClickListener listener : listeners) {
            listener.onFastbootDeviceItemClick(device, view);
        }
    }

    public void addOnFastbootDeviceItemClickListener(OnFastbootDeviceItemClickListener listener) {
        listeners.add(listener);
    }

    public void removeOnFastbootDeviceItemClickListener(OnFastbootDeviceItemClickListener listener) {
        listeners.remove(listener);
    }

    @Override
    public FastbootDeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        LayoutInflater inflater = (LayoutInflater) p1.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_fastboot_device, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FastbootDeviceAdapter.ViewHolder p1, int p2) {
        p1.bind(getItem(p2), this);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView textview;
        
        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            textview = view.findViewById(R.id.tv_device_id);
        }

        public void bind(@NonNull FastbootDevice device, @NonNull OnFastbootDeviceItemClickListener listener) {
            textview.setText(device.getDeviceId());
            view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View p1) {
                        listener.onFastbootDeviceItemClick(device, view);
                    }
                });
        }
    }
}
