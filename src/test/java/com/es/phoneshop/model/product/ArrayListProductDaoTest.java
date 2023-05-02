package com.es.phoneshop.model.product;

import org.codehaus.plexus.util.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;
    private ArrayList<Product> products;

    private ArrayList<Product> orderProduct;

    @Before
    public void setup() {
        orderProduct=new ArrayList<>();
        orderProduct.add(new Product("iphone14max", "abc qwe zxc rty", new BigDecimal(150), Currency.getInstance("USD"), 100, "urlForImage"));
        orderProduct.add(new Product("iPhone13max", "abc qwe rty", new BigDecimal(150), Currency.getInstance("USD"), 100, "urlForImage"));
        orderProduct.add(new Product("imac", "abc qwe", new BigDecimal(150), Currency.getInstance("USD"), 100, "urlForImage"));

        products= new ArrayList<>();
        products.add(new Product("sfold", "Samsung Galaxy Fold", new BigDecimal(150), Currency.getInstance("USD"), 100, "urlForImage"));
        products.add(new Product("wphone", "Windows Phone", null, Currency.getInstance("USD"), 100, "urlForImage"));
        products.add(new Product("redminote5", "Xiaomi Redmi Note5", new BigDecimal(65), Currency.getInstance("USD"), 0, "urlForImage"));

        productDao = new ArrayListProductDao();
    }

    @Test
    public void testGetProduct(){
        Product product = productDao.findProducts("").stream().findAny().get();

        Product searchProduct = productDao.getProduct(product.getId());

        assertEquals(product, searchProduct);
    }

    @Test
    public void testFindSamsungProduct(){
        Product product = products.get(0);
        String descriptionSearch = product.getDescription().split(" ")[0];
        productDao.save(product);

        List<Product> foundedProducts = productDao.findProducts(descriptionSearch);

        assertTrue(foundedProducts.contains(product));

    }

    @Test
    public void testNotFindSamsungProduct(){
        Product product = products.get(0);
        String descriptionSearch = product.getDescription().split(" ")[0];
        productDao.save(product);

        List<Product> foundedProducts = productDao.findProducts("Apple");

        assertFalse(foundedProducts.contains(product));

    }

    @Test
    public void testFindProductOrder(){
        for(int i=orderProduct.size()-1; i>=0; i--){
            productDao.save(orderProduct.get(i));
        }
        String query = orderProduct.get(0).getDescription();

        List<Product> foundedProduct = productDao.findProducts(query).subList(0,orderProduct.size());

        assertEquals(orderProduct, foundedProduct);

    }



    @Test
    public void testFindProductsHaveNullPrices() throws NullPointerException{
        Product product = products.get(1);
        productDao.save(product);

        List<Product> productList = productDao.findProducts("");
        Optional<Product> anyNullPricesProduct = productList.stream()
                .filter(prod->Objects.isNull(prod.getPrice()))
                .findAny();

        assertTrue(anyNullPricesProduct.isEmpty());
    }

    @Test
    public void testFindProductsHaveNegativeStocks() throws NullPointerException{
        Product product = products.get(2);
        productDao.save(product);

        List<Product> productList = productDao.findProducts("");
        Optional<Product> anyNegativeStockProduct = productList.stream()
                .filter(prod->prod.getStock()<=0)
                .findAny();


        assertTrue(anyNegativeStockProduct.isEmpty());
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts("").isEmpty());
    }

    @Test
    public void testSaveProductById() throws ProductNotFoundException{
        Product product = products.get(0);

        productDao.save(product);
        Product searchProduct = productDao.getProduct(product.getId());

        assertEquals(product, searchProduct);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAdd() throws NullPointerException{
        productDao.save(null);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProduct() throws ProductNotFoundException{
        Product product = productDao.findProducts("").stream().findAny().get();
        Long id = product.getId();

        productDao.delete(id);

        productDao.getProduct(id);
    }



    @Test
    public void testConcurrentSave() throws InterruptedException {
        final int numThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        int initialSize = productDao.findProducts("").size();

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                Product product = products.get(0);
                productDao.save(product);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(numThreads+initialSize, productDao.findProducts("").size());
    }



}
