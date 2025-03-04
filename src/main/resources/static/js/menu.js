let total = 0;
let orderItems = {};

function updateTotalPrice() {
    document.getElementById('totalPrice').innerText = 'Total : ' + total.toFixed(2) + '€';
}

function addToCart(id, itemName, price) {

    price = parseFloat(price); // Convertir le prix en nombre
    if (isNaN(price)) {
        console.error("Le prix est invalide :", price);
        return; // Quitter la fonction si le prix n'est pas valide
    }

    if (orderItems[itemName]) {
        orderItems[itemName].quantity += 1;
        orderItems[itemName].price += price;
    } else {
        orderItems[itemName] = {id: id, price: price, quantity: 1};
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

    if (total === 0) {
        document.getElementById('confirmBtn').style.display = 'none';
    } else {
        document.getElementById('confirmBtn').style.display = 'inline-block';
    }
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

function confirmOrder() {
    if (getTotalItems() <= 0) {
        console.log("Aucun article dans la commande.");
        return;
    }

    const orderDishes = Object.values(orderItems).map(item => ({
        dish: {dishId: item.id},
        quantity: item.quantity,
        comment: ""
    }));

    const orderEntity = {
        customerLogin: sessionStorage.getItem("login"),
        totalAmount: total,
        orderDate: new Date().toISOString(),
        orderType: "INDOOR",
        orderDetails: {
            paymentStatus: "UNPAID",
            comment: "No onions",
            orderDishes: orderDishes,
        }
    };
    console.log(JSON.stringify(orderEntity))
    const code = sessionStorage.getItem("code");

    let element = document.getElementById('response');

    fetch(`/api/order/create?code=${encodeURIComponent(code)}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(orderEntity)
    })
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (data.status === 500) {
                element.innerHTML = data.alert.replace(/[\r\n]+/g, ' ');
            } else {

                const loadingDiv = document.createElement('div');
                loadingDiv.className = 'loading';
                loadingDiv.innerHTML = `
        <div class="spinner"></div>
        <p>Votre commande est en cours d'envoi...</p>
    `;
                document.body.appendChild(loadingDiv);

                // Simuler un appel API et rediriger après 3 secondes
                setTimeout(() => {
                    sessionStorage.setItem('orderId', data.data.orderId);
                    // Rediriger vers la page de statut de commande
                    window.location.href = '/view/order/status';
                }, 3000);
                // eventSource.onmessage = function (event) {
                //     let orderId = localStorage.getItem('orderId');
                //     htmx.ajax("GET", "/view/fragment/orderStatus/" + orderId, "#orderStatusTarget");
                // };
            }
        })
}

window.onclick = function (event) {
    const modal = document.getElementById('orderModal');
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

function validateOrder() {
    if (getTotalItems() > 0) {
        closeModal();
        openPaymentChoiceModal();
    }
}

function openPaymentChoiceModal() {
    document.getElementById('paymentChoiceModal').style.display = 'block';
}

function closePaymentChoiceModal() {
    document.getElementById('paymentChoiceModal').style.display = 'none';
}

function openPaymentModal() {
    document.getElementById('paymentModal').style.display = 'block';
    closePaymentChoiceModal();
}

function closePaymentModal() {
    document.getElementById('paymentModal').style.display = 'none';
}

function confirmPayment() {
    event.preventDefault();

    const cardNumber = document.getElementById('cardNumber').value;
    const expiryDate = document.getElementById('expiryDate').value;
    const cvv = document.getElementById('cvv').value;

    // Logique pour traiter le paiement
    alert(`Paiement confirmé avec la carte: ${cardNumber}`);
    closePaymentModal();
    total = 0;
    orderItems = {};
    document.querySelector('.button-container .button').innerText = 'Panier (0)';
    updateTotalPrice();
}

window.onclick = function (event) {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    });
}