package server;

import core.Response;
import core.ResponseCode;
import core.models.transfer.TransferDAO;
import core.models.user.User;
import core.models.user.UserDAO;

public class Authentication {
    public Response login(User userParams) {
        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.findBy("cpf", userParams.getCpf());

            if (user != null) {
                if (userParams.getPassword().equals(user.getPassword())) {
                    user.setOnline(1);
                    userDAO.update(user);
                    return new Response(ResponseCode.OK, user);
                }
            }

            return new Response(ResponseCode.ERROR, "Invalid password or CPF. Please try again.");
        } catch (Exception e) {
            return new Response(ResponseCode.ERROR, "There was a problem with login. Please try again.");
        }
    }

    public Response register(User user) {
        try {
            UserDAO userDAO = new UserDAO();
            TransferDAO transferDAO = new TransferDAO();

            userDAO.create(user);
            transferDAO.create(user.getTransfers().get(0));

            return new Response(ResponseCode.OK, "The user was successfully created.");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(ResponseCode.ERROR, "There was a problem creating this user. Please try again.");
        }
    }
}
