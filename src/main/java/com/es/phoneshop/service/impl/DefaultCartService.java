package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.NoSuchElementException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class DefaultCartService implements CartService {

    public static final String CART_SESSION_NAME = DefaultCartService.class.getName() + ".class";
    private volatile static DefaultCartService instance;
    private ProductDao productDao;


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
        synchronized (session) {
            Cart cart = (Cart) session.getAttribute(CART_SESSION_NAME);
            if (cart == null) {
                cart = new Cart();
                session.setAttribute(CART_SESSION_NAME, cart);
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, UUID productId, int quantity) throws OutOfStockException {
        Product product = productDao.getItem(productId);

        synchronized (cart) {
            Optional<CartItem> optionalFoundCartItem = findCartItem(cart, productId);

            if (optionalFoundCartItem.isPresent()) {
                addProductToExistingCart(optionalFoundCartItem.get(), product, quantity);
            } else {
                addProductToNonExistingCart(cart, product, quantity);
            }
            recalculateCart(cart);
        }
    }

    @Override
    public void destroyCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_NAME);
    }

    @Override
    public void update(Cart cart, UUID productId, int quantity) throws OutOfStockException {
        Product product = productDao.getItem(productId);

        synchronized (cart) {
            Optional<CartItem> optionalFoundCartItem = findCartItem(cart, productId);
            if (optionalFoundCartItem.isPresent()) {
                validateOutOfStock(product, quantity);
                optionalFoundCartItem.get().setQuantity(quantity);
                recalculateCart(cart);
            } else {
                throw new NoSuchElementException(productId);
            }
        }
    }

    @Override
    public void delete(Cart cart, UUID productId) {
        synchronized (cart) {
            cart.getItems().stream()
                    .filter(cartItem -> productId.equals(cartItem.getProduct().getId()))
                    .findAny()
                    .ifPresent(cart.getItems()::remove);
            recalculateCart(cart);
        }
    }

    private void addProductToNonExistingCart(Cart cart, Product product, int quantity) throws OutOfStockException {
        validateOutOfStock(product, quantity);
        cart.getItems().add(new CartItem(product, quantity));
    }

    private void addProductToExistingCart(CartItem cartItem, Product product, int quantity)
            throws OutOfStockException {
        validateOutOfStock(product, quantity + cartItem.getQuantity());
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
    }

    private void validateOutOfStock(Product product, int quantity) throws OutOfStockException {
        if (quantity > product.getStock()) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
    }

    private Optional<CartItem> findCartItem(Cart cart, UUID productId) {
        return cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .filter(cartItem -> cartItem.getProduct().getStock() > 0)
                .findAny();
    }

    private void recalculateCart(Cart cart) {
        recalculateTotalPrice(cart);
        recalculateTotalQuantity(cart);
    }

    private void recalculateTotalPrice(Cart cart) {
        cart.setTotalCost(cart.getItems().stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO));
    }

    private void recalculateTotalQuantity(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .reduce(Integer::sum)
                .orElse(0));
    }

}