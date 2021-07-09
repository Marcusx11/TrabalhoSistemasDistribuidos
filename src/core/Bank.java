package core;

import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.blocks.RequestHandler;

public class Bank extends ReceiverAdapter implements RequestHandler {
    public void receive(Message message) {
        System.out.println("[receive]: " + message.getSrc() + ": " + message.getObject());
    }

    public void viewAccepted(View newView) {
        System.out.println("\t[new view cluster]: " + newView);
    }

    @Override
    public Object handle(Message message) throws Exception {
        System.out.println("[handle]: " + message.getSrc() + ": " + message.getObject());
        return null;
    }
}
