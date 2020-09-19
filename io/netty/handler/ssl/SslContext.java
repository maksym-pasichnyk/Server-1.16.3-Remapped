/*      */ package io.netty.handler.ssl;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.ByteBufInputStream;
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.security.InvalidAlgorithmParameterException;
/*      */ import java.security.InvalidKeyException;
/*      */ import java.security.KeyException;
/*      */ import java.security.KeyFactory;
/*      */ import java.security.KeyStore;
/*      */ import java.security.KeyStoreException;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.PrivateKey;
/*      */ import java.security.Provider;
/*      */ import java.security.UnrecoverableKeyException;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.CertificateException;
/*      */ import java.security.cert.CertificateFactory;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.security.spec.InvalidKeySpecException;
/*      */ import java.security.spec.PKCS8EncodedKeySpec;
/*      */ import java.util.List;
/*      */ import javax.crypto.Cipher;
/*      */ import javax.crypto.EncryptedPrivateKeyInfo;
/*      */ import javax.crypto.NoSuchPaddingException;
/*      */ import javax.crypto.SecretKey;
/*      */ import javax.crypto.SecretKeyFactory;
/*      */ import javax.crypto.spec.PBEKeySpec;
/*      */ import javax.net.ssl.KeyManagerFactory;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLException;
/*      */ import javax.net.ssl.SSLSessionContext;
/*      */ import javax.net.ssl.TrustManagerFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class SslContext
/*      */ {
/*      */   static final CertificateFactory X509_CERT_FACTORY;
/*      */   private final boolean startTls;
/*      */   
/*      */   static {
/*      */     try {
/*   91 */       X509_CERT_FACTORY = CertificateFactory.getInstance("X.509");
/*   92 */     } catch (CertificateException e) {
/*   93 */       throw new IllegalStateException("unable to instance X.509 CertificateFactory", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static SslProvider defaultServerProvider() {
/*  105 */     return defaultProvider();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static SslProvider defaultClientProvider() {
/*  114 */     return defaultProvider();
/*      */   }
/*      */   
/*      */   private static SslProvider defaultProvider() {
/*  118 */     if (OpenSsl.isAvailable()) {
/*  119 */       return SslProvider.OPENSSL;
/*      */     }
/*  121 */     return SslProvider.JDK;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newServerContext(File certChainFile, File keyFile) throws SSLException {
/*  135 */     return newServerContext(certChainFile, keyFile, (String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newServerContext(File certChainFile, File keyFile, String keyPassword) throws SSLException {
/*  151 */     return newServerContext(null, certChainFile, keyFile, keyPassword);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newServerContext(File certChainFile, File keyFile, String keyPassword, Iterable<String> ciphers, Iterable<String> nextProtocols, long sessionCacheSize, long sessionTimeout) throws SSLException {
/*  178 */     return newServerContext((SslProvider)null, certChainFile, keyFile, keyPassword, ciphers, nextProtocols, sessionCacheSize, sessionTimeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newServerContext(File certChainFile, File keyFile, String keyPassword, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
/*  206 */     return newServerContext((SslProvider)null, certChainFile, keyFile, keyPassword, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newServerContext(SslProvider provider, File certChainFile, File keyFile) throws SSLException {
/*  224 */     return newServerContext(provider, certChainFile, keyFile, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newServerContext(SslProvider provider, File certChainFile, File keyFile, String keyPassword) throws SSLException {
/*  242 */     return newServerContext(provider, certChainFile, keyFile, keyPassword, (Iterable<String>)null, IdentityCipherSuiteFilter.INSTANCE, (ApplicationProtocolConfig)null, 0L, 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newServerContext(SslProvider provider, File certChainFile, File keyFile, String keyPassword, Iterable<String> ciphers, Iterable<String> nextProtocols, long sessionCacheSize, long sessionTimeout) throws SSLException {
/*  272 */     return newServerContext(provider, certChainFile, keyFile, keyPassword, ciphers, IdentityCipherSuiteFilter.INSTANCE, 
/*      */         
/*  274 */         toApplicationProtocolConfig(nextProtocols), sessionCacheSize, sessionTimeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newServerContext(SslProvider provider, File certChainFile, File keyFile, String keyPassword, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, Iterable<String> nextProtocols, long sessionCacheSize, long sessionTimeout) throws SSLException {
/*  307 */     return newServerContext(provider, null, trustManagerFactory, certChainFile, keyFile, keyPassword, null, ciphers, IdentityCipherSuiteFilter.INSTANCE, 
/*      */ 
/*      */         
/*  310 */         toApplicationProtocolConfig(nextProtocols), sessionCacheSize, sessionTimeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newServerContext(SslProvider provider, File certChainFile, File keyFile, String keyPassword, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
/*  339 */     return newServerContext(provider, null, null, certChainFile, keyFile, keyPassword, null, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newServerContext(SslProvider provider, File trustCertCollectionFile, TrustManagerFactory trustManagerFactory, File keyCertChainFile, File keyFile, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
/*      */     try {
/*  384 */       return newServerContextInternal(provider, null, toX509Certificates(trustCertCollectionFile), trustManagerFactory, 
/*  385 */           toX509Certificates(keyCertChainFile), 
/*  386 */           toPrivateKey(keyFile, keyPassword), keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, ClientAuth.NONE, null, false, false);
/*      */     
/*      */     }
/*  389 */     catch (Exception e) {
/*  390 */       if (e instanceof SSLException) {
/*  391 */         throw (SSLException)e;
/*      */       }
/*  393 */       throw new SSLException("failed to initialize the server-side SSL context", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static SslContext newServerContextInternal(SslProvider provider, Provider sslContextProvider, X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp) throws SSLException {
/*  406 */     if (provider == null) {
/*  407 */       provider = defaultServerProvider();
/*      */     }
/*      */     
/*  410 */     switch (provider) {
/*      */       case JDK:
/*  412 */         if (enableOcsp) {
/*  413 */           throw new IllegalArgumentException("OCSP is not supported with this SslProvider: " + provider);
/*      */         }
/*  415 */         return new JdkSslServerContext(sslContextProvider, trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, clientAuth, protocols, startTls);
/*      */ 
/*      */ 
/*      */       
/*      */       case OPENSSL:
/*  420 */         verifyNullSslContextProvider(provider, sslContextProvider);
/*  421 */         return new OpenSslServerContext(trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, clientAuth, protocols, startTls, enableOcsp);
/*      */ 
/*      */ 
/*      */       
/*      */       case OPENSSL_REFCNT:
/*  426 */         verifyNullSslContextProvider(provider, sslContextProvider);
/*  427 */         return new ReferenceCountedOpenSslServerContext(trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, clientAuth, protocols, startTls, enableOcsp);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  432 */     throw new Error(provider.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   private static void verifyNullSslContextProvider(SslProvider provider, Provider sslContextProvider) {
/*  437 */     if (sslContextProvider != null) {
/*  438 */       throw new IllegalArgumentException("Java Security Provider unsupported for SslProvider: " + provider);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newClientContext() throws SSLException {
/*  450 */     return newClientContext(null, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newClientContext(File certChainFile) throws SSLException {
/*  463 */     return newClientContext((SslProvider)null, certChainFile);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newClientContext(TrustManagerFactory trustManagerFactory) throws SSLException {
/*  478 */     return newClientContext(null, null, trustManagerFactory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newClientContext(File certChainFile, TrustManagerFactory trustManagerFactory) throws SSLException {
/*  496 */     return newClientContext(null, certChainFile, trustManagerFactory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newClientContext(File certChainFile, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, Iterable<String> nextProtocols, long sessionCacheSize, long sessionTimeout) throws SSLException {
/*  524 */     return newClientContext((SslProvider)null, certChainFile, trustManagerFactory, ciphers, nextProtocols, sessionCacheSize, sessionTimeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newClientContext(File certChainFile, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
/*  554 */     return newClientContext(null, certChainFile, trustManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newClientContext(SslProvider provider) throws SSLException {
/*  570 */     return newClientContext(provider, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newClientContext(SslProvider provider, File certChainFile) throws SSLException {
/*  586 */     return newClientContext(provider, certChainFile, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newClientContext(SslProvider provider, TrustManagerFactory trustManagerFactory) throws SSLException {
/*  604 */     return newClientContext(provider, null, trustManagerFactory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newClientContext(SslProvider provider, File certChainFile, TrustManagerFactory trustManagerFactory) throws SSLException {
/*  624 */     return newClientContext(provider, certChainFile, trustManagerFactory, null, IdentityCipherSuiteFilter.INSTANCE, null, 0L, 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newClientContext(SslProvider provider, File certChainFile, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, Iterable<String> nextProtocols, long sessionCacheSize, long sessionTimeout) throws SSLException {
/*  656 */     return newClientContext(provider, certChainFile, trustManagerFactory, null, null, null, null, ciphers, IdentityCipherSuiteFilter.INSTANCE, 
/*      */ 
/*      */         
/*  659 */         toApplicationProtocolConfig(nextProtocols), sessionCacheSize, sessionTimeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newClientContext(SslProvider provider, File certChainFile, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
/*  691 */     return newClientContext(provider, certChainFile, trustManagerFactory, null, null, null, null, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static SslContext newClientContext(SslProvider provider, File trustCertCollectionFile, TrustManagerFactory trustManagerFactory, File keyCertChainFile, File keyFile, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
/*      */     try {
/*  742 */       return newClientContextInternal(provider, null, 
/*  743 */           toX509Certificates(trustCertCollectionFile), trustManagerFactory, 
/*  744 */           toX509Certificates(keyCertChainFile), toPrivateKey(keyFile, keyPassword), keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, null, sessionCacheSize, sessionTimeout, false);
/*      */     
/*      */     }
/*  747 */     catch (Exception e) {
/*  748 */       if (e instanceof SSLException) {
/*  749 */         throw (SSLException)e;
/*      */       }
/*  751 */       throw new SSLException("failed to initialize the client-side SSL context", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static SslContext newClientContextInternal(SslProvider provider, Provider sslContextProvider, X509Certificate[] trustCert, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, String[] protocols, long sessionCacheSize, long sessionTimeout, boolean enableOcsp) throws SSLException {
/*  762 */     if (provider == null) {
/*  763 */       provider = defaultClientProvider();
/*      */     }
/*  765 */     switch (provider) {
/*      */       case JDK:
/*  767 */         if (enableOcsp) {
/*  768 */           throw new IllegalArgumentException("OCSP is not supported with this SslProvider: " + provider);
/*      */         }
/*  770 */         return new JdkSslClientContext(sslContextProvider, trustCert, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, protocols, sessionCacheSize, sessionTimeout);
/*      */ 
/*      */       
/*      */       case OPENSSL:
/*  774 */         verifyNullSslContextProvider(provider, sslContextProvider);
/*  775 */         return new OpenSslClientContext(trustCert, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, protocols, sessionCacheSize, sessionTimeout, enableOcsp);
/*      */ 
/*      */ 
/*      */       
/*      */       case OPENSSL_REFCNT:
/*  780 */         verifyNullSslContextProvider(provider, sslContextProvider);
/*  781 */         return new ReferenceCountedOpenSslClientContext(trustCert, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, protocols, sessionCacheSize, sessionTimeout, enableOcsp);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  786 */     throw new Error(provider.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   static ApplicationProtocolConfig toApplicationProtocolConfig(Iterable<String> nextProtocols) {
/*      */     ApplicationProtocolConfig apn;
/*  792 */     if (nextProtocols == null) {
/*  793 */       apn = ApplicationProtocolConfig.DISABLED;
/*      */     } else {
/*  795 */       apn = new ApplicationProtocolConfig(ApplicationProtocolConfig.Protocol.NPN_AND_ALPN, ApplicationProtocolConfig.SelectorFailureBehavior.CHOOSE_MY_LAST_PROTOCOL, ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT, nextProtocols);
/*      */     } 
/*      */ 
/*      */     
/*  799 */     return apn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SslContext() {
/*  806 */     this(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SslContext(boolean startTls) {
/*  813 */     this.startTls = startTls;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isServer() {
/*  820 */     return !isClient();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final List<String> nextProtocols() {
/*  848 */     return applicationProtocolNegotiator().protocols();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final SslHandler newHandler(ByteBufAllocator alloc) {
/*  905 */     return newHandler(alloc, this.startTls);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SslHandler newHandler(ByteBufAllocator alloc, boolean startTls) {
/*  913 */     return new SslHandler(newEngine(alloc), startTls);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final SslHandler newHandler(ByteBufAllocator alloc, String peerHost, int peerPort) {
/*  943 */     return newHandler(alloc, peerHost, peerPort, this.startTls);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SslHandler newHandler(ByteBufAllocator alloc, String peerHost, int peerPort, boolean startTls) {
/*  951 */     return new SslHandler(newEngine(alloc, peerHost, peerPort), startTls);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static PKCS8EncodedKeySpec generateKeySpec(char[] password, byte[] key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
/*  974 */     if (password == null) {
/*  975 */       return new PKCS8EncodedKeySpec(key);
/*      */     }
/*      */     
/*  978 */     EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(key);
/*  979 */     SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryptedPrivateKeyInfo.getAlgName());
/*  980 */     PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
/*  981 */     SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
/*      */     
/*  983 */     Cipher cipher = Cipher.getInstance(encryptedPrivateKeyInfo.getAlgName());
/*  984 */     cipher.init(2, pbeKey, encryptedPrivateKeyInfo.getAlgParameters());
/*      */     
/*  986 */     return encryptedPrivateKeyInfo.getKeySpec(cipher);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static KeyStore buildKeyStore(X509Certificate[] certChain, PrivateKey key, char[] keyPasswordChars) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
/* 1001 */     KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
/* 1002 */     ks.load(null, null);
/* 1003 */     ks.setKeyEntry("key", key, keyPasswordChars, (Certificate[])certChain);
/* 1004 */     return ks;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static PrivateKey toPrivateKey(File keyFile, String keyPassword) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, KeyException, IOException {
/* 1011 */     if (keyFile == null) {
/* 1012 */       return null;
/*      */     }
/* 1014 */     return getPrivateKeyFromByteBuffer(PemReader.readPrivateKey(keyFile), keyPassword);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static PrivateKey toPrivateKey(InputStream keyInputStream, String keyPassword) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, KeyException, IOException {
/* 1021 */     if (keyInputStream == null) {
/* 1022 */       return null;
/*      */     }
/* 1024 */     return getPrivateKeyFromByteBuffer(PemReader.readPrivateKey(keyInputStream), keyPassword);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static PrivateKey getPrivateKeyFromByteBuffer(ByteBuf encodedKeyBuf, String keyPassword) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, KeyException, IOException {
/* 1031 */     byte[] encodedKey = new byte[encodedKeyBuf.readableBytes()];
/* 1032 */     encodedKeyBuf.readBytes(encodedKey).release();
/*      */     
/* 1034 */     PKCS8EncodedKeySpec encodedKeySpec = generateKeySpec((keyPassword == null) ? null : keyPassword
/* 1035 */         .toCharArray(), encodedKey);
/*      */     try {
/* 1037 */       return KeyFactory.getInstance("RSA").generatePrivate(encodedKeySpec);
/* 1038 */     } catch (InvalidKeySpecException ignore) {
/*      */       try {
/* 1040 */         return KeyFactory.getInstance("DSA").generatePrivate(encodedKeySpec);
/* 1041 */       } catch (InvalidKeySpecException ignore2) {
/*      */         try {
/* 1043 */           return KeyFactory.getInstance("EC").generatePrivate(encodedKeySpec);
/* 1044 */         } catch (InvalidKeySpecException e) {
/* 1045 */           throw new InvalidKeySpecException("Neither RSA, DSA nor EC worked", e);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected static TrustManagerFactory buildTrustManagerFactory(File certChainFile, TrustManagerFactory trustManagerFactory) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
/* 1061 */     X509Certificate[] x509Certs = toX509Certificates(certChainFile);
/*      */     
/* 1063 */     return buildTrustManagerFactory(x509Certs, trustManagerFactory);
/*      */   }
/*      */   
/*      */   static X509Certificate[] toX509Certificates(File file) throws CertificateException {
/* 1067 */     if (file == null) {
/* 1068 */       return null;
/*      */     }
/* 1070 */     return getCertificatesFromBuffers(PemReader.readCertificates(file));
/*      */   }
/*      */   
/*      */   static X509Certificate[] toX509Certificates(InputStream in) throws CertificateException {
/* 1074 */     if (in == null) {
/* 1075 */       return null;
/*      */     }
/* 1077 */     return getCertificatesFromBuffers(PemReader.readCertificates(in));
/*      */   }
/*      */   
/*      */   private static X509Certificate[] getCertificatesFromBuffers(ByteBuf[] certs) throws CertificateException {
/* 1081 */     CertificateFactory cf = CertificateFactory.getInstance("X.509");
/* 1082 */     X509Certificate[] x509Certs = new X509Certificate[certs.length];
/*      */     
/* 1084 */     int i = 0;
/*      */     try {
/* 1086 */       for (; i < certs.length; i++) {
/* 1087 */         ByteBufInputStream byteBufInputStream = new ByteBufInputStream(certs[i], true);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */ 
/*      */       
/* 1100 */       for (; i < certs.length; i++) {
/* 1101 */         certs[i].release();
/*      */       }
/*      */     } 
/* 1104 */     return x509Certs;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static TrustManagerFactory buildTrustManagerFactory(X509Certificate[] certCollection, TrustManagerFactory trustManagerFactory) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
/* 1110 */     KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
/* 1111 */     ks.load(null, null);
/*      */     
/* 1113 */     int i = 1;
/* 1114 */     for (X509Certificate cert : certCollection) {
/* 1115 */       String alias = Integer.toString(i);
/* 1116 */       ks.setCertificateEntry(alias, cert);
/* 1117 */       i++;
/*      */     } 
/*      */ 
/*      */     
/* 1121 */     if (trustManagerFactory == null) {
/* 1122 */       trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/*      */     }
/* 1124 */     trustManagerFactory.init(ks);
/*      */     
/* 1126 */     return trustManagerFactory;
/*      */   }
/*      */   
/*      */   static PrivateKey toPrivateKeyInternal(File keyFile, String keyPassword) throws SSLException {
/*      */     try {
/* 1131 */       return toPrivateKey(keyFile, keyPassword);
/* 1132 */     } catch (Exception e) {
/* 1133 */       throw new SSLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   static X509Certificate[] toX509CertificatesInternal(File file) throws SSLException {
/*      */     try {
/* 1139 */       return toX509Certificates(file);
/* 1140 */     } catch (CertificateException e) {
/* 1141 */       throw new SSLException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static KeyManagerFactory buildKeyManagerFactory(X509Certificate[] certChain, PrivateKey key, String keyPassword, KeyManagerFactory kmf) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
/* 1149 */     return buildKeyManagerFactory(certChain, KeyManagerFactory.getDefaultAlgorithm(), key, keyPassword, kmf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static KeyManagerFactory buildKeyManagerFactory(X509Certificate[] certChainFile, String keyAlgorithm, PrivateKey key, String keyPassword, KeyManagerFactory kmf) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, UnrecoverableKeyException {
/* 1157 */     char[] keyPasswordChars = (keyPassword == null) ? EmptyArrays.EMPTY_CHARS : keyPassword.toCharArray();
/* 1158 */     KeyStore ks = buildKeyStore(certChainFile, key, keyPasswordChars);
/*      */     
/* 1160 */     if (kmf == null) {
/* 1161 */       kmf = KeyManagerFactory.getInstance(keyAlgorithm);
/*      */     }
/* 1163 */     kmf.init(ks, keyPasswordChars);
/*      */     
/* 1165 */     return kmf;
/*      */   }
/*      */   
/*      */   public abstract boolean isClient();
/*      */   
/*      */   public abstract List<String> cipherSuites();
/*      */   
/*      */   public abstract long sessionCacheSize();
/*      */   
/*      */   public abstract long sessionTimeout();
/*      */   
/*      */   public abstract ApplicationProtocolNegotiator applicationProtocolNegotiator();
/*      */   
/*      */   public abstract SSLEngine newEngine(ByteBufAllocator paramByteBufAllocator);
/*      */   
/*      */   public abstract SSLEngine newEngine(ByteBufAllocator paramByteBufAllocator, String paramString, int paramInt);
/*      */   
/*      */   public abstract SSLSessionContext sessionContext();
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\SslContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */