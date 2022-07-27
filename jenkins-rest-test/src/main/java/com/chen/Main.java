package com.chen;

import com.cdancy.jenkins.rest.JenkinsClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenwh3
 */
@Slf4j
public class Main {

    public static String scrape(String urlString, String username, String password)
            throws ClientProtocolException, IOException {
        URI uri = URI.create(urlString);
        HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
                new UsernamePasswordCredentials(username, password));
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(host, basicAuth);
        CloseableHttpClient httpClient =
                HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("Connection", "keep-alive");
        // Add AuthCache to the execution context
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAuthCache(authCache);

        HttpResponse r1 = httpClient.execute(host, httpGet, localContext);
        Header header = r1.getHeaders("Set-Cookie")[0];
        String node = header.getElements()[0].getValue();
        String crumb = getCrumb(EntityUtils.toString(r1.getEntity()));
        System.out.println(crumb);
        node = "node0h90asjwpgflwthrf22ta212y8219.node0";
        crumb = "ec9cbdc2bc643e27a418d7dc2ceab09565f9f04ff728bfbbe92a546775def662";

        HttpPost httpPost = new HttpPost("http://192.168.26.2:8080/job/QMS/job/qms-platform-build/build?delay=0sec");
        httpPost.setHeader("Cookie", "JSESSIONID.0f09aa34=" + node);
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        EntityBuilder
                .create()
                .setContentType(ContentType.APPLICATION_FORM_URLENCODED)
                .setText("name=branch&value=origin%2Ffeature%2Fmarket-complainV1.0.0-front-end&statusCode=303&redirectTo=.&Jenkins-Crumb=" +
                        crumb +
                        "&json=%7B%22parameter%22%3A+%7B%22name%22%3A+%22branch%22%2C+%22value%22%3A+%22origin%2Ffeature%2Fmarket-complainV1.0.0-front-end%22%7D%2C+%22statusCode%22%3A+%22303%22%2C+%22redirectTo%22%3A+%22.%22%2C+%22Jenkins-Crumb%22%3A+%22" +
                        crumb +
                        "%22%7D&Submit=%E5%BC%80%E5%A7%8B%E6%9E%84%E5%BB%BA").build();
        HttpEntity entity = httpClient.execute(host, httpPost, localContext).getEntity();
        System.out.println(EntityUtils.toString(entity));


        return null;
    }

    public static String getCrumb(String content){
        Matcher matcher = CRUMB_PATTERN.matcher(content);
        matcher.find();
        return matcher.group(1);
    }

    public static Pattern CRUMB_PATTERN = Pattern.compile("data-crumb-value=\"(\\w+)\"");
    public static void main(String[] args) throws IOException {
        System.out.println(scrape("http://192.168.26.2:8080/", "dev", "dev0407@"));

    }
}