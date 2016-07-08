package com.steelzack.biscaje.queues;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

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
    private Stats stats;

    public UserPlayStatsProducer(Stats stats)
    {
        this.stats = stats;
    }

    public void run() {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("TEST.FOO");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a messages
            String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
            ObjectMessage message = session.createObjectMessage();
            message.setObject(stats);
            // Tell the producer to send the message
            System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
            producer.send(message);

            // Clean up
            session.close();
            connection.close();
        }
        catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }
}
