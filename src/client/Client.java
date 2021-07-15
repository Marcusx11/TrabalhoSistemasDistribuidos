package client;

import core.Constants;
import interfaces.BankInterface;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    public static void main(String[] args) {
        try {
            BankInterface bank = (BankInterface) Naming.lookup(Constants.RMI_URL);
            bank.register("Gabriel", 22);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
