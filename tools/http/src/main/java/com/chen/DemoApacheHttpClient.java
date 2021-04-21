package com.chen;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;

/**
 * http代理程序
 **/
public class DemoApacheHttpClient {

    //是否使用https
	static final boolean SECURE = true;

	//下层设备 ip
	private static String neIp = "192.168.97.251";

	private static Integer nePort = 8210;

	//dispatcher ip
	private static String dispatcherIp = "127.0.0.1";

	//对应dispatcher配置文件中的 dcpp.dispatcher.httpServerPort , 缺省9943
	private static Integer dispatcherPort = 9943;

	//dispatcher 登录用户名
	private static String userName = "admin";

	//dispatcher 登录密码
	private static String password = "123456";


	public static void main(String[] args) throws Exception {

		HttpHost host = new HttpHost(neIp, nePort, "http");
		HttpHost proxy = new HttpHost(dispatcherIp, dispatcherPort);

		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

		credentialsProvider.setCredentials(new AuthScope(dispatcherIp, dispatcherPort),
				//dispatcher用户名密码
				new UsernamePasswordCredentials(userName, password));

		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCredentialsProvider(credentialsProvider)
				.disableAutomaticRetries().setProxy(proxy).build();

		if (SECURE) {
			host = new HttpHost(neIp, nePort, "https");

			TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
			SSLContext sslContext = SSLContexts.custom()
					.loadTrustMaterial(null, acceptingTrustStrategy).build();

			httpclient = HttpClients.custom()
					.setDefaultCredentialsProvider(credentialsProvider)
					.disableAutomaticRetries().setProxy(proxy)
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
					.setSSLContext(sslContext).build();

		}

		//下层设备url
		HttpGet httpget = new HttpGet("/hello");

		CloseableHttpResponse response = httpclient.execute(host, httpget);
		httpclient.close();
		System.out.println(response);

	}

}
