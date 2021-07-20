package core.models.user;

import java.io.Serializable;

public class User implements Serializable {
    private long id;
    private String name;
    private String cpf;
    private String password;
    private float balance;
    private int online = 0;

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

    public void setBalance(float balance) {
        this.balance = balance;
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

    public float getBalance() {
        return balance;
    }

    public int getOnline() {
        return online;
    }

    public long getId() {
        return id;
    }
}
