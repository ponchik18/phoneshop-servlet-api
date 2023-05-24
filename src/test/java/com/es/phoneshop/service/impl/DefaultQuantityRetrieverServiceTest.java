package com.es.phoneshop.service.impl;

import com.es.phoneshop.exception.FractionalNumberException;
import com.es.phoneshop.exception.NegativeNumberException;
import com.es.phoneshop.service.QuantityRetrieverService;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Locale;

import static org.junit.Assert.*;

public class DefaultQuantityRetrieverServiceTest {

    private QuantityRetrieverService quantityRetrieverService;
    private Locale locale;

    @Before
    public void setup() {
        quantityRetrieverService = DefaultQuantityRetrieverService.getInstance();
        locale = new Locale("ru");
    }

    @Test
    public void testSuccessQuantityRetriever() throws NegativeNumberException, ParseException, FractionalNumberException {
        String quantityString = "5";
        int expectedQuantity = 5;

        int result = quantityRetrieverService.getProductQuantity(quantityString, locale);

        assertEquals(expectedQuantity, result);
    }

    @Test(expected = FractionalNumberException.class)
    public void testQuantityRetrieverWithFractionalNumberException() throws NegativeNumberException, ParseException, FractionalNumberException {
        String quantityString = "4,6";

        int result = quantityRetrieverService.getProductQuantity(quantityString, locale);
    }

    @Test(expected = NegativeNumberException.class)
    public void testQuantityRetrieverWithNegativeNumberException() throws NegativeNumberException, ParseException, FractionalNumberException {
        String quantityString = "-5";

        int result = quantityRetrieverService.getProductQuantity(quantityString, locale);
    }

    @Test(expected = ParseException.class)
    public void testQuantityRetrieverWithParseException() throws NegativeNumberException, ParseException, FractionalNumberException {
        String quantityString = "asdf";

        int result = quantityRetrieverService.getProductQuantity(quantityString, locale);
    }
}
