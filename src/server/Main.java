package server;

import server.models.HelloWorld;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class Main {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            HelloWorld helloWord = new HelloWorld();

            Naming.rebind("hello-world", helloWord);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
