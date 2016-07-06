package com.steelzack.biscaje.queues;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;

import static org.junit.Assert.assertNotNull;

/**
 * Created by joaofilipesabinoesperancinha on 06-07-16.
 */
@RunWith(Arquillian.class)
public class RemoteMessageProducerTest {

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClasses(
                        UserPlayStatsProducer.class,
                        UserPlayStatsConsumer.class
                )
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("hornetq-jms.xml")
                .addAsManifestResource("standalone-full.xml");

    }

    @Inject
    private RemoteMessageProducer userPlayStatsProducer;

    @Inject
    private RemoteMessageConsumer userPlayStatsConsumer;

    @Resource(lookup = "MyConnectionFactory")
    ConnectionFactory myConnectionFactory;

    @Produces
    @JMSConnectionFactory("MyConnectionFactory")
    JMSContext context;

    @Test
    public void sendMessage() throws Exception {
        assertNotNull(userPlayStatsProducer);
        assertNotNull(userPlayStatsConsumer);

        Stats stats = new Stats();

        userPlayStatsProducer.sendMessage(stats);

        Stats result = userPlayStatsConsumer.receiveMessage();
    }

}