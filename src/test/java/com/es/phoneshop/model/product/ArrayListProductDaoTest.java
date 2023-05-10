package com.es.phoneshop.model.product;

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
    private ArrayList<Product> orderProduct;

    @Before
    public void setup() {
        orderProduct = new ArrayList<>();
        orderProduct.add(new Product("iphone14max", "Apple iPhone 14 Max 64gb", new BigDecimal(1568), Currency.getInstance("USD"), 100, "urlForImage", null));
        orderProduct.add(new Product("iPhone13max", "Apple iPhone 13 Max 64gb", new BigDecimal(685), Currency.getInstance("USD"), 100, "urlForImage", null));
        orderProduct.add(new Product("iphone14", "Apple iPhone 13 64gb ", new BigDecimal(985), Currency.getInstance("USD"), 100, "urlForImage", null));

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
        String descriptionSearch = product.getDescription().split(" ")[0];
        productDao.save(product);

        List<Product> foundedProducts = productDao.findProducts(descriptionSearch, null, null);

        assertTrue(foundedProducts.contains(product));

    }

    @Test
    public void testNotFindSamsungProduct() {
        Product product = products.get(0);
        String descriptionSearch = product.getDescription().split(" ")[0];
        productDao.save(product);

        List<Product> foundedProducts = productDao.findProducts("Apple", null, null);

        assertFalse(foundedProducts.contains(product));

    }

    @Test
    public void testFindProductOrder() {
        for (int i = orderProduct.size() - 1; i >= 0; i--) {
            productDao.save(orderProduct.get(i));
        }
        String query = orderProduct.get(0).getDescription();

        List<Product> foundedProduct = productDao.findProducts(query, null, null).subList(0, orderProduct.size());

        assertEquals(orderProduct, foundedProduct);

    }


    @Test
    public void testFindProductsHaveNullPrices() throws NullPointerException {
        Product testProduct = products.get(1);
        productDao.save(testProduct);

        List<Product> productList = productDao.findProducts("", null, null);
        Optional<Product> anyNullPricesProduct = productList.stream().filter(product -> Objects.isNull(product.getPrice())).findAny();

        assertTrue(anyNullPricesProduct.isEmpty());
    }

    @Test
    public void testFindProductsHaveNegativeStocks() throws NullPointerException {
        Product testProduct = products.get(2);
        productDao.save(testProduct);

        List<Product> productList = productDao.findProducts("", null, null);
        Optional<Product> anyNegativeStockProduct = productList.stream().filter(product -> product.getStock() <= 0).findAny();


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

        List<Product> compareList = productDao.findProducts("", field, order);

        assertEquals(sortProduct, compareList);
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

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                Product product = products.get(0);
                productDao.save(product);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(numThreads + initialSize, productDao.findProducts("", null, null).size());
    }

}
