package com.chen;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.net.URLEncodeUtil;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.NameValuePairHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyHttpClient {

    public static class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {

        public static final String METHOD_NAME = "GET";
        public HttpGetWithEntity(final String url) {
            super();
            setURI(URI.create(url));
        }
        @Override
        public String getMethod() {
            return METHOD_NAME;
        }
    }

    private HttpEntityEnclosingRequestBase request;

    protected HttpContext context = new BasicHttpContext();

    private HttpEntity entity;

    protected BasicCookieStore cookies;

    private HttpResponse response;

    private URI uri ;


    public MyHttpClient post(String url){
        uri = URI.create(url);
        request = new HttpPost(url);
        request.setHeader("Connection","keep-alive");
        return this;
    }

    public MyHttpClient get(String url){
        uri = URI.create(url);
        request = new HttpGetWithEntity(url);
        return this;
    }

    public MyHttpClient header(String key, String value){
        request.setHeader(key, value);
        return this;
    }

    public  void buildCookie(String ip , HttpResponse httpResponse){
        Header[] setCookies = httpResponse.getHeaders("Set-Cookie");
        if (setCookies.length == 0) {
            return;
        }
        HeaderElement element = setCookies[0].getElements()[0];
        if (cookies == null) {
            cookies = new BasicCookieStore();
        }
        BasicClientCookie cookie = new BasicClientCookie(element.getName(), element.getValue());
        cookies.addCookie(cookie);
        cookie.setPath(element.getParameterByName("Path").getValue());
        cookie.setDomain(ip);
        this.context.setAttribute(HttpClientContext.COOKIE_STORE, cookies);
    }

    public MyHttpClient json(Map<String,String> map){
        return this;
    }

    public MyHttpClient formData(Map<String,String> map){
        MultipartEntityBuilder builder = MultipartEntityBuilder
                .create();
        map.forEach(builder::addTextBody);
        builder.setCharset(Charset.forName("utf-8"));
        this.entity = builder.build();
        return this;
    }

    public MyHttpClient urlEncode(String str){
        this.entity = EntityBuilder.create()
                .setContentType(ContentType.APPLICATION_FORM_URLENCODED)
                .setText(str)
                .build();

        return this;
    }


    public MyHttpClient execute(HttpClient httpClient) throws IOException {
        request.setEntity(entity);
        response= httpClient.execute(request, context);
        return this;
    }

    public MyHttpClient execute() throws Exception{
        CloseableHttpClient httpClient = HttpClients.custom().build();
        request.setEntity(entity);
        response = httpClient.execute(request, context);
        return this;
    }

    public static String encode(String s) throws UnsupportedEncodingException {
        return URLEncodeUtil.encode(s );
    }
    public String getStr() throws Exception{
        return EntityUtils.toString(response.getEntity());
    }

    public static void login() throws Exception{
        String userName = "liangzh2";
        String password = "lzh2015.";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        MyHttpClient client = new MyHttpClient().post("http://nh.haday.cn/HIP/login")
                .urlEncode(MessageFormat.format("jzsac_username={0}&jzsac_password={1}&loginSite=2&loginType=1",userName,password)).execute(httpClient);
        String str = client.getStr();
        System.out.println(str);


        str = client.post("http://nh.haday.cn/HIP/dbInfo/executeSql.do")
                .formData(MapBuilder.create(new HashMap<String, String>(2))
                        .put("sql", encode("select * from pp_ddbt where aufnr = '000063141535'"))
                        .put("data", encode("{\"message\":\"192.168.101.21:1433\",\"serviceIp\":\"192.168.101.21\",\"port\":\"1433\"}"))
                        .put("rootName", "qc")
                        .build())
                .execute(httpClient).getStr();
        httpClient.close();
        System.out.println(str);
    }

    public static void main(String[] args) throws Exception {
        HttpPost post = new HttpPost("http://192.168.7.21:8249/contractSys/contractSeal/generateSealFile");

        List<BasicNameValuePair> list = Arrays.asList(new BasicNameValuePair("unilateral_seal_file", "[{\"push_user_name\":\"佛山市海天（高明）调味食品有限公司\",\"original_file_path\":\"/factoryReportList/2022/09/26/zfoqh8b3y13ha1kivxga3pixtae4czh4.pdf\",\"receive_user_name\":\"佛山市海天（高明）调味食品有限公司\",\"seal_user_name\":\"佛山市海天（高明）调味食品有限公司\",\"create_user_name\":\"管理员\",\"file_name\":\"801006175_19000101.pdf\",\"seal_user_id\":\"1200\",\"seal_mode\":\"1\",\"create_user_account\":\"admin\",\"seal_user_type\":\"0\",\"invalid_date\":\"2099-12-31 23:59:59\",\"platform_code\":\"QMS\",\"receive_user_type\":\"0\",\"file_type\":\"ZJBG\",\"push_user_type\":\"0\",\"download_limit\":\"10\",\"anchor\":\"2022-09-25\",\"push_user_id\":\"1200\",\"receive_user_id\":\"1200\",\"platform_file_id\":\"1574566445639839746\"}]"));

        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list,"utf-8");
        System.out.println(EntityUtils.toString(urlEncodedFormEntity));

        post.setEntity(urlEncodedFormEntity);

        CloseableHttpResponse execute = HttpClients.createDefault()
                .execute(post);
        System.out.println(EntityUtils.toString(execute.getEntity()));

    }

}
