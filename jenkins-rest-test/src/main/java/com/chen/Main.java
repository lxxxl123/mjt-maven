package com.chen;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenwh3
 */
@Slf4j
public class Main {

    public static class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {

        public final static String METHOD_NAME = "GET";
        public HttpGetWithEntity(final String url) {
            super();
            setURI(URI.create(url));
        }
        @Override
        public String getMethod() {
            return METHOD_NAME;
        }
    }

    public static BasicCookieStore getCookie(String ip , HttpResponse httpResponse){
        HeaderElement element = httpResponse.getHeaders("Set-Cookie")[0].getElements()[0];
        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie(element.getName(), element.getValue());
        cookieStore.addCookie(cookie);
        cookie.setDomain(ip);
        return cookieStore;
    }

    public static String login(String url, String username, String password) throws IOException {
        URI uri = URI.create(url);

        HttpGetWithEntity httpGet = new HttpGetWithEntity(url);


        String raw = MessageFormat.format("j_username={0}&j_password={1}&from=%2F&Submit=%E7%99%BB%E5%BD%95", username, URLEncoder.encode(password, "utf-8"));
        HttpEntity build = EntityBuilder
                .create()
                .setContentType(ContentType.APPLICATION_FORM_URLENCODED)
                .setText(raw)
                .build();

        httpGet.setEntity(build);


        CloseableHttpClient httpClient = HttpClients.custom()
                .build();

        //get请求随机获取 sessionId
        CloseableHttpResponse res = httpClient.execute(httpGet);
        BasicCookieStore cookie = getCookie(uri.getHost(), res);

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(build);


        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookie);
        //post请求获取可用 sessionId
        res = httpClient.execute(httpPost, localContext);

        cookie = getCookie(uri.getHost(), res);
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookie);

        //get请求获取crumb
        HttpGet get = new HttpGet(uri.getScheme() + "://" + uri.getAuthority());
        res = httpClient.execute(get, localContext);

        String node = cookie.getCookies().get(0).getValue();
        String crumb = getCrumb(EntityUtils.toString(res.getEntity()));

        //执行build方法
        build(node, crumb, "feature/market-complainV1.0.0-front-end");
        return null;
    }

    public static void build(String node, String crumb,String branchName) throws IOException {
        branchName = URLEncoder.encode(branchName, "utf-8");
        HttpPost httpPost = new HttpPost("http://192.168.26.2:8080/job/QMS/job/qms-platform-build/build?delay=0sec");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Cookie", "JSESSIONID.0f09aa34=" + node);
        HttpEntity build = EntityBuilder
                .create()
                .setContentType(ContentType.APPLICATION_FORM_URLENCODED)
                .setText("name=branch&value=" +
                        branchName +
                        "&statusCode=303&redirectTo=.&Jenkins-Crumb=" +
                        crumb +
                        "&json=%7B%22parameter%22%3A+%7B%22name%22%3A+%22branch%22%2C+%22value%22%3A+%22origin%2Ffeature%2Fmarket-complainV1.0.0-front-end%22%7D%2C+%22statusCode%22%3A+%22303%22%2C+%22redirectTo%22%3A+%22.%22%2C+%22Jenkins-Crumb%22%3A+%22" +
                        crumb +
                        "%22%7D&Submit=%E5%BC%80%E5%A7%8B%E6%9E%84%E5%BB%BA").build();

        httpPost.setEntity(build);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpEntity entity = httpClient.execute(httpPost).getEntity();
        System.out.println(EntityUtils.toString(entity));
    }

    public static String getCrumb(String content){
        Matcher matcher = CRUMB_PATTERN.matcher(content);
        matcher.find();
        return matcher.group(1);
    }

    public static Pattern CRUMB_PATTERN = Pattern.compile("data-crumb-value=\"(\\w+)\"");
    public static void main(String[] args) throws IOException {
        login("http://192.168.26.2:8080/j_spring_security_check", "dev", "dev0407@");
    }
}