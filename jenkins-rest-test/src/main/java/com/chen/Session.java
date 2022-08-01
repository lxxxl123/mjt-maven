package com.chen;

import lombok.Getter;
import lombok.Setter;
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
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenwh3
 */
@Slf4j
public class Session {

    public static Pattern CRUMB_PATTERN = Pattern.compile("data-crumb-value=\"(\\w+)\"");


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

    public  BasicCookieStore buildCookie(String ip , HttpResponse httpResponse){
        HeaderElement element = httpResponse.getHeaders("Set-Cookie")[0].getElements()[0];
        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie(element.getName(), element.getValue());
        cookieStore.addCookie(cookie);
        cookie.setDomain(ip);
        this.context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        this.cookie = cookieStore;
        return cookieStore;
    }

    public HttpContext context = new BasicHttpContext();
    public BasicCookieStore cookie;

    @Getter
    public String crumb;

    @Getter
    public String node;

    @Setter
    private String host = "http://192.168.26.2:8080";


    public void login(String url, String username, String password) throws IOException {
        URI uri = URI.create(url);

        String raw = MessageFormat.format("j_username={0}&j_password={1}&from=%2F&Submit=%E7%99%BB%E5%BD%95", username, URLEncoder.encode(password, "utf-8"));
        HttpEntity build = EntityBuilder
                .create()
                .setContentType(ContentType.APPLICATION_FORM_URLENCODED)
                .setText(raw)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .build();

        //get请求随机获取 sessionId
        HttpGetWithEntity httpGet = new HttpGetWithEntity(url);
        httpGet.setEntity(build);
        CloseableHttpResponse res = httpClient.execute(httpGet);
        buildCookie(uri.getHost(), res);

        //post请求获取可用 sessionId
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(build);
        res = httpClient.execute(httpPost, context);
        buildCookie(uri.getHost(), res);

        //get请求获取crumb
        HttpGet get = new HttpGet(uri.getScheme() + "://" + uri.getAuthority());
        res = httpClient.execute(get, context);
        String content = EntityUtils.toString(res.getEntity());
        check(content);

        this.node = cookie.getCookies().get(0).getValue();
        this.crumb = pickCrumb(content);
    }


    public static void check(String res){
        if (res.contains("Authentication required")) {
            throw new RuntimeException("Authentication required");
        }
    }


    public void build(String branchName) throws IOException {

        branchName = URLEncoder.encode(branchName, "utf-8");
        HttpPost httpPost = new HttpPost(host + "/job/QMS/job/qms-platform-build/build?delay=0sec");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Cookie", "JSESSIONID.0f09aa34=" + node);
        HttpEntity build = EntityBuilder
                .create()
                .setContentType(ContentType.APPLICATION_FORM_URLENCODED)
                .setText("name=branch&value=" +
                        branchName +
                        "&statusCode=303&redirectTo=.&Jenkins-Crumb=" +
                        crumb +
                        "&json=%7B%22parameter%22%3A+%7B%22name%22%3A+%22branch%22%2C+%22value%22%3A+%22" +
                        branchName +
                        "%22%7D%2C+%22statusCode%22%3A+%22303%22%2C+%22redirectTo%22%3A+%22.%22%2C+%22Jenkins-Crumb%22%3A+%22" +
                        crumb +
                        "%22%7D&Submit=%E5%BC%80%E5%A7%8B%E6%9E%84%E5%BB%BA").build();

        httpPost.setEntity(build);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpEntity entity = httpClient.execute(httpPost).getEntity();
        check(EntityUtils.toString(entity));
    }

    public static String pickCrumb(String content){
        Matcher matcher = CRUMB_PATTERN.matcher(content);
        matcher.find();
        return matcher.group(1);
    }

    public boolean getProcess(String jobName) throws IOException {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet get = new HttpGet(host+"/job/QMS/job/" + jobName + "/buildHistory/ajax");

        HttpEntity entity = httpClient.execute(get, context).getEntity();
        String res = EntityUtils.toString(entity);
        check(res);
        return res.contains("预计剩余时间：") || res.contains("pending—Waiting for next available executor");
    }

    public void deploy() throws IOException {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpPost post = new HttpPost(host+"/job/QMS/job/qms-platform-deploy/build?delay=0sec");
        post.setHeader("Jenkins-Crumb", crumb);
        HttpEntity entity = httpClient.execute(post, context).getEntity();
        String res = EntityUtils.toString(entity);
        check(res);
    }

    /**
     * 用法: session.checkStatus("QMS » qms-platform-deploy")
     */
    public boolean checkStatus(String queueName) throws IOException {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet method = new HttpGet(host+"/job/QMS/ajaxExecutors");
        method.setHeader("Jenkins-Crumb", crumb);
        HttpEntity entity = httpClient.execute(method, context).getEntity();
        String res = EntityUtils.toString(entity);
        check(res);
        return res.contains(queueName);
    }

    public void buildAndDeploy(String branchName,String host) throws IOException, InterruptedException {
        int wait = 3;
        setHost(host);
        log.info("开始登录");
        login(host + "/j_spring_security_check", "dev", "dev0407@");
        log.info("开始构建");
        build(branchName);
        TimeUnit.SECONDS.sleep(wait);
        while (getProcess("qms-platform-build")) {
            log.info("构建中");
            TimeUnit.SECONDS.sleep(wait);
        }
        log.info("开始部署");
        deploy();
        TimeUnit.SECONDS.sleep(wait);
        while (getProcess("qms-platform-deploy")) {
            log.info("部署中");
            TimeUnit.SECONDS.sleep(wait);
        }
        log.info("部署完成");
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Session session = new Session();
        String branchName = "feature/market-complainV1.0.0-front-end";
//        String branchName = "feature/QT07-v1.0.0";
        session.buildAndDeploy(branchName,"http://192.168.26.2:8080");
    }
}