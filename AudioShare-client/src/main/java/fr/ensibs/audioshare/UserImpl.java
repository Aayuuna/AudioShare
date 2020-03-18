package fr.ensibs.audioshare;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserImpl implements User {

    private String name;
    private final File dir;
    private List<Music> sharedMusics;
    private List<Music> receivedMusics;
    private Properties tags;

    public UserImpl(String name, File dir) {
        this.name = name;
        this.dir = dir;
        this.sharedMusics = new ArrayList<>();
        this.receivedMusics = new ArrayList<>();
        this.loadUser();
        System.out.println("User " + name + " " + dir + " " + sharedMusics + " " + receivedMusics);

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public File getDirectory() {
        return this.dir;
    }

    @Override
    public List<Music> getSharedMusics() {
        return this.sharedMusics;
    }

    @Override
    public List<Music> getReceivedMusics() {
        return this.receivedMusics;
    }

    @Override
    public boolean share(Music music) {
        this.sharedMusics.add(music);
        return true;
    }

    @Override
    public void receive(Music music) {
        this.receivedMusics.add(music);
        System.out.println("Music received ! ^o^ " + music);
    }

    @Override
    public boolean setFilter(Properties tags) {
        this.tags = tags;
        return true;
    }


    private void loadUser(){
        File file = new File(this.dir, "user.bak");
        if (file.isFile()) {
            try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                this.tags = (Properties) in.readObject();
                this.sharedMusics = (List<Music>) in.readObject();
                this.receivedMusics = (List<Music>) in.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Save the user tags and photos to the user.bak file in the user directory
     */
    public void saveUser()
    {
        File file = new File(this.dir, "user.bak");
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            out.writeObject(this.tags);
            out.writeObject(this.sharedMusics);
            out.writeObject(this.receivedMusics);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
