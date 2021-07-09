package server;

import core.Channel;

public class Cluster {

    public static void run() {
        try {
            Channel channel = Channel.getInstance();
            channel.setReceiver((new core.Bank()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}