package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Currency;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class DemoDataServletContextListener implements ServletContextListener {

    private final ProductDao productDao = ArrayListProductDao.getInstance();
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        boolean isInsertDemoData = Boolean.parseBoolean(sce.getServletContext().getInitParameter("insertDemoData"));
        if(isInsertDemoData){
            setSampleProducts();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    private void setSampleProducts(){
        Map<Date, BigDecimal> dateBigDecimalMap = new TreeMap<>();
        dateBigDecimalMap.put(Date.from(LocalDate.of(2020, 1, 8).atStartOfDay(ZoneId.systemDefault()).toInstant()), new BigDecimal(452));
        dateBigDecimalMap.put(Date.from(LocalDate.of(2021, 2, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()), new BigDecimal(356));
        dateBigDecimalMap.put(Date.from(LocalDate.of(2022, 8, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()), new BigDecimal(369));
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product( "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", dateBigDecimalMap));
        productDao.save(new Product( "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg", dateBigDecimalMap));
        productDao.save(new Product( "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", dateBigDecimalMap));
        productDao.save(new Product( "iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg", dateBigDecimalMap));
        productDao.save(new Product( "iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", dateBigDecimalMap));
        productDao.save(new Product( "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg", dateBigDecimalMap));
        productDao.save(new Product( "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg", dateBigDecimalMap));
        productDao.save(new Product( "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg", dateBigDecimalMap));
        productDao.save(new Product( "nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg", dateBigDecimalMap));
        productDao.save(new Product( "palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg", dateBigDecimalMap));
        productDao.save(new Product( "simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg", dateBigDecimalMap));
        productDao.save(new Product( "simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg", dateBigDecimalMap));
        productDao.save(new Product( "simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg", dateBigDecimalMap));
    }

}
