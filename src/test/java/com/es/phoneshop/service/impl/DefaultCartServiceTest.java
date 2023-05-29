package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DefaultCartServiceTest {

    private static final int MAX_STOCK = 10;

    private CartService cartService;

    @Mock
    private ProductDao productDao;

    @Mock
    private HttpSession session;
    private Product testProduct;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        testProduct = new Product("test", "Test Product", new BigDecimal(100), Currency.getInstance("USD"), MAX_STOCK, "https://example.com/test.jpg", null);
        testProduct.setId(1L);
        cartService = DefaultCartService.getInstance();

        when(productDao.getProduct(any())).thenReturn(testProduct);

        Field productDaoField = cartService.getClass().getDeclaredField("productDao");
        productDaoField.setAccessible(true);
        productDaoField.set(cartService, productDao);
    }

    @Test
    public void testGetExistingCart() {
        Cart expectedCart = new Cart();
        when(session.getAttribute(any())).thenReturn(expectedCart);

        Cart result = cartService.getCart(session);

        assertEquals(expectedCart, result);
    }

    @Test
    public void testGetNotExistingCart(){
        when(session.getAttribute(any())).thenReturn(null);

        Cart result = cartService.getCart(session);

        assertNotNull(result);
    }

    @Test
    public void testAddToEmptyCart() throws OutOfStockException {
        Cart cart = new Cart();
        int quantity = MAX_STOCK / 2;

        cartService.add(cart, 1L, quantity);

        assertEquals(testProduct.getPrice().multiply(BigDecimal.valueOf(quantity)), cart.getTotalCost());
        assertEquals(quantity, cart.getTotalQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToCartWithInvalidQuantity() throws OutOfStockException {
        Cart cart = new Cart();
        int quantity = MAX_STOCK * 2;

        cartService.add(cart, 1L, quantity);
    }

    @Test
    public void testAddToExistingCart() throws OutOfStockException {
        Cart cart = new Cart();
        int quantity = MAX_STOCK / 3; //3
        cartService.add(cart, 1L, quantity);

        cartService.add(cart, 1L, 1);
        quantity++;

        assertEquals(testProduct.getPrice().multiply(BigDecimal.valueOf(quantity)), cart.getTotalCost());
        assertEquals(quantity, cart.getTotalQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToExistingCartWithInvalidQuantity() throws OutOfStockException {
        Cart cart = new Cart();
        Long productId = testProduct.getId();
        cartService.add(cart, productId, MAX_STOCK);

        cartService.add(cart, productId, 1);
    }

    @Test
    public void testUpdateCart() throws OutOfStockException {
        Cart cart = new Cart();
        Long productId = testProduct.getId();
        int quantity = MAX_STOCK / 2;
        cartService.add(cart, productId, 2);

        cartService.update(cart, productId, quantity);

        assertEquals(testProduct.getPrice().multiply(BigDecimal.valueOf(quantity)), cart.getTotalCost());
        assertEquals(quantity, cart.getTotalQuantity());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testUpdateCartNonExistentCartItem() throws OutOfStockException {
        Cart cart = new Cart();
        Long productId = testProduct.getId();
        int quantity = MAX_STOCK / 2;

        cartService.update(cart, productId, quantity);
    }

    @Test
    public void testDeleteCartItem() throws OutOfStockException {
        Cart cart = new Cart();
        productDao.save(testProduct);
        Long productId = testProduct.getId();
        int quantity = MAX_STOCK / 2;
        cartService.add(cart, productId, MAX_STOCK);

        cartService.delete(cart, productId);

        assertEquals(BigDecimal.valueOf(0), cart.getTotalCost());
        assertEquals(0, cart.getTotalQuantity());
    }

}
