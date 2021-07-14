package core;

import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.util.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Bank extends ReceiverAdapter implements RequestHandler {
    /**
     * obtemos uma mensagem como argumento.
     * @param message
     */
    public void receive(Message message) {
        System.out.println("[receive]: " + message.getSrc() + ": " + message.getObject());
    }

    /**
     * O retorno de chamada viewAccepted () é chamado sempre que uma nova instância se junta ao cluster ou uma instância
     * existente sai (travamentos incluídos).
     * @param newView -
     */
    @Override
    public void viewAccepted(View newView) {
        System.out.println("\t[new view cluster]: " + newView);
    }

    @Override
    public Object handle(Message message) throws Exception {
        System.out.println("[handle]: " + message.getSrc() + ": " + message.getObject());
        return null;
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        System.out.println("getState");
    }

    /**
     * O método setState () é chamado no solicitante de estado , ou seja. a instância que chamou JChannel.getState ().
     * Sua tarefa é ler o estado do fluxo de entrada e configurá-lo de acordo:
     * @param input
     * @throws Exception
     */
    @Override
    public void setState(InputStream input) throws Exception {
        System.out.println("Set state");
    }
}
