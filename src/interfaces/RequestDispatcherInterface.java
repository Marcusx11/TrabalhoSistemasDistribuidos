package interfaces;

import org.jgroups.Address;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.util.RspList;
import java.util.Collection;

public interface RequestDispatcherInterface {
    RspList sendRequestMulticast(
            Object value,
            ResponseMode responseMode
    ) throws Exception;

     RspList sendRequestMulticast(
            Object value,
            ResponseMode responseMode,
            Address removeAdd
    ) throws Exception;

     RspList sendRequestAnycast(
            Collection<Address> cluster,
            Object value,
            ResponseMode responseMode
    ) throws Exception;

     RspList sendRequestAnycast(
            Collection<Address> cluster,
            Object value,
            ResponseMode responseMode,
            Address remove
    ) throws Exception;

    Object sendRequestUnicast(
            Address receiver,
            Object value,
            ResponseMode responseMode
    ) throws Exception;
}
