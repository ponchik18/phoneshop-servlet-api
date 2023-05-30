package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.NoSuchOrderException;
import com.es.phoneshop.model.order.Order;

import java.util.UUID;

public class ArrayListOrderDao extends GenericArrayListDao<Order> implements OrderDao {

    private volatile static ArrayListOrderDao instance;

    private ArrayListOrderDao() {
        super();
    }

    public static ArrayListOrderDao getInstance() {
        if (instance == null) {
            synchronized (ArrayListOrderDao.class) {
                if (instance == null) {
                    instance = new ArrayListOrderDao();
                }
            }
        }
        return instance;
    }

    @Override
    public Order getItem(UUID id) throws NoSuchOrderException {
        lock.readLock().lock();
        try {
            return items.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new NoSuchOrderException(id));
        } finally {
            lock.readLock().unlock();
        }
    }
}
