package server;

import core.Response;
import core.ResponseCode;
import core.models.transfer.Transfer;
import core.models.transfer.TransferDAO;
import core.models.user.User;
import core.models.user.UserDAO;

import java.util.List;

public class Operation {
    private float userBalance(User user) {
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

        return transfersInTotal - transfersOutTotal;
    }

    public Response transfer(Transfer transfer) {
        try {
            UserDAO userDAO = new UserDAO();
            User userTo = userDAO.findBy("id", String.valueOf(transfer.getToUserId()));
            User userFrom = userDAO.findBy("id", String.valueOf(transfer.getFromUserId()));

            if (userTo == null) {
                return new Response(ResponseCode.ERROR, "Destination account not found.");
            }

            if (userFrom == null) {
                return new Response(ResponseCode.ERROR, "Invalid source account.");
            }

            TransferDAO transferDAO = new TransferDAO();
            transferDAO.create(transfer);

            return new Response(ResponseCode.OK, "The transfer was successfully created.");
        } catch (Exception e) {
            return new Response(ResponseCode.ERROR, "There was a problem creating this transfer. Please try again.");
        }
    }

    public Response balance(User user) {
        try {
            return new Response(ResponseCode.OK, this.userBalance(user));
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

    public Response statementOfAccount(User user) {
        TransferDAO transferDAO = new TransferDAO();

        List<Transfer> transfers = transferDAO.fromUser(String.valueOf(user.getId()));

        return new Response(ResponseCode.OK, transfers);
    }

    public Response bankAmount() {
        UserDAO userDAO = new UserDAO();

        List<User> users = userDAO.selectAll();

        float total = 0;
        for (User user: users) {
            total += this.userBalance(user);
        }

        return new Response(ResponseCode.OK, total);
    }
}
