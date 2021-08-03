package server.models;

import core.*;
import core.models.transfer.Transfer;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.blocks.ResponseMode;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import interfaces.RequestDispatcherInterface;
import interfaces.BankInterface;
import core.models.user.User;
import org.jgroups.blocks.atomic.Counter;
import org.jgroups.util.RspList;

public class Bank extends UnicastRemoteObject implements BankInterface {
    private final JChannel channel;
    private final RequestDispatcherInterface dispatcher;
    private final Counter userCounter;
    private final Counter transferCounter;

    public Bank(
            RequestDispatcherInterface dispatcher,
            JChannel channel,
            Counter userCounter,
            Counter transferCounter) throws RemoteException {
        super();

        this.dispatcher = dispatcher;
        this.channel = channel;
        this.userCounter = userCounter;
        this.transferCounter = transferCounter;
    }

    @Override
    public Response register(String name, String cpf, String password) throws RemoteException {
        try {
            // TODO: Add password hash
            long userId = userCounter.incrementAndGet();
            long transferId = transferCounter.incrementAndGet();
            
            User userRegister = new User(name, cpf, password, userId);

            userRegister.addTransfer(new Transfer(transferId, Constants.INITIAL_BALANCE_VALUE, userId));
            Request request = new Request(RequestCode.REGISTER_USER, userRegister);
            RspList<Response> results = dispatcher.sendRequestMulticast(request, ResponseMode.GET_MAJORITY);

            return results.getFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Response login(String cpf, String password) throws RemoteException {
        try {
            User userParams = new User();
            userParams.setCpf(cpf);
            userParams.setPassword(password);
            userParams.setOnline(1);

            Request request =  new Request(RequestCode.LOGIN_USER, userParams);
            RspList<Response> results = dispatcher.sendRequestMulticast(request, ResponseMode.GET_FIRST);

            return results.getFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Response logout(User user) throws RemoteException {
        try {
            Request request =  new RequestAuth(RequestCode.LOGOUT_USER, user, null);
            RspList<Response> results = dispatcher.sendRequestMulticast(request, ResponseMode.GET_MAJORITY);

            return results.getFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Response balance(User user) throws RemoteException {
        try {
            RequestAuth request =  new RequestAuth(RequestCode.GET_BALANCE, user, null);
            return dispatcher.sendRequestUnicast(getRandomAddress(), request, ResponseMode.GET_FIRST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Response transfer(User from, long toId, float amount) throws RemoteException {
        try {
            long transferId = transferCounter.incrementAndGet();

            Transfer transfer = new Transfer(transferId, amount, toId, from.getId());

            // TODO: Adicionar a trava
            Request request =  new RequestAuth(RequestCode.TRANSFER, from, transfer);
            RspList<Response> results = dispatcher.sendRequestMulticast(request, ResponseMode.GET_MAJORITY);

            return results.getFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Response listAllUsers(User user) throws RemoteException {
        try {
            RequestAuth request =  new RequestAuth(RequestCode.LIST_ALL_USERS, user, null);
            return dispatcher.sendRequestUnicast(channel.getAddress(), request, ResponseMode.GET_FIRST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Response statementOfAccount(User user) throws RemoteException {
        try {
            RequestAuth request =  new RequestAuth(RequestCode.STATEMENT_OF_ACCOUNT, user, null);
            return dispatcher.sendRequestUnicast(getRandomAddress(), request, ResponseMode.GET_FIRST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Response bankAmount(User user) throws RemoteException {
        try {
            RequestAuth request =  new RequestAuth(RequestCode.BANK_AMOUNT, user, null);
            return dispatcher.sendRequestUnicast(getRandomAddress(), request, ResponseMode.GET_FIRST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Address getRandomAddress() {
        int index = (int)(Math.random() * channel.getView().getMembers().size());
        return channel.getView().getMembers().get(index);
    }
}

