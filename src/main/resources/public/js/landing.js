'use strict';

window.onload = function() {
    clear();
    $('#registerForm').on('show.bs.modal', clearForm);
    $("#btn_log").click(doLogin);
    $(document).on("click", "#btn_register", doRegister);
    $(document).on("click", "#btn_clear", clearForm);
};

function doLogin(){
    checkLogin();
    if(($("#usernameLogin").val() !== "") && ($("#passwordLogin").val() !== "")) {
        $.post("/login", {username: $("#usernameLogin").val(),
            password: $("#passwordLogin").val()}, function (data) {
            console.log(data);
            // get a boolean - if true then submit, otherwise do not direct AND show error message
            if (data === true) {
                localStorage.setItem("username", $("#usernameLogin").val());
                document.getElementById("registerInfo").submit();
            }
        }, "json");
    }
}

function doRegister() {
    checkRegister();
    if(checkComplete()) {
        // assume front-end can compare the password with the password confirmation
        $.post("/register", {username: $("#username").val(), school: $("#school").val(),
            age: $("#age").val(), interests: $("#interests").val(),
            password: $("#password").val()}, function (data) {
            console.log(data);
            // if true then register successfully, otherwise there exists the same username
            if (data === true) {
                console.log("success");
                // close the form
                $("#registerForm").modal('hide');
            } else {
                console.log("fail");
                // show error messages on the form
            }
        }, "json");
    }

}

function clear() {
    document.getElementById("usernameLogin").value = "";
    document.getElementById("passwordLogin").value = "";
}

function clearForm() {
    document.getElementById("username").value = "";
    //document.getElementById("zipcode").value="";
    //document.getElementById("email").value="";
    //document.getElementById("phone").value="";
    document.getElementById("school").value = "";
    document.getElementById("age").value = "";
    document.getElementById("interests").value = "";
    document.getElementById("password").value = "";
    document.getElementById("passwordSecond").value = "";
    document.getElementById("usernameAlert").innerText = "";
    document.getElementById("schoolAlert").innerText = "";
    document.getElementById("ageAlert").innerText = "";
    document.getElementById("interestsAlert").innerText = "";
    document.getElementById("passwordAlert").innerText = "";
    document.getElementById("passwordSecondAlert").innerText = "";
}


function validateLoginUserName() {
    var a = document.getElementById("usernameLogin");
    var a2 = document.getElementById("usernameLoginAlert");
    if (a.value === "") {
        a2.innerText = "!";
        return false;
    }
    else {
        a2.innerText = "";
    }
    return true;
}

function validateLoginPassword() {
    var a = document.getElementById("passwordLogin");
    var a2 = document.getElementById("passwordLoginAlert");
    if (a.value === "") {
        a2.innerText = "!";
        return false;
    }
    else {
        a2.innerText = "";
    }
    return true;
}

function checkLogin() {
    validateLoginUserName();
    validateLoginPassword();
}

function validateRegisterUserName() {
    var a = document.getElementById("username");
    var a2 = document.getElementById("usernameAlert");
    if (a.value === "") {
        a2.innerText = "!";
        return false;
    }
    else {
        a2.innerText = ": )";
    }
    return true;
}

function validateRegisterSchool() {
    var a = document.getElementById("school");
    var a2 = document.getElementById("schoolAlert");
    if (a.value === "") {
        a2.innerText = "!";
        return false;
    }
    else {
        a2.innerText = ": )";
    }
    return true;
}

function validateRegisterAge() {
    var a = document.getElementById("age");
    var a2 = document.getElementById("ageAlert");
    if (a.value === "") {
        a2.innerText = "!";
        return false;
    }
    else if(isNaN(a.value)) {
        a2.innerText = "Age should be integer";
        document.getElementById("age").value = "";
        return false;
    }
    else if(parseInt(a.value,10) < 18 || parseInt(a.value,10) >= 110) {
        a2.innerText = "Age from 18 to 110, please";
        document.getElementById("age").value = "";
        return false;
    }
    else {
        a2.innerText = ": )";
    }
    return true;
}

function validateRegisterInterests() {
    var a = document.getElementById("interests");
    var a1 = document.getElementById("interestsInstr");
    var a2 = document.getElementById("interestsAlert");
    if (a.value === "") {
        a2.innerText = "!";
        return false;
    }
    else if(a.validity.patternMismatch && a.value !== "") {
        a1.innerText = "";
        a2.innerText = "Words split by comma";
    }
    else {
        a2.innerText = ": )";
    }
    return true;
}

function validateRegisterPassword() {
    var pass = document.getElementById("password");
    var pass2 = document.getElementById("passwordSecond");
    var alert = document.getElementById("passwordAlert");
    var alert2 = document.getElementById("passwordSecondAlert");
    if(pass.value === "") {
        alert.innerText = "!";
        return false;
    }
    if(pass2.value === "") {
        alert2.innerText = "!";
        return false;
    }
    if (pass.value !== pass2.value){
        alert.innerText = "";
        alert2.innerText = ">_<";
    }
    else {
        alert.innerText = ": )";
        alert2.innerText = ": )";
    }
    return true;
}

function checkComplete() {
    if($("#username").val() !== "" &&
        $("#school").val() !== "" &&
        $("#age").val() !== "" &&
        $("#interests").val() !== "" &&
        ($("#password").val() !== "" && $("#passwordSecond").val() !== "") &&
        ($("#password").val() === $("#passwordSecond").val()))
    {
        return true;
    }
}
function checkRegister() {
    validateRegisterUserName();
    validateRegisterSchool();
    validateRegisterAge();
    validateRegisterInterests();
    validateRegisterPassword();
}

/* Lize - the code below does not have any function, but do not delete them */
/*
function validateZipcode(){
    var a = document.getElementById("zipcode");
    var a2 = document.getElementById("zipcodeAlert");
    if(a.validity.patternMismatch && a.value != ""){
        a2.innerText="      Expect proper format is 5 digits";
        return false;
    }
    else {
        a2.innerText = "";
    }
    return true;
}

function validatePhone(){
    var a = document.getElementById("phone");
    var a2 = document.getElementById("phoneAlert");
    // console.log(a);
    if(a.validity.patternMismatch && a.value != ""){
        // console.log(a);
        a2.innerText="     Expect proper format is 1231231234";
        return false;
    }
    else
    {
        a2.innerText = "";
    }
    return true;
}

function validateEmail(){
    var a = document.getElementById("email");
    var a2 = document.getElementById("emailAlert");
    if(a.validity.patternMismatch && a.value != ""){
        a2.innerText="      Expect to have @ inside the email";
        return false;
    }
    {
        a2.innerText =""
    }
    return true;
}

function validatePassword(){
    var pass = document.getElementById("password");
    var pass2 = document.getElementById("passwordSecond");
    var pass3 = document.getElementById("passwordAlert");
    if (pass.value != pass2.value && pass != ""){
        pass3.innerText="     The password is not matched";
        return false;
    }
    else {
        pass3.innerText = "";
    }
    return true;
}

function updateInfo(){
    validateZipcode();
    validatePhone();
    validateEmail();
    validatePassword();
    clear();
}
*/
