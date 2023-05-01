package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testGetProduct(){
        assertNotNull(productDao.getProduct(1L));
    }

    @Test(expected = NullPointerException.class)
    public void testFindProductsNotHaveNullPrices() throws NullPointerException{
        Product product = new Product("sfold", "Samsung Galaxy Fold", null, Currency.getInstance("USD"), 100, "urlForImage");
        productDao.save(product);

        List<Product> productList = productDao.findProducts();
        Optional<Product> anyNullPricesProduct = productList.stream()
                .filter(prod->Objects.isNull(prod.getPrice()))
                .findAny();

        assertTrue(anyNullPricesProduct.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testFindProductsNotHaveNegativeStocks() throws NullPointerException{
        Product product = new Product("sfold", "Samsung Galaxy Fold", new BigDecimal(100), Currency.getInstance("USD"), -100, "urlForImage");
        productDao.save(product);

        List<Product> productList = productDao.findProducts();
        Optional<Product> anyNegativeStockProduct = productList.stream()
                .filter(prod->prod.getStock()<=0)
                .findAny();


        assertTrue(anyNegativeStockProduct.isEmpty());
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveProductById() throws ProductNotFoundException{
        Product product = new Product("sfold", "Samsung Galaxy Fold", new BigDecimal(100), Currency.getInstance("USD"), 100, "urlForImage");

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
        Product product = productDao.getProduct(1L);
        Long id = product.getId();

        productDao.delete(id);

        productDao.getProduct(id);
    }



    @Test
    public void testConcurrentSave() throws InterruptedException {
        final int numThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        int initialSize = productDao.findProducts().size();

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                Product product = createSampleProduct();
                productDao.save(product);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(numThreads+initialSize, productDao.findProducts().size());
    }

    private Product createSampleProduct() {
        Currency usd = Currency.getInstance("USD");
        return new Product("test", "Test Product", new BigDecimal(100), usd, 10, "https://example.com/test.jpg");
    }

}
