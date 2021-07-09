package server;

import core.Channel;
import server.models.Bank;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class Rmi {

    public static void run() {
        try {
            LocateRegistry.createRegistry(1099);

            Channel channel = Channel.getInstance();
            channel.setReceiver((new core.Bank()));

            Bank bank = new Bank();

            Naming.rebind("bank", bank);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}