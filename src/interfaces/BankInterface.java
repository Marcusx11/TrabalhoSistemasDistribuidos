package interfaces;

import core.models.user.User;

import java.rmi.*;

public interface BankInterface extends Remote {
    boolean register(String name, String cpf, String password) throws RemoteException;
    User login(String cpf, String password) throws RemoteException;
    double consultarSaldo(String cpf, String password) throws RemoteException;
}
