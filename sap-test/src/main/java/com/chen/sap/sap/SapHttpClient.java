package com.chen.sap.sap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * schedulejob
 *
 * @author yangzj2
 * @date 2019/9/7
 */

public class SapHttpClient {
    private static Properties properties=null;
    private static final int timeout = 60000;

    public static HashMap<String ,Object> doRequestForMap(String interfaceId, String functionName, HashMap<String ,String> selectParam, String[] resultParam)throws Exception{
        HashMap<String, Object> map = new HashMap<String ,Object>();
        SapReturnObject obj = SapHttpClient.doRequest(interfaceId, functionName,selectParam,null, null ,null ,null,resultParam);
        map = obj.getExports();
        return map;
    }

    public static List doRequestForList(String interfaceId,String functionName,String[] selectTable,HashMap<String ,String[]>[] selectParam,String resultTable,String[] resultParam)throws Exception{
        //需要对selectparam进行转换
        List<HashMap<String ,String>> list = new ArrayList<HashMap<String ,String>>();
        int w=1,h=1;
        if(selectParam.length>0){
            w=selectParam.length;
            HashMap one = selectParam[0];
            String[] c = (String[]) one.values().iterator().next();
            if(c.length>0){
                h=c.length;
            }
        }

        HashMap<String ,String>[][] inData = new HashMap[w][h];
        for(int i=0;i<w;i++){
            HashMap<String ,String[]> from = selectParam[i];
            for(int j=0;j<h;j++){
                inData[i][j] = new HashMap<String, String>();
                Iterator iterator = from.keySet().iterator();
                while(iterator.hasNext()){
                    String key = (String) iterator.next();
                    inData[i][j].put(key, from.get(key)[j]);
                }
            }
        }
        int k=1;
        if(resultParam!=null){
            k = resultParam.length;
        }
        String[][] resultSelectParam = new String[1][k];
        for(int i=0;i<k;i++){
            resultSelectParam[0][i]=resultParam[i];
        }
        String[] actable = new String[1];
        actable[0] = resultTable;
        SapReturnObject obj = SapHttpClient.doRequest(interfaceId, functionName,null,selectTable, inData ,actable ,resultSelectParam,null);
        list = obj.getTables().get(resultTable);
        return list;
    }


