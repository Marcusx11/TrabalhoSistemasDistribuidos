package server;

import core.*;
import org.jgroups.*;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.util.Util;
import server.models.Bank;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Main extends ReceiverAdapter implements RequestHandler {
    private JChannel channelCluster;
    private RequestDispatcher dispatcherCluster;

    public void start() throws Exception {
        try {
            channelCluster = new JChannel("sequencer.xml");
            dispatcherCluster = new RequestDispatcher(channelCluster, this);

            channelCluster.setReceiver(this);
            channelCluster.connect(Constants.CHANNEL_CLUSTER_NAME);

            while (true) {
                Util.sleep(100);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Object handle(Message message) throws Exception {
        System.out.println(message.getObject());
        return null;
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

    public static void main(String[] args) throws Exception {
        new Main().start();
    }
}