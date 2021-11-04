'use strict';

let intervalID = -1;//id of current interval
const webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatapp");
const UserList = ["Owner", "Admin1", "Admin2", "Member1", "Member2"];
const RoomList = [{roomName: "COMP504", type: "public", limit: 200, cur: 180},
    {roomName: "Amazon", type: "public", limit: 150, cur: 70},
    {roomName: "Google", type: "public", limit: 100, cur: 50},
    {roomName: "Group D", type: "private", limit: 100, cur: 50},
    {roomName: "Amazon", type: "public", limit: 150, cur: 70}];
const notiList = [{sender: "qwe", receiver: "asd", roomName: "zxc"}];
/**
 * Entry point into chat room
 */
window.onload = function () {
    setUsername();
    loadRoomUser();
    clearInterval(intervalID);
    intervalID = setInterval(updateRoomList, 3000);
    let member_btn = $(".member_btn");
    $("#search_btn").click(doSearch);
    member_btn.focus(doSomething);
    $("#join_room").click(joinRooms);
    $("#chatStart").click(createUserChat);
    member_btn.blur(function (){
        console.log($(this).text());
        $('#btn-kick').attr('disabled',"true");
        $('#btn-mute').attr('disabled',"true");
    });
    $("#btn-join").click(getAllRooms);
    $("#btn-chat").click(getAllUsers);
    $("#notificationInfo").click(getNotification);
    $(".invite_ac").click(acceptInvite);
    $(".invite_rj").click(rejectInvite);
    $("#btn-logout").click(doLogOut);
    $(document).on("click", "#btn_createRoomSave", createGroupChat);
    $(document).on("click", "#btn_createRoomCancel", clearCreateForm);

};
/**
* click inner chat button to begin chat
* */
function createUserChat() {
    $.post("/create/userchat", {username: $("#user_name").val(), chatName: $("#chatWith").text()}, function (data) {
        console.log(data);
        if (data === true) {
            updateRoomList();
            $("#chatModal").modal('hide');
        }
        else {
            //notify
        }
    }, "json")
}
/**
 * Update the room list of the current user once per 3 seconds.
 */
function updateRoomList() {
    $.get("/room/update", {username: $("#user_name").val()}, function (data) {
        //console.log(data);
        //data = RoomList;
        // clear the room list first
        let rooms = $(".rooms");
        rooms.empty();
        // update the room list from the data
        let html = "";
        for(let i = 0; i< data.length; i++){
            html += "<span class=\"text\" id=\"btn-room1\" onclick=\"tempFunc()\">\n" +
                "                <button class=\"room_btn btn-outline-primary p-0 m-0 u_btn\">\n" +
                data[i].roomName +
                "                </button>\n" +
                "            </span><br>";
        }
        rooms.append(html);
    }, "json");
}
/**
* click invite notification accept
* */
function acceptInvite() {
    console.log($(this).siblings("div").children(".inviteSender").text(), $(this).siblings("div").children(".inviteRoomName").text());
    $.post("/notification/invite/accept", {receiver: $("#user_name").val(),
        sender: $(this).siblings("div").children(".inviteSender").text(),
        roomName: $(this).siblings("div").children(".inviteRoomName").text(),type: true}, function (data) {
        getNotification();
        updateRoomList();
    }, "json")
}
/**
* click invite notification reject
*/
function rejectInvite() {
    console.log($(this).siblings("div").children(".inviteSender").text(), $(this).siblings("div").children(".inviteRoomName").text());
    $.post("/notification/invite/reject", {receiver: $("#user_name").val(),
        sender: $(this).siblings("div").children(".inviteSender").text(),
        roomName: $(this).siblings("div").children(".inviteRoomName").text(), type: false}, function (data) {
        getNotification();
        updateRoomList();
    }, "json")
}
/**
* outside notification button
* */
function getNotification() {
    $.post("/user/notification", {username: $("#user_name").val()}, function (data) {
        let notifications = $("#notificationBody");
        //clear
        //get

    }, "json")
}
/**
 * inside join button
 * */
function joinRooms(){
    $.post("/join/group", {username: $("#user_name").val(), roomName: $("#roomSelection").text()}, function (data) {
        console.log(data);
        updateRoomList();
        $("#joinModal").modal('hide');
        if (data === true) {
            updateRoomList();
            $("#joinModal").modal('hide');
        }
        else {
            //notify
        }
    }, "json")
}
/**
 * outside chat button
 * */
