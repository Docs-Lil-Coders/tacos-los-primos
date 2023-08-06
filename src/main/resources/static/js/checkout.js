(function () {
    "use strict";

    document.addEventListener("DOMContentLoaded", async function () {

        let stripe = Stripe(stripeKey, {
            apiVersion: '2020-08-27',
        });

        const {
            error: backendError,
            clientSecret
        } = await fetch('/stripe-token').then(r => r.json());
        if (backendError) {
            console.log(backendError.message);
        }
        console.log(`Client secret returned.`);

        const elements = stripe.elements({clientSecret});
        const paymentElement = elements.create('payment');
        paymentElement.mount('#payment-element');
    });

    const prevBtns = document.querySelectorAll(".btn-prev");
    const nextBtns = document.querySelectorAll(".btn-next");
    const progress = document.getElementById("progress");
    const formSteps = document.querySelectorAll(".form-step");
    const progressSteps = document.querySelectorAll(".progress-step");

    //all these are for checking that the user entered the correct and required info
    let selectedOrderType = "";
    let providedEmail;
    let orderTypes = document.getElementsByName("orderType");
    let orderTypeFeedback = document.getElementById("orderTypeFeedback");
    let emailInput = document.getElementById("userEmail");
    let phoneInput = document.getElementById("userPhone");
    let emailFeedback = document.getElementById("emailFeedback");
    let phoneFeedback = document.getElementById("phoneFeedback");
    let emailConfirmation = document.getElementById("emailConfirmation");
    let firstNameInput = document.getElementById("userFirstName");
    let lastNameInput = document.getElementById("userLastName");
    let firstNameFeedback = document.getElementById("firstNameFeedback");
    let lastNameFeedback = document.getElementById("lastNameFeedback");
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const disAllowedRegex = /^[^"'()*+\-/:;<=>?[\]^`{|}~]*$/;
    const phoneRegex = /^[0-9]{10}$/;

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
    let userAddressMessage = document.getElementById("userAddressMessage");

    addContactSheetEventListeners();

    let changeAddressDivs = document.querySelectorAll(".changeAddressDiv");

    for(let i = 0; i < changeAddressDivs.length; i ++){
        changeAddressDivs[i].addEventListener("click", function(event){
            const clickedDivId = event.currentTarget.id;
            let id = clickedDivId.toString().substring(13);
            fillAddressValues(id);
        //     document.getElementById('addressModal').style.display = 'none';
        //     document.querySelector('.modal-backdrop').style.display = 'none';
         })
    }

    // let changeAddressButton = document.getElementById("changeAddressButton");
    // changeAddressButton.addEventListener("click", function(){
    //     document.getElementById('addressModal').style.display = 'block';
    //     document.querySelector('.modal-backdrop').style.display = 'block';
    // })

    function fillAddressValues(id){
        const jsonResponse = JSON.parse(addressesJSON);
        for (let i = 0; i < jsonResponse.length; i++) {
            let currentAddress = jsonResponse[i]

            if(currentAddress[0] == id) {
                streetAddressInput.value = currentAddress[5];
                buildingAddressInput.value = currentAddress[1];
                cityInput.value = currentAddress[2];
                stateInput.value = currentAddress[4];
                zipCodeInput.value = currentAddress[6];
            }
        }

    }




    let formStepsNum = 0;

    nextBtns.forEach((btn) => {
        btn.addEventListener("click", async () => {
            resetFeedbackFields();
            if (formStepsNum === 0 && (checkOrderType() === false)) {
                orderTypeFeedback.classList.remove("d-none");
            } else if (formStepsNum === 1) {
                const contactInfoValid = await checkContactInformation();
                if (contactInfoValid === false) {
                    console.log("contact info not okay");
                    return; // Stop execution here if the contact information is not valid
                } else {
                    orderTypeFeedback.classList.add("d-none");
                    fillChargesDivs();
                    formStepsNum++;
                    updateFormSteps();
                    updateProgressbar();
                }
            } else {
                orderTypeFeedback.classList.add("d-none");
                fillChargesDivs();
                formStepsNum++;
                updateFormSteps();
                updateProgressbar();
            }

        });
    });

    prevBtns.forEach((btn) => {
        btn.addEventListener("click", () => {
            formStepsNum--;
            updateFormSteps();
            updateProgressbar();
        });
    });

    function updateFormSteps() {
        formSteps.forEach((formStep) => {
            formStep.classList.contains("form-step-active") &&
            formStep.classList.remove("form-step-active");
        });

        formSteps[formStepsNum].classList.add("form-step-active");
    }

    function updateProgressbar() {
        progressSteps.forEach((progressStep, idx) => {
            if (idx < formStepsNum + 1) {
                progressStep.classList.add("progress-step-active");
            } else {
                progressStep.classList.remove("progress-step-active");
            }
        });

        const progressActive = document.querySelectorAll(".progress-step-active");

        progress.style.width =
            ((progressActive.length - 1) / (progressSteps.length - 1)) * 100 + "%";
    }

    function resetFeedbackFields() {
      resetAddressFields();
        phoneInput.classList.remove("is-invalid")
        phoneFeedback.classList.remove("invalid-feedback")
        phoneFeedback.innerText = "";
        emailInput.classList.remove("is-invalid")
        emailFeedback.classList.remove("invalid-feedback")
        emailFeedback.innerText = "";
    }

    function resetAddressFields(){
        streetAddressInput.classList.remove("is-invalid")
        streetAddressFeedback.classList.remove("invalid-feedback")
        streetAddressFeedback.innerText = "";
        buildingAddressInput.classList.remove("is-invalid")
        buildingAddressFeedback.classList.remove("invalid-feedback")
        buildingAddressFeedback.innerText = "";
        cityInput.classList.remove("is-invalid")
        cityFeedback.classList.remove("invalid-feedback")
        cityFeedback.innerText = "";
        stateInput.classList.remove("is-invalid")
        stateFeedback.classList.remove("invalid-feedback")
        stateFeedback.innerText = "";
        zipCodeInput.classList.remove("is-invalid")
        zipCodeFeedback.classList.remove("invalid-feedback")
        zipCodeFeedback.innerText = "";
    }


    function geocode(address, token) {
        var baseUrl = 'https://api.mapbox.com';
        var endPoint = '/geocoding/v5/mapbox.places/';

        return fetch(baseUrl + endPoint + encodeURIComponent(address) + '.json' + "?" + 'access_token=' + token)
            .then(function (res) {
                return res.json();
                // to get all the data from the request, comment out the following three lines...
            })
            .then(function (data) {
                return new Promise((resolve, reject) => {
                    resolve(data.features[0].center);
                    if (data.features.length > 0) {
                        resolve(data.features[0].center);
                    } else {
                        reject(new Error('Location not found'));
                    }
                });
            });
    }

    async function addressExists(input) {
        try {
            let data = await geocode(input, mapBoxKey);

            if (data) {
                return true;
                // Your existing code to calculate distance and show the results.
            } else {
                return false;
            }
        } catch (error) {
            return false;
        }
    }

    async function addressIsCloseEnough(input) {
        try {
            let data = await geocode(input, mapBoxKey);

            //getting the lat and lon from the user input
            let lat1 = data[1];
            let lon1 = data[0];

            //this is the lat and long of the restaurant
            let lat2 = 33.480222919384744;
            let lon2 = -112.18853950371074;

            let distance = getDistanceFromLatLonInKm(lat1, lon1, lat2, lon2);

            if (distance > 16) {
                return false;
            } else {
                 return true;
            }
        } catch (error) {
             return false;
        }

    }

    function getDistanceFromLatLonInKm(lat1, lon1, lat2, lon2) {
        var R = 6371; // Radius of the earth in km
        var dLat = deg2rad(lat2 - lat1);  // deg2rad below
        var dLon = deg2rad(lon2 - lon1);
        var a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2)
        ;
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        var d = R * c; // Distance in km
        return d;
    }

    function deg2rad(deg) {
        return deg * (Math.PI / 180)
    }

    function checkPhone() {
        let validInput = false;
        if (phoneInput.value.trim() === "") {
            phoneInput.classList.remove("is-invalid")
            phoneFeedback.classList.remove("invalid-feedback")
            phoneInput.classList.remove("is-valid")
            phoneFeedback.classList.remove("valid-feedback")
            phoneFeedback.innerText = "";
            validInput = true;
        } else if (!phoneRegex.test(phoneInput.value) || !disAllowedRegex.test(phoneInput.value)) {
            phoneInput.classList.remove("is-valid")
            phoneFeedback.classList.remove("valid-feedback")
            phoneInput.classList.add("is-invalid")
            phoneFeedback.classList.add("invalid-feedback")
            phoneFeedback.innerText = "Please ensure phone is a valid ten digit number";
        } else {
            phoneInput.classList.remove("is-invalid")
            phoneFeedback.classList.remove("invalid-feedback")
            phoneInput.classList.add("is-valid")
            phoneFeedback.classList.add("valid-feedback")
            phoneFeedback.innerText = "";
            validInput = true;
        }
        return validInput;
    }

    function checkEmail() {
        let validInput = false;
        if (!emailConfirmation.checked) {
            emailInput.classList.remove("is-invalid")
            emailFeedback.classList.remove("invalid-feedback")
            emailInput.classList.remove("is-valid")
            emailFeedback.classList.remove("valid-feedback")
            emailFeedback.innerText = "";
            validInput = true;
        } else if (!emailRegex.test(emailInput.value) || !disAllowedRegex.test(emailInput.value)) {
            emailInput.classList.remove("is-valid")
            emailFeedback.classList.remove("valid-feedback")
            emailInput.classList.add("is-invalid")
            emailFeedback.classList.add("invalid-feedback")
            emailFeedback.innerText = "Valid email is required for email confirmation (xxx@xxx.xxx)";
        } else {
            emailInput.classList.remove("is-invalid")
            emailFeedback.classList.remove("invalid-feedback")
            emailInput.classList.add("is-valid")
            emailFeedback.classList.add("valid-feedback")
            emailFeedback.innerText = "";
            validInput = true;
        }
        providedEmail = emailInput.value;
        return validInput;
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
            firstNameFeedback.innerText = "";
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
            lastNameFeedback.innerText = "";
            validInput = true;
        }
        return validInput;
    }

    function checkOrderType() {
        let checkedValue = null;
        for (let radio of orderTypes) {
            if (radio.checked) {
                checkedValue = radio.value;
                break;
            }
        }

        if (checkedValue !== null) {
            selectedOrderType = checkedValue;
            return true;
        } else {
            return false;
        }
    }

    async function checkContactInformation() {
    let addressOK = await checkAddress();
        let emailOK = checkEmail();
        let phoneOK = checkPhone();
        let firstNameOK = checkFirstName();
        let lastNameOK = checkLastName();

        return emailOK && phoneOK && firstNameOK && lastNameOK && addressOK;
    }

    function addContactSheetEventListeners() {
        emailConfirmation.addEventListener("change", function () {
            checkEmail();
        });



        firstNameInput.addEventListener("input", function () {
            checkFirstName();
        });

        lastNameInput.addEventListener("input", function () {
            checkLastName();
        });

        emailInput.addEventListener("input", function () {
            checkEmail();
        });

        phoneInput.addEventListener("input", function () {
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


    function cartGetRequest() {
        fetch('/updateCartFinal?orderType=' + selectedOrderType + "&promoCode=" + promoCodeApplied + "&pointsRedeemed=" + pointsRedeemed, {
            method: 'GET',
        })
            .then(response => {
                if (response.ok) {
                    placeOrderRequest();
                } else {
                    // Handle other response statuses if needed
                }
            })
            .catch(error => {
                // Handle errors if any
            });
    }


    function placeOrderRequest() {
        let combinedAddress = streetAddressInput.value + ", " + cityInput.value + ", " + stateInput.value + " " + zipCodeInput.value;
        fetch('/placeOrder?address=' + combinedAddress + "&email=" + emailConfirmation.checked + "&sendTo=" + providedEmail, {
            method: 'GET',
        })
            .then(response => {
                if (response.ok) {
                    // Handle success message from the server
                    return response.text(); // This will read the response as text
                } else {
                    // Handle other response statuses if needed
                    throw new Error('Order placement failed');
                }
            })
            .then(message => {
                // Handle the success message returned from the server
                // console.log(message); // Output the message to the console
                window.location = "/thankYou" + message;
            })
            .catch(error => {
                // Handle errors if any
                console.error(error);
            });
    }

    let initialCartTotal = document.getElementById('cartTotal');
    let deliveryChartDiv = document.getElementById('deliveryCharge');
    let taxesDiv = document.getElementById("taxes");
    let discountDiv = document.getElementById('discount');
    let totalChargeDiv = document.getElementById("totalCharge");
    let availablePointsDiv = document.getElementById("availBalance");
    let pointsRedeemedDiv = document.getElementById("pointsRedeemed");
    let promoCodeDiv = document.getElementById("promoCode");

    function fillChargesDivs() {
        availablePointsDiv.innerText = "Available: " + pointsAvailable;
        deliveryChartDiv.innerText = "$" + calculateDeliveryCharge().toFixed(2);
        taxesDiv.innerText = "$" + calculateTaxes().toFixed(2);
        discountDiv.innerText = "-$" + calculateDiscount().toFixed(2);
        totalChargeDiv.innerText = "$" + calculateTotal().toFixed(2);
        promoCodeDiv.innerText = promoCodeApplied;
        pointsRedeemedDiv.innerText = pointsRedeemed;
    }

    let applyPromoBtn = document.getElementById("applyPromo");
    let promoFeedback = document.getElementById("promoFeedback");
    let promoInput = document.getElementById("userPromo");
    let applyPointsBtn = document.getElementById("applyRewards");
    let pointsFeedback = document.getElementById("rewardsFeedback");
    let pointsInput = document.getElementById("userRewards");
    const pointsRegex = /^[0-9]{1,5}$/;

    let pointsRedeemed = 0;
    let promoCodeApplied = "N/A";

    applyPromoBtn.addEventListener("click", function (e) {
        e.preventDefault();

        if (promoInput.value.trim() == "") {
            promoInput.classList.remove("is-valid")
            promoFeedback.classList.remove("valid-feedback")
            promoInput.classList.add("is-invalid")
            promoFeedback.classList.add("invalid-feedback")
            promoFeedback.innerText = "No code entered to apply.";
        } else if (promoCodeApplied != "N/A") {
            promoInput.classList.remove("is-valid")
            promoFeedback.classList.remove("valid-feedback")
            promoInput.classList.add("is-invalid")
            promoFeedback.classList.add("invalid-feedback")
            promoFeedback.innerHTML = "You can only use one promo code at a time<br>Current promo code is " + promoCodeApplied + " for $" + getPromoCodeValue(promoCodeApplied) + " off.";
        } else {
            if (promoCodes.includes(promoInput.value)) {
                promoCodeApplied = promoInput.value;
                promoInput.classList.remove("is-invalid")
                promoFeedback.classList.remove("invalid-feedback")
                promoInput.classList.add("is-valid")
                promoFeedback.classList.add("valid-feedback")
                promoFeedback.innerText = "Promo code " + promoCodeApplied + " applied for $" + getPromoCodeValue(promoCodeApplied) + " off";
                fillChargesDivs();
            } else {
                promoInput.classList.remove("is-valid")
                promoFeedback.classList.remove("valid-feedback")
                promoInput.classList.add("is-invalid")
                promoFeedback.classList.add("invalid-feedback")
                promoFeedback.innerText = "Promo code entered is not valid";
            }
        }
    });

    applyPointsBtn.addEventListener("click", function (e) {
        e.preventDefault();

        if (pointsInput.value.trim() == "") {
            pointsInput.classList.remove("is-valid")
            pointsFeedback.classList.remove("valid-feedback")
            pointsInput.classList.add("is-invalid")
            pointsFeedback.classList.add("invalid-feedback")
            pointsFeedback.innerText = "No Points entered to redeem.";
        } else if (!pointsRegex.test(pointsInput.value)) {
            pointsInput.classList.remove("is-valid")
            pointsFeedback.classList.remove("valid-feedback")
            pointsInput.classList.add("is-invalid")
            pointsFeedback.classList.add("invalid-feedback")
            pointsFeedback.innerText = "Please enter a valid rewards points value (A positive whole number)";
        } else if (pointsInput.value > pointsAvailable) {
            pointsInput.classList.remove("is-valid")
            pointsFeedback.classList.remove("valid-feedback")
            pointsInput.classList.add("is-invalid")
            pointsFeedback.classList.add("invalid-feedback")
            pointsFeedback.innerText = "You cannot redeem more points than you have available";
        } else {
            pointsInput.classList.remove("is-invalid")
            pointsFeedback.classList.remove("invalid-feedback")
            pointsInput.classList.add("is-valid")
            pointsFeedback.classList.add("valid-feedback")
            pointsFeedback.innerText = pointsInput.value + " points redeemed for $" + calculatePointsForMoney(pointsInput.value) + " off";
            pointsRedeemed += parseFloat(pointsInput.value);
            pointsAvailable -= pointsInput.value;
            fillChargesDivs();
        }
    })

    function calculatePointsForMoney(points) {
        return Math.round((points * .10) * 100) / 100
    }

    function getPromoCodeValue(enteredCode) {
        let value = 0.0;
        if (promoCodeApplied == "N/A") {
            return value;
        } else {
            switch (enteredCode.charAt(0)) {
                case 'X':
                    value = 5.0;
                    break;
                case 'Y':
                    value = 10.0;
                    break;
                case 'Z':
                    value = 15.0;
                    break;
                case 'A':
                    value = 20.0;
                    break;
                default:
                    value = 0.0;
            }
        }
        return value;
    }

    function calculateTaxes() {
        let result = (parseFloat(initialCartTotal.innerText) + calculateDeliveryCharge()) * .0625;
        return Math.round(result * 100) / 100
    }

    function calculateDeliveryCharge() {
        if (selectedOrderType == "delivery") {
            return 10.00;
        } else {
            return 0.0;
        }
    }

    function calculateTotalRedeemedPoints() {
        return calculatePointsForMoney(pointsRedeemed);
    }

    function calculateDiscount() {
        let discount = getPromoCodeValue(promoCodeApplied) + calculateTotalRedeemedPoints();
        return Math.round(discount * 100) / 100
    }

    function calculateTotal() {
        let total = parseFloat(initialCartTotal.innerText);
        total += calculateDeliveryCharge();
        total += calculateTaxes();
        total -= calculateDiscount();
        let roundedTotal = Math.round(total * 100) / 100
        if (roundedTotal < 0.0) {
            return 0.0;
        } else {
            return roundedTotal;
        }
    }

    let payNowBtn = document.getElementById("payNow");
    payNowBtn.addEventListener("click", function (event) {
        event.preventDefault();
        cartGetRequest();
    });

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
    async function checkAddress() {
        let validInput = false;
        if (selectedOrderType === "pickUp") {
            resetAddressFields();
            validInput = true;
        } else if (!checkAddressFields()) {
            console.log("tell the user they need an address for delivery")
            userAddressMessage.textContent = "Valid address is required for all delivery orders."
        } else {
            let combinedAddress = streetAddressInput.value + ", " + cityInput.value + ", " + stateInput.value + " " + zipCodeInput.value;
            const addressExistsResult = await addressExists(combinedAddress);
            const addressIsCloseResult = await addressIsCloseEnough(combinedAddress);
            if (!addressExistsResult) {
                console.log("the address does not exist")
                userAddressMessage.textContent = "This address does not exists"
            } else if (!addressIsCloseResult) {
                console.log("address is not close enough")
                userAddressMessage.textContent = "Sorry, this address is not close enough for delivery"

            } else {
                resetAddressFields();
                validInput = true;
            }
        }
        return validInput;
    }



})();



