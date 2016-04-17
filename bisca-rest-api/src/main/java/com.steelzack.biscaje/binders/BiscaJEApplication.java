package com.steelzack.biscaje.binders;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public class BiscaJEApplication  extends ResourceConfig{
        public BiscaJEApplication() {
            register(new BiscaJEBinder());
            packages(true, "com.steelzack.biscaje.service");
        }
}
