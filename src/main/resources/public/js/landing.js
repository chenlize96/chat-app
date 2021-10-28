function logIn(){
    window.location.href="main.html";
}
function clear(){
    document.getElementById("userName").value="";
    document.getElementById("zipcode").value="";
    document.getElementById("email").value="";
    document.getElementById("phone").value="";
    document.getElementById("password").value="";
    document.getElementById("passwordSecond").value="";
}
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