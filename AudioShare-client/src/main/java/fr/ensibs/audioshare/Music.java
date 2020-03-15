package fr.ensibs.audioshare;

import java.io.Serializable;
import java.util.Properties;
import java.io.File;

public interface Music extends Serializable {

    /**
     * Give the music tags that describe the music in the form key=value
     *
     * @return the photo tags
     */
    Properties getTags();

    /**
     * Give the music binary file that contains the music
     *
     * @return the photo file
     */
    File getFile();

    /**
     * Give the user that shared the music, considered as the music owner
     *
     * @return the photo owner
     */
    String getOwner();
}
