package com.es.phoneshop.web;

<<<<<<< HEAD
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
=======
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
<<<<<<< HEAD
>>>>>>> f456660 (Task 3.1: CartService)
=======
import jakarta.servlet.RequestDispatcher;
>>>>>>> b8dceb9 (Task 3.3: HttpSessionCartService)
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private ServletConfig config;

    @Mock
    private ArrayListProductDao productDao;

    private final ProductListPageServlet servlet = new ProductListPageServlet();

    private final ArrayList<Product> products = new ArrayList<>();

    @Before
    public void setup() throws ServletException {

        servlet.init(config);
        servlet.setProductDao(productDao);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        products.add(new Product("sfold", "Samsung Galaxy Fold", new BigDecimal(100), Currency.getInstance("USD"), 100, "urlForImage"));
        products.add(new Product("iphone14", "IPhone 14", new BigDecimal(256), Currency.getInstance("USD"), 100, "urlForImage"));
        when(productDao.findProducts()).thenReturn(products);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("products"), eq(products));
    }

}