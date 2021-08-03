package client;

import core.models.transfer.Transfer;
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

            viewInitialMenu();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void viewInitialMenu() throws RemoteException {
        while (true) {
            if (authUser == null) {
                viewAuthMenu();
            } else {
                viewDashboardMenu();
            }
        }
    }

    public static void viewAuthMenu() throws RemoteException{
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
            case "3":
                viewStatementOfAccount();
                break;
            case "4":
                viewBankAmount();
                break;
            case "5":
                viewListAllUsers();
                break;
            case "6":
                logout();
                break;
            default:
                System.out.println("invalid option");
        }
    }

    public static void viewBankAmount() throws RemoteException {
        System.out.println("**** Montante do banco ****");

        Response response = bank.bankAmount(authUser);

        if (response.getRequestCode() == ResponseCode.OK) {
            System.out.println("Total: R$ " + response.getBody());
        } else {
            System.out.println(response.getBody());
        }
    }

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

        Response response= bank.transfer(authUser, toId, amount);

        System.out.println(response.getBody());
    }

    public static void viewStatementOfAccount() throws RemoteException {
        System.out.println("**** Extrato ****");
        Response response = bank.statementOfAccount(authUser);

        if (response.getRequestCode() == ResponseCode.OK) {
            List<Transfer> transfers = (List<Transfer>) response.getBody();

            for (Transfer transfer : transfers) {
                if (transfer.getFromUserId() == 0) {
                    System.out.println("Deposito de R$ " + transfer.getAmount());
                } else if (transfer.getFromUserId() == authUser.getId()) {
                    System.out.println("Transferencia de R$ " + transfer.getAmount() + " para a conta " +
                            transfer.getToUserId());
                } else {
                    System.out.println("Transferencia recebida da conta " + transfer.getFromUserId() +
                            " no valor de R$ " + transfer.getAmount());
                }

                System.out.println("---------------------------------------------------");
            }
        } else {
            System.out.println(response.getBody());
        }
    }

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

    public static void viewListAllUsers() throws RemoteException {
        Response response = bank.listAllUsers(authUser);

        if (response.getRequestCode() == ResponseCode.OK) {
            List<User> users = (List<User>) response.getBody();

            System.out.println("| ID |     CPF     | NOME |" );
            for (User user : users) {
                System.out.println(user.getId() + " | " + user.getCpf() + " | " + user.getName());
            }
        } else {
            System.out.println(response.getBody());
        }
    }

    public static void logout() throws RemoteException {
        Response response = bank.logout(authUser);

        if (response.getRequestCode() == ResponseCode.OK) {
            authUser = null;
        } else {
            System.out.println(response.getBody());
        }
    }
}
