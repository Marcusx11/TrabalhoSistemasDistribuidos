package client;

import core.Constants;
import interfaces.BankInterface;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Main {
    private static BankInterface bank;

    public static void main(String[] args) {
        try {
            bank = (BankInterface) Naming.lookup(Constants.RMI_URL);

            viewRegister();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void viewRegister() throws RemoteException {
        Scanner input = new Scanner(System.in);
        System.out.println("**** Register ***");
        System.out.print("Nome: ");
        String name = input.nextLine();
        System.out.print("CPF: ");
        String cpf = input.nextLine();
        System.out.print("Password: ");
        String password = input.nextLine();

        boolean isRegisterd = bank.register(name, cpf, password);
    }
}
