package fr.ensibs.audioshare;

import java.io.File;
import java.util.Properties;
import java.util.Scanner;

public class AudioShareApp {

    private final UserImpl user;

    private AudioShareMessenger messenger;

    /**
     * Print a usage message and exit
     */
    private static void usage()
    {
        System.out.println("Usage: java AudioShareApp <user_name> <directory> <server_host> <server_port>");
        System.out.println("Launch the user music sharing application");
        System.out.println("with:");
        System.out.println("<user_name>    the user name in the community");
        System.out.println("<directory>    the local directory where musics are stored");
        System.out.println("<server_host>  the server host name");
        System.out.println("<server_port>  the server port number");
        System.exit(0);
    }


    public static void main(String[] args) throws Exception
    {
        if (args.length != 4) {
            usage();
        }

        String username = args[0];
        File directory = new File(args[1]);
        if (!directory.isDirectory()) {
            System.out.println("Unknown directory " + args[1]);
            usage();
        }
        String host = args[2];
        int port = 0;
        try {
            port = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            usage();
        }

        AudioShareApp instance = new AudioShareApp(username, directory, host, port);
        instance.run();
    }

    public AudioShareApp(String username, File directory, String host, int port) throws Exception{
        this.messenger = new AudioShareMessenger(host,port);
        this.user = new UserMessage(username, directory, messenger);
    }

    /**
     * Launch the application process that executes user commands: SHARE, FILTER
     */
    public void run()
    {
        System.out.println("Hello, " + this.user.getName() + ". Enter commands:"
                + "\n PLAY <id>                             to play a music"
                + "\n LIKE <id>                             to like a music"
                + "\n DISLIKE <id>                          to dislike a music"
                + "\n SHARE-P <filename> <genre>            to share a new music on the topic"
                + "\n SHARE-D <filename> <pseudo_receiver>  to send a new music directly to someone in particular"
                + "\n FILTER <tags>                         to specify the musics you are interested in"
                + "\n where <tags> is a list of tags in the form \"key1=value1 key2=value2\""
                + "\n HELP                                  display this help to use the app"
                + "\n QUIT                                  to quit the application");

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        while (!line.equals("quit") && !line.equals("QUIT")) {
            String[] command = line.split(" +");
            switch (command[0]) {
                case "play":
                case "PLAY":
                    break;
                case "like":
                case "LIKE":
                    break;
                case "dislike":
                case "DISLIKE":
                    break;
                case "share-d":
                case "SHARE-D":
                    break;
                case "share-p":
                case "SHARE-P":
                    break;

                /*case "share":
                case "SHARE":
                    if (command.length >= 2) {
                        File file = new File(this.user.getDirectory(), command[1]);
                        Properties tags = parseTags(command, 2);
                        share(file, tags);
                    } else {
                        System.err.println("Usage: share <filename> <tags>");
                    }
                    break;*/

                case "filter":
                case "FILTER":
                    filter(parseTags(command, 1));
                    break;
                case "help":
                case "HELP":
                    break;

                default:
                    System.err.println("Unknown command: \"" + command[0] + "\"");
            }
            line = scanner.nextLine();
        }
        quit();
    }


    /**
     * Share a new photo
     *
     * @param file the photo file in the local directory
     * @param tags a list of tags that describe the photo
     */
    public void share(File file, Properties tags)
    {
        /*if (file.isFile()) {
            Music music = new DefaultMusic(file, tags, this.user.getName());
            boolean success = this.user.share(music);
            if (success) {
                System.out.println(music + " has been shared");
            }
        } else {
            System.err.println("File " + file.getAbsolutePath() + " not found");
        }*/
    }

    /**
     * Specify the photos the user is interested in by setting new tags
     *
     * @param tags the new user tags
     */
    public void filter(Properties tags)
    {
        boolean success = this.user.setFilter(tags);
        if (success) {
            System.out.println("Filter " + tags + " has been set");
        }
    }

    /**
     * Stop the application
     */
    public void quit()
    {
        this.user.saveUser();
        this.messenger.close();
        System.exit(0);
    }

    /**
     * Transform a list of words in the form key=value to tags
     *
     * @param tokens a list of strings
     * @param startIdx the index of the first token in the given list that represent
     * a tag
     * @return the tags
     */
    private Properties parseTags(String[] tokens, int startIdx)
    {
        Properties tags = new Properties();
        for (int i=startIdx; i< tokens.length; i++) {
            String[] token = tokens[i].split("=");
            if (token.length == 2) {
                tags.put(token[0], token[1]);
            } else {
                System.err.println("Cannot parse tag: \"" + tokens[i] + "\"");
            }
        }
        return tags;
    }

}
