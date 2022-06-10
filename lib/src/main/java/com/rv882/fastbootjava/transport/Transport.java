package com.rv882.fastbootjava.transport;

interface Transport {
    boolean isConnected();

    void setConnected(final boolean isConnected);

    void connect(final boolean force);

    void disconnect();

    void close();

    void send(final byte[] buffer, final int timeout);

    void receive(final byte[] buffer, final int timeout);
}
