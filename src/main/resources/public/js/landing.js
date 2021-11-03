'use strict';

window.onload = function() {
    clear();
    $('#registerForm').on('show.bs.modal', clearForm);
    $("#btn_log").click(doLogin);
    $(document).on("click", "#btn_register", doRegister);
    $(document).on("click", "#btn_clear", clearForm);
};

function doLogin(){
    $.post("/login", {username: $("#usernameLogin").val(),
        password: $("#passwordLogin").val()}, function (data) {
        console.log(data);
        // get a boolean - if true then submit, otherwise do not direct AND show error message
        if (data.username !== "null") {
            localStorage.setItem("username", $("#usernameLogin").val());
            document.getElementById("registerInfo").submit();
        }
    }, "json");
}

function doRegister() {
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
}


/* Lize - the code below does not have any function, but do not delete them */
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