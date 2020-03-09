package com.cex0.mobiai.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.lang.NonNull;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/9 22:27
 * @Description:
 */
public class HttpClientUtils {

    /**
     * 超时默认五秒
     */
    private final static int TIMEOUT = 5000;

    private HttpClientUtils() {}


    /**
     * 创建https客户端。
     *
     * @param timeout 连接超时时间（ms）
     * @return https客户端
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    @NonNull
    public static CloseableHttpClient createHttpsClient(int timeout) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (certificate, authType) -> true)
                .build();

        return HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setDefaultRequestConfig(getRequestConfig(timeout))
                .build();
    }

    /**
     * 获取请求配置。
     *
     * @param timeout connection timeout (ms)
     * @return request config
     */
    private static RequestConfig getRequestConfig(int timeout) {
        return RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
    }


    /**
     * 多部分文件资源。
     *
     * @author wodenvyoujiaoshaxiong
     */
    public static class MultipartFileResource extends ByteArrayResource  {

        private final String filename;

        public MultipartFileResource(byte[] byteArray, String filename) {
            super(byteArray);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }
    }
}
