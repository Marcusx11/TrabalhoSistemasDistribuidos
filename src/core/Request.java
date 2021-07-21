package core;

import java.io.Serializable;

public class Request implements Serializable {
    private final RequestCode requestCode;
    private final Object body;

    public Request(RequestCode requestCode, Object body) {
        this.requestCode = requestCode;
        this.body = body;
    }

    public Object getBody() {
        return body;
    }

    public RequestCode getRequestCode() {
        return requestCode;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestCode=" + requestCode +
                ", body=" + body +
                '}';
    }
}
