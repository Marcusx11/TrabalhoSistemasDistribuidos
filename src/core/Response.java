package core;

import java.io.Serializable;

public class Response implements Serializable {
    private final ResponseCode responseCode;
    private final Object body;

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

    @Override
    public String toString() {
        return "Response{" +
                "responseCode=" + responseCode +
                ", body=" + body +
                '}';
    }
}
