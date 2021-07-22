package core.models.user;

import core.models.transfer.Transfer;
import core.models.transfer.TransferDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private long id;
    private String name;
    private String cpf;
    private String password;
    private int online = 0;
    private List<Transfer> transfers = new ArrayList<Transfer>();

    public User() {}

    public User(String name, String cpf, String password, long id) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.password = password;
    }

    public User(String name, String cpf, String password) {
        this.name = name;
        this.cpf = cpf;
        this.password = password;
    }

    public User(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getOnline() {
        return online;
    }

    public long getId() {
        return id;
    }

    public void addTransfer(Transfer transfer) {
        transfers.add(transfer);
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
