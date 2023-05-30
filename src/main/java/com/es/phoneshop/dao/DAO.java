package com.es.phoneshop.dao;

import com.es.phoneshop.model.IBean;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface DAO<Bean extends IBean> {
    Bean getItem(UUID id) throws NoSuchElementException;

    List<Bean> getAllItem();

    void save(Bean item);

    void delete(UUID id);
}
