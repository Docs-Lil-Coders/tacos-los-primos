(function () {
    "use strict";

    let firstNameInput = document.getElementById("userFirstName");
    let lastNameInput = document.getElementById("userLastName");
    let usernameInput = document.getElementById("username");
    let emailInput = document.getElementById("userEmail");
    let passwordInput = document.getElementById("userPassword");
    let passwordConfirmInput = document.getElementById("confirmUserPassword");
    let phoneNumInput = document.getElementById("phone");

    const usernameRegex = /^[a-zA-Z0-9_]{1,30}$/;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const passwordRegex = /^(?=.*[0-9])(?=.*[!@#$%&])[a-zA-Z0-9!@#$%&]{8,20}$/;
    const disAllowedRegex = /^[^"'()*+\-/:;<=>?[\]^`{|}~]*$/;
    const phoneRegex = /^[0-9]{10}$/;


    let firstNameFeedback = document.getElementById("firstNameFeedback");
    let lastNameFeedback = document.getElementById("lastNameFeedback");
    let usernameFeedback = document.getElementById("usernameFeedback");
    let emailFeedback = document.getElementById("emailFeedback");
    let phoneFeedback = document.getElementById("phoneFeedback");
    let passwordFeedback = document.getElementById("passwordFeedback");
    let confirmPasswordFeedback = document.getElementById("confirmPasswordFeedback");

    //address fields
    let streetAddressInput = document.getElementById("streetAddress");
    let streetAddressFeedback = document.getElementById("streetFeedback");
    let buildingAddressInput = document.getElementById("buildingAddress");
    let buildingAddressFeedback = document.getElementById("buildingFeedback");
    let cityInput = document.getElementById("cityAddress");
    let cityFeedback = document.getElementById("cityFeedback");
    let stateInput = document.getElementById("stateAddress");
    let stateFeedback = document.getElementById("stateFeedback");
    let zipCodeInput = document.getElementById("zipCodeAddress");
    let zipCodeFeedback = document.getElementById("zipCodeFeedback");
    const zipCodeRegex = /^[0-9]{5}$/;

    let submitBtn = document.getElementById("createAccount");
    let form = document.getElementById("registerForm");

    // filestack
    let picker;
    let imgTag = document.getElementById("img");
    let uploadedPhotoURL = "";
    let userPhotoField = document.getElementById("userPhoto");

    if(usernameTaken == true) {
        usernameInput.classList.add("is-invalid")
        usernameFeedback.classList.add("invalid-feedback")
        usernameFeedback.innerText = "Username is taken.";

    }

    if(emailTaken == true) {
        emailInput.classList.add("is-invalid")
        emailFeedback.classList.add("invalid-feedback")
        emailFeedback.innerText = "Email is taken.";

    }

    addEventListeners();

    submitBtn.addEventListener('click', function (e) {
        e.preventDefault();
        let validInput = checkInputs();

        if (validInput) {
            if(uploadedPhotoURL.trim() == ""){
                userPhotoField.value = "https://cdn.filestackcontent.com/kSmq62MpTRCUZgm0CRni"
            } else {
                userPhotoField.value = uploadedPhotoURL;
            }
            form.submit();
        }
    })

    function addEventListeners() {
        firstNameInput.addEventListener("input", function () {
            checkFirstName();
        });

        lastNameInput.addEventListener("input", function () {
            checkLastName();
        });

        usernameInput.addEventListener("input", function () {
            checkUsername();
        });

        emailInput.addEventListener("input", function () {
            checkEmail();
        });

        passwordInput.addEventListener("input", function () {
            checkPassword();
        });

        passwordConfirmInput.addEventListener("input", function () {
            checkPasswordConfirm();
        });

        phoneNumInput.addEventListener("input", function () {
            checkPhone();
        });

        zipCodeInput.addEventListener('input', function(){
            checkZipCode();
        })

        streetAddressInput.addEventListener('input', function(){
            checkStreet();
        });

        buildingAddressInput.addEventListener('input', function(){
            checkBuilding()
        })

        cityInput.addEventListener('input', function(){
            checkCity();
        });

        stateInput.addEventListener('change', function(){
            checkState()
        })


    }
    function checkInputs() {
        let firstNameInput = checkFirstName();
        let lastNameInput = checkLastName();
        let usernameInput = checkUsername();
        let emailInput = checkEmail();
        let phoneNumberInput = checkPhone();
       let addressOK = checkAddressFields();
        let passwordInput = checkPassword();
        let passwordConfirmInput = false;

        if (passwordInput) {
            passwordConfirmInput = checkPasswordConfirm();
        }
        return firstNameInput && lastNameInput && phoneNumberInput && addressOK && usernameInput && emailInput && passwordInput && passwordConfirmInput;
    }
    function checkFirstName() {
        let validInput = false;
        if (firstNameInput.value.trim() == "") {
            firstNameInput.classList.remove("is-valid")
            firstNameFeedback.classList.remove("valid-feedback")
            firstNameInput.classList.add("is-invalid")
            firstNameFeedback.classList.add("invalid-feedback")
            firstNameFeedback.innerText = "First name cannot be left blank";
        } else {
            firstNameInput.classList.remove("is-invalid")
            firstNameFeedback.classList.remove("invalid-feedback")
            firstNameInput.classList.add("is-valid")
            firstNameFeedback.classList.add("valid-feedback")
            firstNameFeedback.innerText = "Looks good!";
            validInput = true;
        }
        return validInput;
    }
    function checkLastName() {
        let validInput = false;
        if (lastNameInput.value.trim() == "") {
            lastNameInput.classList.remove("is-valid")
            lastNameFeedback.classList.remove("valid-feedback")
            lastNameInput.classList.add("is-invalid")
            lastNameFeedback.classList.add("invalid-feedback")
            lastNameFeedback.innerText = "Last name cannot be left blank";
        } else {
            lastNameInput.classList.remove("is-invalid")
            lastNameFeedback.classList.remove("invalid-feedback")
            lastNameInput.classList.add("is-valid")
            lastNameFeedback.classList.add("valid-feedback")
            lastNameFeedback.innerText = "Looks good!";
            validInput = true;
        }
        return validInput;
    }
    function checkUsername() {
        let validInput = false;
        if (usernameInput.value.trim() === "") {
            usernameInput.classList.remove("is-valid")
            usernameFeedback.classList.remove("valid-feedback")
            usernameInput.classList.add("is-invalid")
            usernameFeedback.classList.add("invalid-feedback")
            usernameFeedback.innerText = "Username cannot be left blank";
        } else if (!usernameRegex.test(usernameInput.value) || !disAllowedRegex.test(usernameInput.value)) {
            usernameInput.classList.remove("is-valid")
            usernameFeedback.classList.remove("valid-feedback")
            usernameInput.classList.add("is-invalid")
            usernameFeedback.classList.add("invalid-feedback")
            usernameFeedback.innerText = "username should contain only letters, numbers, and underscores.";
        } else {
            usernameInput.classList.remove("is-invalid")
            usernameFeedback.classList.remove("invalid-feedback")
            usernameInput.classList.add("is-valid")
            usernameFeedback.classList.add("valid-feedback")
            usernameFeedback.innerText = "Looks good!";
            validInput = true;
        }
        return validInput;
    }
    function checkEmail() {
        let validInput = false;
        if (emailInput.value.trim() === "") {
            emailInput.classList.remove("is-valid")
            emailFeedback.classList.remove("valid-feedback")
            emailInput.classList.add("is-invalid")
            emailFeedback.classList.add("invalid-feedback")
            emailFeedback.innerText = "Email cannot be left balnk ";
        } else if (!emailRegex.test(emailInput.value) || !disAllowedRegex.test(emailInput.value)) {
            emailInput.classList.remove("is-valid")
            emailFeedback.classList.remove("valid-feedback")
            emailInput.classList.add("is-invalid")
            emailFeedback.classList.add("invalid-feedback")
            emailFeedback.innerText = "Emails should be in correct email format (xxx@xxx.xxx) ";
        } else {
            emailInput.classList.remove("is-invalid")
            emailFeedback.classList.remove("invalid-feedback")
            emailInput.classList.add("is-valid")
            emailFeedback.classList.add("valid-feedback")
            emailFeedback.innerText = "Looks good!";
            validInput = true;
        }
        return validInput;
    }
    function checkPassword() {
        let validInput = false;
        if (passwordInput.value.trim() === "") {
            passwordInput.classList.remove("is-valid")
            passwordFeedback.classList.remove("valid-feedback")
            passwordInput.classList.add("is-invalid")
            passwordFeedback.classList.add("invalid-feedback")
            passwordFeedback.innerText = "Password cannot be left blank";
        } else if (!passwordRegex.test(passwordInput.value) || !disAllowedRegex.test(passwordInput.value)) {
            passwordInput.classList.remove("is-valid")
            passwordFeedback.classList.remove("valid-feedback")
            passwordInput.classList.add("is-invalid")
            passwordFeedback.classList.add("invalid-feedback")
            passwordFeedback.innerText = "Passwords must be 8-10 characters long, contain at least 1 uppercase and 1 lower case letter, and a special symbol (!@#$%&)";
        } else {
            passwordInput.classList.remove("is-invalid")
            passwordFeedback.classList.remove("invalid-feedback")
            passwordInput.classList.add("is-valid")
            passwordFeedback.classList.add("valid-feedback")
            passwordFeedback.innerText = "Looks Good!";
            validInput = true;
        }
        return validInput;
    }
    function checkPasswordConfirm() {
        let validInput = false;
        if (passwordInput.value !== passwordConfirmInput.value) {
            passwordConfirmInput.classList.remove("is-valid")
            confirmPasswordFeedback.classList.remove("valid-feedback")
            passwordConfirmInput.classList.add("is-invalid")
            confirmPasswordFeedback.classList.add("invalid-feedback")
            confirmPasswordFeedback.innerText = "Passwords do not match";
        } else {
            passwordConfirmInput.classList.remove("is-invalid")
            confirmPasswordFeedback.classList.remove("invalid-feedback")
            passwordConfirmInput.classList.add("is-valid")
            confirmPasswordFeedback.classList.add("valid-feedback")
            confirmPasswordFeedback.innerText = "Passwords match";
            validInput = true;
        }
        return validInput;
    }
    function checkPhone() {
        let validInput = false;
        if (phoneNumInput.value == "") {
            phoneNumInput.classList.remove("is-invalid")
            phoneFeedback.classList.remove("invalid-feedback")
            phoneNumInput.classList.remove("is-valid")
            phoneFeedback.classList.remove("valid-feedback")
            phoneFeedback.innerText = "";
            validInput = true;
        } else if (!phoneRegex.test(phoneNumInput.value) || !disAllowedRegex.test(phoneNumInput.value)) {
            phoneNumInput.classList.remove("is-valid")
            phoneFeedback.classList.remove("valid-feedback")
            phoneNumInput.classList.add("is-invalid")
            phoneFeedback.classList.add("invalid-feedback")
            phoneFeedback.innerText = "Please enter a valid 10 digit phone number or leave blank";
        } else {
            phoneNumInput.classList.remove("is-invalid")
            phoneFeedback.classList.remove("invalid-feedback")
            phoneNumInput.classList.add("is-valid")
            phoneFeedback.classList.add("valid-feedback")
            phoneFeedback.innerText = "Looks good!";
            validInput = true;
        }
        return validInput;
    }


    function checkStreet() {
        let validInput = false;
        if (streetAddressInput.value.trim() === "") {
            streetAddressInput.classList.remove("is-valid")
            streetAddressFeedback.classList.remove("valid-feedback")
            streetAddressInput.classList.add("is-invalid")
            streetAddressFeedback.classList.add("invalid-feedback")
            streetAddressFeedback.innerText = "Street cannot be left blank.";
        }  else {
            streetAddressInput.classList.remove("is-invalid")
            streetAddressFeedback.classList.remove("invalid-feedback")
            streetAddressInput.classList.add("is-valid")
            streetAddressFeedback.classList.add("valid-feedback")
            streetAddressFeedback.innerText = "";
            validInput = true;
        }
        return validInput;
    }
    function checkBuilding() {
        let validInput = false;
        if (buildingAddressInput.value.trim() === "") {
            buildingAddressInput.classList.remove("is-invalid")
            buildingAddressFeedback.classList.remove("invalid-feedback")
            buildingAddressInput.classList.add("is-valid")
            buildingAddressFeedback.classList.add("valid-feedback")
            buildingAddressFeedback.innerText = "";
            validInput = true;
        }  else {
            buildingAddressInput.classList.remove("is-invalid")
            buildingAddressFeedback.classList.remove("invalid-feedback")
            buildingAddressInput.classList.add("is-valid")
            buildingAddressFeedback.classList.add("valid-feedback")
            buildingAddressFeedback.innerText = "";
            validInput = true;
        }
        return validInput;
    }
    function checkCity() {
        let validInput = false;
        if (cityInput.value.trim() === "") {
            cityInput.classList.remove("is-valid")
            cityFeedback.classList.remove("valid-feedback")
            cityInput.classList.add("is-invalid")
            cityFeedback.classList.add("invalid-feedback")
            cityFeedback.innerText = "City cannot be left blank.";
        }  else {
            cityInput.classList.remove("is-invalid")
            cityFeedback.classList.remove("invalid-feedback")
            cityInput.classList.add("is-valid")
            cityFeedback.classList.add("valid-feedback")
            cityFeedback.innerText = "";
            validInput = true;
        }
        return validInput;
    }
    function checkState() {
        let validInput = false;
        const selectedState = stateInput.value.trim();

        if (selectedState === "") {
            stateInput.classList.remove("is-valid");
            stateFeedback.classList.remove("valid-feedback");
            stateInput.classList.add("is-invalid");
            stateFeedback.classList.add("invalid-feedback");
            stateFeedback.innerText = "Please select a state.";
        } else {
            stateInput.classList.remove("is-invalid");
            stateFeedback.classList.remove("invalid-feedback");
            stateInput.classList.add("is-valid");
            stateFeedback.classList.add("valid-feedback");
            stateFeedback.innerText = "";
            validInput = true;
        }

        return validInput;
    }
    function checkZipCode() {
        let validInput = false;
        if (zipCodeInput.value.trim() === "") {
            zipCodeInput.classList.remove("is-valid")
            zipCodeFeedback.classList.remove("valid-feedback")
            zipCodeInput.classList.add("is-invalid")
            zipCodeFeedback.classList.add("invalid-feedback")
            zipCodeFeedback.innerText = "Zip Code cannot be left blank";
        } else if (!zipCodeRegex.test(zipCodeInput.value) || !disAllowedRegex.test(zipCodeInput.value)) {
            zipCodeInput.classList.remove("is-valid")
            zipCodeFeedback.classList.remove("valid-feedback")
            zipCodeInput.classList.add("is-invalid")
            zipCodeFeedback.classList.add("invalid-feedback")
            zipCodeFeedback.innerText = "Please enter a valid 5 digit zip code.";
        } else {
            zipCodeInput.classList.remove("is-invalid")
            zipCodeFeedback.classList.remove("invalid-feedback")
            zipCodeInput.classList.add("is-valid")
            zipCodeFeedback.classList.add("valid-feedback")
            zipCodeFeedback.innerText = "";
            validInput = true;
        }
        return validInput;
    }
    function checkAddressFields(){
        let streetOK = checkStreet();
        let cityOK = checkCity();
        let buildingOK = checkBuilding();
        let stateOK = checkState();
        let zipCodeOK = checkZipCode();

        return stateOK && streetOK && cityOK && buildingOK && zipCodeOK;
    }





    window.addEventListener('DOMContentLoaded', function () {
        const client = filestack.init(fileStackKey);

        const options = {
            accept: ["image/*"],
            onUploadDone: (res) => {
                const url = res.filesUploaded[0].url;
                uploadedPhotoURL = url;
                imgTag.src = url;
            },

        };


        document.getElementById("uploadPicture").addEventListener("click", function (e) {
            e.preventDefault();
            picker = client.picker(options);
            client.picker(options).open();
        });


    });

})();