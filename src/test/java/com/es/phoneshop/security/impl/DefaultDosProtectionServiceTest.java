package com.es.phoneshop.security.impl;
import com.es.phoneshop.security.DosProtectionService;
import com.es.phoneshop.security.impl.DefaultDosProtectionService;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultDosProtectionServiceTest {
    private static final int THRESHOLD_VALUE = 20;
    private DefaultDosProtectionService dosProtectionService;

    @Before
    public void setUp() {
        dosProtectionService = DefaultDosProtectionService.getInstance();
    }

    @Test
    public void testBlockedRequest() {
        final String ip = "127.0.0.1";
        IntStream.rangeClosed(0, THRESHOLD_VALUE)
                .forEach(i -> dosProtectionService.isAllowed(ip));

        boolean result = dosProtectionService.isAllowed(ip);

        assertFalse(result);
    }

    @Test
    public void testInactiveIp() {
        final String ip = "127.0.0.6";

        boolean result = dosProtectionService.isAllowed(ip);

        assertTrue(result);
    }
}
