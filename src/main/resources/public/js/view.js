'use strict';

const webSocket = new WebSocket("");

/**
 * Entry point into chat room
 */
window.onload = function() {

};



/**
 * Send a message to the server.
 * @param msg  The message to send to the server.
 */
function sendMessage(msg) {
    if (msg !== "") {
        webSocket.send(msg);
        $("#message").val("");
    }
}

/**
 * Enter a chatRoom
 */
function tempFunc() {
    document.getElementById("roomName").innerText = "Room 1"
}
function tempFunc2() {
    document.getElementById("roomName").innerText = "Room 2"
}
function tempFunc3() {
    document.getElementById("roomName").innerText = "Room 3"
}
function tempFunc4() {
    document.getElementById("roomName").innerText = "User 1"
}
function tempFunc5() {
    document.getElementById("roomName").innerText = "User 2"
}


