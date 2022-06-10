package com.rv882.fastbootjava.transport;

import androidx.annotation.NonNull;

public interface Transport {
    boolean isConnected();

    void setConnected(boolean isConnected);

    void connect(boolean force);

    void disconnect();

    void close();

    void send(@NonNull byte[] buffer, int timeout);

    void receive(@NonNull byte[] buffer, int timeout);
}
