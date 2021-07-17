package core.models;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String name;
    private String cpf;
    private String password;

    public User() {}

    public User(String name, String cpf, String password, int id) {
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

    public void setId(int id) {
        this.id = id;
    }


    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getId() {
        return id;
    }
}
