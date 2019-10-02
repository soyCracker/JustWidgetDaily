package com.x94.justwidgetdaily.Model;

public class HttpResult
{
    public final boolean isHandshakeOk;
    public final int responseCode;
    public final String responseBody;

    public static HttpResult handShakeOk(final int responseCode, final String responseBody)
    {
        final HttpResult result = new HttpResult(responseCode,responseBody);
        return result;
    }

    public static HttpResult handShakeError()
    {
        return new HttpResult();
    }

    private HttpResult()
    {
        this.isHandshakeOk = false;
        this.responseCode  = 0;
        this.responseBody  = "";
    }

    private HttpResult(final int responseCode, final String responseBody)
    {
        this.isHandshakeOk = true;
        this.responseCode  = responseCode;
        this.responseBody  = responseBody;
    }
}
