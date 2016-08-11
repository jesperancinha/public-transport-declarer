package com.steelzack.biscaje.server;

import com.steelzack.biscaje.security.BiscaJESecurityGenerator;
import com.steelzack.biscaje.security.BiscaJESecurityGeneratorImpl;
import com.steelzack.biscaje.service.BiscaService;
import com.steelzack.biscaje.service.BiscaServiceImpl;
import org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by joaofilipesabinoesperancinha on 10-08-16.
 */
@RunWith(Arquillian.class)
public class BiscaRestServerTest {

    @Deployment
    public static WebArchive createDeployment() throws IOException {
        return ShrinkWrap.create(WebArchive.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClasses(
                        BiscaRestServer.class,
                        BiscaService.class,
                        BiscaServiceImpl.class,
                        BiscaJESecurityGenerator.class,
                        BiscaJESecurityGeneratorImpl.class,
                        MongoDBDatastoreProvider.class
                );
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