package server;

import core.*;
import core.database.Database;
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
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import server.models.Bank;

public class Main extends ReceiverAdapter implements RequestHandler, LockNotification {
    private JChannel channel;
    private RequestDispatcher dispatcher;
    private Counter userCounter;
    private Counter transferCounter;
    private Authentication authentication;
    private Operation operation;
    private LockService lockService;
    private Lock lock;

    public void start() {
        try {
            channel = new JChannel("src/configs.xml");
            dispatcher = new RequestDispatcher(channel, this);

            CounterService counterService = new CounterService(channel);
            channel.connect("counter-cluster");

            lockService = new LockService(channel);
            lockService.addLockListener(this);
            lock = lockService.getLock("my-lock");

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
    public Object handle(Message message) throws InterruptedException {
        if (message.getObject() instanceof Request) {
            Request request = (Request) message.getObject();
            Response response = null;

            switch (request.getRequestCode()) {
                case REGISTER_USER:
                    return authentication.register((User) request.getBody());
                case LOGIN_USER:
                    return authentication.login((User) request.getBody());
                case GET_BALANCE:
                    if (lock.tryLock(2000, TimeUnit.MILLISECONDS)) {
                        try {
                            response = operation.balance((User) request.getBody());
                        } finally {
                            lock.unlock();
                        }
                    }
                    return response;
                case LIST_ALL_USERS:
                    lock.lock();
                    try {
                        return operation.listAllUsers();
                    } finally {
                        lock.unlock();
                    }
                case TRANSFER:
                    lock.lock();
                    try {
                        return operation.transfer((Transfer) request.getBody());
                    } finally {
                        lock.unlock();
                    }
                case STATEMENT_OF_ACCOUNT:
                    lock.lock();
                    try {
                        return operation.statementOfAccount((User) request.getBody());
                    } finally {
                        lock.unlock();
                    }
                case BANK_AMOUNT:
                    lock.lock();
                    try {
                        return operation.bankAmount();
                    } finally {
                        lock.unlock();
                    }
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
