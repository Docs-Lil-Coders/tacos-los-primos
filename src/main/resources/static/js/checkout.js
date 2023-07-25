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


})();



