package com.chen;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.BasicHttpEntity;
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
import java.util.List;

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
//        HttpGet httpGet = new HttpGet(uri);
        HttpPost httpPost = new HttpPost(uri);
        List<BasicNameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("Jenkins-Crumb", "fe832c8dc80105fc304f231e1c7a496d1bd39c19e1d7e8fca19605201fb6a865"));
        list.add(new BasicNameValuePair("Submit", "开始构建"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list);
        httpPost.setEntity(urlEncodedFormEntity);

        // Add AuthCache to the execution context
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAuthCache(authCache);

        HttpResponse response = httpClient.execute(host, httpPost, localContext);


        return EntityUtils.toString(response.getEntity());
    }

    public static void main(String[] args) throws IOException {
        System.out.println(scrape("http://192.168.26.2:8080/job/QMS/job/qms-platform-build/build?delay=0sec", "dev", "dev0407@"));
    }
}