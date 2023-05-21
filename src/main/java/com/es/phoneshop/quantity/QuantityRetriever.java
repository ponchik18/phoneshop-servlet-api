package com.es.phoneshop.quantity;

import com.es.phoneshop.exception.FractionalNumberException;
import com.es.phoneshop.exception.NegativeNumberException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class QuantityRetriever {
    public static int getProductQuantity(String quantityString, Locale locale)
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
