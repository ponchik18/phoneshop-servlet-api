package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveProductById(){
        Product product = new Product("sfold", "Samsung Galaxy Fold", new BigDecimal(100), Currency.getInstance("USD"), 100, "urlForImage");
        productDao.save(product);
        Long id = product.getId();
        try {
            Product searchProduct = productDao.getProduct(id);
            assertEquals(product, searchProduct);
        } catch (ProductNotFoundException e) {
            fail();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullAdd(){
        productDao.save(null);
    }

    @Test
    public void testDeleteProduct(){
        Product product = new Product("sfold", "Samsung Galaxy Fold", new BigDecimal(100), Currency.getInstance("USD"), 100, "urlForImage");
        productDao.save(product);
        Long id = product.getId();
        productDao.delete(id);
        try{
            productDao.getProduct(id);
            fail();
        } catch (ProductNotFoundException e) {
            assertTrue(true);
        }
    }

}
