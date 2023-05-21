package com.es.phoneshop.model.cart;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.iml.DefaultCartService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultCartServiceTest {

    private static final int MAX_STOCK = 10;
    private final CartService cartService = DefaultCartService.getInstance();
    private final ProductDao productDao = ArrayListProductDao.getInstance();
    private final Cart cart = new Cart();
    private Product testProduct;
    private Product testEmptyProduct;

    @Before
    public void setup() {
        testProduct = new Product("test", "Test Product", new BigDecimal(100), Currency.getInstance("USD"), MAX_STOCK, "https://example.com/test.jpg", null);
        testEmptyProduct = new Product("test15", "Test Product1", new BigDecimal(100), Currency.getInstance("USD"), MAX_STOCK, "https://example.com/test.jpg", null);
        productDao.save(testProduct);

    }

    @Test
    public void testAddToEmptyCart() throws OutOfStockException {
        productDao.save(testEmptyProduct);
        Long productId = testEmptyProduct.getId();
        int quantity = MAX_STOCK / 2;

        cartService.add(cart, productId, quantity);

        CartItem expectedCartItem = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findAny()
                .orElse(null);
        assert expectedCartItem != null;
        assertEquals(expectedCartItem.getProduct().getId(), productId);
        assertEquals(expectedCartItem.getQuantity(), quantity);
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToCartWithInvalidQuantity() throws OutOfStockException {
        Long productId = testProduct.getId();
        int quantity = MAX_STOCK * 2;

        cartService.add(cart, productId, quantity);
    }

    @Test
    public void testAddToExistingCart() throws OutOfStockException {
        Long productId = testProduct.getId();
        int quantity = MAX_STOCK / 3; //3
        cartService.add(cart, productId, quantity);

        cartService.add(cart, productId, 1);
        quantity++;

        CartItem expectedCartItem = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findAny()
                .orElse(null);
        assert expectedCartItem != null;
        assertEquals(expectedCartItem.getQuantity(), quantity);
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToExistingCartWithInvalidQuantity() throws OutOfStockException {
        Long productId = testProduct.getId();
        cartService.add(cart, productId, MAX_STOCK);

        cartService.add(cart, productId, 1);
    }

    @Test
    public void testUpdateCart() throws OutOfStockException {
        productDao.save(testEmptyProduct);
        Long productId = testEmptyProduct.getId();
        int quantity = MAX_STOCK / 2;
        cartService.add(cart, productId, 2);

        cartService.update(cart, productId, quantity);

        CartItem expectedCartItem = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findAny()
                .orElse(null);
        assert expectedCartItem != null;
        assertEquals(expectedCartItem.getProduct().getId(), productId);
        assertEquals(expectedCartItem.getQuantity(), quantity);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testUpdateCartNonexistentCartItem() throws OutOfStockException {
        productDao.save(testEmptyProduct);
        Long productId = testEmptyProduct.getId();
        int quantity = MAX_STOCK / 2;

        cartService.update(cart, productId, quantity);
    }

    @Test
    public void testDeleteCartItem() throws OutOfStockException {
        productDao.save(testEmptyProduct);
        Long productId = testEmptyProduct.getId();
        int quantity = MAX_STOCK / 2;
        cartService.add(cart, productId, MAX_STOCK);

        cartService.delete(cart, productId);

        CartItem foundCartItem = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findAny()
                .orElse(null);
        assertNull(foundCartItem);
    }


}
