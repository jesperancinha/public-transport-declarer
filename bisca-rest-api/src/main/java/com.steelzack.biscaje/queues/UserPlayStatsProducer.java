package com.steelzack.biscaje.queues;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;

/**
 * Created by joaofilipesabinoesperancinha on 06-07-16.
 */
@Stateless
@Remote(RemoteMessageProducer.class)
@JMSDestinationDefinitions(
        value = {
                @JMSDestinationDefinition(
                        name = "jms/StatsQueue",
                        interfaceName = "javax.jms.Queue",
                        destinationName = "playStats"
                )
        }
)
public class UserPlayStatsProducer implements RemoteMessageProducer{


    private static final long serialVersionUID = 1027344735528882571L;
    /**
     * JMS Context, This combines in a single object the functionality of two
     * separate objects from the JMS 1.1 API: a Connection and a Session.
     *
     */
    @Inject
    @JMSConnectionFactory("MyConnectionFactory")
    JMSContext context;

    /**
     * Queue
     */
    @Resource(mappedName = "StatsQueue")
    Queue queue;


    /**
     * Send Message.
     */
    @Override
    public void sendMessage(Stats stats) throws JMSException {
        ObjectMessage message = context.createObjectMessage();
        message.setObject(stats);
        context.createProducer().send(queue, message);
    }
}
