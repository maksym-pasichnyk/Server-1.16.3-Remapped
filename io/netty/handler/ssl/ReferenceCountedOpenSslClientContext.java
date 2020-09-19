/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.internal.tcnative.CertificateRequestedCallback;
/*     */ import io.netty.internal.tcnative.SSLContext;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.KeyStore;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ import javax.net.ssl.SSLSessionContext;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509ExtendedKeyManager;
/*     */ import javax.net.ssl.X509ExtendedTrustManager;
/*     */ import javax.net.ssl.X509KeyManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import javax.security.auth.x500.X500Principal;
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
/*     */ public final class ReferenceCountedOpenSslClientContext
/*     */   extends ReferenceCountedOpenSslContext
/*     */ {
/*  50 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountedOpenSslClientContext.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final OpenSslSessionContext sessionContext;
/*     */ 
/*     */ 
/*     */   
/*     */   ReferenceCountedOpenSslClientContext(X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, String[] protocols, long sessionCacheSize, long sessionTimeout, boolean enableOcsp) throws SSLException {
/*  59 */     super(ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, 0, (Certificate[])keyCertChain, ClientAuth.NONE, protocols, false, enableOcsp, true);
/*     */     
/*  61 */     boolean success = false;
/*     */     try {
/*  63 */       this.sessionContext = newSessionContext(this, this.ctx, this.engineMap, trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory);
/*     */       
/*  65 */       success = true;
/*     */     } finally {
/*  67 */       if (!success) {
/*  68 */         release();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   OpenSslKeyMaterialManager keyMaterialManager() {
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public OpenSslSessionContext sessionContext() {
/*  80 */     return this.sessionContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static OpenSslSessionContext newSessionContext(ReferenceCountedOpenSslContext thiz, long ctx, OpenSslEngineMap engineMap, X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory) throws SSLException {
/*  89 */     if ((key == null && keyCertChain != null) || (key != null && keyCertChain == null)) {
/*  90 */       throw new IllegalArgumentException("Either both keyCertChain and key needs to be null or none of them");
/*     */     }
/*     */     
/*     */     try {
/*  94 */       if (!OpenSsl.useKeyManagerFactory()) {
/*  95 */         if (keyManagerFactory != null) {
/*  96 */           throw new IllegalArgumentException("KeyManagerFactory not supported");
/*     */         }
/*     */         
/*  99 */         if (keyCertChain != null) {
/* 100 */           setKeyMaterial(ctx, keyCertChain, key, keyPassword);
/*     */         }
/*     */       } else {
/*     */         
/* 104 */         if (keyManagerFactory == null && keyCertChain != null) {
/* 105 */           keyManagerFactory = buildKeyManagerFactory(keyCertChain, key, keyPassword, keyManagerFactory);
/*     */         }
/*     */ 
/*     */         
/* 109 */         if (keyManagerFactory != null) {
/* 110 */           X509KeyManager keyManager = chooseX509KeyManager(keyManagerFactory.getKeyManagers());
/* 111 */           OpenSslKeyMaterialManager materialManager = useExtendedKeyManager(keyManager) ? new OpenSslExtendedKeyMaterialManager((X509ExtendedKeyManager)keyManager, keyPassword) : new OpenSslKeyMaterialManager(keyManager, keyPassword);
/*     */ 
/*     */ 
/*     */           
/* 115 */           SSLContext.setCertRequestedCallback(ctx, new OpenSslCertificateRequestedCallback(engineMap, materialManager));
/*     */         }
/*     */       
/*     */       } 
/* 119 */     } catch (Exception e) {
/* 120 */       throw new SSLException("failed to set certificate and key", e);
/*     */     } 
/*     */     
/* 123 */     SSLContext.setVerify(ctx, 0, 10);
/*     */     
/*     */     try {
/* 126 */       if (trustCertCollection != null) {
/* 127 */         trustManagerFactory = buildTrustManagerFactory(trustCertCollection, trustManagerFactory);
/* 128 */       } else if (trustManagerFactory == null) {
/* 129 */         trustManagerFactory = TrustManagerFactory.getInstance(
/* 130 */             TrustManagerFactory.getDefaultAlgorithm());
/* 131 */         trustManagerFactory.init((KeyStore)null);
/*     */       } 
/* 133 */       X509TrustManager manager = chooseTrustManager(trustManagerFactory.getTrustManagers());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 142 */       if (useExtendedTrustManager(manager)) {
/* 143 */         SSLContext.setCertVerifyCallback(ctx, new ExtendedTrustManagerVerifyCallback(engineMap, (X509ExtendedTrustManager)manager));
/*     */       } else {
/*     */         
/* 146 */         SSLContext.setCertVerifyCallback(ctx, new TrustManagerVerifyCallback(engineMap, manager));
/*     */       } 
/* 148 */     } catch (Exception e) {
/* 149 */       throw new SSLException("unable to setup trustmanager", e);
/*     */     } 
/* 151 */     return new OpenSslClientSessionContext(thiz);
/*     */   }
/*     */   
/*     */   static final class OpenSslClientSessionContext
/*     */     extends OpenSslSessionContext {
/*     */     OpenSslClientSessionContext(ReferenceCountedOpenSslContext context) {
/* 157 */       super(context);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSessionTimeout(int seconds) {
/* 162 */       if (seconds < 0) {
/* 163 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public int getSessionTimeout() {
/* 169 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSessionCacheSize(int size) {
/* 174 */       if (size < 0) {
/* 175 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public int getSessionCacheSize() {
/* 181 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setSessionCacheEnabled(boolean enabled) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isSessionCacheEnabled() {
/* 191 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier {
/*     */     private final X509TrustManager manager;
/*     */     
/*     */     TrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509TrustManager manager) {
/* 199 */       super(engineMap);
/* 200 */       this.manager = manager;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth) throws Exception {
/* 206 */       this.manager.checkServerTrusted(peerCerts, auth);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ExtendedTrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier {
/*     */     private final X509ExtendedTrustManager manager;
/*     */     
/*     */     ExtendedTrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509ExtendedTrustManager manager) {
/* 214 */       super(engineMap);
/* 215 */       this.manager = manager;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth) throws Exception {
/* 221 */       this.manager.checkServerTrusted(peerCerts, auth, engine);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class OpenSslCertificateRequestedCallback implements CertificateRequestedCallback {
/*     */     private final OpenSslEngineMap engineMap;
/*     */     private final OpenSslKeyMaterialManager keyManagerHolder;
/*     */     
/*     */     OpenSslCertificateRequestedCallback(OpenSslEngineMap engineMap, OpenSslKeyMaterialManager keyManagerHolder) {
/* 230 */       this.engineMap = engineMap;
/* 231 */       this.keyManagerHolder = keyManagerHolder;
/*     */     }
/*     */ 
/*     */     
/*     */     public CertificateRequestedCallback.KeyMaterial requested(long ssl, byte[] keyTypeBytes, byte[][] asn1DerEncodedPrincipals) {
/* 236 */       ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl); try {
/*     */         X500Principal[] issuers;
/* 238 */         Set<String> keyTypesSet = supportedClientKeyTypes(keyTypeBytes);
/* 239 */         String[] keyTypes = keyTypesSet.<String>toArray(new String[keyTypesSet.size()]);
/*     */         
/* 241 */         if (asn1DerEncodedPrincipals == null) {
/* 242 */           issuers = null;
/*     */         } else {
/* 244 */           issuers = new X500Principal[asn1DerEncodedPrincipals.length];
/* 245 */           for (int i = 0; i < asn1DerEncodedPrincipals.length; i++) {
/* 246 */             issuers[i] = new X500Principal(asn1DerEncodedPrincipals[i]);
/*     */           }
/*     */         } 
/* 249 */         return this.keyManagerHolder.keyMaterial(engine, keyTypes, issuers);
/* 250 */       } catch (Throwable cause) {
/* 251 */         ReferenceCountedOpenSslClientContext.logger.debug("request of key failed", cause);
/* 252 */         SSLHandshakeException e = new SSLHandshakeException("General OpenSslEngine problem");
/* 253 */         e.initCause(cause);
/* 254 */         engine.handshakeException = e;
/* 255 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static Set<String> supportedClientKeyTypes(byte[] clientCertificateTypes) {
/* 268 */       Set<String> result = new HashSet<String>(clientCertificateTypes.length);
/* 269 */       for (byte keyTypeCode : clientCertificateTypes) {
/* 270 */         String keyType = clientKeyType(keyTypeCode);
/* 271 */         if (keyType != null)
/*     */         {
/*     */ 
/*     */           
/* 275 */           result.add(keyType); } 
/*     */       } 
/* 277 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     private static String clientKeyType(byte clientCertificateType) {
/* 282 */       switch (clientCertificateType) {
/*     */         case 1:
/* 284 */           return "RSA";
/*     */         case 3:
/* 286 */           return "DH_RSA";
/*     */         case 64:
/* 288 */           return "EC";
/*     */         case 65:
/* 290 */           return "EC_RSA";
/*     */         case 66:
/* 292 */           return "EC_EC";
/*     */       } 
/* 294 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\ReferenceCountedOpenSslClientContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */