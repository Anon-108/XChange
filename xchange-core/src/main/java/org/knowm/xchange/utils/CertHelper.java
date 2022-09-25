package org.knowm.xchange.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class CertHelper {

  /**
   * Creates a custom {@link SSLSocketFactory} that accepts an expired certificate.
   * * 创建一个接受过期证书的自定义 {@link SSLSocketFactory}。
   *
   * @param subjectPrincipalName RFC 2253 name on the expired certificate
   *                             过期证书上的 RFC 2253 名称
   *
   * @return An {@link SSLSocketFactory} that will accept the passed certificate if it is expired
   * * @return 一个 {@link SSLSocketFactory}，如果它过期了将接受通过的证书
   */
  public static SSLSocketFactory createExpiredAcceptingSSLSocketFactory(
      final String subjectPrincipalName) {

    try {
      final TrustManagerFactory trustManagerFactory =
          TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init((KeyStore) null);

      X509TrustManager trustManager =
          new X509TrustManager() {

            private X509TrustManager getDefaultTrustManager() {

              TrustManager trustManagers[] = trustManagerFactory.getTrustManagers();
              for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager)
                  return (X509TrustManager) trustManager;
              }

              throw new IllegalStateException();
            }

            private boolean certificateMatches(X509Certificate[] certs, boolean needsToBeExpired) {

              for (X509Certificate cert : certs)
                if (cert.getSubjectX500Principal().getName().equals(subjectPrincipalName)
                    && (!needsToBeExpired || cert.getNotAfter().before(new Date()))) return true;

              return false;
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {

              return getDefaultTrustManager().getAcceptedIssuers();
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {

              System.out.println("checking client trusted 检查客户信任: " + Arrays.toString(certs));

              getDefaultTrustManager().checkClientTrusted(certs, authType);
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {

              X509Certificate matchingCertificate;

              try {
                getDefaultTrustManager().checkServerTrusted(certs, authType);

                if (certificateMatches(certs, false))
                  throw new CertificateException(
                      "Update code to reject expired certificate, up-to-date certificate found 更新代码以拒绝过期的证书，找到最新的证书: "
                          + subjectPrincipalName);
              } catch (CertificateException e) {
                for (Throwable t = e; t != null; t = t.getCause())
                  if (t instanceof CertificateExpiredException && certificateMatches(certs, true))
                    return;
                throw e;
              }
            }
          };

      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, new TrustManager[] {trustManager}, null);
      return sslContext.getSocketFactory();

    } catch (GeneralSecurityException e) {

      throw new IllegalStateException(e);
    }
  }

  /**
   * Creates a custom {@link SSLSocketFactory} that disallows the use of a set of protocols and/or ciphers, no matter the current default configuration.
   * * 创建一个自定义 {@link SSLSocketFactory}，它不允许使用一组协议和/或密码，无论当前的默认配置如何。
   *
   * @param disabledProtocolsAndCiphers list of protocol or cipher names to disallow
   *                                    要禁止的协议或密码名称列表
   *
   * @return An {@link SSLSocketFactory} that will never use the passed protocols or ciphers
   * * @return 一个永远不会使用传递的协议或密码的 {@link SSLSocketFactory}
   */
  public static SSLSocketFactory createRestrictedSSLSocketFactory(
      String... disabledProtocolsAndCiphers) {

    final Set<String> disabled =
        new CopyOnWriteArraySet<>(Arrays.asList(disabledProtocolsAndCiphers));

    return new SSLSocketFactory() {

      private String[] filter(String[] original, String[] supported) throws IOException {

        Set<String> filtered = new CopyOnWriteArraySet<>(Arrays.asList(original));
        filtered.removeAll(disabled);

        if (filtered.isEmpty()) {
          filtered.addAll(Arrays.asList(supported));
          filtered.removeAll(disabled);
        }

        if (filtered.isEmpty())
          throw new IOException(
              "No supported SSL attributed enabled. 未启用受支持的 SSL 属性。 "
                  + Arrays.toString(original)
                  + " provided, "
                  + disabled.toString()
                  + " disabled, "
                  + Arrays.toString(supported)
                  + " supported, result 支持，结果: "
                  + filtered.toString());

        return filtered.toArray(new String[filtered.size()]);
      }

      private SSLSocket fixupSocket(Socket socket) throws IOException {

        SSLSocket sslSocket = (SSLSocket) socket;

        sslSocket.setEnabledProtocols(
            filter(sslSocket.getEnabledProtocols(), sslSocket.getSupportedProtocols()));
        sslSocket.setEnabledCipherSuites(
            filter(sslSocket.getEnabledCipherSuites(), sslSocket.getSupportedCipherSuites()));

        return sslSocket;
      }

      private SSLSocketFactory getDefaultFactory() {

        return (SSLSocketFactory) SSLSocketFactory.getDefault();
      }

      @Override
      public String[] getDefaultCipherSuites() {

        return getDefaultFactory().getDefaultCipherSuites();
      }

      @Override
      public String[] getSupportedCipherSuites() {
        return getDefaultFactory().getSupportedCipherSuites();
      }

      @Override
      public Socket createSocket(Socket s, String host, int port, boolean autoClose)
          throws IOException {
        return fixupSocket(getDefaultFactory().createSocket(s, host, port, autoClose));
      }

      @Override
      public Socket createSocket(String host, int port) throws IOException {
        return fixupSocket(getDefaultFactory().createSocket(host, port));
      }

      @Override
      public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
          throws IOException {
        return fixupSocket(getDefaultFactory().createSocket(host, port, localHost, localPort));
      }

      @Override
      public Socket createSocket(InetAddress host, int port) throws IOException {
        return fixupSocket(getDefaultFactory().createSocket(host, port));
      }

      @Override
      public Socket createSocket(
          InetAddress address, int port, InetAddress localAddress, int localPort)
          throws IOException {
        return fixupSocket(
            getDefaultFactory().createSocket(address, port, localAddress, localPort));
      }
    };
  }

  /**
   * Creates a custom {@link HostnameVerifier} that allows a specific certificate to be accepted for a mismatching hostname.
   * * 创建一个自定义 {@link HostnameVerifier}，允许接受不匹配主机名的特定证书。
   *
   * @param requestHostname hostname used to access the service which offers the incorrectly named   certificate
   *                        * @param requestHostname 用于访问提供错误命名证书的服务的主机名
   *
   * @param certPrincipalName RFC 2253 name on the certificate
   *                          * @param certPrincipalName 证书上的 RFC 2253 名称
   *
   * @return A {@link HostnameVerifier} that will accept the provided combination of names
   * * @return 将接受提供的名称组合的 {@link HostnameVerifier}
   */
  public static HostnameVerifier createIncorrectHostnameVerifier(
      final String requestHostname, final String certPrincipalName) {

    return new HostnameVerifier() {

      @Override
      public boolean verify(String hostname, SSLSession session) {

        try {

          String principalName = session.getPeerPrincipal().getName();

          if (hostname.equals(requestHostname) && principalName.equals(certPrincipalName))
            return true;

        } catch (SSLPeerUnverifiedException e) {
        }

        return HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
      }
    };
  }

  /**
   * Manually override the JVM's TrustManager to accept all HTTPS connections. Use this ONLY for
    testing, and even at that use it cautiously. Someone could steal your API keys with a MITM attack!
   手动覆盖 JVM 的 TrustManager 以接受所有 HTTPS 连接。 仅用于
   测试，甚至在那个时候谨慎使用它。 有人可以通过 MITM 攻击窃取您的 API 密钥！
   *
   * @deprecated create an exclusion specific to your need rather than changing all behavior
   * * @deprecated 创建一个特定于您需要的排除项，而不是更改所有行为
   */
  @Deprecated
  public static void trustAllCerts() throws Exception {

    TrustManager[] trustAllCerts =
        new TrustManager[] {
          new X509TrustManager() {

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {

              return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
          }
        };

    // Install the all-trusting trust manager
    // 安装全信任信任管理器
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    // Create all-trusting host name verifier
    // 创建全信任主机名验证器
    HostnameVerifier allHostsValid =
        new HostnameVerifier() {

          @Override
          public boolean verify(String hostname, SSLSession session) {

            return true;
          }
        };

    // Install the all-trusting host verifier
    // 安装全信任主机验证器
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
  }
}
