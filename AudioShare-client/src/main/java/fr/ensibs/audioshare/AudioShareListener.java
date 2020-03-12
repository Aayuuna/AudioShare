package fr.ensibs.audioshare;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class AudioShareListener implements MessageListener {

    private UserMessage user;

    public AudioShareListener(UserMessage user) {
        this.user = user;
    }

    @Override
    public void onMessage(Message message) {

        if (message instanceof BytesMessage) {
            this.user.onMessageReceived((BytesMessage)message);
        }
    }
}
