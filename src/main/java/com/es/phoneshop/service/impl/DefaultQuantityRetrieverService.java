package com.es.phoneshop.service.impl;

import com.es.phoneshop.exception.FractionalNumberException;
import com.es.phoneshop.exception.NegativeNumberException;
import com.es.phoneshop.service.QuantityRetrieverService;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class DefaultQuantityRetrieverService implements QuantityRetrieverService {

    private volatile static DefaultQuantityRetrieverService instance;

    private DefaultQuantityRetrieverService() {
    }

    public static DefaultQuantityRetrieverService getInstance() {
        if (instance == null) {
            synchronized (DefaultQuantityRetrieverService.class) {
                if (instance == null) {
                    instance = new DefaultQuantityRetrieverService();
                }
            }
        }
        return instance;
    }

    @Override
    public int getProductQuantity(String quantityString, Locale locale)
            throws ParseException, FractionalNumberException, NegativeNumberException {
        NumberFormat format = NumberFormat.getInstance(locale);
        float quantityDouble = format.parse(quantityString).floatValue();
        if (quantityDouble != (int) quantityDouble) {
            throw new FractionalNumberException();
        }
        if (quantityDouble <= 0) {
            throw new NegativeNumberException();
        }
        return (int) quantityDouble;
    }
}
