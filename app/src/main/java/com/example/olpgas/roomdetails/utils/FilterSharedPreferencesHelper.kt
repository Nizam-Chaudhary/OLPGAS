package com.example.olpgas.roomdetails.utils

import android.content.Context
import com.example.olpgas.roomdetails.data.model.Filter

class FilterSharedPreferencesHelper(
    private val context: Context
) {

    fun saveFilter(filter: Filter) {
        saveCityFilter(filter.city)
        if(filter.minRentAmount != null) {
            saveMinRentFilter(filter.minRentAmount)
        } else {
            saveMinRentFilter(0)
        }
        if(filter.maxRentAmount != null) {
            saveMaxRentFilter(filter.maxRentAmount)
        } else {
            saveMaxRentFilter(0)
        }
    }

    fun getFilter() : Filter {
        val city = getCityFilter()
        val minRent = if(getMinRentFilter() == 0) null else getMinRentFilter()
        val maxRent = if(getMaxRentFilter() == 0) null else getMaxRentFilter()
        return Filter(city, minRent, maxRent)
    }

    fun clearFilter() {
        val sharedPreferences = context.getSharedPreferences(MY_FILTER_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    private fun saveCityFilter(city: String?) {
        val sharedPreferences = context.getSharedPreferences(MY_FILTER_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(MY_FILTER_CITY_KEY, city).apply()
    }

    private fun saveMinRentFilter(minRent: Int) {
        val sharedPreferences = context.getSharedPreferences(MY_FILTER_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(MY_FILTER_MIN_RENT_KEY, minRent).apply()
    }

    private fun saveMaxRentFilter(maxRent: Int) {
        val sharedPreferences = context.getSharedPreferences(MY_FILTER_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(MY_FILTER_MAX_RENT_KEY, maxRent).apply()
    }

    private fun getCityFilter() : String? {
        val sharedPreferences = context.getSharedPreferences(MY_FILTER_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(MY_FILTER_CITY_KEY, null)
    }

    private fun getMinRentFilter() : Int {
        val sharedPreferences = context.getSharedPreferences(MY_FILTER_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(MY_FILTER_MIN_RENT_KEY, 0)
    }

    private fun getMaxRentFilter() : Int {
        val sharedPreferences = context.getSharedPreferences(MY_FILTER_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(MY_FILTER_MAX_RENT_KEY, 0)
    }

    companion object {
        private const val MY_FILTER_PREF_KEY = "FILTER_PREF_KEY"
        private const val MY_FILTER_CITY_KEY = "FILTER_CITY_KEY"
        private const val MY_FILTER_MIN_RENT_KEY = "FILTER_MIN_RENT_KEY"
        private const val MY_FILTER_MAX_RENT_KEY = "FILTER_MAX_RENT_KEY"
    }
}