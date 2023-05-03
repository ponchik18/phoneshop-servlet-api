package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private static ArrayListProductDao instance ;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private List<Product> products;
    private Long maxId = 0L;

    public static ArrayListProductDao getInstance(){
        if(instance ==null){
            synchronized (ArrayListProductDao.class){
                if(instance == null){
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
    public Product getProduct(Long id) throws ProductNotFoundException{
        lock.readLock().lock();
        try{
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(()->new ProductNotFoundException(id));
        }
        finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String search, SortField field, SortOrder order) {
        lock.readLock().lock();
        try {
            String[] queryElement = Optional.ofNullable(search).orElse("").trim().split(" ");
            Comparator<Product> comparator = createComparator(field, order);
            return products.stream()
                    .filter(product -> Objects.isNull(search) || search.isEmpty() || isContainAnyWorld(product.getDescription(), queryElement))
                    .sorted((prod1, prod2)-> getAnInt(queryElement, prod1, prod2))
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }
        finally {
            lock.readLock().unlock();
        }
    }

    private int getAnInt(String[] queryElement, Product prod1, Product prod2) {
        return countOfMatch(prod2.getDescription(), queryElement) - countOfMatch(prod1.getDescription(), queryElement);
    }

    @Override
    public void save(Product product){
        Objects.requireNonNull(product, "We can't add null!");
        lock.writeLock().lock();
        try {
            product.setId(++maxId);
            products.add(Objects.requireNonNull(product));
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id){
        lock.writeLock().lock();
        try {
            Product delProduct = products
                    .stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(()->new ProductNotFoundException(id));

            products.remove(delProduct);

        }
        finally {
            lock.writeLock().unlock();
        }
    }

    private boolean isContainAnyWorld(String prodDescription, String[] queryElements){
        for(String element: queryElements){
            if(prodDescription.contains(element))
                return true;
        }
        return false;
    }

    private int countOfMatch(String prodDescription, String[] queryElements){
        int count=0;
        for(String element: queryElements){
            if(prodDescription.contains(element))
                count++;
        }
        return count;
    }

    private Comparator<Product> createComparator(SortField field, SortOrder order){
        Comparator<Product> comparator;
        if(SortField.description==field)
            comparator = Comparator.comparing(Product::getDescription);
        else if(SortField.price == field)
            comparator =  Comparator.comparing(Product::getPrice);
        else{
            comparator = (Product o1, Product o2)-> 0;
        }
        if(SortOrder.desc  == order)
            comparator = comparator.reversed();
        return comparator;

    }

}
