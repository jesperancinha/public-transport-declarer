package com.steelzack.biscaje.binders;

import com.steelzack.biscaje.security.BiscaJESecurityGenerator;
import com.steelzack.biscaje.security.BiscaJESecurityGeneratorImpl;
import com.steelzack.biscaje.service.BiscaService;
import com.steelzack.biscaje.service.BiscaServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;


/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public class BiscaJEBinder extends AbstractBinder{
    @Override
    protected void configure() {
        bind(BiscaJESecurityGeneratorImpl.class).to(BiscaJESecurityGenerator.class);
        bind(BiscaServiceImpl.class).to(BiscaService.class);
    }
}
