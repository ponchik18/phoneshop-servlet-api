package com.es.phoneshop.dao;

import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.exception.NoSuchElementException;
import com.es.phoneshop.exception.NoSuchOrderException;
import com.es.phoneshop.model.order.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ArrayListOrderDaoTest {
    private OrderDao orderDao;

    @Before
    public void setup() {
        orderDao = ArrayListOrderDao.getInstance();
        orderDao.save(new Order());

    }

    @Test
    public void testGetProduct() {
        Order order = getAnyOrder();

        Order searchProduct = orderDao.getItem(order.getId());

        assertEquals(order, searchProduct);
    }

    @Test
    public void testSaveProductById() throws NoSuchElementException {
        Order order = new Order();

        orderDao.save(order);
        Order searchProduct = orderDao.getItem(order.getId());

        assertEquals(order, searchProduct);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAdd() throws NullPointerException {
        orderDao.save(null);
    }

    @Test(expected = NoSuchOrderException.class)
    public void testDeleteProduct() {
        Order order = new Order();
        orderDao.save(order);
        UUID id = order.getId();

        orderDao.delete(id);

        orderDao.getItem(id);
    }

    private Order getAnyOrder() {
        return orderDao.getAllItem().stream().findAny().get();
    }

}