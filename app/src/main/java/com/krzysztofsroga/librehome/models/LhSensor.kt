package com.krzysztofsroga.librehome.models

import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.connection.DomoticzService

sealed class LhSensor(id: Int, name: String) : LhAbstractDevice(id, name) {

    override suspend fun sendState(service: DomoticzService) {}

    class LhMotionSensor(id: Int, name: String, override var state: String) : LhSensor(id, name), SimpleSensorData, SimpleName {
        override val icon: Int = R.drawable.ic_directions_run_black_24dp
    }

    class LhDoorLockSensor(id: Int, name: String, override var state: String) : LhSensor(id, name), SimpleSensorData, SimpleName {
        override val icon: Int = R.drawable.ic_lock_black_24dp
    }

    class LhSmokeDetector(id: Int, name: String, override var state: String) : LhSensor(id, name), SimpleSensorData, SimpleName {
        override val icon: Int = R.drawable.ic_whatshot_black_24dp
    }

    class LhMediaPlayerSensor(id: Int, name: String, override var state: String) : LhSensor(id, name), SimpleSensorData, SimpleName {
        override val icon: Int = R.drawable.ic_movie_black_24dp
    }

    class LhLuxSensor(id: Int, name: String, override var state: String) : LhSensor(id, name), SimpleSensorData, SimpleName {
        override val icon: Int = R.drawable.ic_wb_sunny_black_24dp
    }

    class LhTempSensor(id: Int, name: String, override var state: String) : LhSensor(id, name), SimpleSensorData, SimpleName {
        override val icon: Int = R.drawable.ic_beach_access_black_24dp
    }

    class LhGeneralSensor(id: Int, name: String, override var state: String) : LhSensor(id, name), SimpleSensorData, SimpleName {
        override val icon: Int = R.drawable.ic_developer_board_black_24dp
    }
}