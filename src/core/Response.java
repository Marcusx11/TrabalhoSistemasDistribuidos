package core;

import java.io.Serializable;

public class Response implements Serializable {
    private ResponseCode responseCode;
    private Object body;

    public Response(ResponseCode responseCode, Object body) {
        this.responseCode = responseCode;
        this.body = body;
    }

    public Object getBody() {
        return body;
    }

    public ResponseCode getRequestCode() {
        return responseCode;
    }
}
