package core;

import org.jgroups.blocks.MessageDispatcher;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.util.RspList;
import org.jgroups.*;
import java.util.Collection;
import interfaces.RequestDispatcherInterface;

public class RequestDispatcher implements RequestDispatcherInterface
{
    private final MessageDispatcher dispatcher;

    public RequestDispatcher(JChannel channel, RequestHandler requestHandler)
    {
        this.dispatcher = new MessageDispatcher(
            channel,
            (MessageListener)requestHandler,
            (MembershipListener)requestHandler,
            requestHandler
        );
    }

    public RspList sendRequestMulticast(
            Object value,
            ResponseMode responseMode
    ) throws Exception
    {
        Message message = new Message(null, value);

        RequestOptions options = new RequestOptions();
        options.setMode(responseMode);
        options.setAnycasting(false);

        return this.dispatcher.castMessage(null, message, options);
    }

    public RspList sendRequestMulticast(
            Object value,
            ResponseMode responseMode,
            Address removeAdd
    ) throws Exception
    {
        Message message = new Message(null, value);

        RequestOptions options = new RequestOptions();
        options.setMode(responseMode);
        options.setAnycasting(false);
        options.setExclusionList(removeAdd);

        return this.dispatcher.castMessage(null, message, options);
    }

    public RspList sendRequestAnycast(
            Collection<Address> cluster,
            Object value,
            ResponseMode responseMode
    ) throws Exception
    {
        Message message = new Message(null, value);

        RequestOptions options = new RequestOptions();
        options.setMode(responseMode);
        options.setAnycasting(true);

        return this.dispatcher.castMessage(cluster, message, options);
    }

    public RspList sendRequestAnycast(
            Collection<Address> cluster,
            Object value,
            ResponseMode responseMode,
            Address remove
    ) throws Exception
    {
        Message message = new Message(null, value);

        RequestOptions options = new RequestOptions();
        options.setMode(responseMode);
        options.setAnycasting(true);
        options.setExclusionList(remove);

        return this.dispatcher.castMessage(cluster, message, options);
    }

    public Object sendRequestUnicast(
            Address receiver,
            Object value,
            ResponseMode responseMode
    ) throws Exception
    {
        Message message = new Message(receiver, value);

        RequestOptions options = new RequestOptions();
        options.setMode(responseMode);

        return this.dispatcher.sendMessage(message, options);
    }
}
