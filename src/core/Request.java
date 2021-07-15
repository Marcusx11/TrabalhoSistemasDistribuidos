package core;

import java.io.Serializable;

public class Request implements Serializable {
    private RequestCode requestCode;
    private Object body;

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
}
