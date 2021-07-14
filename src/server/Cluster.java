package server;

import core.*;
import org.jgroups.Address;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Cluster {
    public static void run() {
        try {
            Channel channel = Channel.getInstance();
            System.out.println("> Set channel receiver");
            channel.setReceiver(new Bank());

            eventLoop();

            channel.getChannel().close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void eventLoop() throws Exception {
        Channel channel = Channel.getInstance();
        Address meuEndereco = channel.getChannel().getAddress();

        InputStreamReader inputStreamReader = new InputStreamReader(System.in);

        BufferedReader input = new BufferedReader(inputStreamReader);

        while (true) {
            System.out.print("> ");
            System.out.flush();
            String line = input.readLine();

            System.out.println("Main Cluster members"+channel.getChannel().getView().getMembers());
            if(line.startsWith("quit") || line.startsWith("exit")) break;
        }

    }


}