package com.abiquo.android;

import java.security.KeyStore;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

public class CustomHTTPClient extends DefaultHttpClient {
 
    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", newSslSocketFactory(), 443));
        return new SingleClientConnManager(getParams(), registry);
    }
 
    private SSLSocketFactory newSslSocketFactory() {
        try {
			 try {
			      KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			      trustStore.load(null, null);
			      CustomSSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
			      sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			      return sf;
			    }			 
			 catch (Exception e) {  throw new AssertionError(e);   }
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}