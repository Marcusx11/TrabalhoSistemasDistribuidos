package core;


import org.jgroups.*;
import org.jgroups.blocks.*;
import org.jgroups.util.*;

import java.util.*;

public class Channel {
    private static final String channelName = "bankGroup";
    private static Channel instance = null;

    private final JChannel channel;
    private final MessageDispatcher messageDispatcher;

    private Channel() throws Exception {
        channel = new JChannel("src/configs.xml");
        messageDispatcher = new MessageDispatcher(this.channel, null, null);
        channel.connect(Channel.channelName);
    }

    public void setReceiver(Receiver receiver) {
        this.channel.setReceiver(receiver);
    }

    public JChannel getChannel() {
        return channel;
    }

    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }

    public RspList sendAll(String body, ResponseMode mode) throws Exception {
        Message message = new Message(null, body);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.setMode(mode);
        requestOptions.setAnycasting(false);

        return this.messageDispatcher.castMessage(null, message, requestOptions);
    }

    public RspList sendAny(Collection<Address> nodes, String body, ResponseMode mode) throws Exception{
        Message message = new Message(null, body);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.setMode(mode);
        requestOptions.setAnycasting(true);

        return this.messageDispatcher.castMessage(nodes, message, requestOptions);
    }

    public String send(Address dest, String body) throws Exception{
        Message message = new Message(dest, body);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.setMode(ResponseMode.GET_FIRST);

        return this.messageDispatcher.sendMessage(message, requestOptions);
    }

    public static Channel getInstance() throws Exception {
        if (instance == null) {
            instance = new Channel();
        }

        return instance;
    }
}
