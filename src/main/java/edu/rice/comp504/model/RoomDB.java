package edu.rice.comp504.model;

import edu.rice.comp504.model.chatroom.ChatRoom;
import edu.rice.comp504.model.chatroom.GroupChat;
import edu.rice.comp504.model.user.User;

import java.beans.PropertyChangeEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomDB {
    private static RoomDB ONLY;
    public int nextRoomID = 1;
    private final Map<String, ChatRoom> rooms = new ConcurrentHashMap<>();

    private RoomDB() {
    }

    public RoomDB getONLY() {
        return ONLY;
    }

    public static RoomDB make() {
        if (ONLY == null) {
            ONLY = new RoomDB();
        }
        return ONLY;
    }

    public Map<String, ChatRoom> getRooms() {
        return rooms;
    }

    /**
     * Create a group chat room method .
     *
     * @return true is the room is created
     */
    public boolean addGroupRoom(int duration, int userLimit, int roomId, String roomName, String type,
                                String ownerUsername, int adminLimit, boolean isPublic,
                                String roomPassword, String rules, String blockList) {
        if (rooms.containsKey(roomName)) return false;
        GroupChat temp = new GroupChat(duration, userLimit, roomId, roomName, type,
                ownerUsername, adminLimit, isPublic,
                roomPassword, rules, blockList);
        rooms.put(roomName, temp);
        return true;
    }

    public int getNextRoomID() {
        return this.nextRoomID ++;
    }
}
