package com.es.phoneshop.web.filter;

import com.es.phoneshop.security.DosProtectionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    private final DosFilter filter = new DosFilter();
    @Mock
    private FilterChain filterChain;
    @Mock
    private HttpSession sessionTest;
    @Mock
    private DosProtectionService dosProtectionService;
    @Mock
    private HttpServletResponse servletResponse;
    @Mock
    private HttpServletRequest servletRequest;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Field cartPageServletFieldOrderService = filter.getClass().getDeclaredField("dosProtectionService");
        cartPageServletFieldOrderService.setAccessible(true);
        cartPageServletFieldOrderService.set(filter, dosProtectionService);
    }

    @Test
    public void testDoFilterSuccess() throws ServletException, IOException {
        when(dosProtectionService.isAllowed(any())).thenReturn(Boolean.TRUE);

        filter.doFilter(servletRequest, servletResponse, filterChain);

        verify(filterChain, only()).doFilter(servletRequest, servletResponse);

    }

    @Test
    public void testFailDoFilter() throws ServletException, IOException {
        when(dosProtectionService.isAllowed(any())).thenReturn(Boolean.FALSE);

        filter.doFilter(servletRequest, servletResponse, filterChain);

        verify((HttpServletResponse) servletResponse).setStatus(429);
    }

}