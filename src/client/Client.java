package client;

import java.rmi.*;

import core.Constants;
import interfaces.BankInterface;

public class Client {
    public static void run() {
        try {
            //Localiza o objeto remoto, através do nome cadastrado no registro RMI do localhost
            BankInterface bank = (BankInterface) Naming.lookup(Constants.RMI_URL);
            bank.register("Gabriel Augusto", 22);
            System.out.println("User created!");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
