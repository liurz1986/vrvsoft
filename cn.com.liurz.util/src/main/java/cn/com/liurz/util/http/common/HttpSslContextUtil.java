package cn.com.liurz.util.http.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpSslContextUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpSslContextUtil.class);

    public HttpSslContextUtil() {
    }

    /**
     *  创建SSLContext对象，并使用我们指定的信任管理器初始化
     *  1.用于对安全套接字执行身份验证的X509证书的信任管理器
     *  2.使用TLS传输数据
     * @return
     */
    public SSLContext buildSslContext() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};
        SSLContext sslContext = null;

        try {
            // TLS握手协议主要实现三个目的：协商密码套件，协商秘钥和认证  TLS 1.3 、 TLS1.2 ，TLS 1.3不支持用RSA进行秘钥交换
            // TLS全称传输层安全（Transport Layer Security）
            sslContext = SSLContext.getInstance("TLS");

            sslContext.init((KeyManager[])null, trustAllCerts, (SecureRandom)null);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            this.logger.error("buildSslContext1", noSuchAlgorithmException);
        } catch (KeyManagementException kyManagementException) {
            this.logger.error("buildSslContext2", kyManagementException);
        }

        return sslContext;
    }
}
