package server;

import core.Constants;
import core.Request;
import core.RequestDispatcher;
import core.models.User;
import org.jgroups.*;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.util.Util;
import server.database.Database;
import server.models.Bank;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Main extends ReceiverAdapter implements RequestHandler {
    private JChannel channelCluster;
    private RequestDispatcher dispatcherCluster;

    public void start() {
        try {
            channelCluster = new JChannel("sequencer.xml");
            dispatcherCluster = new RequestDispatcher(channelCluster, this);

            channelCluster.setReceiver(this);
            channelCluster.connect(Constants.CHANNEL_CLUSTER_NAME);

            // Create database tables if they do not exist
            Database.bootstrap();

            while (true) {
                Util.sleep(100);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Object handle(Message message) throws Exception {
        if (message.getObject() instanceof Request) {
            Request request = (Request) message.getObject();

            switch (request.getRequestCode()) {
                case REGISTER_USER:
                    return this.register((User)request.getBody());
                default:
                    return null; // TODO: What to return?
            }
        }

        return null; // TODO: What to return?
    }

    /**
     * O retorno de chamada viewAccepted () é chamado sempre que uma nova instância se junta ao cluster ou uma instância
     * existente sai (travamentos incluídos).
     * @param newView -
     */
    @Override
    public void viewAccepted(View newView) {
        System.out.println("\t[new member]: " + newView);
        Address myAddress = channelCluster.getAddress();
        Address coordinator = channelCluster.getView().getMembers().get(0);

        if(myAddress.equals(coordinator)) {
            try {
                LocateRegistry.createRegistry(1099);
            } catch (RemoteException ignored) {}

            try {
                Bank bank = new Bank(dispatcherCluster);
                Naming.rebind(Constants.RMI_NAME, bank);
            } catch (RemoteException | MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * obtemos uma mensagem como argumento.
     * @param message
     */
    public void receive(Message message) {
        System.out.println("[receive]: " + message.getSrc() + ": " + message.getObject());
    }

    private Object register(User user) {
        System.out.println(user.getName());
        System.out.println(user.getCpf());
        System.out.println(user.getPassword());

        // TODO: Save to datatabse

        return null; // TODO: What to return?
    }

    public static void main(String[] args) throws Exception {
        new Main().start();
    }
}