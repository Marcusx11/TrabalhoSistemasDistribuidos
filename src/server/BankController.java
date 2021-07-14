package server;

import core.*;
import org.jgroups.*;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.util.Util;

public class BankController extends ReceiverAdapter implements RequestHandler {
    private JChannel channelCluster;
    private JChannel channelClient = null;
    private RequestDispatcher dispatcherCluster;
    private RequestDispatcher dispatcherClient = null;

    public void start() throws Exception {
        try {
            // instancia o canal e o despachante do controle
            channelCluster = new JChannel("sequencer.xml");
            channelCluster.setReceiver(this);
            dispatcherCluster = new RequestDispatcher(channelCluster, this);

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
        if (message.getObject() instanceof Request) {
            return new Response("Response 1");
        } else if (message.getObject() instanceof String && message.getObject().equals("quem é o controlador?")) {
            return channelClient != null ? "Eu sou" : null;
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
        Address meuEndereco = channelCluster.getAddress();
        Address coordenador = channelCluster.getView().getMembers().get(0);

        if(meuEndereco.equals(coordenador)) {
            if (channelClient == null) {
                try {
                    channelClient = new JChannel("configs.xml");
                    channelClient.setReceiver(this);
                    dispatcherClient = new RequestDispatcher(channelClient, this);

                    // se conecta aos canais do controle e da visao
                    channelClient.connect(Constants.CHANNEL_CLIENT_NAME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        new BankController().start();
    }
}