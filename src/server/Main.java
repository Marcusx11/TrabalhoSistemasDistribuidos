package server;

import core.*;
import core.database.Database;
import core.models.RequestLog;
import core.models.transfer.Transfer;
import core.models.transfer.TransferDAO;
import core.models.user.User;
import core.models.user.UserDAO;
import org.jgroups.*;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.blocks.atomic.Counter;
import org.jgroups.blocks.atomic.CounterService;
import org.jgroups.blocks.locking.LockNotification;
import org.jgroups.blocks.locking.LockService;
import org.jgroups.util.Owner;
import org.jgroups.util.Util;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;

import server.models.Bank;

public class Main extends ReceiverAdapter implements RequestHandler, LockNotification {
    private JChannel channel;
    private RequestDispatcher dispatcher;
    private Counter userCounter;
    private Counter transferCounter;
    private Authentication authentication;
    private Operation operation;
    //private LockService lockService;
    //private Lock lock;
    private ArrayList<RequestLog> logs;

    public void start() {
        try {
            channel = new JChannel("src/configs.xml");
            dispatcher = new RequestDispatcher(channel, this);

            CounterService counterService = new CounterService(channel);
            channel.connect("counter-cluster");

            /*lockService = new LockService(channel);
            lockService.addLockListener(this);
            lock = lockService.getLock("my-lock");*/

            logs = new ArrayList<RequestLog>();

            channel.setReceiver(this);
            channel.connect(Constants.CHANNEL_CLUSTER_NAME);

            authentication = new Authentication();
            operation = new Operation();

            Database.bootstrap(channel.getAddress().toString());

            while (true) {
                Util.sleep(100);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private void initCounters() {
        CounterService counterService = new CounterService(channel);

        userCounter = counterService.getOrCreateCounter("user_id", 1);
        transferCounter = counterService.getOrCreateCounter("transfer_id", 1);
    }

    private void createRmiInstance() {
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException ignored) {}

        try {
            if (userCounter == null || transferCounter == null) this.initCounters();

            Bank bank = new Bank(dispatcher, channel, userCounter, transferCounter);
            Naming.rebind(Constants.RMI_NAME, bank);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * O retorno de chamada viewAccepted () é chamado sempre que uma nova instância se junta ao cluster ou uma instância
     * existente sai (travamentos incluídos).
     * @param newView -
     */
    @Override
    public void viewAccepted(View newView) {
        System.out.println("\t[new member]: " + newView);
        Address myAddress = channel.getAddress();
        Address coordinator = channel.getView().getMembers().get(0);

        if(myAddress.equals(coordinator)) {
            this.createRmiInstance();
        }
    }

    @Override
    public Object handle(Message message) {
        if (message.getObject() instanceof Request) {
            Request request = (Request) message.getObject();

            switch (request.getRequestCode()) {
                case REGISTER_USER:
                    Response response = authentication.register((User) request.getBody());
                    logs.add(new RequestLog(null, request, response, LocalDateTime.now()));
                    return response;
                case LOGIN_USER:
                    return authentication.login((User) request.getBody());
                case GET_BALANCE:
                    return operation.balance((User) request.getBody());
                case LIST_ALL_USERS:
                    return operation.listAllUsers();
                case TRANSFER:
                    return operation.transfer((Transfer) request.getBody());
                case STATEMENT_OF_ACCOUNT:
                    return operation.statementOfAccount((User) request.getBody());
                case BANK_AMOUNT:
                    return operation.bankAmount();
            }
        }

        return false;
    }

    public static void main(String[] args) {
        new Main().start();
    }

    @Override
    public void lockCreated(String s) {
        System.out.println("Lock creada: " + s);
    }

    @Override
    public void lockDeleted(String s) {
        System.out.println("Lock deletada: " + s);
    }

    @Override
    public void locked(String s, Owner owner) {
        System.out.println("Recurso lockado por " + owner.toString());
    }

    @Override
    public void unlocked(String s, Owner owner) {
        System.out.println("Recurso deslockado por " + owner.toString());
    }

    @Override
    public void awaiting(String s, Owner owner) {
        System.out.println(owner.toString() + " está esperando para liberar a trava...");
    }

    @Override
    public void awaited(String s, Owner owner) {
        System.out.println(owner.toString() + " esperou para liberar a trava.");
    }
}
