package pl.tym.stock.sales;


import pl.tym.stock.sales.offerting.Offer;
import pl.tym.stock.sales.offerting.OfferMaker;

public class SalesFacade {
    private final BasketStorage basketStorage;
    private final ProductDetailsProvider productDetailsProvider;
    private OfferMaker offerMaker;

    public SalesFacade(BasketStorage basketStorage, ProductDetailsProvider productDetailsProvider, OfferMaker offerMaker) {
        this.basketStorage = basketStorage;
        this.productDetailsProvider = productDetailsProvider;
        this.offerMaker = offerMaker;
    }

    public void addToBasket(String customerId, String productId) {
        Basket basket = loadBasketForCustomer(customerId);
        ProductDetails product = productDetailsProvider.getProductDetails(productId);

        basket.add(BasketItem.of(product.getId(), product.getPrice()));

        basketStorage.save(customerId, basket);
    }

    private Basket loadBasketForCustomer(String customerId) {
        return basketStorage.getForCustomer(customerId).orElse(Basket.empty());
    }

    public Offer getCurrentOffer(String customerId) {
        Basket basket = loadBasketForCustomer(customerId);
        return offerMaker.makeAnOffer(basket);
    }

    public ReservationDetails acceptOffer(String customerId, CustomerData customerData) {
        return ReservationDetails.ofPayment("reservationId", "paymentId", "paymentUrl");
    }
}
