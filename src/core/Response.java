package core;

import java.io.Serializable;

public class Response implements Serializable
{
    public String content;

    public Response(String content)
    {
        this.content = content;
    }
}
