"use strict";

(function () {

    let url;

    let qMinusButtons = document.querySelectorAll('.minusQuantity');
    qMinusButtons.forEach(button => {
        button.addEventListener('click', handleMinusClick);
    });

    let qAddButtons = document.querySelectorAll('.addQuantity');
    qAddButtons.forEach(button => {
        button.addEventListener('click', handleAddQuantityClick);
    });

    const addToBagButtons = document.querySelectorAll('.addToBag');
    addToBagButtons.forEach(button => {
        button.addEventListener('click', handleAddToBagClick);
    });

    let stayOnMenuBtn = document.getElementById('stayMenu');
    stayOnMenuBtn.addEventListener("click", function () {
        window.location.href = url + "&destination=menu";
    })

    let goToBagBtn = document.getElementById("goToBag");
    goToBagBtn.addEventListener("click", function () {
        window.location.href = url + "&destination=bag";
    })


    function handleMinusClick(event) {
        const buttonId = event.target.id;
        let itemId = parseInt(buttonId.toString().substring(13));
        let quantityDisplay = document.getElementById("currentQuantity" + itemId);
        let quantity = parseInt(quantityDisplay.innerText);
        if (quantity > 0) {
            quantity = quantity - 1;
            quantityDisplay.innerText = quantity.toString();
        }
    }

    function handleAddQuantityClick(event) {
        const buttonId = event.target.id;
        let itemId = parseInt(buttonId.toString().substring(11));
        let quantityDisplay = document.getElementById("currentQuantity" + itemId);
        let quantity = parseInt(quantityDisplay.innerText);
        quantity = quantity + 1;
        quantityDisplay.innerText = quantity.toString();
    }

    function handleAddToBagClick(event) {
        let buttonId = event.target.id;
        let itemId = parseInt(buttonId.toString().substring(8));
        let feedback = document.getElementById('feedback' + itemId);
        let quantityDisplay = document.getElementById("currentQuantity" + itemId);
        let quantity = quantityDisplay.innerText;
        let options = [];

        let selectedMeat = getSelectedMeats(itemId);
        let selectedToppings = getSelectedToppings(itemId);

        if (selectedMeat == null) {
            feedback.innerText = "Please choose a meat before proceeding";
        } else {
            options = selectedToppings.slice();
            if (selectedMeat != "") {
                options.unshift(selectedMeat);
            }
            let optionList = options.toString();
            url = "addToBag?menuItem=" + itemId + "&quantity=" + quantity + "&options=" + optionList;
            $('#optionsModal' + itemId).modal('hide');
            showConfirmationModal();
        }

    }

    function getSelectedMeats(itemId) {
        let radioButtonGroup = document.getElementsByName('meatOptions' + itemId);

        if (radioButtonGroup.length === 0) {
            //this is for if there is no meat required
            return "";
        }

        //find the option selected
        for (const radioButton of radioButtonGroup) {
            if (radioButton.checked) {
                return radioButton.value
            }
        }

        //if nothing is selected
        return null;
    }

    function getSelectedToppings(itemId) {
        let checkboxesGroup = document.getElementsByName('toppingOptions' + itemId);
        let selectedValues = [];

        //add all selected items to array
        for (const checkbox of checkboxesGroup) {
            if (checkbox.checked) {
                selectedValues.push(checkbox.value);
            }
        }
        return selectedValues;
    }

    function showConfirmationModal() {
        const $modal = $('#confirmation');
        $modal.modal({
            backdrop: 'static', // Prevents closing the modal by clicking outside
            keyboard: false     // Prevents closing the modal by pressing the Escape key
        });

        $modal.modal('show');
    }

//sticky menu-nav
    window.onscroll = function () {
        stickyMenu();
    };

    let menuNav = document.getElementById("menu-nav");
    let margin = 120;

    function stickyMenu() {
        console.log("window.pageYOffset:", window.pageYOffset);

        console.log()
        if (window.pageYOffset > margin) {
            menuNav.classList.add("sticky");
        } else if (window.pageYOffset <= margin){
            menuNav.classList.remove("sticky");
        }
    }

}());