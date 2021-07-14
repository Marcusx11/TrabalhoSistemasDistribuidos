package server;

import core.*;
import org.jgroups.*;
import org.jgroups.blocks.RequestHandler;

public class BankController extends ReceiverAdapter implements RequestHandler {
    private JChannel channelCluster;
    private JChannel channelClient;
    private RequestDispatcher dispatcherCluster;
    private RequestDispatcher dispatcherClient;

    public void run() throws Exception {
        try {
            // instancia o canal e o despachante do controle
            channelCluster = new JChannel("configs.xml");
            channelCluster.setReceiver(this);
            dispatcherCluster = new RequestDispatcher(channelCluster, this);

            // Instância o cliente
            channelClient = new JChannel("configs.xml");
            channelClient.setReceiver(this);
            dispatcherClient = new RequestDispatcher(channelClient, this);

            // se conecta aos canais do controle e da visao
            channelClient.connect(Constants.CHANNEL_CLIENT_NAME);
            channelCluster.connect(Constants.CHANNEL_CLUSTER_NAME);

            for (;;) {}
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Object handle(Message message) throws Exception {
        if (message.getObject() instanceof Request) {
            return new Response("Response 1");
        }

        return new Response("Response 2");
    }

    /**
     * O retorno de chamada viewAccepted () é chamado sempre que uma nova instância se junta ao cluster ou uma instância
     * existente sai (travamentos incluídos).
     * @param newView -
     */
    @Override
    public void viewAccepted(View newView) {
        System.out.println("\t[new view cluster]: " + newView);
    }

    /**
     * obtemos uma mensagem como argumento.
     * @param message
     */
    public void receive(Message message) {
        System.out.println("[receive]: " + message.getSrc() + ": " + message.getObject());
    }

    public static void main(String[] args) throws Exception {
        new BankController().run();
    }
}