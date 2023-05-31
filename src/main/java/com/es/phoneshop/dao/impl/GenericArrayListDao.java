package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.DAO;
import com.es.phoneshop.exception.NoSuchOrderException;
import com.es.phoneshop.exception.NoSuchProductException;
import com.es.phoneshop.exception.factory.NoSuchElementExceptionFactory;
import com.es.phoneshop.model.UniqueItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GenericArrayListDao<Item extends UniqueItem> implements DAO<Item> {

    protected final ReadWriteLock lock = new ReentrantReadWriteLock();
    protected final List<Item> items;
    private Class<Item> beanClass;
    private final NoSuchElementExceptionFactory exceptionFactory = new NoSuchElementExceptionFactory();

    protected GenericArrayListDao(Class<Item> beanClass) {
        items = new ArrayList<>();
        this.beanClass = beanClass;
    }

    @Override
    public List<Item> getAllItem() {
        lock.readLock().lock();
        try{
            return items;
        }
        finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(Item item) {
        Objects.requireNonNull(item, "We can't add null!");
        lock.writeLock().lock();
        try {
            item.setId(UUID.randomUUID());
            items.add(Objects.requireNonNull(item));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(UUID id) {
        lock.writeLock().lock();
        try {
            items.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .ifPresent(items::remove);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Item getItem(UUID id) throws NoSuchOrderException {
        lock.readLock().lock();
        try {
            return items.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> exceptionFactory.getException(beanClass,id));
        } finally {
            lock.readLock().unlock();
        }
    }
}
