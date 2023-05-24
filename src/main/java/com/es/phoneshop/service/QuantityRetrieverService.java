package com.es.phoneshop.service;

import com.es.phoneshop.exception.FractionalNumberException;
import com.es.phoneshop.exception.NegativeNumberException;

import java.text.ParseException;
import java.util.Locale;

public interface QuantityRetrieverService {
    int getProductQuantity(String quantityString, Locale locale)
            throws ParseException, FractionalNumberException, NegativeNumberException;
}
