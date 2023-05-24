package com.es.phoneshop.web.listener;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DemoDataServletContextListenerTest {
    @Mock
    private ServletContextEvent servletContextEvent;
    @Mock
    private ServletContext servletContext;
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private DemoDataServletContextListener listener;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);

        Field productDaoField = listener.getClass().getDeclaredField("productDao");
        productDaoField.setAccessible(true);
        productDaoField.set(listener, productDao);
    }

    @Test
    public void testWhenInsertDataFalse() {
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("false");

        listener.contextInitialized(servletContextEvent);

        verify(productDao, never()).save(any(Product.class));
    }

    @Test
    public void testWhenInsertDataTrue() {
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");

        listener.contextInitialized(servletContextEvent);

        verify(productDao, times(13)).save(any());
    }

}
