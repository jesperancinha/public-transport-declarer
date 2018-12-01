package com.jesperancinha.biscaje.binders;

import com.jesperancinha.biscaje.security.BiscaJESecurityGenerator;
import com.jesperancinha.biscaje.security.BiscaJESecurityGeneratorImpl;
import com.jesperancinha.biscaje.service.BiscaService;
import com.jesperancinha.biscaje.service.BiscaUserDao;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public class BiscaJEBinder extends AbstractBinder {
	@Override
	protected void configure() {
		bind(BiscaJESecurityGeneratorImpl.class).to(BiscaJESecurityGenerator.class);
		bind(BiscaUserDao.class).to(BiscaService.class);
	}
}
