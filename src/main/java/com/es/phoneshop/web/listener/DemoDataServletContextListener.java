package com.es.phoneshop.web.listener;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.price.PriceHistory;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class DemoDataServletContextListener implements ServletContextListener {

    private final ProductDao productDao = ArrayListProductDao.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        boolean isInsertDemoData = Boolean.parseBoolean(servletContextEvent.getServletContext().getInitParameter("insertDemoData"));
        if (isInsertDemoData) {
            setSampleProducts();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContextListener.super.contextDestroyed(servletContextEvent);
    }

    private void setSampleProducts() {
        List<PriceHistory> priceHistories = new ArrayList<>();
        priceHistories.add(new PriceHistory(Date.from(LocalDate.of(2020, 1, 8).
                atStartOfDay(ZoneId.systemDefault()).toInstant()),
                new BigDecimal(452)));
        priceHistories.add(new PriceHistory(Date.from(LocalDate.of(2021, 2, 15)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()),
                new BigDecimal(356)));
        priceHistories.add(new PriceHistory(Date.from(LocalDate.of(2022, 8, 15)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()),
                new BigDecimal(369)));
        Currency usd = Currency.getInstance("USD");

        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", new ArrayList<>(priceHistories)));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg", new ArrayList<>(priceHistories)));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", new ArrayList<>(priceHistories)));
        productDao.save(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg", new ArrayList<>(priceHistories)));
        productDao.save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", new ArrayList<>(priceHistories)));
        productDao.save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg", new ArrayList<>(priceHistories)));
        productDao.save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg", new ArrayList<>(priceHistories)));
        productDao.save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg", new ArrayList<>(priceHistories)));
        productDao.save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg", new ArrayList<>(priceHistories)));
        productDao.save(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg", new ArrayList<>(priceHistories)));
        productDao.save(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg", new ArrayList<>(priceHistories)));
        productDao.save(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg", new ArrayList<>(priceHistories)));
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg", new ArrayList<>(priceHistories)));
    }

}
