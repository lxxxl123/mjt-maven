package com.chen;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HtmlUtil;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import org.apache.http.impl.client.HttpClientBuilder;
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
public class Session {

    public static final String AUTHENTICATION_REQUIRED = "Authentication required";
    public static final String TIME_REMAIN = "预计剩余时间：";
    public static final String PENDING_WAITING_FOR_NEXT_AVAILABLE_EXECUTOR = "pending—Waiting for next available executor";
    public static final String FINISHED_FAILURE = "Finished: FAILURE";
    public static final String JOB_QMS_JOB = "/job/QMS/job/";
    public static final Pattern CRUMB_PATTERN = Pattern.compile("data-crumb-value=\"(\\w+)\"");


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

    protected HttpContext context = new BasicHttpContext();
    protected BasicCookieStore cookie;

    @Getter
    public String crumb;

    @Getter
    public String node;

    @Setter
    @Getter
    private String host = "http://192.168.26.2:8080";
//    private String host = "http://192.168.26.2:8081";

    private String jobSeq;

    private String sessionIdPrefix;



    public void login(String url, String username, String password) throws IOException {
        URI uri = URI.create(url);


        @Cleanup
        CloseableHttpClient httpClient = HttpClients.custom()
                .build();

        //get请求随机获取 sessionId
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse res = httpClient.execute(httpGet);
        buildCookie(uri.getHost(), res);

        this.sessionIdPrefix = cookie.getCookies().get(0).getName().replace("JSESSIONID.", "");

        //post请求登录该sessionId
        HttpPost httpPost = new HttpPost(url);
        String raw = MessageFormat.format("j_username={0}&j_password={1}&from=%2F&Submit=%E7%99%BB%E5%BD%95", username, URLEncoder.encode(password, "utf-8"));
        HttpEntity build = EntityBuilder
                .create()
                .setContentType(ContentType.APPLICATION_FORM_URLENCODED)
                .setText(raw)
                .build();
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
        if (res.contains(AUTHENTICATION_REQUIRED)) {
            throw new RuntimeException(AUTHENTICATION_REQUIRED);
        }
    }


    public void build(String job, String branchName) throws IOException {
        branchName = "origin/" + branchName;

        HttpPost httpPost = new HttpPost(CharSequenceUtil.format("{}/job/QMS/job/{}/build?delay=0sec", host, job));
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Cookie", String.format("JSESSIONID.%s=%s", sessionIdPrefix, node));
        EntityBuilder entityBuilder = EntityBuilder
                .create()
                .setContentType(ContentType.APPLICATION_FORM_URLENCODED);
        if (StringUtils.isNotBlank(branchName)) {
            branchName = URLEncoder.encode(branchName, "utf-8");
            entityBuilder
                    .setText("name=branch&value=" +
                            branchName +
                            "&statusCode=303&redirectTo=.&Jenkins-Crumb=" +
                            crumb +
                            "&json=%7B%22parameter%22%3A+%7B%22name%22%3A+%22branch%22%2C+%22value%22%3A+%22" +
                            branchName +
                            "%22%7D%2C+%22statusCode%22%3A+%22303%22%2C+%22redirectTo%22%3A+%22.%22%2C+%22Jenkins-Crumb%22%3A+%22" +
                            crumb +
                            "%22%7D&Submit=%E5%BC%80%E5%A7%8B%E6%9E%84%E5%BB%BA");
        }
        HttpEntity build = entityBuilder.build();
        httpPost.setEntity(build);
        @Cleanup
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpEntity entity = httpClient.execute(httpPost).getEntity();
        check(EntityUtils.toString(entity));

    }

    public static String pickCrumb(String content){
        Matcher matcher = CRUMB_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }else{
            throw new RuntimeException("登录失败 , 无法获取crumb");
        }
    }

    private boolean isRunning = false;

    public boolean getProcess(String jobName) throws IOException {
        @Cleanup
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet get = new HttpGet(CharSequenceUtil.format("{}{}{}/buildHistory/ajax", host, JOB_QMS_JOB, jobName));

        try {
            CloseableHttpResponse execute = httpClient.execute(get, context);
            HttpEntity entity = execute.getEntity();
            String res = EntityUtils.toString(entity);
            check(res);
            if (StringUtils.containsAny(res, TIME_REMAIN, PENDING_WAITING_FOR_NEXT_AVAILABLE_EXECUTOR, "Estimated remaining time:")) {
                if (jobSeq == null) {
                    jobSeq = ReUtil.extractMulti(String.format("%s/(\\d+)/console", jobName), res, "$1");
                } else {
                    isRunning = true;
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("", e);
            if (isRunning) {
                return false;
            }
            throw e;
        }
    }

    public static String formatHtml(String s) {
        return HtmlUtil.cleanHtmlTag(s.replace("\n]", "]"));
    }


    public void triggerJob(String job) throws IOException {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpPost post = new HttpPost(host + JOB_QMS_JOB + job + "/build?delay=0sec");
        post.setHeader("Jenkins-Crumb", crumb);
        HttpEntity entity = httpClient.execute(post, context).getEntity();
        String res = EntityUtils.toString(entity);
        check(res);
    }


    public String getConsole(String jobName) throws IOException {
        @Cleanup
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet http = new HttpGet(host + JOB_QMS_JOB + jobName + "/" + jobSeq + "/console");
        this.jobSeq = null;
        HttpEntity entity = httpClient.execute(http, context).getEntity();
        String res = EntityUtils.toString(entity);
        check(res);
        res = formatHtml(StringUtils.substringAfter(res, "Started by user "));
        checkConsole(res);
        return res;
    }

    private void checkConsole(String res) {
        if (res.contains(FINISHED_FAILURE)) {
            log.error("build or deploy fail ; msg: \n{}", res);
            throw new RuntimeException("build or deploy fail");
        }
    }


    /**
     * 用法: session.checkStatus("QMS » qms-platform-deploy")
     */
    public boolean checkStatus(String queueName) throws IOException {
        @Cleanup
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet method = new HttpGet(host+"/job/QMS/ajaxExecutors");
        method.setHeader("Jenkins-Crumb", crumb);
        HttpEntity entity = httpClient.execute(method, context).getEntity();
        String res = EntityUtils.toString(entity);
        check(res);
        return res.contains(queueName);
    }





}