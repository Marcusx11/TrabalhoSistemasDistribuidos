package server.models;

import interfaces.BankInterface;
import interfaces.RequestDispatcherInterface;
import org.jgroups.blocks.ResponseMode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Bank extends UnicastRemoteObject implements BankInterface {
    RequestDispatcherInterface dispatcher;

    public Bank(RequestDispatcherInterface requestDispatcher) throws RemoteException {
        super();
        dispatcher = requestDispatcher;
    }

    @Override
    public void register(String name, int age) throws RemoteException {
        try {
            dispatcher.sendRequestMulticast("Registra ai amigao: " + name + ", " + age, ResponseMode.GET_ALL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
