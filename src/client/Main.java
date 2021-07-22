package client;

import interfaces.BankInterface;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
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
        System.out.println("**** E-Banking ****");
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

    public static void viewDashboardMenu() throws RemoteException {
        System.out.println("**** Dashboard ****");
        System.out.println("Ola, seja bem vindo(a) " + authUser.getName());
        System.out.println("Ações");
        System.out.println("[1] - Verificar saldo");
        System.out.println("[2] - Transferência");
        System.out.println("[3] - Verificar extrato");
        System.out.println("[4] - Verificar montante do banco");
        System.out.println("[5] - Listar todos os usuários cadastrados");
        System.out.println("[6] - Sair");
        System.out.print("> ");

        String option = input.nextLine();
        switch (option) {
            case "1":
                viewBalance();
                break;
            case "2":
                viewTransfer();
                break;

            case "5":
                viewListAllUsers();

            case "6":
                logout();
                break;
            default:
                System.out.println("invalid option");
        }
    }


    // TODO implementar depois da classe tranf. ser feita
    public static void viewBalance() throws RemoteException {
        System.out.println("**** Saldo ****");

        Response response = bank.balance(authUser);

        if (response.getRequestCode() == ResponseCode.OK) {
            System.out.println("Total: R$ " + response.getBody());
        } else {
            System.out.println(response.getBody());
        }
    }

    public static void viewTransfer() throws RemoteException {
        System.out.println("**** Transferência ****");
        System.out.print("Conta destino: ");
        long toId  = input.nextInt();
        System.out.print("Valor: ");
        float amount = input.nextFloat();

        Response response= bank.transfer(authUser.getId(), toId, amount);

        System.out.println(response.getBody());
    }

    public static void viewVerificarExtrato() throws RemoteException {}
    public static void viewVerificarMontanteBanco() throws RemoteException {}

    public static void viewRegister() throws RemoteException {
        System.out.println("**** Registro ****");
        System.out.print("Nome: ");
        String name = input.nextLine();
        System.out.print("CPF: ");
        String cpf = input.nextLine();
        System.out.print("Senha: ");
        String password = input.nextLine();

        Response response = bank.register(name, cpf, password);

        System.out.println(response.getBody());
    }

    public static void viewLogin() throws RemoteException {
        System.out.println("**** Entrar ****");
        System.out.print("CPF: ");
        String cpf = input.nextLine();
        System.out.print("Senha: ");
        String password = input.nextLine();

        Response response = bank.login(cpf, password);

        if (response.getRequestCode() == ResponseCode.OK) {
            authUser = (User) response.getBody();
            viewDashboardMenu();
        } else {
            System.out.println(response.getBody());
        }
    }

    @SuppressWarnings("unchecked")
    public static void viewListAllUsers() throws RemoteException {
        Response response = bank.listAllUsers();

        if (response.getRequestCode() == ResponseCode.OK) {
            List<User> users;
            users = (List<User>) response.getBody();

            System.out.println("| ID |     CPF     | NOME |" ));
            for (User user : users) {
                System.out.println(user.getId() + " | " + user.getCpf() + " | " + user.getName());
            }

            viewDashboardMenu();
        } else {
            System.out.println(response.getBody());
        }
    }

    public static void logout() {
        authUser = null;
    }
}
