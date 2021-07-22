package server.models;

import core.Constants;
import core.Response;
import core.models.transfer.Transfer;
import org.jgroups.JChannel;
import org.jgroups.blocks.ResponseMode;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import interfaces.RequestDispatcherInterface;
import interfaces.BankInterface;
import core.models.user.User;
import core.RequestCode;
import core.Request;
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

            RspList<Response> results = dispatcher.sendRequestMulticast(
                new Request(RequestCode.REGISTER_USER, userRegister),
                ResponseMode.GET_FIRST);

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

    @Override
    public Response balance(User user) throws RemoteException {
        return null;

    }
}
