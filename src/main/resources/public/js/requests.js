const getSendMsgRequest = (username, roomName, message) => ({
    username: username,
    //timeStamp: new Date().getTime(),
    roomName: roomName,
    message: message,
    action: "send"
});

const getLoginRequest = (username) => ({
    username: username,
    //timeStamp: new Date().getTime(),
    action: "login"
});

const getInviteRequest = (username, roomName, otherName) => ({
    userSendInvite: username,
    roomName: roomName,
    userGetInvite: otherName,
    //timeStamp: new Date().getTime(),
    action: "invite"
});

const getInviteUsersRequest = (username, roomName) => ({
    userSendInvite: username,
    roomName: roomName,
    //timeStamp: new Date().getTime(),
    action: "getInviteUsers"
});

const getMuteRequest = (roomName, otherName) => ({
    roomName: roomName,
    userMute: otherName,
    //timeStamp: new Date().getTime(),
    action: "mute"
});

const getKickRequest = (roomName, otherName) => ({
    roomName: roomName,
    userKick: otherName,
    //timeStamp: new Date().getTime(),
    action: "kick"
});

// do not do this
const getBlockRequest= (roomName, otherName) => ({
    roomName: roomName,
    userBlock: otherName,
    //timeStamp: new Date().getTime(),
    action: "block"
});

const getLeaveRequest = (username, roomName) => ({
    username: username,
    roomName: roomName,
    //timeStamp: new Date().getTime(),
    action: "leave"
});

const getUpdateMsg = (roomName) => ({
    roomName: roomName,
    //timeStamp: new Date().getTime(),
    action: "updateMessage"
});

const getLogoutRequest = (username) => ({
    username: username,
    //timeStamp: new Date().getTime(),
    action: "logout"
});

// do not use
const getRecallUpdateRequest = (msgId, roomName, newContext) => ({
    type: newContext ? "editMsg" : "recallMsg",
    timeStamp: new Date().getTime(),
    newContext,
    roomName,
    msgId
});

// do not use
const getDeleteRequest = (msgId, roomName) => ({
    type: "deleteMsg",
    roomName,
    msgId,
    timeStamp: new Date().getTime(),
});

const requests = {
    getSendMsgRequest,
    getLoginRequest,
    getInviteRequest,
    getMuteRequest,
    getKickRequest,
    getBlockRequest,
    getLeaveRequest,
    getUpdateMsg,
    getLogoutRequest,
    getDeleteRequest,
    getRecallUpdateRequest,
    getInviteUsersRequest
}

export {requests};