function getAllUsers(){
    $.post("/chat/getUsers", {username: $("#user_name").val()}, function (data) {

        console.log(data);// list of room info about name, type, limit, num
        data = UserList;
        let userTable = $("#userTable");
        userTable.empty();
        $("#chatWith").text("");
        let html = "";
        for(let i = 0; i < data.length; i++){
            html += "<tr><th scope=\"row\"><input type=\"radio\" name=\"chat\"></th><td>" +
                data[i] + "</td></tr>";
        }
        userTable.append(html);
        $("input:radio[name='chat']").change(function (){
            let opt = $("input:radio[name='chat']:checked").parent("th").next("td").text();
            console.log(opt);
            $("#chatWith").text(opt);
            $("#chatStart").removeAttr("disabled");
        });
    }, "json");
}
/**
 * outside join button
 * */
function getAllRooms(){
    $.post("/join/getRooms", {username: $("#user_name").val()}, function (data) {

        console.log(data);// list of room info about name, type, limit, num
        //data = RoomList;
        let roomTable = $("#roomTable");
        $("#roomSelection").text("");
        roomTable.empty();
        let html = "";
        for(let i = 0; i < data.length; i++){
            html += "<tr><th scope=\"row\"><input type=\"radio\" name=\"join\"></th><td>" +
                data[i].roomName + "</td><td>" +(data[i].isPublic === true?"public": "private") +
                "</td><td>"+data[i].curNumUser+"/"+data[i].userLimit+"</td></tr>";
        }
        roomTable.append(html);
        $("input:radio[name='join']").change(function (){
            let opt = $("input:radio[name='join']:checked").parent("th").next("td").text();
            $("#roomSelection").text(opt);
            $("#join_room").removeAttr("disabled");
        });
    }, "json");
}

function loadRoomUser() {
    $(".members .member_btn").remove();
    let html = '';
    for(let i = 0; i < UserList.length; i++){
        html += "<button class=\"member_btn btn-outline-primary\" id='" + UserList[i] +"'>" + UserList[i] + "</button>";
    }
    $(".members").append(html);
}

function doSearch() {
    let sh_input = $("#search_input").val();
    console.log(sh_input);
    if(sh_input === ""){
        for(let i = 0; i < UserList.length; i++){
            $("#" + UserList[i]).show();
        }
    }
    else{
        for(let i = 0; i < UserList.length; i++){
            if(UserList[i].indexOf(sh_input) >= 0) $("#" + UserList[i]).show();
            else $("#" + UserList[i]).hide();
        }
    }
}

function doSomething(){
    let username = $("#user_name").text().replace(/[\r\n]/g,"").replace(/[ ]/g,"");
    console.log(username);
    if(username === "Owner" || username === "Admin1" || username === "Admin2"){

        let btn = $(this).text();
        if(btn === "Member1" || btn === "Member2"){
            $('#btn-kick').removeAttr("disabled");
            $('#btn-mute').removeAttr("disabled");
        }
    }
}

/**
 * Set the username from the landing page.
 */
function setUsername() {
    if ($("#user_name").val() == "") {
        console.log("set the username successfully");
        $("#user_name").text(localStorage.getItem("username"));
        $("#user_name").val(localStorage.getItem("username"));
        $.post("/userInfo", {username: $("#user_name").val()},function (data) {
            console.log(data);
            $("#age").text(data.age);
            $("#school").text(data.school);
            let newText = $("#interests").text();
            for (let i = 0; i < data.interests.length; i++) {
                if(i === data.interests.length - 1) newText += data.interests[i];
                else newText += data.interests[i] + ", ";
            }
            $("#interests").text(newText);
        });
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
        console.log(data);
        if (data === true) { // the size of the roomList increases
            console.log(data);
            console.log("create room success");
            clearCreateForm();
            $("#createModal").modal('hide');
            updateRoomList();
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
            window.location.href = "index.html"
        }
    }, "json");
}

/**
 * Send a message to the server.
 */
function sendMessage() {
    $.post("/sendMessage", {
        username: $("#user_name").val(),
        roomName:document.getElementById("title").innerText,
        message:$("#inputArea").val()
    }, function (data) {
        if(data === true) {
            console.log("ready to send");
            $('#inputArea').val('').focus();
        } else {
            console.log("will not be sent");
        }
        /*
        if (msg !== "") {
            webSocket.send(msg);
            $("#message").val("");
        }
        */
    }, "json");
}

/**
 * Press Enter button, then send the message.
 */
function onKeyPress(event) {
    event = event || window.event;
    if (event.which === 13) {
        event.preventDefault();
        event.returnValue = false;
        console.log("onKeyPress");
        sendMessage();
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
