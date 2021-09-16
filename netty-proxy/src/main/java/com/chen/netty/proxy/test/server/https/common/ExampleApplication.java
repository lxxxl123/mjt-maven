package com.chen.netty.proxy.test.server.https.common;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ExampleApplication {

	public static X509Certificate[] getCertificates(InputStream inputStream)
			throws CertificateException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate x509Cert = (X509Certificate) cf.generateCertificate(inputStream);
		return new X509Certificate[] { x509Cert };
	}

	public static PrivateKey getKey(InputStream inputStream) throws Exception {
		String content = new String(readByte(inputStream));
		Matcher m = KEY_PATTERN.matcher(content);
		m.find();
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				Base64.getMimeDecoder().decode(m.group(1).getBytes()));
		return KeyFactory.getInstance("RSA").generatePrivate(pkcs8EncodedKeySpec);
	}

	public static byte[] readByte(InputStream inputStream) throws Exception {
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes);
		return bytes;
	}

	private static final Pattern KEY_PATTERN = Pattern
			.compile("-+BEGIN\\s+.*PRIVATE\\s+KEY[^-]*-+(?:\\s|\\r|\\n)+" + // Header
					"([a-z0-9+/=\\r\\n]+)" + // Base64 text
					"-+END\\s+.*PRIVATE\\s+KEY[^-]*-+", // Footer
					Pattern.CASE_INSENSITIVE);

	public static KeyManager[] getKeyManagers(String pemPath, String keyPath)
			throws Exception {
		// InputStream i1 =
		// AutomatedTelnetClient.class.getClassLoader().getResourceAsStream(pemPath);
		File file1 = new File(pemPath);
		File file2 = new File(keyPath);
		log.info("file path = {},exist = {}", pemPath, file1.exists());
		log.info("file path = {},exist = {}", keyPath, file2.exists());
		InputStream i1 = new FileInputStream(file1);
		X509Certificate[] keyCertChain = getCertificates(i1);
		InputStream i2 = new FileInputStream(file2);
		// InputStream i2 =
		// AutomatedTelnetClient.class.getClassLoader().getResourceAsStream(keyPath);
		PrivateKey key = getKey(i2);
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setKeyEntry("key", key, new char[] {}, keyCertChain);
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(ks, new char[] {});
		return keyManagerFactory.getKeyManagers();
	}

	public static TrustManager[] getTrustMangers() {
		TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
					throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		};
		return new TrustManager[] { trustManager };
	}

	public static SSLSocketFactory buildSslCtx(String cert , String key) throws Exception{
		// TLS 认证相关
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(getKeyManagers(cert, key), getTrustMangers(), null);
		SSLSocketFactory socketFactory = context.getSocketFactory();
		return socketFactory;
	}


}
