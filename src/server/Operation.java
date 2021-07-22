package server;

import core.Response;
import core.ResponseCode;
import core.models.transfer.Transfer;
import core.models.transfer.TransferDAO;
import core.models.user.User;
import core.models.user.UserDAO;

import java.util.List;

public class Operation {
    public Response transfer(Transfer transfer) {
        try {
            TransferDAO transferDAO = new TransferDAO();
            transferDAO.create(transfer);

            return new Response(ResponseCode.OK, "The transfer was successfully created.");
        } catch (Exception e) {
            return new Response(ResponseCode.ERROR, "There was a problem creating this transfer. Please try again.");
        }
    }

    public Response balance(User user) {
        try {
            TransferDAO transferDAO = new TransferDAO();

            List<Transfer> transfersOut = transferDAO.selectBy("from_user_id", String.valueOf(user.getId()));
            List<Transfer> transfersIn = transferDAO.selectBy("to_user_id", String.valueOf(user.getId()));

            float transfersOutTotal = 0;
            for (Transfer transfer:transfersOut) {
                transfersOutTotal += transfer.getAmount();
            }

            float transfersInTotal = 0;
            for (Transfer transfer:transfersIn) {
                transfersInTotal += transfer.getAmount();
            }

            return new Response(ResponseCode.OK, transfersInTotal - transfersOutTotal);
        } catch (Exception e) {
            return new Response(ResponseCode.ERROR, "There was a problem. Please try again.");
        }
    }

    public Response listAllUsers() {
        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.selectAll();

            return new Response(ResponseCode.OK, users);
        } catch (Exception e) {
            return new Response(ResponseCode.ERROR, "There was a problem. Please try again.");
        }
    }
}
