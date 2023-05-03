package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private List<Product> products;
    private Long maxId = 0L;

    public ArrayListProductDao() {
        products = new ArrayList<>();
        setSampleProducts();
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException{
        lock.readLock().lock();
        try{
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(ProductNotFoundException::new);
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
                    .orElseThrow(ProductNotFoundException::new);

            products.remove(delProduct);

        }
        finally {
            lock.writeLock().unlock();
        }
    }

    private void setSampleProducts(){
        Currency usd = Currency.getInstance("USD");
        save(new Product( "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        save(new Product( "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        save(new Product( "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        save(new Product( "iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        save(new Product( "iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        save(new Product( "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        save(new Product( "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        save(new Product( "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        save(new Product( "nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        save(new Product( "palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        save(new Product( "simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        save(new Product( "simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        save(new Product( "simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
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
