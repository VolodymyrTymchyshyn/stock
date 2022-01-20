package pl.tym.stock.sales;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.tym.stock.sales.offerting.OfferMaker;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectingProductsTest {
    private BasketStorage basketStorage;
    private DummyProductDetailsProvider productDetailsProvider;

    @BeforeEach
    void initializeSharedObjects() {
        basketStorage = new BasketStorage();
        productDetailsProvider = new DummyProductDetailsProvider();
    }

    @Test
    void itAllowsAddingProductsToBasket() {
        //Arrange
        String productId = thereIsProduct("product-1");
        String customerId = thereIsCustomer("customer-1");
        SalesFacade sales = thereIsSalesModule();

        //Act
        sales.addToBasket(customerId, productId);

        //Assert
        thereIsXProductsInCustomersBasket(1, customerId);
    }

    @Test
    void itAllowsToAddMultipleProductsToBasket() {
        //arrange
        String productId1 = thereIsProduct("product-1");
        String productId2 = thereIsProduct("product-2");
        String customerId = thereIsCustomer("customer-1");
        SalesFacade sales = thereIsSalesModule();

        //Act
        sales.addToBasket(customerId, productId1);
        sales.addToBasket(customerId, productId2);

        //Assert
        thereIsXProductsInCustomersBasket(2, customerId);
    }

    @Test
    void itDoNotShareCustomersBaskets() {
        //arrange
        String productId1 = thereIsProduct("product-1");
        String customerId1 = thereIsCustomer("customer-1");
        String customerId2 = thereIsCustomer("customer-2");
        SalesFacade sales = thereIsSalesModule();

        //Act
        sales.addToBasket(customerId1, productId1);
        sales.addToBasket(customerId2, productId1);

        //Assert
        thereIsXProductsInCustomersBasket(1, customerId1);
        thereIsXProductsInCustomersBasket(1, customerId2);
    }

    private SalesFacade thereIsSalesModule() {
        return new SalesFacade(
                basketStorage,
                productDetailsProvider,
                new OfferMaker(productDetailsProvider)
        );
    }

    private String thereIsCustomer(String customerName) {
        return customerName;
    }

    private String thereIsProduct(String productId) {
        ProductDetails product = new ProductDetails(productId, BigDecimal.TEN);
        productDetailsProvider.products.put(productId, product);
        return productId;
    }

    private void thereIsXProductsInCustomersBasket(int productsCount, String customerId) {
        Basket basket = loadBasketForCustomer(customerId);
        assertEquals(productsCount, basket.getProductsCount());
    }

    private Basket loadBasketForCustomer(String customerId) {
        return basketStorage.getForCustomer(customerId).get();
    }
}