/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.KeyException;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Security;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.crypto.NoSuchPaddingException;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLSessionContext;
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
/*     */ public class JdkSslContext
/*     */   extends SslContext
/*     */ {
/*     */   static {
/*     */     SSLContext context;
/*     */   }
/*     */   
/*  56 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(JdkSslContext.class);
/*     */   
/*     */   static final String PROTOCOL = "TLS";
/*     */   
/*     */   private static final String[] DEFAULT_PROTOCOLS;
/*     */   private static final List<String> DEFAULT_CIPHERS;
/*     */   private static final Set<String> SUPPORTED_CIPHERS;
/*     */   private final String[] protocols;
/*     */   
/*     */   static {
/*     */     try {
/*  67 */       context = SSLContext.getInstance("TLS");
/*  68 */       context.init(null, null, null);
/*  69 */     } catch (Exception e) {
/*  70 */       throw new Error("failed to initialize the default SSL context", e);
/*     */     } 
/*     */     
/*  73 */     SSLEngine engine = context.createSSLEngine();
/*     */ 
/*     */     
/*  76 */     String[] supportedProtocols = engine.getSupportedProtocols();
/*  77 */     Set<String> supportedProtocolsSet = new HashSet<String>(supportedProtocols.length); int i;
/*  78 */     for (i = 0; i < supportedProtocols.length; i++) {
/*  79 */       supportedProtocolsSet.add(supportedProtocols[i]);
/*     */     }
/*  81 */     List<String> protocols = new ArrayList<String>();
/*  82 */     SslUtils.addIfSupported(supportedProtocolsSet, protocols, new String[] { "TLSv1.2", "TLSv1.1", "TLSv1" });
/*     */ 
/*     */ 
/*     */     
/*  86 */     if (!protocols.isEmpty()) {
/*  87 */       DEFAULT_PROTOCOLS = protocols.<String>toArray(new String[protocols.size()]);
/*     */     } else {
/*  89 */       DEFAULT_PROTOCOLS = engine.getEnabledProtocols();
/*     */     } 
/*     */ 
/*     */     
/*  93 */     String[] supportedCiphers = engine.getSupportedCipherSuites();
/*  94 */     SUPPORTED_CIPHERS = new HashSet<String>(supportedCiphers.length);
/*  95 */     for (i = 0; i < supportedCiphers.length; i++) {
/*  96 */       String supportedCipher = supportedCiphers[i];
/*  97 */       SUPPORTED_CIPHERS.add(supportedCipher);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 107 */       if (supportedCipher.startsWith("SSL_")) {
/* 108 */         String tlsPrefixedCipherName = "TLS_" + supportedCipher.substring("SSL_".length());
/*     */         try {
/* 110 */           engine.setEnabledCipherSuites(new String[] { tlsPrefixedCipherName });
/* 111 */           SUPPORTED_CIPHERS.add(tlsPrefixedCipherName);
/* 112 */         } catch (IllegalArgumentException illegalArgumentException) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 117 */     List<String> ciphers = new ArrayList<String>();
/* 118 */     SslUtils.addIfSupported(SUPPORTED_CIPHERS, ciphers, SslUtils.DEFAULT_CIPHER_SUITES);
/* 119 */     SslUtils.useFallbackCiphersIfDefaultIsEmpty(ciphers, engine.getEnabledCipherSuites());
/* 120 */     DEFAULT_CIPHERS = Collections.unmodifiableList(ciphers);
/*     */     
/* 122 */     if (logger.isDebugEnabled()) {
/* 123 */       logger.debug("Default protocols (JDK): {} ", Arrays.asList(DEFAULT_PROTOCOLS));
/* 124 */       logger.debug("Default cipher suites (JDK): {}", DEFAULT_CIPHERS);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final String[] cipherSuites;
/*     */ 
/*     */   
/*     */   private final List<String> unmodifiableCipherSuites;
/*     */ 
/*     */   
/*     */   private final JdkApplicationProtocolNegotiator apn;
/*     */   
/*     */   private final ClientAuth clientAuth;
/*     */   
/*     */   private final SSLContext sslContext;
/*     */   
/*     */   private final boolean isClient;
/*     */ 
/*     */   
/*     */   public JdkSslContext(SSLContext sslContext, boolean isClient, ClientAuth clientAuth) {
/* 146 */     this(sslContext, isClient, (Iterable<String>)null, IdentityCipherSuiteFilter.INSTANCE, JdkDefaultApplicationProtocolNegotiator.INSTANCE, clientAuth, (String[])null, false);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public JdkSslContext(SSLContext sslContext, boolean isClient, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, ClientAuth clientAuth) {
/* 163 */     this(sslContext, isClient, ciphers, cipherFilter, toNegotiator(apn, !isClient), clientAuth, (String[])null, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   JdkSslContext(SSLContext sslContext, boolean isClient, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, JdkApplicationProtocolNegotiator apn, ClientAuth clientAuth, String[] protocols, boolean startTls) {
/* 169 */     super(startTls);
/* 170 */     this.apn = (JdkApplicationProtocolNegotiator)ObjectUtil.checkNotNull(apn, "apn");
/* 171 */     this.clientAuth = (ClientAuth)ObjectUtil.checkNotNull(clientAuth, "clientAuth");
/* 172 */     this.cipherSuites = ((CipherSuiteFilter)ObjectUtil.checkNotNull(cipherFilter, "cipherFilter")).filterCipherSuites(ciphers, DEFAULT_CIPHERS, SUPPORTED_CIPHERS);
/*     */     
/* 174 */     this.protocols = (protocols == null) ? DEFAULT_PROTOCOLS : protocols;
/* 175 */     this.unmodifiableCipherSuites = Collections.unmodifiableList(Arrays.asList(this.cipherSuites));
/* 176 */     this.sslContext = (SSLContext)ObjectUtil.checkNotNull(sslContext, "sslContext");
/* 177 */     this.isClient = isClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SSLContext context() {
/* 184 */     return this.sslContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isClient() {
/* 189 */     return this.isClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SSLSessionContext sessionContext() {
/* 197 */     if (isServer()) {
/* 198 */       return context().getServerSessionContext();
/*     */     }
/* 200 */     return context().getClientSessionContext();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<String> cipherSuites() {
/* 206 */     return this.unmodifiableCipherSuites;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long sessionCacheSize() {
/* 211 */     return sessionContext().getSessionCacheSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public final long sessionTimeout() {
/* 216 */     return sessionContext().getSessionTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public final SSLEngine newEngine(ByteBufAllocator alloc) {
/* 221 */     return configureAndWrapEngine(context().createSSLEngine(), alloc);
/*     */   }
/*     */ 
/*     */   
/*     */   public final SSLEngine newEngine(ByteBufAllocator alloc, String peerHost, int peerPort) {
/* 226 */     return configureAndWrapEngine(context().createSSLEngine(peerHost, peerPort), alloc);
/*     */   }
/*     */ 
/*     */   
/*     */   private SSLEngine configureAndWrapEngine(SSLEngine engine, ByteBufAllocator alloc) {
/* 231 */     engine.setEnabledCipherSuites(this.cipherSuites);
/* 232 */     engine.setEnabledProtocols(this.protocols);
/* 233 */     engine.setUseClientMode(isClient());
/* 234 */     if (isServer()) {
/* 235 */       switch (this.clientAuth) {
/*     */         case NONE:
/* 237 */           engine.setWantClientAuth(true);
/*     */           break;
/*     */         case ALPN:
/* 240 */           engine.setNeedClientAuth(true);
/*     */           break;
/*     */         case NPN:
/*     */           break;
/*     */         default:
/* 245 */           throw new Error("Unknown auth " + this.clientAuth);
/*     */       } 
/*     */     }
/* 248 */     JdkApplicationProtocolNegotiator.SslEngineWrapperFactory factory = this.apn.wrapperFactory();
/* 249 */     if (factory instanceof JdkApplicationProtocolNegotiator.AllocatorAwareSslEngineWrapperFactory) {
/* 250 */       return ((JdkApplicationProtocolNegotiator.AllocatorAwareSslEngineWrapperFactory)factory)
/* 251 */         .wrapSslEngine(engine, alloc, this.apn, isServer());
/*     */     }
/* 253 */     return factory.wrapSslEngine(engine, this.apn, isServer());
/*     */   }
/*     */ 
/*     */   
/*     */   public final JdkApplicationProtocolNegotiator applicationProtocolNegotiator() {
/* 258 */     return this.apn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static JdkApplicationProtocolNegotiator toNegotiator(ApplicationProtocolConfig config, boolean isServer) {
/* 269 */     if (config == null) {
/* 270 */       return JdkDefaultApplicationProtocolNegotiator.INSTANCE;
/*     */     }
/*     */     
/* 273 */     switch (config.protocol()) {
/*     */       case NONE:
/* 275 */         return JdkDefaultApplicationProtocolNegotiator.INSTANCE;
/*     */       case ALPN:
/* 277 */         if (isServer) {
/* 278 */           switch (config.selectorFailureBehavior()) {
/*     */             case NONE:
/* 280 */               return new JdkAlpnApplicationProtocolNegotiator(true, config.supportedProtocols());
/*     */             case ALPN:
/* 282 */               return new JdkAlpnApplicationProtocolNegotiator(false, config.supportedProtocols());
/*     */           } 
/* 284 */           throw new UnsupportedOperationException("JDK provider does not support " + config
/* 285 */               .selectorFailureBehavior() + " failure behavior");
/*     */         } 
/*     */         
/* 288 */         switch (config.selectedListenerFailureBehavior()) {
/*     */           case NONE:
/* 290 */             return new JdkAlpnApplicationProtocolNegotiator(false, config.supportedProtocols());
/*     */           case ALPN:
/* 292 */             return new JdkAlpnApplicationProtocolNegotiator(true, config.supportedProtocols());
/*     */         } 
/* 294 */         throw new UnsupportedOperationException("JDK provider does not support " + config
/* 295 */             .selectedListenerFailureBehavior() + " failure behavior");
/*     */ 
/*     */       
/*     */       case NPN:
/* 299 */         if (isServer) {
/* 300 */           switch (config.selectedListenerFailureBehavior()) {
/*     */             case NONE:
/* 302 */               return new JdkNpnApplicationProtocolNegotiator(false, config.supportedProtocols());
/*     */             case ALPN:
/* 304 */               return new JdkNpnApplicationProtocolNegotiator(true, config.supportedProtocols());
/*     */           } 
/* 306 */           throw new UnsupportedOperationException("JDK provider does not support " + config
/* 307 */               .selectedListenerFailureBehavior() + " failure behavior");
/*     */         } 
/*     */         
/* 310 */         switch (config.selectorFailureBehavior()) {
/*     */           case NONE:
/* 312 */             return new JdkNpnApplicationProtocolNegotiator(true, config.supportedProtocols());
/*     */           case ALPN:
/* 314 */             return new JdkNpnApplicationProtocolNegotiator(false, config.supportedProtocols());
/*     */         } 
/* 316 */         throw new UnsupportedOperationException("JDK provider does not support " + config
/* 317 */             .selectorFailureBehavior() + " failure behavior");
/*     */     } 
/*     */ 
/*     */     
/* 321 */     throw new UnsupportedOperationException("JDK provider does not support " + config
/* 322 */         .protocol() + " protocol");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected static KeyManagerFactory buildKeyManagerFactory(File certChainFile, File keyFile, String keyPassword, KeyManagerFactory kmf) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, CertificateException, KeyException, IOException {
/* 342 */     String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
/* 343 */     if (algorithm == null) {
/* 344 */       algorithm = "SunX509";
/*     */     }
/* 346 */     return buildKeyManagerFactory(certChainFile, algorithm, keyFile, keyPassword, kmf);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected static KeyManagerFactory buildKeyManagerFactory(File certChainFile, String keyAlgorithm, File keyFile, String keyPassword, KeyManagerFactory kmf) throws KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IOException, CertificateException, KeyException, UnrecoverableKeyException {
/* 369 */     return buildKeyManagerFactory(toX509Certificates(certChainFile), keyAlgorithm, 
/* 370 */         toPrivateKey(keyFile, keyPassword), keyPassword, kmf);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\JdkSslContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */