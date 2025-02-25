function showUpdateConfirmationModal(button) {
    const form = button.closest('form');
    const newPriceInput = form.querySelector('#newPrice');

    if (!newPriceInput.checkValidity()) {
        newPriceInput.reportValidity();
        return;
    }

    const oldPrice = form.querySelector('#oldPrice').value;
    const newPrice = form.querySelector('#newPrice').value;
    const specialPrice = form.querySelector('#specialPrice').checked;

    const confirmationDetails = document.getElementById('confirmationDetails');
    confirmationDetails.innerHTML = `
        <p><strong>Ancien prix :</strong> ${oldPrice} €</p>
        <p><strong>Nouveau prix :</strong> ${newPrice} €</p>
        <p><strong>Prix spécial :</strong> ${specialPrice ? 'Oui' : 'Non'}</p>
    `;

    const modal = document.getElementById('confirmationModal');
    modal.style.display = "block";

    document.getElementById('confirmUpdate').onclick = function () {
        htmx.ajax('POST', form.action, {
            target: form.getAttribute('hx-target'),
            swap: form.getAttribute('hx-swap'),
            values: {
                oldPrice: oldPrice,
                newPrice: newPrice,
                specialPrice: specialPrice
            }
        });
        modal.style.display = "none";
    };

    document.getElementById('cancelUpdate').onclick = function () {
        modal.style.display = "none";
    };

    document.getElementById('closeModal').onclick = function () {
        modal.style.display = "none";
    };
}

window.onclick = function (event) {
    const modal = document.getElementById('confirmationModal');
    if (event.target === modal) {
        modal.style.display = "none";
    }
};

function showDeleteConfirmationModal(button) {
    const form = button.closest('form');
    const dishId = form.querySelector('input[id="dishId"]').value;
    const dishName = form.closest('.dish').querySelector('h3').innerText;

    const deleteConfirmationDetails = document.getElementById('deleteConfirmationDetails');
    deleteConfirmationDetails.innerHTML = `<p><strong>Plat :</strong> ${dishName}</p>`;

    const deleteModal = document.getElementById('deleteConfirmationModal');
    deleteModal.style.display = "block";

    document.getElementById('confirmDelete').onclick = function () {
        htmx.ajax('post', '/api/dish/delete/' + dishId, {
            target: form.getAttribute('hx-target'),
            swap: form.getAttribute('hx-swap'),
        });
        deleteModal.style.display = "none";
    };

    document.getElementById('cancelDelete').onclick = function () {
        deleteModal.style.display = "none";
    };

    document.getElementById('closeDeleteModal').onclick = function () {
        deleteModal.style.display = "none";
    };
}

window.onclick = function (event) {
    const deleteModal = document.getElementById('deleteConfirmationModal');
    if (event.target === deleteModal) {
        deleteModal.style.display = "none";
    }
};