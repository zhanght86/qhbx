package com.rh.ldap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * java ldaps 访问 ad 时，信任所有的证书
 * 
 * @author chujie
 * 
 */
public class ADFactory extends SSLSocketFactory {
	private SSLContext sslContext = SSLContext.getInstance("TLS");

	/**
	 * 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 */
	public ADFactory() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException,
			UnrecoverableKeyException {
			TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		sslContext.init(null, new TrustManager[] { tm }, null);
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
		return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException {
		return sslContext.getSocketFactory().createSocket(host, port);
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
		return sslContext.getSocketFactory().createSocket(host, port, localHost, localPort);
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		return sslContext.getSocketFactory().createSocket(host, port);
	}

	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
			throws IOException {
		return sslContext.getSocketFactory().createSocket(address, port, localAddress, localPort);
	}

	@Override
	public Socket createSocket() throws IOException {
		return sslContext.getSocketFactory().createSocket();
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return new String[0];
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return new String[0];
	}
}