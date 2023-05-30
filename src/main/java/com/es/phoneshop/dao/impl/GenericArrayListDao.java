package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.DAO;
import com.es.phoneshop.exception.NoSuchElementException;
import com.es.phoneshop.model.IBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GenericArrayListDao<Bean extends IBean> implements DAO<Bean> {

    protected final ReadWriteLock lock = new ReentrantReadWriteLock();
    protected final List<Bean> items;

    protected GenericArrayListDao() {
        items = new ArrayList<>();
    }


    public Bean getItem(UUID id) throws NoSuchElementException {
        lock.readLock().lock();
        try {
            return items.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException(id));
        } finally {
            lock.readLock().unlock();
        }
    }



    @Override
    public List<Bean> getAllItem() {
        lock.readLock().lock();
        try{
            return items;
        }
        finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(Bean item) {
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
}
