package client;

import core.Constants;
import core.Request;
import core.RequestDispatcher;
import core.Response;
import org.jgroups.*;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.blocks.ResponseMode;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client extends ReceiverAdapter implements RequestHandler {
    private JChannel channelController;
    private RequestDispatcher dispatcherController;

    public void run() throws Exception {
        channelController = new JChannel("configs.xml");
        channelController.setReceiver(this);
        dispatcherController = new RequestDispatcher(channelController, this);

        channelController.connect(Constants.CHANNEL_CLUSTER_NAME);

        eventLoop();
    }

    private void eventLoop() throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader input = new BufferedReader(inputStreamReader);

        while (true) {
            System.out.print("> ");
            System.out.flush();
            String line = input.readLine();

            dispatcherController.sendRequestMulticast(new Request(line), ResponseMode.GET_ALL);
            if(line.startsWith("quit") || line.startsWith("exit")) break;
        }
    }

    @Override
    public Object handle(Message message) throws Exception {
        if (message.getObject() instanceof Response) {
            String body = (String)message.getObject();
            System.out.println(body);
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        new Client().run();
    }
}
