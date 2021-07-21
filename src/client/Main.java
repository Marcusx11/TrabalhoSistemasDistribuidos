package client;

import interfaces.BankInterface;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import core.Constants;
import core.models.user.User;

public class Main {
    private static BankInterface bank;

    private static Scanner input;

    private static User userLogado;

    public static void main(String[] args) {
        try {
            bank = (BankInterface) Naming.lookup(Constants.RMI_URL);
            input = new Scanner(System.in);

            viewMenu();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void viewMenu() throws RemoteException{
        while (true) {
            System.out.println("**** Menu ****");
            System.out.println("[1] - Login");
            System.out.println("[2] - Register");
            System.out.print("> ");

            String option = input.nextLine();
            switch (option) {
                case "1":
                    viewLogin();
                    break;
                case "2":
                    viewRegister();
                    break;
                default:
                    System.out.println("invalid option");
            }
        }
    }

    public static void viewRegister() throws RemoteException {
        System.out.println("**** Register ****");
        System.out.print("Nome: ");
        String name = input.nextLine();
        System.out.print("CPF: ");
        String cpf = input.nextLine();
        System.out.print("Password: ");
        String password = input.nextLine();

        boolean isRegisterd = bank.register(name, cpf, password);
    }

    public static void viewLogin() throws RemoteException {
        System.out.println("**** Login ****");
        System.out.print("CPF: ");
        String cpf = input.nextLine();
        System.out.print("Password: ");
        String password = input.nextLine();

        userLogado = bank.login(cpf, password);
    }

    public static void viewHome() throws RemoteException {
        String mensagem = "**** Operações ****\n";
        mensagem += "[1] - Consultar saldo\n";
        mensagem += "[2] - Transferir valor\n";
        mensagem += "[3] - Consultar histórico\n";
        mensagem += "[4] - Consultar montante do banco\n";
        mensagem += "> ";
        System.out.println(mensagem);
    }
}
