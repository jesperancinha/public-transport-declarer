package com.steelzack.biscaje.binders;

import com.steelzack.biscaje.service.BiscaServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public class BiscaJEBinder extends AbstractBinder{
    @Override
    protected void configure() {
        bind(BiscaServiceImpl.class).to(BiscaServiceImpl.class);
    }
}
