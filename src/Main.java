import client.Client;
import server.Cluster;
import server.Rmi;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("[ERROR]: invalid arguments");
            return;
        }

        switch (args[0]) {
            case "rmi":
                Rmi.run();
                break;
            case "cluster":
                Cluster.run();
                break;
            case "client":
                Client.run();
                break;
        }
    }
}

