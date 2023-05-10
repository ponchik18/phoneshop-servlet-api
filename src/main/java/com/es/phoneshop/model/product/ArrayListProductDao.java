package com.es.phoneshop.model.product;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private volatile static ArrayListProductDao instance;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<Product> products;
    private Long maxId = 0L;

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

    private ArrayListProductDao() {
        products = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        lock.readLock().lock();
        try {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String search, SortField field, SortOrder order) {
        lock.readLock().lock();
        try {

            String[] searchWords = Optional.ofNullable(search).orElse(StringUtils.EMPTY).trim().split(" ");
            Comparator<Product> comparator = createComparator(field, order, searchWords);
            return products.stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .filter(product -> Objects.isNull(search) || search.isEmpty() || isContainAnyWorld(product.getDescription(), searchWords))
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(Product product) {
        Objects.requireNonNull(product, "We can't add null!");
        lock.writeLock().lock();
        try {
            product.setId(++maxId);
            products.add(Objects.requireNonNull(product));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        lock.writeLock().lock();
        try {
            products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .ifPresent(products::remove);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean isContainAnyWorld(String productDescription, String[] searchWords) {
        return Arrays.stream(searchWords)
                .anyMatch(productDescription::contains);
    }

    private long countOfMatch(String productDescription, String[] searchWords) {
        return Arrays.stream(searchWords)
                .filter(productDescription::contains)
                .count();
    }

    private Comparator<Product> createComparator(SortField field, SortOrder order, String[] searchWords) {
        Comparator<Product> comparator = (Product o1, Product o2) -> (int) (countOfMatch(o1.getDescription(), searchWords)
                - countOfMatch(o2.getDescription(), searchWords));
        if (SortField.description == field)
            comparator = comparator.thenComparing(Product::getDescription);
        else if (SortField.price == field)
            comparator = comparator.thenComparing(Product::getPrice);

        if (SortOrder.desc == order)
            comparator = comparator.reversed();
        return comparator;

    }

}
