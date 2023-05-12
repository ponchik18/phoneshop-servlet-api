package com.es.phoneshop.model.cart;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;

import java.util.Objects;

public class DefaultCartService implements CartService {

    private static DefaultCartService instance;
    private final Cart cart;
    private final ProductDao productDao;

    private DefaultCartService() {
        cart = new Cart();
        productDao = ArrayListProductDao.getInstance();
    }

    public static DefaultCartService getInstance() {
        if (instance == null) {
            synchronized (ArrayListProductDao.class) {
                if (instance == null) {
                    instance = new DefaultCartService();
                }
            }
        }
        return instance;
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void add(Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        CartItem foundedCartItem = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .filter(cartItem -> cartItem.getProduct().getStock() > 0)
                .findAny()
                .orElse(null);
        if (Objects.isNull(foundedCartItem)) {
            cart.getItems().add(new CartItem(product, quantity));
        } else if (foundedCartItem.getQuantity() + quantity <= product.getStock()) {
            foundedCartItem.setQuantity(foundedCartItem.getQuantity() + quantity);
        } else {
            throw new OutOfStockException(product, foundedCartItem.getQuantity() + quantity, product.getStock());
        }
    }
}