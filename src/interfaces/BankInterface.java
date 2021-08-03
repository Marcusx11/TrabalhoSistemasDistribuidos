package interfaces;

import core.Response;
import core.models.user.User;

import java.rmi.*;

public interface BankInterface extends Remote {
    Response register(String name, String cpf, String password) throws RemoteException;
    Response login(String cpf, String password) throws RemoteException;
    Response logout(User user) throws RemoteException;
    Response balance(User user) throws RemoteException;
    Response transfer(User user, long toId, float amount) throws RemoteException;
    Response listAllUsers(User user) throws RemoteException;
    Response statementOfAccount(User user) throws RemoteException;
    Response bankAmount(User user) throws RemoteException;
}
