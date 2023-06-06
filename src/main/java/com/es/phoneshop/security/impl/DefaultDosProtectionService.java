package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultDosProtectionService implements DosProtectionService {

    private static final long THRESHOLD = 20;
    private volatile static DefaultDosProtectionService instance;
    private final Map<String, Long> countMap = new ConcurrentHashMap<>();

    {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(countMap::clear, 0, 1, TimeUnit.MINUTES);
    }

    private DefaultDosProtectionService() {
    }

    public static DefaultDosProtectionService getInstance() {
        if (instance == null) {
            synchronized (DefaultDosProtectionService.class) {
                if (instance == null) {
                    instance = new DefaultDosProtectionService();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip) {
        AtomicBoolean isAllowed = new AtomicBoolean(true);
        countMap.computeIfPresent(ip, (key, count) -> {
            if (count < THRESHOLD) {
                return count + 1;
            } else {
                isAllowed.set(false);
                return count;
            }
        });
        countMap.computeIfAbsent(ip, k->1L);
        return isAllowed.get();
    }
}
