'use strict';

const webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatapp");

/**
 * Entry point into chat room
 */
window.onload = function() {
    $("#user_name").text(localStorage.getItem("username"));
    $("#btn-logout").click(doLogOut);
};

/**
 * Logout the account, and redirect to the landing page.
 */
function doLogOut() {
    $.get("/logout", {username: $("#user_name").val()}, function (data) {
        // this username is the name on the top-left corner of main.html
        // in this case, controller can do something according to this username
        console.log(data);
        // it should be always true, just keep it for testing the controller
        if (data === true) {
            window.location.href ="index.html"
        }
    }, "json");
}

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
 * Press Enter button, then send the message.
 */
function onKeyPress(event) {
    event = event || window.event;
    if (event.keyCode == 13) {
        event.returnValue = false;
        $('#inputArea').val('').focus();
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


