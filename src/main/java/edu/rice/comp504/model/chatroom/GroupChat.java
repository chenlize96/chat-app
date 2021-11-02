package edu.rice.comp504.model.chatroom;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A concrete class to represent multiple users chat room.
 */
public class GroupChat extends ChatRoom{

    // owner is the username of the owner
    private String owner;
    private int adminLimit;
    // adminList and userList are stored as ArrayList<String> with user's username
    private ArrayList<String> adminList;
    private ArrayList<String> userList;
    private int curNumUser;
    private boolean isPublic;
    private String roomPassword;
    private ArrayList<String> rules;
    private ArrayList<String> blockList;


    /**
     * A constructor for group chat room
     * @param duration The duration
     * @param userLimit The user limit
     * @param roomId The room id
     * @param roomName The room name
     * @param type The type of the room, group chat will always be groupchat
     * @param ownerUsername The username of first user
     * @param adminLimit The admin number limit
     * @param blockList The block list of usernames
     * @param isPublic If this room is public or not
     * @param roomPassword The room password
     * @param rules The rule string.
     */
    public GroupChat(int duration , int userLimit, int roomId, String roomName, String type,
                     String ownerUsername, int adminLimit, boolean isPublic,
                     String roomPassword, String rules, String blockList) {
        // Call super constructor
        super(duration, userLimit, roomId, roomName, "groupchat");
        // Initialize attributes
        this.owner = ownerUsername;
        this.adminLimit = adminLimit;
        this.curNumUser = 1;
        this.isPublic = isPublic;
        this.roomPassword = roomPassword;
        // Initialize list attributes
        this.adminList = new ArrayList<>();
        this.userList = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.blockList = new ArrayList<>();
        // Add elements to list attributes
        this.addToUserList(ownerUsername);
        this.addRules(rules);
        this.addToBlockList(blockList);
    }

    /**
     * Get admin list of this room.
     * @return The admin list
     */
    public ArrayList<String> getAdminList() {
        return adminList;
    }

    /**
     * Get block list of this room.
     * @return The block list
     */
    public ArrayList<String> getBlockList() {
        return blockList;
    }

    /**
     * Get all user list of this room
     * @return The user list.
     */
    public ArrayList<String> getUserList() {
        return userList;
    }

    /**
     * Get whether this room is public or not.
     * @return A boolean flag
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Get admin limit number.
     * @return The admin number limit
     */
    public int getAdminLimit() {
        return adminLimit;
    }

    /**
     * Get current number of all users.
     * @return The num of all users
     */
    public int getCurNumUser() {
        return curNumUser;
    }

    /**
     * Get central admin of this room (room owner).
     * @return The owner's username string
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Get room's password.
     * @return Room password string
     */
    public String getRoomPassword() {
        return roomPassword;
    }

    /**
     * Get room's rule as a list.
     * @return Room rules ArrayList
     */
    public ArrayList<String> getRules() {
        return rules;
    }

    /**
     * Set admin limit number.
     * @param adminLimit Admin limit int
     */
    public void setAdminLimit(int adminLimit) {
        this.adminLimit = adminLimit;
    }


    /**
     * Set number of users.
     * @param curNumUser Number of users.
     */
    public void setCurNumUser(int curNumUser) {
        this.curNumUser = curNumUser;
    }

    /**
     * Set this chat room's owner.
     * @param owner Owner's username
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Set current chat room to private.
     */
    public void setToPrivate() {
        this.isPublic = false;
    }

    /**
     * Set current chat room to public.
     */
    public void setToPublic() {
        this.isPublic = true;
    }

    /**
     * Set the password to this room.
     * @param roomPassword The room's password.
     */
    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    /**
     * Add rules to rule list.
     * @param rules The rules need to be added
     */
    public void addRules(String rules) {
        try {
            this.rules.addAll(Arrays.asList(rules.split(",")));
        } catch (StringIndexOutOfBoundsException sioobe) {
            System.out.println("IndexOutOfBoundsException, addRules failed!");
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, addRules failed!");
        }
    }

    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }

    /**
     * Add users to user list.
     * @param users The users as a string input like "aaa, bbb, ..."
     */
    public void addToUserList(String users) {
        try {
            List<String> toAdd = Arrays.asList(users.split(","));
            this.userList.addAll(toAdd);
            this.setCurNumUser(this.getCurNumUser() + toAdd.size());
        } catch (StringIndexOutOfBoundsException sioobe) {
            System.out.println("IndexOutOfBoundsException, addToBlockList failed!");
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, addToBlockList failed!");
        }
    }


    /**
     * Add admin to adminList.
     * @param admins The admin or admins as a string input like "aaa, bbb, ..."
     */
    public void addToAdminList(String admins) {
        try {
            this.adminList.addAll(Arrays.asList(admins.split(",")));
        } catch (StringIndexOutOfBoundsException sioobe) {
            System.out.println("IndexOutOfBoundsException, addToAdminList failed!");
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, addToAdminList failed!");
        }
    }

    /**
     * Add block users to blocklist.
     * @param blockUsers The block users as a string input like "aaa, bbb, ..."
     */
    public void addToBlockList(String blockUsers) {
        try {
            this.adminList.addAll(Arrays.asList(blockUsers.split(",")));
        } catch (StringIndexOutOfBoundsException sioobe) {
            System.out.println("IndexOutOfBoundsException, addToBlockList failed!");
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, addToBlockList failed!");
        }
    }


    /**
     * According to property change event's content to modified current chat room's instances.
     * @param evt The property change event
     */
    public void propertyChange(PropertyChangeEvent evt) {

    }



}
