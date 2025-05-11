package ai.frontdesk.agent.datasource

import android.content.Context
import android.util.Log
import io.livekit.android.LiveKit
import io.livekit.android.ConnectOptions
import io.livekit.android.RoomOptions
import io.livekit.android.events.RoomEvent
import io.livekit.android.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ai.frontdesk.agent.listener.RoomEventListener
import io.livekit.android.events.collect

object LiveKitManager {
    private var room: Room? = null

    @JvmStatic
    fun connect(
        context: Context,
        url: String,
        token: String,
        listener: RoomEventListener
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val connectOptions = ConnectOptions(audio = true)
                val roomOptions = RoomOptions()

                room = LiveKit.create(context)
                room?.connect(url, token, connectOptions)

                room?.events?.collect { event ->
                    listener.onRoomEvent(event)
                }

                Log.d("LiveKitManager", "✅ Connected to room ${room?.name}")
            } catch (e: Exception) {
                Log.e("LiveKitManager", "❌ LiveKit connect failed", e)
            }
        }
    }

    @JvmStatic
    fun disconnect() {
        room?.disconnect()
        room = null
        Log.d("LiveKitManager", "🔌 Disconnected from LiveKit")
    }

    @JvmStatic
    fun isConnected(): Boolean {
        return room?.state == Room.State.CONNECTED
    }
}
