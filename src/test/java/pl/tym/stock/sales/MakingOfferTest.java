package pl.tym.stock.sales;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.tym.stock.sales.offerting.Offer;
import pl.tym.stock.sales.offerting.OfferMaker;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MakingOfferTest {

    private BasketStorage basketStorage;
    private DummyProductDetailsProvider productDetailsProvider;

    @BeforeEach
    void initializeSharedObjects() {
        basketStorage = new BasketStorage();
        productDetailsProvider = new DummyProductDetailsProvider();
    }

    @Test
    void itGeneratesOfferBasedOnProductsWithinBasket() {
        String productId = thereIsProduct("product-1", BigDecimal.valueOf(10.10));
        String customerId = thereIsCustomer();
        SalesFacade sales = thereIsSalesModule();

        sales.addToBasket(customerId, productId);
        sales.addToBasket(customerId, productId);
        Offer currentOffer = sales.getCurrentOffer(customerId);

        assertEquals(BigDecimal.valueOf(20.20), currentOffer.getTotal());
        assertEquals(2, currentOffer.getLinesCount());
    }

    private String thereIsProduct(String productId, BigDecimal price) {
        ProductDetails product = new ProductDetails(productId, price);
        productDetailsProvider.products.put(productId, product);
        return productId;
    }

    private String thereIsCustomer() {
        return UUID.randomUUID().toString();
    }

    private SalesFacade thereIsSalesModule() {
        return new SalesFacade(
                basketStorage,
                productDetailsProvider,
                new OfferMaker(productDetailsProvider)
        );
    }
}
