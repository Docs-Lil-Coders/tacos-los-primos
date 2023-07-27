"use strict";

(function () {

    function handleMinusClick(event) {
        const buttonId = event.target.id;
        let itemId = parseInt(buttonId.toString().substring(13));
        let quantityDisplay = document.getElementById("currentQuantity" + itemId);
        let quantity = parseInt(quantityDisplay.innerText);
        if (quantity > 0) {
            quantity = quantity - 1;
            quantityDisplay.innerText = quantity.toString();
        }
        window.location.href = "editBag?quantity=" + quantity + "&index=" + itemId;
    }

    function handleAddQuantityClick(event) {
        const buttonId = event.target.id;
        let itemId = parseInt(buttonId.toString().substring(11));
        let quantityDisplay = document.getElementById("currentQuantity" + itemId);
        let quantity = parseInt(quantityDisplay.innerText);
        quantity = quantity + 1;
        quantityDisplay.innerText = quantity.toString();
        window.location.href = "editBag?quantity=" + quantity + "&index=" + itemId;
    }

    function handleDeleteClick(event) {
        const buttonId = event.target.id;
        let itemId = buttonId.toString().substring(10);
        window.location.href = "removeItem?index=" + itemId;
    }

    let qMinusButtons = document.querySelectorAll('.minusQuantity');
    qMinusButtons.forEach(button => {
        button.addEventListener('click', handleMinusClick);
    });

    let qAddButtons = document.querySelectorAll('.addQuantity');
    qAddButtons.forEach(button => {
        button.addEventListener('click', handleAddQuantityClick);
    });

    let deleteButtons = document.querySelectorAll('.deleteItem');
    deleteButtons.forEach(button => {
        button.addEventListener('click', handleDeleteClick);
    });

}());