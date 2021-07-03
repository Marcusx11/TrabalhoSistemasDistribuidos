package client;

import java.rmi.*;
import interfaces.HelloWorldInterface;

public class Main {
    public static void main(String[] args) {
        try {
            HelloWorldInterface helloWorld = (HelloWorldInterface) Naming.lookup("//localhost/hello-world");

            System.out.println(helloWorld.say());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
