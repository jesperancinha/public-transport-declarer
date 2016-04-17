package com.steelzack.biscaje.binders;

import com.steelzack.biscaje.service.BiscaService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public class BiscaJEBinder extends AbstractBinder{
    @Override
    protected void configure() {
        bind(BiscaService.class).to(BiscaService.class);
    }
}
