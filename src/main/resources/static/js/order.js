function loadOrderDetails(orderId) {
    htmx.ajax("GET", "/fragment/order/dishesSummary/" + orderId, "#orderDetails");
}

function valideOrder(orderId, button) {
    htmx.ajax("POST", "/api/order/validate/" + orderId, "#validationMessage")
        .then(function (response) {
            // Désactiver le bouton après validation
            button.disabled = true;
            button.classList.add('disabled-button');
            // Ajouter la classe .validated à la carte correspondante
            const card = button.closest('.card');
            if (card) {
                card.classList.add('validated');
            }
        });
    // Ouvrir la modal après l'envoi de la requête
    document.getElementById('orderValidationModal').style.display = 'block';
}

function closeValidationModal() {
    document.getElementById('orderValidationModal').style.display = 'none';
}
function closeSummaryModal() {
    document.getElementById('orderSummaryModal').style.display = 'none';
}

document.addEventListener('htmx:afterSwap', function (evt) {
    document.getElementById('orderSummaryModal').style.display = 'block';
});