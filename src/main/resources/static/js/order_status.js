(function () {
    "use strict";

    let statusForm = document.getElementById("statusForm");
    let submitBtn = document.getElementById("checkStatus");
    let orderNumberInput = document.getElementById("orderNumber");
    let orderFeedback = document.getElementById("orderFeedback");
    const orderNumberRegex = /^[0-9]{1,20}$/;

    submitBtn.addEventListener("click", function (event) {
        event.preventDefault();
        let validOrder = checkOrderNumber();
        console.log(validOrder)

        if (validOrder === true) {
            statusForm.action = "/order-status?orderId=" + orderNumberInput.value;
            statusForm.submit();
        }
    });

    function checkOrderNumber() {
        let validInput = false;
        if (orderNumberInput.value.trim() === "") {
            orderNumberInput.classList.remove("is-valid")
            orderFeedback.classList.remove("valid-feedback")
            orderNumberInput.classList.add("is-invalid")
            orderFeedback.classList.add("invalid-feedback")
            orderFeedback.innerText = "No order number entered.";
        } else if (!orderNumberRegex.test(orderNumberInput.value)) {
            orderNumberInput.classList.remove("is-valid")
            orderFeedback.classList.remove("valid-feedback")
            orderNumberInput.classList.add("is-invalid")
            orderFeedback.classList.add("invalid-feedback")
            orderFeedback.innerText = "Please enter a valid order number";
        } else {
            orderNumberInput.classList.remove("is-invalid")
            orderFeedback.classList.remove("invalid-feedback")
            orderNumberInput.classList.add("is-valid")
            orderFeedback.classList.add("valid-feedback")
            orderFeedback.innerText = "";
            validInput = true;
        }
        return validInput;
    }


// status bar
    let progress;
    let stepStatus;
    let totalSteps = 0;
    let stepsComplete = 0;

    // update progress
    function updateProgressBar() {
        let progressSteps = document.querySelectorAll(".progress-step");

        progressSteps.forEach((progressStep, idx) => {
            if (idx < stepsComplete + 1) {
                progressStep.classList.add("progress-step-active");
            } else {
                progressStep.classList.remove("progress-step-active");
            }
        });

        const progressActive = document.querySelectorAll(".progress-step-active");

        progress.style.width = ((progressActive.length - 1) / (totalSteps)) * 100 + "%";
    }

    // set variables
    function orderStatusDelivery() {
        let d1 = document.getElementById("delivery1").innerText;
        let d2 = document.getElementById("delivery2").innerText;
        let d3 = document.getElementById("delivery3").innerText;
        let d4 = document.getElementById("delivery4").innerText;

        if (d1 === "yes") {
            stepsComplete++;
        } else if (d2 === "yes") {
            stepsComplete += 2;
        } else if (d3 === "yes") {
            stepsComplete += 3;
        } else if (d4 === "yes") {
            stepsComplete += 4;
        }
        updateProgressBar();
    }

    function orderStatusPickup() {
        let p1 = document.getElementById("pickup1").innerText;
        let p2 = document.getElementById("pickup2").innerText;
        let p3 = document.getElementById("pickup3").innerText;

        if (p1 === "yes") {
            stepsComplete++;
        } else if (p2 === "yes") {
            stepsComplete += 2;
        } else if (p3 === "yes") {
            stepsComplete += 3;
        }
        updateProgressBar();
    }

    // check for pickup or delivery
    function orderType(order) {
        if (order === "DELIVERY") {
            stepStatus = document.querySelectorAll(".delivery-step");
            totalSteps += 4;
            progress = document.getElementById("progress-delivery");
            orderStatusDelivery();
        } else if (order === "PICKUP") {
            stepStatus = document.querySelectorAll(".pickup-step");
            totalSteps += 3;
            progress = document.getElementById("progress-pickup");
            orderStatusPickup();
        }
    }

    // load status bar after reload
    document.addEventListener("DOMContentLoaded", function () {
        const urlParams = new URLSearchParams(window.location.search);
        const orderId = urlParams.get("orderId");
        if (orderId) {
            let order = document.getElementById("order-type").innerText;
            orderType(order);
        }
    });


})();