// ai/frontdesk/agent/listener/RoomEventListener.java
package ai.frontdesk.agent.listener;

import io.livekit.android.events.RoomEvent;

public interface RoomEventListener {
    void onRoomEvent(RoomEvent event);
}
