(function () {
    "use strict";


//code for adding a new address
    let addBtn = document.getElementById("addAddressBtn");
    let cancelBtn = document.getElementById("cancelButton");
    let newAddressDiv = document.getElementById("newAddressDiv")
    addBtn.addEventListener('click', function () {
        newAddressDiv.classList.remove("d-none");
    })
    cancelBtn.addEventListener('click', function (event) {
        event.preventDefault();
        newAddressDiv.classList.add("d-none");
    })

//code for top section and password input validation
//grabbing inputs
    let firstNameInput = document.getElementById("userFirstName");
    let lastNameInput = document.getElementById("userLastName");
    let usernameInput = document.getElementById("username");
    let emailInput = document.getElementById("userEmail");
    let phoneNumInput = document.getElementById("phone");
    let addressInput = document.getElementById("userAddress");
    let currentPasswordInput = document.getElementById("password");
    let newPasswordInput = document.getElementById("newPassword");
    let confirmPasswordInput = document.getElementById("confirmPassword");

//creating requirements
    const usernameRegex = /^[a-zA-Z0-9_]{1,30}$/;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const disAllowedRegex = /^[^"'()*+\-/:;<=>?[\]^`{|}~]*$/;
    const phoneRegex = /^[0-9]{10}$/;
    const passwordRegex = /^(?=.*[0-9])(?=.*[!@#$%&])[a-zA-Z0-9!@#$%&]{8,20}$/;
    const addressRegex = /^[a-zA-Z0-9, ]{1,50}$/; //TODO - we will need to check this against the API instead once we have it

//grabbing the feedback divs
    let firstNameFeedback = document.getElementById("firstNameFeedback");
    let lastNameFeedback = document.getElementById("lastNameFeedback");
    let usernameFeedback = document.getElementById("usernameFeedback");
    let emailFeedback = document.getElementById("emailFeedback");
    let phoneFeedback = document.getElementById("phoneFeedback");
    let addressFeedback = document.getElementById("addressFeedback");
    let passwordFeedback = document.getElementById("passwordFeedback");
    let confirmPasswordFeedback = document.getElementById("confirmPasswordFeedback");
    let currentPasswordFeedback = document.getElementById("currentPasswordFeedback");
    let profileUpdateStatus = document.getElementById("profileUpdateStatus");
    let passwordUpdateStatus = document.getElementById("passwordUpdateStatus");

//buttons and forms for submission
    let submitTopBtn = document.getElementById("saveChanges");
    let editForm = document.getElementById("editForm");
    let savePasswordBtn = document.getElementById("savePassword");
    let passwordForm = document.getElementById("passwordForm");

    // filestack
    let picker;
    let imgTag = document.getElementById("newPhotoTag");
    let uploadedPhotoURL = "";
    let userPhotoField = document.getElementById("userPhotoEdit");

    savePasswordBtn.addEventListener("click", function (e) {
        e.preventDefault();
        let validInput = checkPasswordInputs();

        if (validInput) {
            passwordForm.submit();
        }
    })

    if (usernameTaken == true) {
        usernameInput.classList.add("is-invalid")
        usernameFeedback.classList.add("invalid-feedback")
        usernameFeedback.innerText = "Username is taken.";
    }

    if (emailTaken == true) {
        emailInput.classList.add("is-invalid")
        emailFeedback.classList.add("invalid-feedback")
        emailFeedback.innerText = "Email is taken.";
    }

    if (incorrectPassword == true) {
        currentPasswordInput.classList.add("is-invalid")
        currentPasswordFeedback.classList.add("invalid-feedback")
        currentPasswordFeedback.innerText = "Password could not be verified.";
    }

    if (profileUpdated == true) {
        profileUpdateStatus.classList.remove("d-none");
    }

    if (passwordUpdated == true) {
        passwordUpdateStatus.classList.remove("d-none");
    }

    addTopSectionEventListeners();
    addPasswordEventListeners();

    submitTopBtn.addEventListener('click', function (e) {
        e.preventDefault();
        let validInput = checkTopInputs();

        if (validInput) {
            if(uploadedPhotoURL.trim() != ""){
                userPhotoField.value = uploadedPhotoURL;
            }
            editForm.submit();
        }
    })

    function addTopSectionEventListeners() {
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

        phoneNumInput.addEventListener("input", function () {
            checkPhone();
        });

        addressInput.addEventListener("input", function () {
            checkAddress();
        });

    }

    function addPasswordEventListeners() {
        newPasswordInput.addEventListener("input", function () {
            checkPassword()
        });

        confirmPasswordInput.addEventListener("input", function () {
            checkPasswordConfirm()
        });
    }

    function checkTopInputs() {
        let firstNameInput = checkFirstName();
        let lastNameInput = checkLastName();
        let usernameInput = checkUsername();
        let emailInput = checkEmail();
        let phoneNumberInput = checkPhone();
        let addressInput = checkAddress();

        return firstNameInput && lastNameInput && phoneNumberInput && addressInput && usernameInput && emailInput;
    }

    function checkPasswordInputs() {
        let passwordInput = checkPassword();
        let passwordConfirmInput = false;
        let currentPasswordInput = checkCurrentPassword();

        if (passwordInput) {
            passwordConfirmInput = checkPasswordConfirm();
        }

        return passwordInput && passwordConfirmInput && currentPasswordInput;
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
        if (!usernameRegex.test(usernameInput.value) || !disAllowedRegex.test(usernameInput.value)) {
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
        if (!emailRegex.test(emailInput.value) || !disAllowedRegex.test(emailInput.value)) {
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
            phoneFeedback.innerText = "Please enter a valid 10 digit phone number or leave phone number blank";
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

    function checkAddress() {
        let validInput = false;
        if (!addressRegex.test(addressInput.value) || !disAllowedRegex.test(addressInput.value)) {
            addressInput.classList.remove("is-valid")
            addressFeedback.classList.remove("valid-feedback")
            addressInput.classList.add("is-invalid")
            addressFeedback.classList.add("invalid-feedback")
            addressFeedback.innerText = "Please enter a valid address";
        } else {
            addressInput.classList.remove("is-invalid")
            addressFeedback.classList.remove("invalid-feedback")
            addressInput.classList.add("is-valid")
            addressFeedback.classList.add("valid-feedback")
            addressFeedback.innerText = "Looks good!";
            validInput = true;
        }
        return validInput;
    }


    function checkPassword() {
        let validInput = false;
        if (!passwordRegex.test(newPasswordInput.value) || !disAllowedRegex.test(newPasswordInput.value)) {
            newPasswordInput.classList.remove("is-valid")
            passwordFeedback.classList.remove("valid-feedback")
            newPasswordInput.classList.add("is-invalid")
            passwordFeedback.classList.add("invalid-feedback")
            passwordFeedback.innerText = "Passwords must be 8-10 characters long, contain at least 1 uppercase and 1 lower case letter, and a special symbol (!@#$%&)";
        } else {
            newPasswordInput.classList.remove("is-invalid")
            passwordFeedback.classList.remove("invalid-feedback")
            newPasswordInput.classList.add("is-valid")
            passwordFeedback.classList.add("valid-feedback")
            passwordFeedback.innerText = "Password meets complexity requirements.";
            validInput = true;
        }
        return validInput;
    }

//
    function checkPasswordConfirm() {
        let validInput = false;
        if (newPasswordInput.value !== confirmPasswordInput.value) {
            confirmPasswordInput.classList.remove("is-valid")
            confirmPasswordFeedback.classList.remove("valid-feedback")
            confirmPasswordInput.classList.add("is-invalid")
            confirmPasswordFeedback.classList.add("invalid-feedback")
            confirmPasswordFeedback.innerText = "Passwords do not match";
        } else {
            confirmPasswordInput.classList.remove("is-invalid")
            confirmPasswordFeedback.classList.remove("invalid-feedback")
            confirmPasswordInput.classList.add("is-valid")
            confirmPasswordFeedback.classList.add("valid-feedback")
            confirmPasswordFeedback.innerText = "Passwords match";
            validInput = true;
        }
        return validInput;
    }

    function checkCurrentPassword() {
        let validInput = false;
        if (currentPasswordInput.value.trim() == "") {
            currentPasswordInput.classList.remove("is-valid")
            currentPasswordFeedback.classList.remove("valid-feedback")
            currentPasswordInput.classList.add("is-invalid")
            currentPasswordFeedback.classList.add("invalid-feedback")
            currentPasswordFeedback.innerText = "Please enter your current password to save your changes";
        } else {
            currentPasswordInput.classList.remove("is-invalid")
            currentPasswordFeedback.classList.remove("invalid-feedback")
            currentPasswordFeedback.innerText = "";
            validInput = true;
        }
        return validInput;
    }

    //filestack
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


        document.getElementById("updatePicture").addEventListener("click", function (e) {
            e.preventDefault();
            picker = client.picker(options);
            client.picker(options).open();
        });


    });

})();