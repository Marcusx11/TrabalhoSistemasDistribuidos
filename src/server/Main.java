package server;

import org.jgroups.*;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.util.Util;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

import core.Constants;
import core.Request;
import core.RequestDispatcher;
import core.models.user.User;
import core.database.Database;
import core.models.user.UserDAO;
import server.models.Bank;

public class Main extends ReceiverAdapter implements RequestHandler {
    private JChannel channel;
    private RequestDispatcher dispatcher;

    public void start() {
        try {
            channel = new JChannel("sequencer.xml");
            dispatcher = new RequestDispatcher(channel, this);

            channel.setReceiver(this);
            channel.connect(Constants.CHANNEL_CLUSTER_NAME);

            Database.bootstrap(channel.getAddress().toString());

            while (true) {
                Util.sleep(100);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void createRmiInstance() {
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException ignored) {}

        try {
            Bank bank = new Bank(dispatcher, channel);
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

    /**
     * obtemos uma mensagem como argumento.
     * @param message
     */
    public void receive(Message message) {
        System.out.println("[receive]: " + message.getSrc() + ": " + message.getObject());
    }

    @Override
    public Object handle(Message message) {
        if (message.getObject() instanceof Request) {
            Request request = (Request) message.getObject();

            switch (request.getRequestCode()) {
                case REGISTER_USER:
                    return this.register((User) request.getBody());
                case LOGIN_USER:
                    return this.login((User) request.getBody());
            }
        }

        return false;
    }

    private boolean register(User user) {
        UserDAO userDAO = new UserDAO();

        userDAO.create(user);

        return true;
    }

    private User login(User userParams) {
        UserDAO userDAO = new UserDAO();

        User user = userDAO.findBy("cpf", userParams.getCpf());
        System.out.println(user);
        return user;
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
