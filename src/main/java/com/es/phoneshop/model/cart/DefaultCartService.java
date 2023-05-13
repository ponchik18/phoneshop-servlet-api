package com.es.phoneshop.model.cart;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

public class DefaultCartService implements CartService {

    public static final String CART_SESSION_NAME = DefaultCartService.class.getName() + ".class";
    private static DefaultCartService instance;
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
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_NAME);
        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_NAME, cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        CartItem foundedCartItem = cart.getItems().stream().filter(cartItem -> cartItem.getProduct().getId().equals(productId)).filter(cartItem -> cartItem.getProduct().getStock() > 0).findAny().orElse(null);
        if (Objects.isNull(foundedCartItem)) {
            cart.getItems().add(new CartItem(product, quantity));
        } else if (foundedCartItem.getQuantity() + quantity <= product.getStock()) {
            foundedCartItem.setQuantity(foundedCartItem.getQuantity() + quantity);
        } else {
            throw new OutOfStockException(product, foundedCartItem.getQuantity() + quantity, product.getStock());
        }
    }
}