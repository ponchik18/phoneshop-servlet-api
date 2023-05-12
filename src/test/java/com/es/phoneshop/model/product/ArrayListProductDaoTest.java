package com.es.phoneshop.model.product;

<<<<<<< HEAD
=======
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.dto.SortField;
import com.es.phoneshop.dto.SortOrder;
import com.es.phoneshop.exception.ProductNotFoundException;
>>>>>>> f456660 (Task 3.1: CartService)
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
<<<<<<< HEAD
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
=======
import java.util.*;
>>>>>>> f456660 (Task 3.1: CartService)
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

<<<<<<< HEAD
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
=======
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
>>>>>>> f456660 (Task 3.1: CartService)

        assertTrue(anyNullPricesProduct.isEmpty());
    }

<<<<<<< HEAD
    @Test(expected = NullPointerException.class)
    public void testFindProductsNotHaveNegativeStocks() throws NullPointerException{
        Product product = new Product("sfold", "Samsung Galaxy Fold", new BigDecimal(100), Currency.getInstance("USD"), -100, "urlForImage");
        productDao.save(product);

        List<Product> productList = productDao.findProducts();
        Optional<Product> anyNegativeStockProduct = productList.stream()
                .filter(prod->prod.getStock()<=0)
                .findAny();

=======
    @Test
    public void testFindProductsHaveNegativeStocks() throws NullPointerException {
        Product testProduct = products.get(2);
        productDao.save(testProduct);

        List<Product> result = productDao.findProducts("", null, null);
        Optional<Product> anyNegativeStockProduct = result.stream().filter(product -> product.getStock() <= 0).findAny();
>>>>>>> f456660 (Task 3.1: CartService)

        assertTrue(anyNegativeStockProduct.isEmpty());
    }

    @Test
    public void testFindProductsNoResults() {
<<<<<<< HEAD
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveProductById() throws ProductNotFoundException{
        Product product = new Product("sfold", "Samsung Galaxy Fold", new BigDecimal(100), Currency.getInstance("USD"), 100, "urlForImage");
=======
        assertFalse(productDao.findProducts("", null, null).isEmpty());
    }

    @Test
    public void testSaveProductById() throws ProductNotFoundException {
        Product product = products.get(0);
>>>>>>> f456660 (Task 3.1: CartService)

        productDao.save(product);
        Product searchProduct = productDao.getProduct(product.getId());

        assertEquals(product, searchProduct);
    }

    @Test(expected = NullPointerException.class)
<<<<<<< HEAD
    public void testNullAdd() throws NullPointerException{
=======
    public void testNullAdd() throws NullPointerException {
>>>>>>> f456660 (Task 3.1: CartService)
        productDao.save(null);
    }

    @Test(expected = ProductNotFoundException.class)
<<<<<<< HEAD
    public void testDeleteProduct() throws ProductNotFoundException{
        Product product = productDao.getProduct(1L);
=======
    public void testDeleteProduct() {
        Product product = products.get(0);
        productDao.save(product);
>>>>>>> f456660 (Task 3.1: CartService)
        Long id = product.getId();

        productDao.delete(id);

        productDao.getProduct(id);
    }

<<<<<<< HEAD
=======
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
>>>>>>> f456660 (Task 3.1: CartService)


    @Test
    public void testConcurrentSave() throws InterruptedException {
<<<<<<< HEAD
        final int numThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        int initialSize = productDao.findProducts().size();

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                Product product = createSampleProduct();
=======
        final int amountOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(amountOfThreads);
        int initialSize = productDao.findProducts("", null, null).size();
        final int expectedSize = initialSize + amountOfThreads;

        for (int i = 0; i < amountOfThreads; i++) {
            executorService.execute(() -> {
                Product product = products.get(0);
>>>>>>> f456660 (Task 3.1: CartService)
                productDao.save(product);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

<<<<<<< HEAD
        assertEquals(numThreads+initialSize, productDao.findProducts().size());
    }

    private Product createSampleProduct() {
        Currency usd = Currency.getInstance("USD");
        return new Product("test", "Test Product", new BigDecimal(100), usd, 10, "https://example.com/test.jpg");
=======
        assertEquals(expectedSize, productDao.findProducts("", null, null).size());
>>>>>>> f456660 (Task 3.1: CartService)
    }

}
