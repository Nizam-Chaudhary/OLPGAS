package com.example.olpgas.core.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkConnectivity(app: Application)  {

    private val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isConnected = MutableLiveData(false)
    val isConnected: LiveData<Boolean> = _isConnected

    init {
        connectivityManager.registerDefaultNetworkCallback(object: ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _isConnected.value = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _isConnected.value = false
            }

            override fun onUnavailable() {
                super.onUnavailable()
                _isConnected.value = false
            }
        })
    }
}