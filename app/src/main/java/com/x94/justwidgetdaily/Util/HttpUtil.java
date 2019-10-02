package com.x94.justwidgetdaily.Util;

import android.util.Log;
import com.x94.justwidgetdaily.Base.Config;
import com.x94.justwidgetdaily.Model.HttpResult;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpUtil
{
    public static HttpResult get(final String url, final String languageCode, final int timeout)
    {
        try
        {
            //发送GET请求
            final HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            //HttpURLConnection默认就是用GET发送请求，所以下面的setRequestMethod可以省略
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Language", languageCode);
            conn.setConnectTimeout(timeout * 1000);
            conn.setReadTimeout(timeout * 1000);
            conn.setUseCaches(false);
            //conn.setDoOutput(true);
            conn.setDoInput(true);
            //在对各种参数配置完成后，通过调用connect方法建立TCP连接，但是并未真正获取数据
            //conn.connect()方法不必显式调用，当调用conn.getInputStream()方法时内部也会自动调用connect方法
            conn.connect();
            //调用getInputStream方法后，服务端才会收到请求，并阻塞式地接收服务端返回的数据
            conn.getInputStream();
            String body = recvResponseBodyImpl(conn);
            return HttpResult.handShakeOk(conn.getResponseCode(), body);
        }
        catch(Exception e)
        {
            Log.e(Config.TAG,"HttpUtil exception:"+e.getMessage() + "\n@@:" + e.getLocalizedMessage());
        }
        return null;
    }

    public static HttpResult post(final String url, final String body, final String contentType, final int timeout, final String languageCode)
    {
        HttpURLConnection conn = null;
        try
        {
            conn = tcpConnect( url, contentType, timeout, languageCode);
            sendResuest( conn, body);
            return recvResponseBody(conn);
        }
        catch (Exception e)
        {
            Log.e(Config.TAG,e.getMessage());
            return HttpResult.handShakeError();
        }
        finally
        {
            if( conn != null )
                conn.disconnect();
        }
    }

    // Private:
    private static HttpURLConnection tcpConnect(final String url, final String contentType, final int timeout, final String languageCode) throws Exception {
        try {
            return tcpConnectImpl(url, contentType, timeout, languageCode);
        } catch (MalformedURLException e) {
            Log.e(Config.TAG,"HttpUtil::configureConnection::MalformedURLException");
            throw e;
        } catch (ProtocolException e) {
            Log.e(Config.TAG,"HttpUtil::configureConnection::ProtocolException");
            throw e;
        } catch (IOException e) {
            Log.e(Config.TAG,"HttpUtil::configureConnection::IOException");
            throw e;
        }
    }

    private static void sendResuest(final HttpURLConnection conn, final String body) throws Exception {
        try {
            sendResuestImpl(conn, body);
        } catch (IOException e) {
            Log.e(Config.TAG,"HttpUtil::sendResuest::IOException");
            throw e;
        }
    }

    private static HttpResult recvResponseBody(final HttpURLConnection conn) throws IOException
    {
        try {
            final String body = recvResponseBodyImpl(conn);
            Log.d(Config.TAG, body);
            return HttpResult.handShakeOk(conn.getResponseCode(), body);
        } catch (IOException e) {
            final int code = conn.getResponseCode();
            if (isServerEcho(code))
                return HttpResult.handShakeOk(code, "");
            else
                throw e;
        }
    }

    // Private Impl
    private static HttpURLConnection tcpConnectImpl(final String url, final String contentType, final int timeout, final String languageCode) throws IOException {
        final HttpURLConnection result = (HttpURLConnection) new URL(url).openConnection();

        result.setRequestMethod("POST");
        result.setRequestProperty("Content-Type", contentType);
        result.setRequestProperty("User-Agent", "JustWidgetDaily");
        result.setRequestProperty("Accept-Language", "languageCode");
        result.setConnectTimeout(timeout * 1000);
        result.setReadTimeout(timeout * 1000);
        result.setUseCaches(false);
        result.setDoOutput(true);
        result.setDoInput(true);
        result.connect();
        return result;
    }

    private static void sendResuestImpl(final HttpURLConnection conn, final String body) throws IOException
    {
        try (final DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.writeBytes(body);
            wr.flush();
        }
    }

    private static String recvResponseBodyImpl(final HttpURLConnection conn) throws IOException
    {
        try (final InputStreamReader stream = new InputStreamReader(conn.getInputStream()))
        {
            try (final BufferedReader reader = new BufferedReader(stream))
            {
                return readToEnd(reader);
            }
        }
    }

    private static String readToEnd(final BufferedReader reader) throws IOException
    {
        final StringBuffer buffer = new StringBuffer();

        while (true) {
            final String line = reader.readLine();
            if (line == null)
                break;
            else
                buffer.append(line);
        }
        return buffer.toString();
    }

    private static boolean isServerEcho(final int code)
    {
        final int[] supportCode = new int[]{
                400, 401, 402, 403, 404, 405, 406, 407, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 428, 429, 431, 451,
                500, 501, 503, 505, 507, 508, 510};

        for (int c : supportCode)
            if (c == code) return true;
        return false;
    }
}