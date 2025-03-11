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
}

function closeValidationModal() {
    document.getElementById('orderValidationModal').style.display = 'none';
}

function closeSummaryModal() {
    document.getElementById('orderSummaryModal').style.display = 'none';
}

// Écouter l'événement afterSwap
document.addEventListener('htmx:afterSwap', function (evt) {
    // Vérifier quel élément a été mis à jour
    const targetId = evt.detail.target.id;

    // Ouvrir la modal correspondante selon l'élément ciblé
    if (targetId === 'orderDetails') {
        document.getElementById('orderSummaryModal').style.display = 'block';
    } else if (targetId === 'validationMessage') {
        document.getElementById('orderValidationModal').style.display = 'block';
    }
});

function getOrderSummary(orderId) {
    htmx.ajax("GET", "/fragment/order/dishesSummary/" + orderId, "#orderDetails");
}