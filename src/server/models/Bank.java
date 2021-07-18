package server.models;

import org.jgroups.JChannel;
import org.jgroups.blocks.ResponseMode;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import interfaces.RequestDispatcherInterface;
import interfaces.BankInterface;
import core.models.user.User;
import core.RequestCode;
import core.Request;

public class Bank extends UnicastRemoteObject implements BankInterface {
    private final JChannel channel;
    private final RequestDispatcherInterface dispatcher;

    public Bank(
        RequestDispatcherInterface dispatcher,
        JChannel channel
    ) throws RemoteException {
        super();
        this.dispatcher = dispatcher;
        this.channel = channel;
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
    public User login(String cpf, String password) throws RemoteException {
        try {
            Object data = dispatcher.sendRequestUnicast(this.channel.getAddress(),
                    new Request(RequestCode.LOGIN_USER, null),
                    ResponseMode.GET_FIRST);

            if (data != null) {
                return (User) data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