    /**
     *
     * @param interfaceId 要调用接口id(0:200，1:120)
     * @param functionName 要调用的SAP函数名称
     * @param param 参数import
     * @param selectTable  查询的表
     * @param selectParam  查询的表的字段
     * @param resultTable  返回接口的表
     * @param resultSelectParam  返回表的字段
     * @param resultParam  返回字段
     * @return SapReturnObject 返回值，包含export和tables
     * @throws Exception
     */
    public static SapReturnObject doRequest(String interfaceId,String functionName,HashMap<String, String> param,String[] selectTable,HashMap<String ,String>[][] selectParam,String[] resultTable,String[][] resultSelectParam,String[] resultParam)throws Exception {
        SapReturnObject srObj = null;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build();

        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .setMaxConnTotal(100)
                .setConnectionTimeToLive(1, TimeUnit.MINUTES).build();

        try {
            if(properties==null){
                properties=loadProperties("config-saphttp.properties");
            }
            HttpPost httpPost = new HttpPost(properties.getProperty("requestUrl"));

            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("interfaceId", interfaceId));
            nvps.add(new BasicNameValuePair("functionName", functionName));
            nvps.add(new BasicNameValuePair("param", param==null?"": JSONObject.toJSONString(param)));
            nvps.add(new BasicNameValuePair("selectTable", selectTable==null?"": JSONArray.toJSONString(selectTable)));
            nvps.add(new BasicNameValuePair("selectParam", selectParam==null?"":JSONArray.toJSONString(selectParam)));
            nvps.add(new BasicNameValuePair("resultTable", resultTable==null?"":JSONArray.toJSONString(resultTable)));
            nvps.add(new BasicNameValuePair("resultSelectParam", resultSelectParam==null?"":JSONArray.toJSONString(resultSelectParam)));
            nvps.add(new BasicNameValuePair("resultParam", resultParam==null?"":JSONArray.toJSONString(resultParam)));
            nvps.add(new BasicNameValuePair("from", "HIPSERVICE"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    String json = EntityUtils.toString(entity);
//            		使用json方法无法解析负责类型的HashMap<String,List<HashMap<String ,String>>>()字段
//            		Map<String, Class> classMap = new HashMap<String, Class>();
//            		classMap.put("tables",new HashMap<String,List<HashMap<String ,String>>>().getClass());
//            		classMap.put("exports", HashMap.class);
//            		srObj =  (SapReturnObject) JSONObject.toBean(JSONObject.fromObject(json),SapReturnObject.class,classMap);
                    System.out.printf("%s调用sap返回的json:%s%n", functionName, json);
//            		ObjectMapper m = new ObjectMapper();
                    srObj = JSON.parseObject(json, SapReturnObject.class);
                    EntityUtils.consume(entity);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        } finally {
            httpclient.close();
            return srObj;
        }
    }

    /**
     * 加载配置文件
     */
    public static Properties loadProperties(String fileName){
        InputStream stream=Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        Properties properties=new Properties();
        try {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
            properties=null;
        }finally{
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }


    public static void main(String[] args) throws Exception {

//    	String[] inTable = {"T_MATERIAL","T_ms"};
//    	System.out.println(JSONArray.fromObject(inTable).toString());
//
//    	String[] resultSelectParam1={"PLANT","STGE_LOC", "MATERIAL", "QTY", "QTY2", "QTY3", "QTY4", "QTY5"};
//
//		System.out.println(JSONArray.fromObject(resultSelectParam1).toString());
//
//
//		List<HashMap<String, String>> datas = new ArrayList<HashMap<String,String>>();
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("PLANT", "1200");
//		map.put("MATERIAL", "000000080100612800");
//		map.put("STGE_LOC", "2140");
//		map.put("REQ_DATE", "20160405");
//		datas.add(map);
//		HashMap<String, String> map2 = new HashMap<String, String>();
//		map2.put("PLANT", "1200");
//		map2.put("MATERIAL", "000000080100612800");
//		map2.put("STGE_LOC", "2140");
//		map2.put("REQ_DATE", "20160405");
//		datas.add(map2);
//		String[] inTable = {"T_MATERIAL"};
//		HashMap<String, String>[][] inData = new HashMap[inTable.length][datas.size()];
//		//给第0个表赋值
//		for(int i = 0 ; i<datas.size();i++){
//			inData[0][i] = new HashMap<String, String>();
//			inData[0][i].put("PLANT",datas.get(i).get("PLANT"));//ID
//			inData[0][i].put("MATERIAL",datas.get(i).get("MATERIAL"));//工厂
//			inData[0][i].put("STGE_LOC",datas.get(i).get("STGE_LOC"));//物料编码
//			inData[0][i].put("REQ_DATE",datas.get(i).get("REQ_DATE"));//物料描述
//
//		}
//		JSONArray jarr = JSONArray.fromObject(inData);
//		System.out.println("JSONARRAY1 = |" + jarr.toString());
//		for(int i=0;i<inTable.length;i++){
//			JSONArray row = jarr.getJSONArray(i);
//			for(int j=0;j<datas.size();j++){
//				JSONObject obj = row.getJSONObject(j);
//				System.out.println("cell = |" + obj.toString());
//			}
//
//
//		}

//
//		String[] resultSelectParam1={"PLANT","STGE_LOC", "MATERIAL", "QTY", "QTY2", "QTY3", "QTY4", "QTY5"};
//		String[][] outData = new String[1][resultSelectParam1.length];
//		outData[0]=resultSelectParam1;
//		//String dd = StringUtil.join(outData, ",");
//		JSONArray j2 = JSONArray.fromObject(outData);
//		System.out.println("String[][] = |" + j2);



//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        try {
//            HttpPost httpPost = new HttpPost("http://httpbin.org/post");
//            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
//            nvps.add(new BasicNameValuePair("username", "vip"));
//            nvps.add(new BasicNameValuePair("password", "secret"));
//            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
//            CloseableHttpResponse response2 = httpclient.execute(httpPost);
//
//            try {
//                System.out.println(response2.getStatusLine());
//                HttpEntity entity2 = response2.getEntity();
//                // do something useful with the response body
//                // and ensure it is fully consumed
//                EntityUtils.consume(entity2);
//            } finally {
//                response2.close();
//            }
//        } finally {
//            httpclient.close();
//        }


//    	String a1 = "[[{\"PLANT\":\"1200\",\"MATERIAL\":\"000000080100612800\",\"STGE_LOC\":\"2140\",\"REQ_DATE\":\"20160405\"}]]";
//    	String a2 = "[[\"PLANT\",\"STGE_LOC\",\"MATERIAL\",\"QTY\",\"QTY2\",\"QTY3\",\"QTY4\",\"QTY5\"]]";
//    	JSONArray jarr = JSONArray.fromObject(a1);
//
//    	JSONArray j2 = JSONArray.fromObject(a2);
//
//    	System.out.println("jarr.size() = " + jarr.size()+",j2.size()"+j2.size());





        List<HashMap<String, String>> datas = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("PLANT", "1200");
        map.put("MATERIAL", "000000080100612800");
        map.put("STGE_LOC", "2140");
        map.put("REQ_DATE", "20160405");
        datas.add(map);
        String interfaceId="2";
        List<HashMap<String, String>> resultList=null;
        try{
            String functionName = "ZSDID_ATP2SQL";
            //输入tables
            String[] inTable = {"T_MATERIAL"};
            @SuppressWarnings("unchecked")
            HashMap<String, String>[][] inData = new HashMap[inTable.length][datas.size()];
            //给第0个表赋值
            for(int i = 0 ; i<datas.size();i++){
                inData[0][i] = new HashMap<String, String>();
                inData[0][i].put("PLANT",datas.get(i).get("PLANT"));//ID
                inData[0][i].put("MATERIAL",datas.get(i).get("MATERIAL"));//工厂
                inData[0][i].put("STGE_LOC",datas.get(i).get("STGE_LOC"));//物料编码
                inData[0][i].put("REQ_DATE",datas.get(i).get("REQ_DATE"));//物料描述

            }

            //输出tables
            String[] outTable = {"T_ATP_MATERIAL"};
            //输出表字段
            String[] resultSelectParam1={"PLANT","STGE_LOC", "MATERIAL", "QTY", "QTY2", "QTY3", "QTY4", "QTY5"};
            String[][] outData = new String[1][resultSelectParam1.length];
            outData[0]=resultSelectParam1;


            SapReturnObject obj = SapHttpClient.doRequest(interfaceId, functionName,null,inTable, inData , outTable,outData,null);
            if(obj!=null && obj.getTables()!=null){
                resultList =obj.getTables().get(""+outTable[0]+"");//返回第一个table
                for(int i=0;i<resultList.size();i++){

                    HashMap<String, String> tmmap = resultList.get(i);
                    Double qty = new Double(tmmap.get("QTY"))==null?0:new Double(tmmap.get("QTY"));
                    Double qty5 = new Double(tmmap.get("QTY5"))==null?0:new Double(tmmap.get("QTY5"));
                    String plant = tmmap.get("PLANT").trim();
                    String stgelog = tmmap.get("STGE_LOC").trim();
                    String material = tmmap.get("MATERIAL").trim();
                    System.out.println("material="+material);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}