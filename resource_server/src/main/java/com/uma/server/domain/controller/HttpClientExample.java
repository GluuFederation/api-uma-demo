package com.uma.server.domain.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

public class HttpClientExample {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        HttpClientExample http = new HttpClientExample();
        http.sendGet();
    }

    // HTTP GET request
    private void sendGet() throws Exception {

        SSLContextBuilder builder = new SSLContextBuilder();

        System.getProperty("C:/Program Files/Java/jre7/lib/security/cacerts");
        System.out.println("Builer");

        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());

        // HttpGet httpGet = new HttpGet("https://ce.gluu.info/.well-known/uma-configuration");
        HttpGet httpGet = new HttpGet("http://ce.gluu.info/uma/scopes/view");
        System.out.println("sslsf");
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

        CloseableHttpResponse response = httpclient.execute(httpGet);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

        System.out.println("close http client");
        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
    }
}
