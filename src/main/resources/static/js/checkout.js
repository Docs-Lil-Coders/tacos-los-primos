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
    let orderTypes = document.getElementsByName("orderType");
    let orderTypeFeedback = document.getElementById("orderTypeFeedback");
    let emailInput = document.getElementById("userEmail");
    let phoneInput = document.getElementById("userPhone");
    let addressInput = document.getElementById("userAddress");
    let emailFeedback = document.getElementById("emailFeedback");
    let phoneFeedback = document.getElementById("phoneFeedback");
    let addressFeedback = document.getElementById("addressFeedback");
    let emailConfirmation = document.getElementById("emailConfirmation");
    let phoneConfirmation = document.getElementById("phoneConfirmation");
    let firstNameInput = document.getElementById("userFirstName");
    let lastNameInput = document.getElementById("userLastName");
    let firstNameFeedback = document.getElementById("firstNameFeedback");
    let lastNameFeedback = document.getElementById("lastNameFeedback");
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const disAllowedRegex = /^[^"'()*+\-/:;<=>?[\]^`{|}~]*$/;
    const phoneRegex = /^[0-9]{10}$/;
    const addressRegex = /^[a-zA-Z0-9, ]{1,50}$/; //TODO - we will need to check this against the API instead once we have it

    addContactSheetEventListeners();


    let formStepsNum = 0;

    nextBtns.forEach((btn) => {
        btn.addEventListener("click", () => {
            resetFeedbackFields();
            if (formStepsNum === 0 && (checkOrderType() === false)) {
                orderTypeFeedback.classList.remove("d-none");
            } else if (formStepsNum === 1 && (checkContactInformation() === false)) {
                console.log("contact info not okay")
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
        addressInput.classList.remove("is-invalid")
        addressFeedback.classList.remove("invalid-feedback")
        addressFeedback.innerText = "";
        phoneInput.classList.remove("is-invalid")
        phoneFeedback.classList.remove("invalid-feedback")
        phoneFeedback.innerText = "";
        emailInput.classList.remove("is-invalid")
        emailFeedback.classList.remove("invalid-feedback")
        emailFeedback.innerText = "";
    }

    function checkAddress() {
        let validInput = false;
        if (selectedOrderType == "pickUp") {
            addressInput.classList.remove("is-valid")
            addressFeedback.classList.remove("valid-feedback")
            addressInput.classList.remove("is-invalid")
            addressFeedback.classList.remove("invalid-feedback")
            addressFeedback.innerText = "";
            validInput = true;
        } else if (!addressRegex.test(addressInput.value) || !disAllowedRegex.test(addressInput.value)) {
            addressInput.classList.remove("is-valid")
            addressFeedback.classList.remove("valid-feedback")
            addressInput.classList.add("is-invalid")
            addressFeedback.classList.add("invalid-feedback")
            addressFeedback.innerText = "Valid address is required for all delivery orders.";
        } else {
            addressInput.classList.remove("is-invalid")
            addressFeedback.classList.remove("invalid-feedback")
            addressInput.classList.add("is-valid")
            addressFeedback.classList.add("valid-feedback")
            addressFeedback.innerText = "";
            validInput = true;
        }
        return validInput;
    }

    function checkPhone() {
        let validInput = false;
        if (!phoneConfirmation.checked) {
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
            phoneFeedback.innerText = "A valid 10 digit phone number is required for text confirmations";
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

    function checkContactInformation() {
        let addressOK = checkAddress();
        let emailOK = checkEmail();
        let phoneOK = checkPhone();
        let firstNameOK = checkFirstName();
        let lastNameOK = checkLastName();

        return addressOK && emailOK && phoneOK && firstNameOK && lastNameOK;
    }

    function addContactSheetEventListeners() {
        emailConfirmation.addEventListener("change", function () {
            checkEmail();
        });

        phoneConfirmation.addEventListener("change", function () {
            checkPhone();
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

        addressInput.addEventListener("input", function () {
            checkAddress();
        });
    }


    function cartGetRequest() {
        fetch('/updateCartFinal?orderType=' + selectedOrderType + "&promoCode=" + promoCodeApplied + "&pointsRedeemed=" + pointsRedeemed, {
            method: 'GET',
        })
            .then(response => {
                if (response.ok) {
                    // Empty response received, handle success if needed
                } else {
                    // Handle other response statuses if needed
                }
            })
            .catch(error => {
                // Handle errors if any
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
    const pointsRegex = /^[0-9]{1,3}$/;

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
                    value = 5.0;
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
    payNowBtn.addEventListener("click", function () {
        cartGetRequest();
    })

})();



