package core;

import core.models.user.User;

public class RequestAuth extends Request {
    private final User user;

    public RequestAuth(RequestCode requestCode, User user, Object body) {
        super(requestCode, user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
