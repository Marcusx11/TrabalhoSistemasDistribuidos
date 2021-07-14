package client;

import core.Constants;
import core.Request;
import core.RequestDispatcher;
import org.jgroups.*;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.util.RspList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

public class Client extends ReceiverAdapter implements RequestHandler {
    private JChannel channel;
    private RequestDispatcher dispatcher;
    private Address controlador;

    public void start() throws Exception {
        channel = new JChannel("configs.xml");
        channel.setReceiver(this);
        dispatcher = new RequestDispatcher(channel, this);

        channel.connect(Constants.CHANNEL_CLIENT_NAME);

        eventLoop();
    }

    private void eventLoop() throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader input = new BufferedReader(inputStreamReader);

        while (true) {
            System.out.print("> ");
            System.out.flush();
            String line = input.readLine();

            dispatcher.sendRequestUnicast(controlador, new Request(line), ResponseMode.GET_FIRST);
            if(line.startsWith("quit") || line.startsWith("exit")) break;
        }
    }

    /**
     * O retorno de chamada viewAccepted () é chamado sempre que uma nova instância se junta ao cluster ou uma instância
     * existente sai (travamentos incluídos).
     * @param newView -
     */
    @Override
    public void viewAccepted(View newView) {
        System.out.println("\t[new view cluster]: " + newView);
        try {
            RspList respostas = dispatcher.sendRequestMulticast("quem é o controlador?", ResponseMode.GET_ALL);

            respostas.values().removeIf(Objects::isNull);

            controlador = (Address) respostas.keySet().toArray()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object handle(Message message) throws Exception {
        return null;
    }

    public static void main(String[] args) throws Exception {
        new Client().start();
    }
}
