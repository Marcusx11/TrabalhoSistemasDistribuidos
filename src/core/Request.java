package core;

import java.io.Serializable;

public class Request implements Serializable
{
    public String content;

    public Request(String content)
    {
        this.content = content;
    }
}
