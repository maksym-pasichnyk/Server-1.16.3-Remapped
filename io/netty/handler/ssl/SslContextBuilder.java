/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.Provider;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.TrustManagerFactory;
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
/*     */ public final class SslContextBuilder
/*     */ {
/*     */   private final boolean forServer;
/*     */   private SslProvider provider;
/*     */   private Provider sslContextProvider;
/*     */   private X509Certificate[] trustCertCollection;
/*     */   private TrustManagerFactory trustManagerFactory;
/*     */   private X509Certificate[] keyCertChain;
/*     */   private PrivateKey key;
/*     */   private String keyPassword;
/*     */   private KeyManagerFactory keyManagerFactory;
/*     */   private Iterable<String> ciphers;
/*     */   
/*     */   public static SslContextBuilder forClient() {
/*  43 */     return new SslContextBuilder(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SslContextBuilder forServer(File keyCertChainFile, File keyFile) {
/*  54 */     return (new SslContextBuilder(true)).keyManager(keyCertChainFile, keyFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SslContextBuilder forServer(InputStream keyCertChainInputStream, InputStream keyInputStream) {
/*  65 */     return (new SslContextBuilder(true)).keyManager(keyCertChainInputStream, keyInputStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SslContextBuilder forServer(PrivateKey key, X509Certificate... keyCertChain) {
/*  76 */     return (new SslContextBuilder(true)).keyManager(key, keyCertChain);
/*     */   }
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
/*     */   public static SslContextBuilder forServer(File keyCertChainFile, File keyFile, String keyPassword) {
/*  90 */     return (new SslContextBuilder(true)).keyManager(keyCertChainFile, keyFile, keyPassword);
/*     */   }
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
/*     */   public static SslContextBuilder forServer(InputStream keyCertChainInputStream, InputStream keyInputStream, String keyPassword) {
/* 104 */     return (new SslContextBuilder(true)).keyManager(keyCertChainInputStream, keyInputStream, keyPassword);
/*     */   }
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
/*     */   public static SslContextBuilder forServer(PrivateKey key, String keyPassword, X509Certificate... keyCertChain) {
/* 118 */     return (new SslContextBuilder(true)).keyManager(key, keyPassword, keyCertChain);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SslContextBuilder forServer(KeyManagerFactory keyManagerFactory) {
/* 128 */     return (new SslContextBuilder(true)).keyManager(keyManagerFactory);
/*     */   }
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
/* 141 */   private CipherSuiteFilter cipherFilter = IdentityCipherSuiteFilter.INSTANCE;
/*     */   private ApplicationProtocolConfig apn;
/*     */   private long sessionCacheSize;
/*     */   private long sessionTimeout;
/* 145 */   private ClientAuth clientAuth = ClientAuth.NONE;
/*     */   private String[] protocols;
/*     */   private boolean startTls;
/*     */   private boolean enableOcsp;
/*     */   
/*     */   private SslContextBuilder(boolean forServer) {
/* 151 */     this.forServer = forServer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder sslProvider(SslProvider provider) {
/* 158 */     this.provider = provider;
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder sslContextProvider(Provider sslContextProvider) {
/* 167 */     this.sslContextProvider = sslContextProvider;
/* 168 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder trustManager(File trustCertCollectionFile) {
/*     */     try {
/* 177 */       return trustManager(SslContext.toX509Certificates(trustCertCollectionFile));
/* 178 */     } catch (Exception e) {
/* 179 */       throw new IllegalArgumentException("File does not contain valid certificates: " + trustCertCollectionFile, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder trustManager(InputStream trustCertCollectionInputStream) {
/*     */     try {
/* 190 */       return trustManager(SslContext.toX509Certificates(trustCertCollectionInputStream));
/* 191 */     } catch (Exception e) {
/* 192 */       throw new IllegalArgumentException("Input stream does not contain valid certificates.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder trustManager(X509Certificate... trustCertCollection) {
/* 200 */     this.trustCertCollection = (trustCertCollection != null) ? (X509Certificate[])trustCertCollection.clone() : null;
/* 201 */     this.trustManagerFactory = null;
/* 202 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder trustManager(TrustManagerFactory trustManagerFactory) {
/* 209 */     this.trustCertCollection = null;
/* 210 */     this.trustManagerFactory = trustManagerFactory;
/* 211 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder keyManager(File keyCertChainFile, File keyFile) {
/* 222 */     return keyManager(keyCertChainFile, keyFile, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder keyManager(InputStream keyCertChainInputStream, InputStream keyInputStream) {
/* 233 */     return keyManager(keyCertChainInputStream, keyInputStream, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder keyManager(PrivateKey key, X509Certificate... keyCertChain) {
/* 244 */     return keyManager(key, (String)null, keyCertChain);
/*     */   }
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
/*     */   public SslContextBuilder keyManager(File keyCertChainFile, File keyFile, String keyPassword) {
/*     */     X509Certificate[] keyCertChain;
/*     */     PrivateKey key;
/*     */     try {
/* 260 */       keyCertChain = SslContext.toX509Certificates(keyCertChainFile);
/* 261 */     } catch (Exception e) {
/* 262 */       throw new IllegalArgumentException("File does not contain valid certificates: " + keyCertChainFile, e);
/*     */     } 
/*     */     try {
/* 265 */       key = SslContext.toPrivateKey(keyFile, keyPassword);
/* 266 */     } catch (Exception e) {
/* 267 */       throw new IllegalArgumentException("File does not contain valid private key: " + keyFile, e);
/*     */     } 
/* 269 */     return keyManager(key, keyPassword, keyCertChain);
/*     */   }
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
/*     */   public SslContextBuilder keyManager(InputStream keyCertChainInputStream, InputStream keyInputStream, String keyPassword) {
/*     */     X509Certificate[] keyCertChain;
/*     */     PrivateKey key;
/*     */     try {
/* 286 */       keyCertChain = SslContext.toX509Certificates(keyCertChainInputStream);
/* 287 */     } catch (Exception e) {
/* 288 */       throw new IllegalArgumentException("Input stream not contain valid certificates.", e);
/*     */     } 
/*     */     try {
/* 291 */       key = SslContext.toPrivateKey(keyInputStream, keyPassword);
/* 292 */     } catch (Exception e) {
/* 293 */       throw new IllegalArgumentException("Input stream does not contain valid private key.", e);
/*     */     } 
/* 295 */     return keyManager(key, keyPassword, keyCertChain);
/*     */   }
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
/*     */   public SslContextBuilder keyManager(PrivateKey key, String keyPassword, X509Certificate... keyCertChain) {
/* 308 */     if (this.forServer) {
/* 309 */       ObjectUtil.checkNotNull(keyCertChain, "keyCertChain required for servers");
/* 310 */       if (keyCertChain.length == 0) {
/* 311 */         throw new IllegalArgumentException("keyCertChain must be non-empty");
/*     */       }
/* 313 */       ObjectUtil.checkNotNull(key, "key required for servers");
/*     */     } 
/* 315 */     if (keyCertChain == null || keyCertChain.length == 0) {
/* 316 */       this.keyCertChain = null;
/*     */     } else {
/* 318 */       for (X509Certificate cert : keyCertChain) {
/* 319 */         if (cert == null) {
/* 320 */           throw new IllegalArgumentException("keyCertChain contains null entry");
/*     */         }
/*     */       } 
/* 323 */       this.keyCertChain = (X509Certificate[])keyCertChain.clone();
/*     */     } 
/* 325 */     this.key = key;
/* 326 */     this.keyPassword = keyPassword;
/* 327 */     this.keyManagerFactory = null;
/* 328 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder keyManager(KeyManagerFactory keyManagerFactory) {
/* 340 */     if (this.forServer) {
/* 341 */       ObjectUtil.checkNotNull(keyManagerFactory, "keyManagerFactory required for servers");
/*     */     }
/* 343 */     this.keyCertChain = null;
/* 344 */     this.key = null;
/* 345 */     this.keyPassword = null;
/* 346 */     this.keyManagerFactory = keyManagerFactory;
/* 347 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder ciphers(Iterable<String> ciphers) {
/* 355 */     return ciphers(ciphers, IdentityCipherSuiteFilter.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder ciphers(Iterable<String> ciphers, CipherSuiteFilter cipherFilter) {
/* 364 */     ObjectUtil.checkNotNull(cipherFilter, "cipherFilter");
/* 365 */     this.ciphers = ciphers;
/* 366 */     this.cipherFilter = cipherFilter;
/* 367 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder applicationProtocolConfig(ApplicationProtocolConfig apn) {
/* 374 */     this.apn = apn;
/* 375 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder sessionCacheSize(long sessionCacheSize) {
/* 383 */     this.sessionCacheSize = sessionCacheSize;
/* 384 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder sessionTimeout(long sessionTimeout) {
/* 392 */     this.sessionTimeout = sessionTimeout;
/* 393 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder clientAuth(ClientAuth clientAuth) {
/* 400 */     this.clientAuth = (ClientAuth)ObjectUtil.checkNotNull(clientAuth, "clientAuth");
/* 401 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder protocols(String... protocols) {
/* 410 */     this.protocols = (protocols == null) ? null : (String[])protocols.clone();
/* 411 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder startTls(boolean startTls) {
/* 418 */     this.startTls = startTls;
/* 419 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContextBuilder enableOcsp(boolean enableOcsp) {
/* 430 */     this.enableOcsp = enableOcsp;
/* 431 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContext build() throws SSLException {
/* 440 */     if (this.forServer) {
/* 441 */       return SslContext.newServerContextInternal(this.provider, this.sslContextProvider, this.trustCertCollection, this.trustManagerFactory, this.keyCertChain, this.key, this.keyPassword, this.keyManagerFactory, this.ciphers, this.cipherFilter, this.apn, this.sessionCacheSize, this.sessionTimeout, this.clientAuth, this.protocols, this.startTls, this.enableOcsp);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 446 */     return SslContext.newClientContextInternal(this.provider, this.sslContextProvider, this.trustCertCollection, this.trustManagerFactory, this.keyCertChain, this.key, this.keyPassword, this.keyManagerFactory, this.ciphers, this.cipherFilter, this.apn, this.protocols, this.sessionCacheSize, this.sessionTimeout, this.enableOcsp);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\SslContextBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */