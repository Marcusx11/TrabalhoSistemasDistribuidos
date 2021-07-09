package interfaces;

import java.rmi.*;

public interface BankInterface extends Remote {
    void register(String name, int age) throws RemoteException;
}
