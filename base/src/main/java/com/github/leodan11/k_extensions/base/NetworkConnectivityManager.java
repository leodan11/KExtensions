package com.github.leodan11.k_extensions.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

public class NetworkConnectivityManager extends LiveData<Boolean> {

    private final Context mContext;
    private final ConnectivityManager.NetworkCallback networkCallback;

    public NetworkConnectivityManager(@NonNull Context context) {
        this.mContext = context;
        this.networkCallback = new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                postValue(true);
                Log.d(this.getClass().getSimpleName(), "onAvailable");
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                postValue(false);
                Log.d(this.getClass().getSimpleName(), "onLost");
            }

            @Override
            public void onLosing(@NonNull Network network, int maxMsToLive) {
                super.onLosing(network, maxMsToLive);
                Log.d(this.getClass().getSimpleName(), "onLosing");
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                Log.d(this.getClass().getSimpleName(), "onUnavailable");
            }

        };
    }

    @Override
    protected void onActive() {
        super.onActive();
        registerNetworkCallback();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        unregisterNetworkCallback();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    public boolean isManagerEnabled() {
        boolean isEnabled = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (networkCapabilities != null) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    isEnabled = true;
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    isEnabled = true;
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    isEnabled = true;
                }
            }
        }
        return isEnabled;
    }

    @SuppressLint("MissingPermission")
    private void registerNetworkCallback() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .build();

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    private void unregisterNetworkCallback() {
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            connectivityManager.unregisterNetworkCallback(networkCallback);
        } catch (Exception exception) {
            Log.e(this.getClass().getSimpleName(), "unregisterNetworkCallback", exception);
        }
    }


}
