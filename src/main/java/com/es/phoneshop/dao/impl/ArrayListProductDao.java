package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dto.SortField;
import com.es.phoneshop.dto.SortOrder;
import com.es.phoneshop.exception.NoSuchProductException;
import com.es.phoneshop.model.product.Product;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayListProductDao extends GenericArrayListDao<Product> implements ProductDao {

    private volatile static ArrayListProductDao instance;

    private ArrayListProductDao() {
        super(Product.class);
    }

    public static ArrayListProductDao getInstance() {
        if (instance == null) {
            synchronized (ArrayListProductDao.class) {
                if (instance == null) {
                    instance = new ArrayListProductDao();
                }
            }
        }
        return instance;
    }


    @Override
    public List<Product> findProducts(String search, SortField field, SortOrder order) {
        lock.readLock().lock();
        try {

            String[] searchWords = Optional.ofNullable(search).orElse(StringUtils.EMPTY).trim().split(" ");
            Comparator<Product> comparator = createComparator(field, order, searchWords);
            return items.stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .filter(product -> Objects.isNull(search) || search.isEmpty() || containsAnyWord(product.getDescription(), searchWords))
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }



    private boolean containsAnyWord(String productDescription, String[] searchWords) {
        return Arrays.stream(searchWords)
                .anyMatch(searchWord -> containsIgnoreCase(productDescription, searchWord));
    }

    private long countOfMatch(String productDescription, String[] searchWords) {
        return Arrays.stream(searchWords)
                .filter(searchWord -> containsIgnoreCase(productDescription, searchWord))
                .count();
    }

    private boolean containsIgnoreCase(String searchWord, String searchStr) {
        return searchWord.toLowerCase().contains(searchStr.toLowerCase());
    }

    private Comparator<Product> createComparator(SortField field, SortOrder order, String[] searchWords) {
        Comparator<Product> comparator = Comparator.comparing(p -> countOfMatch(p.getDescription(), searchWords));
        comparator = comparator.reversed();
        if (SortField.description == field) {
            comparator = comparator.thenComparing(Product::getDescription);
        } else if (SortField.price == field) {
            comparator = comparator.thenComparing(Product::getPrice);
        }
        if (SortOrder.desc == order) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

}
