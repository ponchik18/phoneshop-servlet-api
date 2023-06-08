package com.es.phoneshop.service.impl;

import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.ValidateParameterService;
import com.es.phoneshop.web.constant.ServletConstant;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class DefaultValidateParameterService implements ValidateParameterService {

    private volatile static DefaultValidateParameterService instance;

    private DefaultValidateParameterService() {
    }

    public static DefaultValidateParameterService getInstance() {
        if (instance == null) {
            synchronized (DefaultValidateParameterService.class) {
                if (instance == null) {
                    instance = new DefaultValidateParameterService();
                }
            }
        }
        return instance;
    }

    @Override
    public void setRequiredStringParameter(Map<String, String> errors, String parameterName,
                                           HttpServletRequest request, Consumer<String> consumer) {
        String parameter = request.getParameter(parameterName);
        if (Objects.isNull(parameter) || parameter.trim().isEmpty()) {
            errors.put(parameterName, ServletConstant.Message.ERROR_VALUE_IS_REQUIRED);
        } else {
            consumer.accept(parameter.trim());
        }
    }

    @Override
    public void setRequiredPhoneParameter(Map<String, String> errors, HttpServletRequest request,
                                          Consumer<String> consumer) {
        String parameterName = ServletConstant.RequestParameterName.PHONE;
        String parameter = request.getParameter(parameterName);
        if (Objects.isNull(parameter) || parameter.trim().isEmpty()) {
            errors.put(parameterName, ServletConstant.Message.ERROR_VALUE_IS_REQUIRED);
        } else if (parameter.trim().matches(ServletConstant.Regex.PHONE_REGEX)) {
            consumer.accept(parameter);
        } else {
            errors.put(parameterName, ServletConstant.Message.ERROR_INVALID_FORMAT);
        }
    }

    @Override
    public void setRequiredDeliveryDateParameter(Map<String, String> errors, HttpServletRequest request,
                                                 Consumer<LocalDate> consumer) {
        String parameterName = ServletConstant.RequestParameterName.DELIVERY_DATE;
        String parameter = request.getParameter(parameterName);
        if (Objects.isNull(parameter) || parameter.trim().isEmpty()) {
            errors.put(parameterName, ServletConstant.Message.ERROR_VALUE_IS_REQUIRED);
        } else {
            try {
                LocalDate deliveryDate = LocalDate.parse(parameter);
                LocalDate compareDate = LocalDate.now().minusDays(1);
                if (deliveryDate.isBefore(compareDate)) {
                    errors.put(parameterName, ServletConstant.Message.ERROR_INVALID_DATE);
                }
                consumer.accept(deliveryDate);
            } catch (DateTimeParseException exception) {
                errors.put(parameterName, ServletConstant.Message.ERROR_INVALID_FORMAT);
            }
        }
    }

    @Override
    public void setRequiredPaymentMethodParameter(Map<String, String> errors,
                                                  HttpServletRequest request, Consumer<PaymentMethod> consumer) {
        String parameterName = ServletConstant.RequestParameterName.PAYMENT_METHOD;
        String parameter = request.getParameter(parameterName);
        if (Objects.isNull(parameter) || parameter.trim().isEmpty()) {
            errors.put(parameterName, ServletConstant.Message.ERROR_VALUE_IS_REQUIRED);
        } else {
            PaymentMethod paymentMethod = PaymentMethod.valueOf(parameter);
            consumer.accept(paymentMethod);
        }
    }

    @Override
    public BigDecimal validatePrice(Map<String, String> errors, String price, String parameterName) {
        if (Objects.isNull(price) || price.trim().isEmpty()) {
            return null;
        } else {
            try {
                BigDecimal priceDecimal = new BigDecimal(price.trim());
                if (priceDecimal.compareTo(new BigDecimal("0")) < 0) {
                    errors.put(parameterName, "Negative doesn't allow");
                    return null;
                }
                return priceDecimal;
            } catch (NumberFormatException exception) {
                errors.put(parameterName, "Not a number");
                return null;
            }
        }
    }
}
