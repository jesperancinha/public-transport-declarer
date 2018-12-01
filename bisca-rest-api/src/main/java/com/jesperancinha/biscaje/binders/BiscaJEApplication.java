package com.jesperancinha.biscaje.binders;

import org.glassfish.jersey.server.ResourceConfig;

public class BiscaJEApplication extends ResourceConfig {

    private static final String SECURITY_PACKAGE = "com.jesperancinha.biscaje.security";
    private static final String SERVICE_PACKAGE = "com.jesperancinha.biscaje.service";

    public BiscaJEApplication() {
        register(new com.jesperancinha.biscaje.binders.BiscaJEBinder());
        packages(true, SERVICE_PACKAGE, SECURITY_PACKAGE);
    }
}
