package server.models;

import core.Response;
import core.ResponseCode;
import core.models.user.UserDAO;
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
    private final Counter counter;

    public Bank(
            RequestDispatcherInterface dispatcher,
            JChannel channel,
            Counter counter) throws RemoteException {
        super();
        this.dispatcher = dispatcher;
        this.channel = channel;
        this.counter = counter;
    }

    @Override
    public Response register(String name, String cpf, String password) throws RemoteException {
        try {
            // TODO: Add password hash
            long id = counter.incrementAndGet();
            User userRegister = new User(name, cpf, password, id);
            userRegister.setBalance(1000);

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

            RspList<Response> results = dispatcher.sendRequestMulticast(new Request(RequestCode.LOGIN_USER, userParams),
                    ResponseMode.GET_FIRST);

            return results.getFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public float consultarSaldo(String cpf, String password) throws RemoteException {
        try {
            User userParams = UserDAO.findByCpfAndPassword(cpf, password);

            Object user = dispatcher.sendRequestMulticast(
                    new Request(RequestCode.CONSULTA_SALDO, userParams), ResponseMode.GET_MAJORITY);

            User response = (User) user;

            if (user != null) {
                return response.getBalance();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
