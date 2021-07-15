package interfaces;

import java.rmi.*;

public interface BankInterface extends Remote {
    boolean register(String name, String cpf, String password) throws RemoteException;
    void login(String cpf, String password) throws RemoteException;
}
