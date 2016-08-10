package com.steelzack.biscaje.server;

import com.steelzack.biscaje.security.BiscaJESecurityGenerator;
import com.steelzack.biscaje.security.BiscaJESecurityGeneratorImpl;
import com.steelzack.biscaje.service.BiscaService;
import com.steelzack.biscaje.service.BiscaServiceImpl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * Created by joaofilipesabinoesperancinha on 10-08-16.
 */
@RunWith(Arquillian.class)
public class BiscaRestServerTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(
                        BiscaRestServer.class,
                        BiscaService.class,
                        BiscaServiceImpl.class,
                        BiscaJESecurityGenerator.class,
                        BiscaJESecurityGeneratorImpl.class
                )
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    BiscaRestServer biscaRestServer;


    @Test
    public void ping() throws Exception {
        String result = biscaRestServer.toString();
    }

    @Test
    public void createNewUser() throws Exception {

    }

}