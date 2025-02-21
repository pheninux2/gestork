function updateDateTime() {
    const now = new Date();
    const options = {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
    };
    document.getElementById('date-time').innerText = now.toLocaleDateString('fr-FR', options);
}

setInterval(updateDateTime, 1000);


const eventSource = new EventSource('/order/stream');

eventSource.onmessage = function (event) {
    htmx.ajax("GET", "/fragment/notification/orders", "#ordersNotificationTarget");
};


