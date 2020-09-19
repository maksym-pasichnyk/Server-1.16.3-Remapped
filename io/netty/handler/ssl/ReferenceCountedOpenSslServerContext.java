/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.internal.tcnative.SSLContext;
/*     */ import io.netty.internal.tcnative.SniHostNameMatcher;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.KeyStore;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLSessionContext;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509ExtendedKeyManager;
/*     */ import javax.net.ssl.X509ExtendedTrustManager;
/*     */ import javax.net.ssl.X509KeyManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ReferenceCountedOpenSslServerContext
/*     */   extends ReferenceCountedOpenSslContext
/*     */ {
/*  48 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountedOpenSslServerContext.class);
/*  49 */   private static final byte[] ID = new byte[] { 110, 101, 116, 116, 121 };
/*     */ 
/*     */   
/*     */   private final OpenSslServerSessionContext sessionContext;
/*     */ 
/*     */   
/*     */   private final OpenSslKeyMaterialManager keyMaterialManager;
/*     */ 
/*     */   
/*     */   ReferenceCountedOpenSslServerContext(X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp) throws SSLException {
/*  59 */     this(trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, 
/*  60 */         toNegotiator(apn), sessionCacheSize, sessionTimeout, clientAuth, protocols, startTls, enableOcsp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ReferenceCountedOpenSslServerContext(X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, OpenSslApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp) throws SSLException {
/*  70 */     super(ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, 1, (Certificate[])keyCertChain, clientAuth, protocols, startTls, enableOcsp, true);
/*     */ 
/*     */     
/*  73 */     boolean success = false;
/*     */     try {
/*  75 */       ServerContext context = newSessionContext(this, this.ctx, this.engineMap, trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory);
/*     */       
/*  77 */       this.sessionContext = context.sessionContext;
/*  78 */       this.keyMaterialManager = context.keyMaterialManager;
/*  79 */       success = true;
/*     */     } finally {
/*  81 */       if (!success) {
/*  82 */         release();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public OpenSslServerSessionContext sessionContext() {
/*  89 */     return this.sessionContext;
/*     */   }
/*     */ 
/*     */   
/*     */   OpenSslKeyMaterialManager keyMaterialManager() {
/*  94 */     return this.keyMaterialManager;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ServerContext
/*     */   {
/*     */     OpenSslServerSessionContext sessionContext;
/*     */     
/*     */     OpenSslKeyMaterialManager keyMaterialManager;
/*     */   }
/*     */ 
/*     */   
/*     */   static ServerContext newSessionContext(ReferenceCountedOpenSslContext thiz, long ctx, OpenSslEngineMap engineMap, X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory) throws SSLException {
/* 108 */     ServerContext result = new ServerContext();
/*     */     try {
/* 110 */       SSLContext.setVerify(ctx, 0, 10);
/* 111 */       if (!OpenSsl.useKeyManagerFactory()) {
/* 112 */         if (keyManagerFactory != null) {
/* 113 */           throw new IllegalArgumentException("KeyManagerFactory not supported");
/*     */         }
/*     */         
/* 116 */         ObjectUtil.checkNotNull(keyCertChain, "keyCertChain");
/*     */         
/* 118 */         setKeyMaterial(ctx, keyCertChain, key, keyPassword);
/*     */       }
/*     */       else {
/*     */         
/* 122 */         if (keyManagerFactory == null) {
/* 123 */           keyManagerFactory = buildKeyManagerFactory(keyCertChain, key, keyPassword, keyManagerFactory);
/*     */         }
/*     */         
/* 126 */         X509KeyManager keyManager = chooseX509KeyManager(keyManagerFactory.getKeyManagers());
/* 127 */         result.keyMaterialManager = useExtendedKeyManager(keyManager) ? new OpenSslExtendedKeyMaterialManager((X509ExtendedKeyManager)keyManager, keyPassword) : new OpenSslKeyMaterialManager(keyManager, keyPassword);
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 132 */     catch (Exception e) {
/* 133 */       throw new SSLException("failed to set certificate and key", e);
/*     */     } 
/*     */     try {
/* 136 */       if (trustCertCollection != null) {
/* 137 */         trustManagerFactory = buildTrustManagerFactory(trustCertCollection, trustManagerFactory);
/* 138 */       } else if (trustManagerFactory == null) {
/*     */         
/* 140 */         trustManagerFactory = TrustManagerFactory.getInstance(
/* 141 */             TrustManagerFactory.getDefaultAlgorithm());
/* 142 */         trustManagerFactory.init((KeyStore)null);
/*     */       } 
/*     */       
/* 145 */       X509TrustManager manager = chooseTrustManager(trustManagerFactory.getTrustManagers());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 154 */       if (useExtendedTrustManager(manager)) {
/* 155 */         SSLContext.setCertVerifyCallback(ctx, new ExtendedTrustManagerVerifyCallback(engineMap, (X509ExtendedTrustManager)manager));
/*     */       } else {
/*     */         
/* 158 */         SSLContext.setCertVerifyCallback(ctx, new TrustManagerVerifyCallback(engineMap, manager));
/*     */       } 
/*     */       
/* 161 */       X509Certificate[] issuers = manager.getAcceptedIssuers();
/* 162 */       if (issuers != null && issuers.length > 0) {
/* 163 */         long bio = 0L;
/*     */         try {
/* 165 */           bio = toBIO(issuers);
/* 166 */           if (!SSLContext.setCACertificateBio(ctx, bio)) {
/* 167 */             throw new SSLException("unable to setup accepted issuers for trustmanager " + manager);
/*     */           }
/*     */         } finally {
/* 170 */           freeBio(bio);
/*     */         } 
/*     */       } 
/*     */       
/* 174 */       if (PlatformDependent.javaVersion() >= 8)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 179 */         SSLContext.setSniHostnameMatcher(ctx, new OpenSslSniHostnameMatcher(engineMap));
/*     */       }
/* 181 */     } catch (SSLException e) {
/* 182 */       throw e;
/* 183 */     } catch (Exception e) {
/* 184 */       throw new SSLException("unable to setup trustmanager", e);
/*     */     } 
/*     */     
/* 187 */     result.sessionContext = new OpenSslServerSessionContext(thiz);
/* 188 */     result.sessionContext.setSessionIdContext(ID);
/* 189 */     return result;
/*     */   }
/*     */   
/*     */   private static final class TrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier {
/*     */     private final X509TrustManager manager;
/*     */     
/*     */     TrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509TrustManager manager) {
/* 196 */       super(engineMap);
/* 197 */       this.manager = manager;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth) throws Exception {
/* 203 */       this.manager.checkClientTrusted(peerCerts, auth);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ExtendedTrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier {
/*     */     private final X509ExtendedTrustManager manager;
/*     */     
/*     */     ExtendedTrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509ExtendedTrustManager manager) {
/* 211 */       super(engineMap);
/* 212 */       this.manager = manager;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth) throws Exception {
/* 218 */       this.manager.checkClientTrusted(peerCerts, auth, engine);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class OpenSslSniHostnameMatcher implements SniHostNameMatcher {
/*     */     private final OpenSslEngineMap engineMap;
/*     */     
/*     */     OpenSslSniHostnameMatcher(OpenSslEngineMap engineMap) {
/* 226 */       this.engineMap = engineMap;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean match(long ssl, String hostname) {
/* 231 */       ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
/* 232 */       if (engine != null) {
/* 233 */         return engine.checkSniHostnameMatch(hostname);
/*     */       }
/* 235 */       ReferenceCountedOpenSslServerContext.logger.warn("No ReferenceCountedOpenSslEngine found for SSL pointer: {}", Long.valueOf(ssl));
/* 236 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\ReferenceCountedOpenSslServerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */