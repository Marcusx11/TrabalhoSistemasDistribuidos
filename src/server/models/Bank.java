package server.models;

import core.Channel;
import interfaces.BankInterface;
import org.jgroups.Receiver;
import org.jgroups.blocks.ResponseMode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Bank extends UnicastRemoteObject implements BankInterface {
    private final Channel channel;

    public Bank() throws Exception {
        super();
        channel = Channel.getInstance();
    }

    @Override
    public void register(String name, int age) throws RemoteException {
        String body = "{Name: " + name + ", Age: " + age + "}\n";

        try {
            channel.sendAll(body, ResponseMode.GET_ALL);
            System.out.println("> Client request "+ body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}