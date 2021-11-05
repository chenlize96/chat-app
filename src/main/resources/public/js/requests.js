const getSendMsgRequest = (username, roomName, message) => ({
    username: username,
    //timeStamp: new Date().getTime(),
    roomName: roomName,
    message: message,
    action: "send"
});

const getSendLoginRequest = (username) => ({
    username: username,
    //timeStamp: new Date().getTime(),
    action: "login"
});


const requests = {
    getSendMsgRequest,
    getSendLoginRequest
}
export {requests};