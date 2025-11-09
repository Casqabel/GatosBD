package com.example.catdeployer.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="Settings")
    val KEY_ORDER= booleanPreferencesKey("key_order")
class PreferenceStore(private val context: Context) {

    val orderFlow: Flow<Boolean> = context.dataStore.data.map{
        preferences ->
        preferences[KEY_ORDER]?: false
    }

    suspend fun saveOrder(order: Boolean){
        context.dataStore.edit{
            preferences ->
                preferences[KEY_ORDER]=order
            Log.d("store", "escribo un"+order.toString())

        }


    }
}