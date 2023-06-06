package com.es.phoneshop.service.impl;

import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.ValidateParameterService;
import com.es.phoneshop.service.impl.DefaultValidateParameterService;
import com.es.phoneshop.web.constant.ServletConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DefaultValidateParameterServiceTest {

    private ValidateParameterService validateParameterService;

    @Mock
    private HttpServletRequest request;
    @Mock
    Consumer<String> stringConsumer;
    @Mock
    Consumer<LocalDate> localDateConsumer;
    @Mock
    Consumer<PaymentMethod> paymentMethodConsumer;


    @Before
    public void setup() {
        validateParameterService = DefaultValidateParameterService.getInstance();
    }

    @Test
    public void testSetRequiredStringParameter() {
        Map<String, String> errors = new HashMap<>();
        String parameterName = "name";
        String parameterValue = "John Doe";
        when(request.getParameter(parameterName)).thenReturn(parameterValue);

        validateParameterService.setRequiredStringParameter(errors, parameterName, request, stringConsumer);

        assertEquals(0, errors.size());
        verify(stringConsumer).accept(parameterValue);
    }

    @Test
    public void testSetRequiredPhoneParameter() {
        Map<String, String> errors = new HashMap<>();
        String parameterName = ServletConstant.RequestParameterName.PHONE;
        String parameterValue = "(333) 333-3333";
        when(request.getParameter(parameterName)).thenReturn(parameterValue);

        validateParameterService.setRequiredPhoneParameter(errors, request, stringConsumer);

        assertEquals(0, errors.size());
        verify(stringConsumer).accept(parameterValue);
    }

    @Test
    public void testSetRequiredDeliveryDateParameter() {
        Map<String, String> errors = new HashMap<>();
        String parameterName = ServletConstant.RequestParameterName.DELIVERY_DATE;
        String parameterValue = "2023-06-01";
        when(request.getParameter(parameterName)).thenReturn(parameterValue);

        validateParameterService.setRequiredDeliveryDateParameter(errors, request, localDateConsumer);

        assertEquals(0, errors.size());
        verify(localDateConsumer).accept(LocalDate.parse(parameterValue));
    }

    @Test
    public void testSetRequiredPaymentMethodParameter() {
        Map<String, String> errors = new HashMap<>();
        String parameterName = ServletConstant.RequestParameterName.PAYMENT_METHOD;
        String parameterValue = "CREDIT_CARD";
        when(request.getParameter(parameterName)).thenReturn(parameterValue);

        validateParameterService.setRequiredPaymentMethodParameter(errors, request, paymentMethodConsumer);

        assertEquals(0, errors.size());

        verify(paymentMethodConsumer).accept(PaymentMethod.valueOf(parameterValue));
    }
}
