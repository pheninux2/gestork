let total = 0;
let orderItems = {};

function updateTotalPrice() {
    document.getElementById('totalPrice').innerText = 'Total : ' + total.toFixed(2) + '€';
}

function addToCart(itemName, price) {

    price = parseFloat(price); // Convertir le prix en nombre
    if (isNaN(price)) {
        console.error("Le prix est invalide :", price);
        return; // Quitter la fonction si le prix n'est pas valide
    }

    if (orderItems[itemName]) {
        orderItems[itemName].quantity += 1;
        orderItems[itemName].price += price;
    } else {
        orderItems[itemName] = {price: price, quantity: 1};
    }
    total += price;
    document.querySelector('.button-container .button').innerText = 'Panier (' + getTotalItems() + ')';
    updateTotalPrice();
}

function removeFromCart(itemName) {
    if (orderItems[itemName]) {
        total -= orderItems[itemName].price;
        delete orderItems[itemName];
        document.querySelector('.button-container .button').innerText = 'Panier (' + getTotalItems() + ')';
        updateTotalPrice();
        showDetails();
    }
}

function getTotalItems() {
    return Object.values(orderItems).reduce((acc, item) => acc + item.quantity, 0);
}

function showDetails() {
    const orderList = document.getElementById('orderList');
    orderList.innerHTML = '';
    for (const item in orderItems) {
        const li = document.createElement('li');
        li.style.display = 'flex';
        li.style.justifyContent = 'space-between';
        li.style.alignItems = 'center'; // Centrer verticalement les éléments

        // Ajouter le texte de l'article et la quantité
        const itemDetails = document.createElement('span');
        itemDetails.innerText = `${item} x ${orderItems[item].quantity} - `;
        li.appendChild(itemDetails);

        // Créer un élément pour le prix et le mettre en gras
        const priceElement = document.createElement('strong');
        priceElement.innerText = `${orderItems[item].price.toFixed(2)}€`;
        priceElement.style.marginLeft = '10px'; // Ajouter une marge à gauche du prix
        priceElement.style.marginLeft = 'auto'; // Pousse le prix à droite
        priceElement.style.marginRight = '5px'; // Ajoute une marge à droite du prix
        li.appendChild(priceElement); // Ajouter le prix au li

        // Créer un conteneur pour les boutons
        const buttonContainer = document.createElement('span');
        buttonContainer.style.display = 'flex';
        buttonContainer.style.alignItems = 'center';

        const decreaseBtn = document.createElement('span');
        decreaseBtn.innerHTML = '<i class="fas fa-minus-circle" style="color: blue; cursor: pointer; margin-right: 5px;"></i>';
        decreaseBtn.onclick = () => decreaseQuantity(item);
        buttonContainer.appendChild(decreaseBtn);

        const removeBtn = document.createElement('span');
        removeBtn.innerHTML = '<i class="fas fa-trash" style="color: red; cursor: pointer;"></i>';
        removeBtn.onclick = () => removeFromCart(item);
        buttonContainer.appendChild(removeBtn);

        // Ajouter le conteneur des boutons au li
        li.appendChild(buttonContainer);
        orderList.appendChild(li);
    }
    document.getElementById('orderTotal').innerText = 'Total : ' + total.toFixed(2) + '€';
    openModal();
}

function decreaseQuantity(item) {
    if (orderItems[item].quantity > 1) {
        // Réduire la quantité
        orderItems[item].quantity -= 1;

        // Soustraire le prix d'un seul article
        total -= orderItems[item].price / (orderItems[item].quantity + 1);
        orderItems[item].price -= orderItems[item].price / (orderItems[item].quantity + 1);

        // Mettre à jour l'interface utilisateur
        document.querySelector('.button-container .button').innerText = 'Panier (' + getTotalItems() + ')';
        updateTotalPrice();
        showDetails();
    } else {
        removeFromCart(item);
    }
}

function openModal() {
    document.getElementById('orderModal').style.display = "block";
}

function closeModal() {
    document.getElementById('orderModal').style.display = "none";
}

function validateOrder() {
    if (getTotalItems() > 0) {
        alert('Commande validée avec succès !');
        total = 0;
        orderItems = {};
        document.querySelector('.button-container .button').innerText = 'Panier (0)';
        updateTotalPrice();
    } else {
        alert('Votre panier est vide.');
    }
}

window.onclick = function (event) {
    const modal = document.getElementById('orderModal');
    if (event.target == modal) {
        modal.style.display = "none";
    }
}