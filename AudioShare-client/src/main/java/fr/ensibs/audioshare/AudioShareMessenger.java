package fr.ensibs.audioshare;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

public class AudioShareMessenger {

    private static final String TOPIC_NAME = "AudioShare";

    private JMSContext session;

    private Destination destination;

    private JMSProducer sender;

    private JMSConsumer receiver;

    private MessageListener listener;

    private UserMessage user;


    /**
     * Constructor
     * @param host JMS server host name
     * @param port JMS server port number
     */
    public AudioShareMessenger(String host, int port) throws Exception{

        // initialize the JMS context properties
        System.setProperty("java.naming.factory.initial", "fr.dyade.aaa.jndi2.client.NamingContextFactory");
        System.setProperty("java.naming.factory.host", host);
        System.setProperty("java.naming.factory.port", Integer.toString(port));
        Context context = new InitialContext();

        // create the JMS connection and session
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        this.session = factory.createContext();

        // create a message producer
        this.destination = (Destination) context.lookup(TOPIC_NAME);
        this.sender = this.session.createProducer();
    }

    /**
     * Set the user
     * @param user the user
     */
    public void setUser (UserMessage user) throws Exception{
        this.user = user;
        this.listener = new AudioShareListener(user);
    }

    /**
     * Send JMS message which contains the music binary data
     * @param music the music to be sent
     */
    public void send(Music music) throws Exception
    {
        BytesMessage message = this.session.createBytesMessage();
        /*try (FileInputStream in = new FileInputStream(music.getFile())) {
            byte[] data = new byte[in.available()];
            in.read(data);
            message.writeBytes(data);
        }
        message.setStringProperty("owner", music.getOwner());
        message.setStringProperty("filename", music.getFile().getName());
        Properties tags = music.getTags();
        for (String key : tags.stringPropertyNames()) {
            message.setStringProperty(key, tags.getProperty(key));
        }*/
        this.sender.send(this.destination, message);
    }

    /**
     * Create receiver who subscribe to the topic to receive messages
     * @param tags tags that interest the receiver
     */
    public void subscribe(Properties tags) throws Exception
    {
        if (this.receiver != null) {
            this.receiver.close();
        }
        JMSContext consumerSession = session.createContext(JMSContext.AUTO_ACKNOWLEDGE);
        String selector = makeSelector(tags);
        if (selector != null) {
            this.receiver = consumerSession.createConsumer(destination, selector);
        } else {
            this.receiver = consumerSession.createConsumer(destination);
        }
        this.receiver.setMessageListener(this.listener);
    }

    /**
     * close the app
     */
    public void close()
    {
        try {
            this.session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a selector string from the given tags
     *
     * @param tags properties used to compose the selector
     */
    private String makeSelector(Properties tags)
    {
        Iterator<String> it = tags.stringPropertyNames().iterator();
        if (it.hasNext()) {
            String key = it.next();
            String selector = key + " = '" + tags.getProperty(key) + "'";
            while (it.hasNext()) {
                key = it.next();
                selector += " OR " + key + " = '" + tags.getProperty(key) + "'";
            }
            return selector;
        }
        return null;
    }
    
}
