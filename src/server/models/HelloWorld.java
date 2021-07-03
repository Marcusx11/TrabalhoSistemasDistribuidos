package server.models;

import interfaces.HelloWorldInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloWorld extends UnicastRemoteObject implements HelloWorldInterface {
    public HelloWorld() throws RemoteException {
        super();
    }

    @Override
    public String say() throws RemoteException {
        return "Hello world";
    }
}
