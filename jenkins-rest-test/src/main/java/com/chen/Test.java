package com.chen;

import cn.hutool.http.HtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class Test {


    public static String formatHtml(String s) {
        return HtmlUtil.cleanHtmlTag(s.replaceAll("\n]", "]"));
    }
    private static String cutString(String value,int num) {
        StringBuffer sb = new StringBuffer();
        int curLen = 0;
        int totalLen = value.length();
        try {
            for (int i = 0; i < totalLen && curLen <= num; i++) {
                String temp = value.substring(i, i + 1);
                curLen += temp.getBytes("GBK").length;
            }
        } catch (Exception e) {

        }
        return sb.toString();
    }


    public static Map<String,String> test(){

        HashMap<String, String> map = new HashMap<>();
        try {
            map.put("1", "1");
            return map;
        } finally {
            map.put("2", "2");
        }
    }

    public static void div(double v1,double v2){
        System.out.println(v1 / v2);
    }

    public static void post() throws Exception {
        HttpPost httpPost = new HttpPost("http://192.168.7.21:8249/contractSys/contractSeal/generateSealFile");

        BasicNameValuePair name = new BasicNameValuePair("unilateral_seal_file", "" +
                "[{\"push_user_name\":\"佛山市海天（高明）调味食品有限公司\",\"original_file_path\":\"/factoryReportList/2022/09/05/wpcv2r6numrjv37zmqt3wg7l7w2u9rtu.pdf\",\"receive_user_name\":\"佛山市海天（高明）调味食品有限公司\",\"seal_user_name\":\"佛山市海天（高明）调味食品有限公司\",\"create_user_name\":\"管理员\",\"file_name\":\"801006175_19000101.pdf\",\"seal_user_id\":\"1200\",\"seal_mode\":\"1\",\"create_user_account\":\"admin\",\"seal_user_type\":\"0\",\"invalid_date\":\"2099-12-31 23:59:59\",\"platform_code\":\"QMS\",\"receive_user_type\":\"0\",\"file_type\":\"ZJBG\",\"push_user_type\":\"0\",\"download_limit\":\"10\",\"anchor\":\"1900-01-03\",\"push_user_id\":\"1200\",\"receive_user_id\":\"1200\",\"platform_file_id\":\"1566596841286193154\"}]");
        UrlEncodedFormEntity body = new UrlEncodedFormEntity(Arrays.asList(name));
        httpPost.setEntity(body);


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpEntity entity = httpClient.execute(httpPost).getEntity();
        System.out.println(EntityUtils.toString(entity));
    }



    public static void batchSealFiles()  throws Exception {
        HttpPost httpPost = new HttpPost("http://192.168.7.21:8249/contractSys/contractSeal/batchSealFiles");

        HttpEntity build = MultipartEntityBuilder.create()
                .addTextBody("batchSealFileParam"
                        , "{\"sealParam\":[{\"fileId\":\"1053\"}]}")
                .setCharset(StandardCharsets.UTF_8)
                .build();
        httpPost.setEntity(build);

//        EntityBuilder entityBuilder = EntityBuilder.create().setContentType(ContentType.MULTIPART_FORM_DATA);
//        BasicNameValuePair name = new BasicNameValuePair(
//                "unilateral_seal_file"
//                , "[{\"push_user_name\":\"佛山市海天（高明）调味食品有限公司\",\"original_file_path\":\"/factoryReportList/2022/09/05/wpcv2r6numrjv37zmqt3wg7l7w2u9rtu.pdf\",\"receive_user_name\":\"佛山市海天（高明）调味食品有限公司\",\"seal_user_name\":\"佛山市海天（高明）调味食品有限公司\",\"create_user_name\":\"管理员\",\"file_name\":\"801006175_19000101.pdf\",\"seal_user_id\":\"1200\",\"seal_mode\":\"1\",\"create_user_account\":\"admin\",\"seal_user_type\":\"0\",\"invalid_date\":\"2099-12-31 23:59:59\",\"platform_code\":\"QMS\",\"receive_user_type\":\"0\",\"file_type\":\"ZJBG\",\"push_user_type\":\"0\",\"download_limit\":\"10\",\"anchor\":\"1900-01-03\",\"push_user_id\":\"1200\",\"receive_user_id\":\"1200\",\"platform_file_id\":\"1566596841286193154\"}]");
//
//        entityBuilder.setParameters(name);
//        httpPost.setEntity(entityBuilder.build());

//        BasicNameValuePair name = new BasicNameValuePair("unilateral_seal_file", "[{\"push_user_name\":\"佛山市海天（高明）调味食品有限公司\",\"original_file_path\":\"/factoryReportList/2022/09/05/wpcv2r6numrjv37zmqt3wg7l7w2u9rtu.pdf\",\"receive_user_name\":\"佛山市海天（高明）调味食品有限公司\",\"seal_user_name\":\"佛山市海天（高明）调味食品有限公司\",\"create_user_name\":\"管理员\",\"file_name\":\"801006175_19000101.pdf\",\"seal_user_id\":\"1200\",\"seal_mode\":\"1\",\"create_user_account\":\"admin\",\"seal_user_type\":\"0\",\"invalid_date\":\"2099-12-31 23:59:59\",\"platform_code\":\"QMS\",\"receive_user_type\":\"0\",\"file_type\":\"ZJBG\",\"push_user_type\":\"0\",\"download_limit\":\"10\",\"anchor\":\"1900-01-03\",\"push_user_id\":\"1200\",\"receive_user_id\":\"1200\",\"platform_file_id\":\"1566596841286193154\"}]");
//        UrlEncodedFormEntity body = new UrlEncodedFormEntity(Arrays.asList(name));
//        httpPost.setEntity(body);


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpEntity entity = httpClient.execute(httpPost).getEntity();
        System.out.println(EntityUtils.toString(entity));
    }

    public static void main(String[] args) throws Exception {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
//        Deque<Integer> deque = new LinkedList<>();
        long from = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            deque.push(1);
        }
        System.out.println(deque.size());
        System.out.println("time:" + (System.currentTimeMillis() - from));

    }


}
