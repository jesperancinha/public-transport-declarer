package com.steelzack.biscaje.binders;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public class BiscaJEApplication extends ResourceConfig {

    public static final String COM_STEELZACK_BISCAJE_SECURITY = "com.steelzack.biscaje.security";
    public static final String COM_STEELZACK_BISCAJE_SERVICE = "com.steelzack.biscaje.service";

    public BiscaJEApplication() {
        register(new BiscaJEBinder());
        packages(true, COM_STEELZACK_BISCAJE_SERVICE, COM_STEELZACK_BISCAJE_SECURITY);
    }
}
