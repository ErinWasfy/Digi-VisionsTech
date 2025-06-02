package com.stc.rentalplatform.tenant;

import org.springframework.stereotype.Component;

@Component
public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static String getTenant() {
        return currentTenant.get();
    }

    public static void setTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public static void clear() {
        currentTenant.remove();
    }
}
