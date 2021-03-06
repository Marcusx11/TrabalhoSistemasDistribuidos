package interfaces;

import core.Response;
import org.jgroups.Address;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.util.RspList;
import java.util.Collection;

public interface RequestDispatcherInterface {
    RspList<Response> sendRequestMulticast(
            Object value,
            ResponseMode responseMode
    ) throws Exception;

     RspList<Response> sendRequestMulticast(
            Object value,
            ResponseMode responseMode,
            Address removeAdd
    ) throws Exception;

     RspList<Response> sendRequestAnycast(
            Collection<Address> cluster,
            Object value,
            ResponseMode responseMode
    ) throws Exception;

     RspList<Response> sendRequestAnycast(
            Collection<Address> cluster,
            Object value,
            ResponseMode responseMode,
            Address remove
    ) throws Exception;

    Response sendRequestUnicast(
            Address receiver,
            Object value,
            ResponseMode responseMode
    ) throws Exception;
}
