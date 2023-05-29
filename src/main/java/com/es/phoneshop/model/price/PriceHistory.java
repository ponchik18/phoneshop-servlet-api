package com.es.phoneshop.model.price;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PriceHistory implements Serializable {
    private Date date;
    private BigDecimal price;

    public PriceHistory(Date date, BigDecimal price) {
        this.date = date;
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
