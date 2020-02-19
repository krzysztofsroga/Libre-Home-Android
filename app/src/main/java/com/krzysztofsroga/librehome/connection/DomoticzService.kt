package com.krzysztofsroga.librehome.connection

import com.krzysztofsroga.librehome.models.DomoticzSwitches
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface DomoticzService {
    @Headers("Content-Type: application/json")
    @GET("json.htm?type=devices&filter=lights&used=true&order=Name")
    suspend fun getSwitches(): DomoticzSwitches

    @Headers("Content-Type: application/json")
    @GET("json.htm?type=command&param=switchlight")
    suspend fun sendSwitchState(@Query("idx") lightSwitchId: Int?, @Query("switchcmd", encoded = true) switchCmd: String, @Query("level") level : Int?): OnlineSwitches.DomoticzResponse //TODO ID should not be null
}