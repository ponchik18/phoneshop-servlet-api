package com.es.phoneshop.service.iml;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class DefaultCartService implements CartService {

    public static final String CART_SESSION_NAME = DefaultCartService.class.getName() + ".class";
    private volatile static DefaultCartService instance;
    private final ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static DefaultCartService getInstance() {
        if (instance == null) {
            synchronized (DefaultCartService.class) {
                if (instance == null) {
                    instance = new DefaultCartService();
                }
            }
        }
        return instance;
    }

    @Override
    public Cart getCart(HttpSession session) {
        synchronized (session.getId().intern()) {
            Cart cart = (Cart) session.getAttribute(CART_SESSION_NAME);
            if (cart == null) {
                session.setAttribute(CART_SESSION_NAME, cart = new Cart());
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity, String sessionId) throws OutOfStockException {
        Product product = productDao.getProduct(productId);

        synchronized (cart) {
            Optional<CartItem> foundCartItem = cart.getItems().stream()
                    .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                    .filter(cartItem -> cartItem.getProduct().getStock() > 0)
                    .findAny();

            if (foundCartItem.isPresent()) {
                addProductToExistingCart(foundCartItem.get(), product, quantity);
            } else {
                addProductToNonExistingCart(cart, product, quantity);
            }
        }
    }

    private void addProductToNonExistingCart(Cart cart, Product product, int quantity) throws OutOfStockException {
        validateOutOfStock(product, quantity);
        cart.getItems().add(new CartItem(product, quantity));
    }

    private void addProductToExistingCart(CartItem cartItem, Product product, int quantity)
            throws OutOfStockException {
        validateOutOfStock( product, quantity+cartItem.getQuantity());
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
    }

    private void validateOutOfStock( Product product, int quantity) throws OutOfStockException {
        if (quantity > product.getStock() ) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
    }

}