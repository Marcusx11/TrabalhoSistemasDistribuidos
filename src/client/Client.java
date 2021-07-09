package client;

import java.rmi.*;
import interfaces.BankInterface;

public class Client {
    public static void run() {
        try {
            BankInterface bank = (BankInterface) Naming.lookup("//localhost/bank");

            bank.register("Gabriel Augusto", 22);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
