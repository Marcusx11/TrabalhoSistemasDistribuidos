package interfaces;

import java.rmi.*;

public interface HelloWorldInterface extends Remote {
    public String say() throws RemoteException;
}
