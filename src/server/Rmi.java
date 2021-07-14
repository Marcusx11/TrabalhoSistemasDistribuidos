package server;

import core.Channel;
import core.Constants;
import server.models.Bank;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class Rmi {
    /**
     * Conecta a instância do objeto "Bank" no registro RMI da maquína.
     * Nesse caso localhost.
     */
    public static void run() {
        try {
            LocateRegistry.createRegistry(1099);
            System.out.println("> Create registry on port 1099");

            Channel channel = Channel.getInstance();
            channel.setReceiver((new core.Bank()));

            Bank bank = new Bank();

            Naming.rebind(Constants.RMI_NAME, bank);
            System.out.println("> Start Rmi server with the name " + Constants.RMI_NAME );
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}