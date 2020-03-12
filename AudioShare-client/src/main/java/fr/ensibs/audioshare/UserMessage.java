package fr.ensibs.audioshare;

import org.objectweb.joram.client.jms.admin.User;

import javax.jms.BytesMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class UserMessage extends UserImpl {

    private AudioShareMessenger messenger;


    /**
     * Constructor
     * @param name user name
     * @param dir user directory where his musics are stored
     * @param messenger object to communicate with openJMS
     * @throws Exception
     */
    public UserMessage(String name, File dir, AudioShareMessenger messenger) throws Exception {
        super(name, dir);
        this.messenger = messenger;
        this.messenger.setUser(this);
    }


    @Override
    public boolean share(Music music)
    {
        super.share(music);
        try {
            this.messenger.send(music);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setFilter(Properties tags)
    {
        super.setFilter(tags);
        try {
            this.messenger.subscribe(tags);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onMessageReceived(BytesMessage message)
    {
        try {
            // initialize tags
            Properties tags = new Properties();
            for (Enumeration e = message.getPropertyNames(); e.hasMoreElements();) {
                String name = (String) e.nextElement();
                if (!name.equals("owner") && !name.equals("filename") && ! name.startsWith("JMS")) {
                    tags.put(name, message.getStringProperty(name));
                }
            }

            // write the message body to a binary file
            String filename = message.getStringProperty("filename");
            String owner = message.getStringProperty("owner");
            byte[] data = new byte[(int)message.getBodyLength()];
            message.readBytes(data);
            File file = new File(getDirectory(), owner + "-" + filename);
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(data);
            }

            // create a photo and add it to the received photos
            //MusicImpl music = new MusicImpl(file, tags, owner);
            //super.receive(music);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
