const getProducts = () => {
    return fetch("/api/products")
        .then(response => response.json())
        .catch(error => console.log(error))
}

const createHtmlElementFromString = (htmlElementAsString) => {
    const myElement = document.createElement("div");
    myElement.innerHTML = htmlElementAsString.trim();
    return myElement.firstChild;
}

const createProductHtmlComponent = (product) => {
    const template = `
        <li class="product">
            <h3>${product.title}</h3>
            <div class="product__image-container">
                <img src="${product.filePath}"/>
            </div>
            <span class="product__price">${product.price}</span>
            <button
                class="product__add-to-basket"
                data-product-id="${product.id}"
            >
                Add to basket
            </button>
        </li>
    `;

    return createHtmlElementFromString(template);
}

const appendProductsToList = (listEl, productsAsHtml) => {
    productsAsHtml.forEach(el => listEl.appendChild(el));
    return productsAsHtml;
}

const refreshCurrentOffer = () => {
    const basketEl = document.querySelector('.basket');
    fetch('/api/current-offer')
        .then(r => r.json())
        .then(offer => {
            basketEl.querySelector('.basket__total').innerText = `${offer.total} PLN`;
            basketEl.querySelector('.basket__items-count').innerText = `${offer.linesCount} Items`;
        })
}

const initializeAddToBasketHandler = (productHtmlEl) => {
    const addToBasketButton = productHtmlEl.querySelector(".product__add-to-basket")
    addToBasketButton.addEventListener("click", (event) => {
        const productId = event.target.getAttribute("data-product-id");
        const url = `/api/add-product/${productId}`;

        fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
        }).then(() => refreshCurrentOffer())
            .catch(err => console.log(err))
    })
}

const initializeAddToBasketHandling = (productsAsHtmlEl) => {
    productsAsHtmlEl.forEach(el => initializeAddToBasketHandler(el))
    return productsAsHtmlEl;
}

const handleCheckout = () => {
    console.log("lets do some checkout steps")
}

(() => {
    const productList = document.getElementById("products")
    getProducts()
        .then(productsAsObjects => productsAsObjects.map(createProductHtmlComponent))
        .then(productsAsHtmlEl => initializeAddToBasketHandling(productsAsHtmlEl))
        .then(productsAsHtmlEl => appendProductsToList(productList, productsAsHtmlEl))

    refreshCurrentOffer();
})();
