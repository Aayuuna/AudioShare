package fr.ensibs.audioshare;

import java.util.Properties;
import java.io.File;

public class DefaultMusic implements Music {


    /**
     * the music tags that describe the music
     */
    private Properties tags;

    /**
     * the music binary file that contains the music
     */
    private File file;

    /**
     * the user that shared the music, considered as the music owner
     */
    private String owner;

    /**
     * Constructor
     *
     * @param file the music binary file
     * @param tags the music tags
     * @param owner the user that shared the music
     */
    public DefaultMusic(File file, Properties tags, String owner)
    {
        this.file = file;
        this.tags = tags;
        this.owner = owner;
    }

    @Override
    public Properties getTags()
    {
        return this.tags;
    }

    @Override
    public File getFile()
    {
        return this.file;
    }

    @Override
    public String getOwner()
    {
        return this.owner;
    }

    @Override
    public String toString()
    {
        return "{ music: file=" + file + ",owner=" + owner + ",tags=" + tags + " }";
    }

}
