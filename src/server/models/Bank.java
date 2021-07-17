package server.models;

import org.jgroups.blocks.ResponseMode;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import interfaces.RequestDispatcherInterface;
import interfaces.BankInterface;
import core.models.user.User;
import core.RequestCode;
import core.Request;

public class Bank extends UnicastRemoteObject implements BankInterface {
    RequestDispatcherInterface dispatcher;

    public Bank(RequestDispatcherInterface requestDispatcher) throws RemoteException {
        super();
        dispatcher = requestDispatcher;
    }

    @Override
    public boolean register(String name, String cpf, String password) throws RemoteException {
        try {
            // TODO: Add unique id and password hash
            User userRegister = new User(name, cpf, password);

            dispatcher.sendRequestMulticast(
                new Request(RequestCode.REGISTER_USER, userRegister),
                ResponseMode.GET_FIRST);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void login(String cpf, String password) throws RemoteException {
        // ...
    }
}
