package com.android.pokeapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Rémi OLLIVIER on 29/08/2018.
 */
public class Network {

    private Network() {
        throw new UnsupportedOperationException("You can't instantiate me...");
    }

    public static boolean isConnectionAvailable(Context context) {
        boolean networkAvailable;
        if (context == null) {
            return false;
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork;
            if (connectivityManager != null) {
                activeNetwork = connectivityManager.getActiveNetworkInfo();
                networkAvailable =  activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                return networkAvailable;
            }
            return false;
        }
    }
}
