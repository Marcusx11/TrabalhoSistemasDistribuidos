package server;

import core.*;
import org.jgroups.*;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.blocks.atomic.Counter;
import org.jgroups.blocks.atomic.CounterService;
import org.jgroups.util.Util;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import core.models.user.User;
import core.database.Database;
import core.models.user.UserDAO;
import server.models.Bank;

public class Main extends ReceiverAdapter implements RequestHandler {
    private JChannel channel;
    private RequestDispatcher dispatcher;
    private Counter counter;

    public void start() {
        try {
            channel = new JChannel("src/configs.xml");
            dispatcher = new RequestDispatcher(channel, this);

            CounterService counterService = new CounterService(channel);
            channel.connect("counter-cluster");
            counter = counterService.getOrCreateCounter("id", 1);

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
            Bank bank = new Bank(dispatcher, channel, counter);
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
                    return this.register((User) request.getBody());
                case LOGIN_USER:
                    return this.login((User) request.getBody());
            }
        }

        return false;
    }

    private Response register(User user) {
        try {
            UserDAO userDAO = new UserDAO();
            userDAO.create(user);

            return new Response(ResponseCode.OK, "The user was successfully created.");
        } catch (Exception e) {
            return new Response(ResponseCode.ERROR, "There was a problem creating this user. Please try again.");
        }
    }

    private Response login(User userParams) {
        UserDAO userDAO = new UserDAO();

        try {
            User user = userDAO.findBy("cpf", userParams.getCpf());

            if (user != null) {
                if (userParams.getPassword().equals(user.getPassword())) {
                    user.setOnline(1);
                    userDAO.update(user);
                    return new Response(ResponseCode.OK, user);
                }
            }

            return new Response(ResponseCode.ERROR, "Invalid password or CPF. Please try again.");
        } catch (Exception e) {
            return new Response(ResponseCode.ERROR, "There was a problem with login. Please try again.");
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
