'use strict';

const webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatapp");

/**
 * Entry point into chat room
 */
window.onload = function() {
    setUsername();
    $("#btn-logout").click(doLogOut);
    $(document).on("click", "#btn_createRoomSave", createGroupChat);
    $(document).on("click", "#btn_createRoomCancel", clearCreateForm);

};

/**
 * Set the username.
 */
function setUsername() {
    if ($("#user_name").val() == "") {
        console.log("set the username successfully");
        $("#user_name").text(localStorage.getItem("username"));
        $("#user_name").val(localStorage.getItem("username"));
    } else {
        console.log("do not reset the username");
    }
}

/**
 * Clear the create form.
 */
function clearCreateForm() {
    document.getElementById("new_room_name").value = "";
    document.getElementById("interest").value = "";
    document.getElementById("maxUser").value = 1;
    document.getElementById("password").value = "";
}

/**
 * Create a new group chat room.
 */
function createGroupChat() {
    $.post("/create/groupchat", {
        username: $("#user_name").val(),
        roomName: $("#new_room_name").val(),
        interest: $("#interest").val(), // only one interest for each room
        maxUser: $("#maxUser").val(),
        isPublic: $("#isPublic").is(':checked'),
        password: $("#password").val(),
    }, function (data) {
        if (data === true) { // the size of the roomList increases
            console.log(data);
            console.log("create room success");
            clearCreateForm();
            $("#createModal").modal('hide');
        } else {
            console.log("create room fail");
        }
    }, "json");
}

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


