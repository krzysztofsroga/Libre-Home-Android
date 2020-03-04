package com.krzysztofsroga.librehome.connection

import android.util.Log
import com.krzysztofsroga.librehome.models.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OnlineSwitches(hostname: String) {

    private val service: DomoticzService = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://$hostname:${InternetConfiguration.defaultDomoticzPort}")
        .client(OkHttpClient().newBuilder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build())
        .build()
        .create(DomoticzService::class.java)

    suspend fun suspendGetAllSwitches(): List<LhDevice> {
        return service.getSwitches().result.map { LhDevice.fromDomoticzComponent(it) }
    }

    suspend fun getAllGroupScenes(): List<LhGroupScene> {
        return service.getGroups().result.map { LhGroupScene.fromDomoticzComponent(it) }
    }

    suspend fun sendComponentState(component: LhComponent) {
        component.sendState(service)
    }

    data class DomoticzResponse(val status: String, val title: String)

    data class DomoticzResponseComponents(val result: List<DomoticzComponent>)
}

