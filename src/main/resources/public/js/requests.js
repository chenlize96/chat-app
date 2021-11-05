const getSendMsgRequest = (username, roomName, message) => ({
    username: username,
    //timeStamp: new Date().getTime(),
    roomName: roomName,
    message: message
});

const requests = {
    getSendMsgRequest
}
export {requests};