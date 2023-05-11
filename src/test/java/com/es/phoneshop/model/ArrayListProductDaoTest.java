package com.es.phoneshop.model;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.implementation.ArrayListProductDao;
import com.es.phoneshop.dto.SortField;
import com.es.phoneshop.dto.SortOrder;
import com.es.phoneshop.exception.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private ArrayList<Product> products;
    private ArrayList<Product> sortedProducts;

    @Before
    public void setup() {
        sortedProducts = new ArrayList<>();
        sortedProducts.add(new Product("iphone14max", "Apple iPhone 14 Max 64gb", new BigDecimal(1568), Currency.getInstance("USD"), 100, "urlForImage", null));
        sortedProducts.add(new Product("iPhone13max", "Apple iPhone 13 Max 64gb", new BigDecimal(685), Currency.getInstance("USD"), 100, "urlForImage", null));
        sortedProducts.add(new Product("iphone14", "Apple iPhone 13 64gb", new BigDecimal(985), Currency.getInstance("USD"), 100, "urlForImage", null));

        products = new ArrayList<>();
        products.add(new Product("sfold", "Samsung Galaxy Fold", new BigDecimal(150), Currency.getInstance("USD"), 100, "urlForImage", null));
        products.add(new Product("wphone", "Windows Phone", null, Currency.getInstance("USD"), 100, "urlForImage", null));
        products.add(new Product("redminote5", "Xiaomi Redmi Note5", new BigDecimal(65), Currency.getInstance("USD"), 0, "urlForImage", null));

        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void testGetProduct() {
        Product product = productDao.findProducts("", null, null).stream().findAny().get();

        Product searchProduct = productDao.getProduct(product.getId());

        assertEquals(product, searchProduct);
    }

    @Test
    public void testFindSamsungProduct() {
        Product product = products.get(0);
        String expectedDescription = product.getDescription().split(" ")[0];
        productDao.save(product);

        List<Product> foundedProducts = productDao.findProducts(expectedDescription, null, null);

        assertTrue(foundedProducts.contains(product));

    }

    @Test
    public void testNotFindSamsungProduct() {
        Product product = products.get(0);
        productDao.save(product);

        List<Product> result = productDao.findProducts("Apple", null, null);

        assertFalse(result.contains(product));

    }

    @Test
    public void testFindProductOrder() {
        sortedProducts.forEach(productDao::save);
        String query = sortedProducts.get(0).getDescription();

        List<Product> result = productDao.findProducts(query, null, null).subList(0, sortedProducts.size());

        assertEquals(sortedProducts, result);

    }


    @Test
    public void testFindProductsHaveNullPrices() throws NullPointerException {
        Product testProduct = products.get(1);
        productDao.save(testProduct);

        List<Product> result = productDao.findProducts("", null, null);
        Optional<Product> anyNullPricesProduct = result.stream().filter(product -> Objects.isNull(product.getPrice())).findAny();

        assertTrue(anyNullPricesProduct.isEmpty());
    }

    @Test
    public void testFindProductsHaveNegativeStocks() throws NullPointerException {
        Product testProduct = products.get(2);
        productDao.save(testProduct);

        List<Product> result = productDao.findProducts("", null, null);
        Optional<Product> anyNegativeStockProduct = result.stream().filter(product -> product.getStock() <= 0).findAny();

        assertTrue(anyNegativeStockProduct.isEmpty());
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts("", null, null).isEmpty());
    }

    @Test
    public void testSaveProductById() throws ProductNotFoundException {
        Product product = products.get(0);

        productDao.save(product);
        Product searchProduct = productDao.getProduct(product.getId());

        assertEquals(product, searchProduct);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAdd() throws NullPointerException {
        productDao.save(null);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProduct() {
        Product product = products.get(0);
        productDao.save(product);
        Long id = product.getId();

        productDao.delete(id);

        productDao.getProduct(id);
    }

    @Test
    public void testSortAscFindProducts() {
        List<Product> sortProduct = productDao.findProducts("", null, null);
        sortProduct.sort(Comparator.comparing(Product::getPrice));
        SortField field = SortField.price;
        SortOrder order = SortOrder.asc;

        List<Product> result = productDao.findProducts("", field, order);

        assertEquals(sortProduct, result);
    }

    @Test
    public void testSortDescFindProducts() {
        List<Product> sortProduct = productDao.findProducts("", null, null);
        sortProduct.sort(Comparator.comparing(Product::getDescription, Comparator.reverseOrder()));
        SortField field = SortField.description;
        SortOrder order = SortOrder.desc;

        List<Product> compareList = productDao.findProducts("", field, order);

        assertEquals(sortProduct, compareList);
    }


    @Test
    public void testConcurrentSave() throws InterruptedException {
        final int numThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        int initialSize = productDao.findProducts("", null, null).size();
        final int expectedSize = initialSize + numThreads;

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                Product product = products.get(0);
                productDao.save(product);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(expectedSize, productDao.findProducts("", null, null).size());
    }

}
