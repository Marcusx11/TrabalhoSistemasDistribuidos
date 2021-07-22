package client;

import interfaces.BankInterface;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import core.Constants;
import core.models.user.User;
import core.Response;
import core.ResponseCode;

public class Main {
    private static BankInterface bank;

    private static Scanner input;

    private static User authUser = null;

    public static void main(String[] args) {
        try {
            bank = (BankInterface) Naming.lookup(Constants.RMI_URL);
            input = new Scanner(System.in);

            switchBetweenMenus();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void switchBetweenMenus() throws RemoteException {
        while (true) {
            if (authUser == null) {
                viewInitialMenu();
            } else {
                viewDashboardMenu();
            }
        }
    }

    public static void viewInitialMenu() throws RemoteException{
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

    public static void viewDashboardMenu() {
        System.out.println("**** Dashboard ****");
        System.out.println("Ola, seja bem vindo(a) " + authUser.getName());
        System.out.println("Ações");
        System.out.println("[1] - Verificar saldo");
        System.out.println("[2] - Transferencia");
        System.out.println("[3] - Verificar extrato");
        System.out.println("[4] - Verificar montante do banco");
        System.out.print("> ");

        String option = input.nextLine();
        switch (option) {
            default:
                System.out.println("invalid option");
        }

    }

    // TODO implementar depois da classe tranf. ser feita
    public static void viewSaldo() throws RemoteException {}
    public static void viewTransferencia() throws RemoteException {}
    public static void viewVerificarExtrato() throws RemoteException {}
    public static void viewVerificarMontanteBanco() throws RemoteException {}

    public static void viewRegister() throws RemoteException {
        System.out.println("**** Register ****");
        System.out.print("Nome: ");
        String name = input.nextLine();
        System.out.print("CPF: ");
        String cpf = input.nextLine();
        System.out.print("Password: ");
        String password = input.nextLine();

        Response response = bank.register(name, cpf, password);

        System.out.println(response.getBody());
    }

    public static void viewLogin() throws RemoteException {
        System.out.println("**** Login ****");
        System.out.print("CPF: ");
        String cpf = input.nextLine();
        System.out.print("Password: ");
        String password = input.nextLine();

        Response response = bank.login(cpf, password);

        if (response.getRequestCode() == ResponseCode.OK) {
            authUser = (User) response.getBody();
            viewDashboardMenu();
        } else {
            System.out.println(response.getBody());
        }
    }
}
