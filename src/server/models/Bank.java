package server.models;

import core.Request;
import core.RequestCode;
import core.models.User;
import interfaces.BankInterface;
import interfaces.RequestDispatcherInterface;
import org.jgroups.blocks.ResponseMode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

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

            UserDAO userDAO = new UserDAO();

            userDAO.create(userRegister);


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
