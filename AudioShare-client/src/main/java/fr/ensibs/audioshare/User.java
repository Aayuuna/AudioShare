package fr.ensibs.audioshare;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * a user of the music sharing app
 */

public interface User {

    String getName();

    File getDirectory();

    List<Music> getSharedMusics();

    List<Music> getReceivedMusics();

    boolean share(Music music);

    void receive(Music music);

    boolean setFilter(Properties tags);


}
