package com.es.phoneshop.service;

import com.es.phoneshop.exception.FractionalNumberException;
import com.es.phoneshop.exception.NegativeNumberException;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public interface ValidateParameterService {
    void setRequiredStringParameter(Map<String, String> errors, String parameterName,
                                    HttpServletRequest request, Consumer<String> consumer);
    void setRequiredPhoneParameter(Map<String, String> errors, HttpServletRequest request, Consumer<String> consumer);
    void setRequiredDeliveryDateParameter(Map<String, String> errors, HttpServletRequest request,
                                          Consumer<LocalDate> consumer);
    void setRequiredPaymentMethodParameter(Map<String, String> errors,
                                           HttpServletRequest request, Consumer<PaymentMethod> consumer);

    BigDecimal validatePrice(Map<String, String> errors, String price, String parameterName);

}
