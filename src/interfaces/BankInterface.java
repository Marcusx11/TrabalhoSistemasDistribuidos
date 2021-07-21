package interfaces;

import core.Response;
import core.models.user.User;

import java.rmi.*;

public interface BankInterface extends Remote {
    Response register(String name, String cpf, String password) throws RemoteException;
    Response login(String cpf, String password) throws RemoteException;
}
