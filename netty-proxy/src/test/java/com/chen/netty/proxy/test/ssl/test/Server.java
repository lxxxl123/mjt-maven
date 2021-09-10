package com.chen.netty.proxy.test.ssl.test;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.concurrent.CompletableFuture;

/**
 * @author chenwh
 * @date 2021/9/10
 */

public class Server {
    public static String SERVER_KEY_STORE_PASSWORD = "123456";
    public static String SERVER_TRUST_KEY_STORE_PASSWORD = "123456";
    public static Integer DEFAULT_PORT = 0;


    public static SSLServerSocket startServer() throws Exception{
        SSLContext ctx = SSLContext.getInstance("SSL");

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore tks = KeyStore.getInstance("JKS");

        ks.load(new FileInputStream("./kserver.keystore"), SERVER_KEY_STORE_PASSWORD.toCharArray());
        tks.load(new FileInputStream("./tserver.keystore"), SERVER_TRUST_KEY_STORE_PASSWORD.toCharArray());

        kmf.init(ks, SERVER_KEY_STORE_PASSWORD.toCharArray());
        tmf.init(tks);

        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return (SSLServerSocket) ctx.getServerSocketFactory().createServerSocket(DEFAULT_PORT);
    }

    public static String CLIENT_KEY_STORE_PASSWORD = "123456";
    public static String CLIENT_TRUST_KEY_STORE_PASSWORD = "123456";
    public static String  DEFAULT_HOST = "localhost";


    public static SSLSocket startClient() throws Exception {
        SSLContext ctx = SSLContext.getInstance("SSL");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore tks = KeyStore.getInstance("JKS");

        ks.load(new FileInputStream("./kclient.keystore"), CLIENT_KEY_STORE_PASSWORD.toCharArray());
        tks.load(new FileInputStream("./tclient.keystore"), CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());

        kmf.init(ks, CLIENT_KEY_STORE_PASSWORD.toCharArray());
        tmf.init(tks);

        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return (SSLSocket) ctx.getSocketFactory().createSocket(DEFAULT_HOST, DEFAULT_PORT);

    }

    public static void main(String[] args) throws Exception {
        CompletableFuture.runAsync(() -> {
            try {
                startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        startClient();
    }
}

