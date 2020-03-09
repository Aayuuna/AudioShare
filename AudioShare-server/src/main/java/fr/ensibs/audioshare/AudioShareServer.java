package fr.ensibs.audioshare;

import fr.ensibs.joram.JoramAdmin;
import fr.ensibs.joram.Joram;
import javax.jms.Topic;

/**
 * Launch a JMS server and creates a topic to share musics
 */

public class AudioShareServer {

    private static final String TOPIC_NAME = "AudioShare";

    private JoramAdmin admin;

    /**
     * Print a usage message and exits
     */
    private static void usage()
    {
        System.out.println("java AudioShareServer <port>");
        System.out.println("Launch a JMS server and creates the " + TOPIC_NAME + " topic if needed");
        System.exit(0);
    }

    /**
     * Program entry point
     *
     * @param args see {@link usage()}
     */
    public static void main(String[] args) throws Exception
    {
        if (args.length != 1 || args[0].equals("-h")) {
            usage();
        }

        try {
            int port = Integer.parseInt(args[0]);
            AudioShareServer server = new AudioShareServer(port);
            server.makeTopic(TOPIC_NAME);
        } catch (NumberFormatException e) {
            usage();
        }
    }

    public AudioShareServer(int port) throws Exception {

        Joram joram = new Joram(port);
        joram.start();
        Thread.sleep(3000);
        this.admin = new JoramAdmin("localhost", port);
    }

    /**
     * Create a topic having the given name if it doesn't already exists
     *
     * @param topicName name of the topic to be created
     */
    public void makeTopic(String topicName) throws Exception
    {
        if (!this.hasTopic(topicName)) {
            this.admin.createTopic(topicName);
        } else {
            System.out.println(topicName + " topic already exists");
        }
    }

    /**
     * Check whether a topic having the given name exists
     *
     * @param topicName name of the topic
     */
    private boolean hasTopic(String topicName) throws Exception
    {
        for (String name : this.admin.getTopics()) {
            if (name.equals(topicName)) {
                return true;
            }
        }
        return false;
    }
}
