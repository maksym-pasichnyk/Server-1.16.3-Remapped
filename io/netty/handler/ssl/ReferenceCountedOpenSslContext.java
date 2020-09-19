/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.internal.tcnative.CertificateVerifier;
/*     */ import io.netty.internal.tcnative.SSL;
/*     */ import io.netty.internal.tcnative.SSLContext;
/*     */ import io.netty.util.AbstractReferenceCounted;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.ResourceLeakDetector;
/*     */ import io.netty.util.ResourceLeakDetectorFactory;
/*     */ import io.netty.util.ResourceLeakTracker;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ import javax.net.ssl.SSLSessionContext;
/*     */ import javax.net.ssl.TrustManager;
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
/*     */ public abstract class ReferenceCountedOpenSslContext
/*     */   extends SslContext
/*     */   implements ReferenceCounted
/*     */ {
/*  77 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountedOpenSslContext.class);
/*     */ 
/*     */   
/*  80 */   private static final int DEFAULT_BIO_NON_APPLICATION_BUFFER_SIZE = ((Integer)AccessController.<Integer>doPrivileged(new PrivilegedAction<Integer>()
/*     */       {
/*     */         public Integer run() {
/*  83 */           return Integer.valueOf(Math.max(1, 
/*  84 */                 SystemPropertyUtil.getInt("io.netty.handler.ssl.openssl.bioNonApplicationBufferSize", 2048)));
/*     */         }
/*     */       })).intValue();
/*     */ 
/*     */   
/*     */   private static final Integer DH_KEY_LENGTH;
/*     */   
/*  91 */   private static final ResourceLeakDetector<ReferenceCountedOpenSslContext> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(ReferenceCountedOpenSslContext.class);
/*     */   
/*     */   protected static final int VERIFY_DEPTH = 10;
/*     */   
/*     */   protected long ctx;
/*     */   
/*     */   private final List<String> unmodifiableCiphers;
/*     */   
/*     */   private final long sessionCacheSize;
/*     */   
/*     */   private final long sessionTimeout;
/*     */   
/*     */   private final OpenSslApplicationProtocolNegotiator apn;
/*     */   
/*     */   private final int mode;
/*     */   
/*     */   private final ResourceLeakTracker<ReferenceCountedOpenSslContext> leak;
/*     */ 
/*     */   
/* 110 */   private final AbstractReferenceCounted refCnt = new AbstractReferenceCounted()
/*     */     {
/*     */       public ReferenceCounted touch(Object hint) {
/* 113 */         if (ReferenceCountedOpenSslContext.this.leak != null) {
/* 114 */           ReferenceCountedOpenSslContext.this.leak.record(hint);
/*     */         }
/*     */         
/* 117 */         return ReferenceCountedOpenSslContext.this;
/*     */       }
/*     */ 
/*     */       
/*     */       protected void deallocate() {
/* 122 */         ReferenceCountedOpenSslContext.this.destroy();
/* 123 */         if (ReferenceCountedOpenSslContext.this.leak != null) {
/* 124 */           boolean closed = ReferenceCountedOpenSslContext.this.leak.close(ReferenceCountedOpenSslContext.this);
/* 125 */           assert closed;
/*     */         } 
/*     */       }
/*     */     };
/*     */   
/*     */   final Certificate[] keyCertChain;
/*     */   final ClientAuth clientAuth;
/*     */   final String[] protocols;
/*     */   final boolean enableOcsp;
/* 134 */   final OpenSslEngineMap engineMap = new DefaultOpenSslEngineMap();
/* 135 */   final ReadWriteLock ctxLock = new ReentrantReadWriteLock();
/*     */   
/* 137 */   private volatile int bioNonApplicationBufferSize = DEFAULT_BIO_NON_APPLICATION_BUFFER_SIZE;
/*     */ 
/*     */   
/* 140 */   static final OpenSslApplicationProtocolNegotiator NONE_PROTOCOL_NEGOTIATOR = new OpenSslApplicationProtocolNegotiator()
/*     */     {
/*     */       public ApplicationProtocolConfig.Protocol protocol()
/*     */       {
/* 144 */         return ApplicationProtocolConfig.Protocol.NONE;
/*     */       }
/*     */ 
/*     */       
/*     */       public List<String> protocols() {
/* 149 */         return Collections.emptyList();
/*     */       }
/*     */ 
/*     */       
/*     */       public ApplicationProtocolConfig.SelectorFailureBehavior selectorFailureBehavior() {
/* 154 */         return ApplicationProtocolConfig.SelectorFailureBehavior.CHOOSE_MY_LAST_PROTOCOL;
/*     */       }
/*     */ 
/*     */       
/*     */       public ApplicationProtocolConfig.SelectedListenerFailureBehavior selectedListenerFailureBehavior() {
/* 159 */         return ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT;
/*     */       }
/*     */     };
/*     */   
/*     */   static {
/* 164 */     Integer dhLen = null;
/*     */     
/*     */     try {
/* 167 */       String dhKeySize = AccessController.<String>doPrivileged(new PrivilegedAction<String>()
/*     */           {
/*     */             public String run() {
/* 170 */               return SystemPropertyUtil.get("jdk.tls.ephemeralDHKeySize");
/*     */             }
/*     */           });
/* 173 */       if (dhKeySize != null) {
/*     */         try {
/* 175 */           dhLen = Integer.valueOf(dhKeySize);
/* 176 */         } catch (NumberFormatException e) {
/* 177 */           logger.debug("ReferenceCountedOpenSslContext supports -Djdk.tls.ephemeralDHKeySize={int}, but got: " + dhKeySize);
/*     */         }
/*     */       
/*     */       }
/* 181 */     } catch (Throwable throwable) {}
/*     */ 
/*     */     
/* 184 */     DH_KEY_LENGTH = dhLen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ReferenceCountedOpenSslContext(Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apnCfg, long sessionCacheSize, long sessionTimeout, int mode, Certificate[] keyCertChain, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp, boolean leakDetection) throws SSLException {
/* 191 */     this(ciphers, cipherFilter, toNegotiator(apnCfg), sessionCacheSize, sessionTimeout, mode, keyCertChain, clientAuth, protocols, startTls, enableOcsp, leakDetection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ReferenceCountedOpenSslContext(Iterable<String> ciphers, CipherSuiteFilter cipherFilter, OpenSslApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout, int mode, Certificate[] keyCertChain, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp, boolean leakDetection) throws SSLException {
/* 200 */     super(startTls);
/*     */     
/* 202 */     OpenSsl.ensureAvailability();
/*     */     
/* 204 */     if (enableOcsp && !OpenSsl.isOcspSupported()) {
/* 205 */       throw new IllegalStateException("OCSP is not supported.");
/*     */     }
/*     */     
/* 208 */     if (mode != 1 && mode != 0) {
/* 209 */       throw new IllegalArgumentException("mode most be either SSL.SSL_MODE_SERVER or SSL.SSL_MODE_CLIENT");
/*     */     }
/* 211 */     this.leak = leakDetection ? leakDetector.track(this) : null;
/* 212 */     this.mode = mode;
/* 213 */     this.clientAuth = isServer() ? (ClientAuth)ObjectUtil.checkNotNull(clientAuth, "clientAuth") : ClientAuth.NONE;
/* 214 */     this.protocols = protocols;
/* 215 */     this.enableOcsp = enableOcsp;
/*     */     
/* 217 */     this.keyCertChain = (keyCertChain == null) ? null : (Certificate[])keyCertChain.clone();
/*     */     
/* 219 */     this.unmodifiableCiphers = Arrays.asList(((CipherSuiteFilter)ObjectUtil.checkNotNull(cipherFilter, "cipherFilter")).filterCipherSuites(ciphers, OpenSsl.DEFAULT_CIPHERS, 
/* 220 */           OpenSsl.availableJavaCipherSuites()));
/*     */     
/* 222 */     this.apn = (OpenSslApplicationProtocolNegotiator)ObjectUtil.checkNotNull(apn, "apn");
/*     */ 
/*     */     
/* 225 */     boolean success = false;
/*     */     try {
/*     */       try {
/* 228 */         this.ctx = SSLContext.make(31, mode);
/* 229 */       } catch (Exception e) {
/* 230 */         throw new SSLException("failed to create an SSL_CTX", e);
/*     */       } 
/*     */       
/* 233 */       SSLContext.setOptions(this.ctx, SSLContext.getOptions(this.ctx) | SSL.SSL_OP_NO_SSLv2 | SSL.SSL_OP_NO_SSLv3 | SSL.SSL_OP_CIPHER_SERVER_PREFERENCE | SSL.SSL_OP_NO_COMPRESSION | SSL.SSL_OP_NO_TICKET);
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
/* 250 */       SSLContext.setMode(this.ctx, SSLContext.getMode(this.ctx) | SSL.SSL_MODE_ACCEPT_MOVING_WRITE_BUFFER);
/*     */       
/* 252 */       if (DH_KEY_LENGTH != null) {
/* 253 */         SSLContext.setTmpDHLength(this.ctx, DH_KEY_LENGTH.intValue());
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 258 */         SSLContext.setCipherSuite(this.ctx, CipherSuiteConverter.toOpenSsl(this.unmodifiableCiphers));
/* 259 */       } catch (SSLException e) {
/* 260 */         throw e;
/* 261 */       } catch (Exception e) {
/* 262 */         throw new SSLException("failed to set cipher suite: " + this.unmodifiableCiphers, e);
/*     */       } 
/*     */       
/* 265 */       List<String> nextProtoList = apn.protocols();
/*     */       
/* 267 */       if (!nextProtoList.isEmpty()) {
/* 268 */         String[] appProtocols = nextProtoList.<String>toArray(new String[nextProtoList.size()]);
/* 269 */         int selectorBehavior = opensslSelectorFailureBehavior(apn.selectorFailureBehavior());
/*     */         
/* 271 */         switch (apn.protocol()) {
/*     */           case CHOOSE_MY_LAST_PROTOCOL:
/* 273 */             SSLContext.setNpnProtos(this.ctx, appProtocols, selectorBehavior);
/*     */             break;
/*     */           case ACCEPT:
/* 276 */             SSLContext.setAlpnProtos(this.ctx, appProtocols, selectorBehavior);
/*     */             break;
/*     */           case null:
/* 279 */             SSLContext.setNpnProtos(this.ctx, appProtocols, selectorBehavior);
/* 280 */             SSLContext.setAlpnProtos(this.ctx, appProtocols, selectorBehavior);
/*     */             break;
/*     */           default:
/* 283 */             throw new Error();
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/* 288 */       if (sessionCacheSize <= 0L)
/*     */       {
/* 290 */         sessionCacheSize = SSLContext.setSessionCacheSize(this.ctx, 20480L);
/*     */       }
/* 292 */       this.sessionCacheSize = sessionCacheSize;
/* 293 */       SSLContext.setSessionCacheSize(this.ctx, sessionCacheSize);
/*     */ 
/*     */       
/* 296 */       if (sessionTimeout <= 0L)
/*     */       {
/* 298 */         sessionTimeout = SSLContext.setSessionCacheTimeout(this.ctx, 300L);
/*     */       }
/* 300 */       this.sessionTimeout = sessionTimeout;
/* 301 */       SSLContext.setSessionCacheTimeout(this.ctx, sessionTimeout);
/*     */       
/* 303 */       if (enableOcsp) {
/* 304 */         SSLContext.enableOcsp(this.ctx, isClient());
/*     */       }
/* 306 */       success = true;
/*     */     } finally {
/* 308 */       if (!success) {
/* 309 */         release();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int opensslSelectorFailureBehavior(ApplicationProtocolConfig.SelectorFailureBehavior behavior) {
/* 315 */     switch (behavior) {
/*     */       case CHOOSE_MY_LAST_PROTOCOL:
/* 317 */         return 0;
/*     */       case ACCEPT:
/* 319 */         return 1;
/*     */     } 
/* 321 */     throw new Error();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<String> cipherSuites() {
/* 327 */     return this.unmodifiableCiphers;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long sessionCacheSize() {
/* 332 */     return this.sessionCacheSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long sessionTimeout() {
/* 337 */     return this.sessionTimeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public ApplicationProtocolNegotiator applicationProtocolNegotiator() {
/* 342 */     return this.apn;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isClient() {
/* 347 */     return (this.mode == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public final SSLEngine newEngine(ByteBufAllocator alloc, String peerHost, int peerPort) {
/* 352 */     return newEngine0(alloc, peerHost, peerPort, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final SslHandler newHandler(ByteBufAllocator alloc, boolean startTls) {
/* 357 */     return new SslHandler(newEngine0(alloc, null, -1, false), startTls);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final SslHandler newHandler(ByteBufAllocator alloc, String peerHost, int peerPort, boolean startTls) {
/* 362 */     return new SslHandler(newEngine0(alloc, peerHost, peerPort, false), startTls);
/*     */   }
/*     */   
/*     */   SSLEngine newEngine0(ByteBufAllocator alloc, String peerHost, int peerPort, boolean jdkCompatibilityMode) {
/* 366 */     return new ReferenceCountedOpenSslEngine(this, alloc, peerHost, peerPort, jdkCompatibilityMode, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SSLEngine newEngine(ByteBufAllocator alloc) {
/* 376 */     return newEngine(alloc, null, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final long context() {
/* 388 */     Lock readerLock = this.ctxLock.readLock();
/* 389 */     readerLock.lock();
/*     */     try {
/* 391 */       return this.ctx;
/*     */     } finally {
/* 393 */       readerLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final OpenSslSessionStats stats() {
/* 404 */     return sessionContext().stats();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setRejectRemoteInitiatedRenegotiation(boolean rejectRemoteInitiatedRenegotiation) {
/* 414 */     if (!rejectRemoteInitiatedRenegotiation) {
/* 415 */       throw new UnsupportedOperationException("Renegotiation is not supported");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean getRejectRemoteInitiatedRenegotiation() {
/* 425 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBioNonApplicationBufferSize(int bioNonApplicationBufferSize) {
/* 433 */     this
/* 434 */       .bioNonApplicationBufferSize = ObjectUtil.checkPositiveOrZero(bioNonApplicationBufferSize, "bioNonApplicationBufferSize");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBioNonApplicationBufferSize() {
/* 441 */     return this.bioNonApplicationBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void setTicketKeys(byte[] keys) {
/* 451 */     sessionContext().setTicketKeys(keys);
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
/*     */   @Deprecated
/*     */   public final long sslCtxPointer() {
/* 466 */     Lock readerLock = this.ctxLock.readLock();
/* 467 */     readerLock.lock();
/*     */     try {
/* 469 */       return this.ctx;
/*     */     } finally {
/* 471 */       readerLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void destroy() {
/* 479 */     Lock writerLock = this.ctxLock.writeLock();
/* 480 */     writerLock.lock();
/*     */     try {
/* 482 */       if (this.ctx != 0L) {
/* 483 */         if (this.enableOcsp) {
/* 484 */           SSLContext.disableOcsp(this.ctx);
/*     */         }
/*     */         
/* 487 */         SSLContext.free(this.ctx);
/* 488 */         this.ctx = 0L;
/*     */       } 
/*     */     } finally {
/* 491 */       writerLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static X509Certificate[] certificates(byte[][] chain) {
/* 496 */     X509Certificate[] peerCerts = new X509Certificate[chain.length];
/* 497 */     for (int i = 0; i < peerCerts.length; i++) {
/* 498 */       peerCerts[i] = new OpenSslX509Certificate(chain[i]);
/*     */     }
/* 500 */     return peerCerts;
/*     */   }
/*     */   
/*     */   protected static X509TrustManager chooseTrustManager(TrustManager[] managers) {
/* 504 */     for (TrustManager m : managers) {
/* 505 */       if (m instanceof X509TrustManager) {
/* 506 */         return (X509TrustManager)m;
/*     */       }
/*     */     } 
/* 509 */     throw new IllegalStateException("no X509TrustManager found");
/*     */   }
/*     */   
/*     */   protected static X509KeyManager chooseX509KeyManager(KeyManager[] kms) {
/* 513 */     for (KeyManager km : kms) {
/* 514 */       if (km instanceof X509KeyManager) {
/* 515 */         return (X509KeyManager)km;
/*     */       }
/*     */     } 
/* 518 */     throw new IllegalStateException("no X509KeyManager found");
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
/*     */   static OpenSslApplicationProtocolNegotiator toNegotiator(ApplicationProtocolConfig config) {
/* 530 */     if (config == null) {
/* 531 */       return NONE_PROTOCOL_NEGOTIATOR;
/*     */     }
/*     */     
/* 534 */     switch (config.protocol()) {
/*     */       case null:
/* 536 */         return NONE_PROTOCOL_NEGOTIATOR;
/*     */       case CHOOSE_MY_LAST_PROTOCOL:
/*     */       case ACCEPT:
/*     */       case null:
/* 540 */         switch (config.selectedListenerFailureBehavior()) {
/*     */           case CHOOSE_MY_LAST_PROTOCOL:
/*     */           case ACCEPT:
/* 543 */             switch (config.selectorFailureBehavior()) {
/*     */               case CHOOSE_MY_LAST_PROTOCOL:
/*     */               case ACCEPT:
/* 546 */                 return new OpenSslDefaultApplicationProtocolNegotiator(config);
/*     */             } 
/*     */             
/* 549 */             throw new UnsupportedOperationException("OpenSSL provider does not support " + config
/*     */                 
/* 551 */                 .selectorFailureBehavior() + " behavior");
/*     */         } 
/*     */ 
/*     */         
/* 555 */         throw new UnsupportedOperationException("OpenSSL provider does not support " + config
/*     */             
/* 557 */             .selectedListenerFailureBehavior() + " behavior");
/*     */     } 
/*     */ 
/*     */     
/* 561 */     throw new Error();
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean useExtendedTrustManager(X509TrustManager trustManager) {
/* 566 */     return (PlatformDependent.javaVersion() >= 7 && trustManager instanceof javax.net.ssl.X509ExtendedTrustManager);
/*     */   }
/*     */   
/*     */   static boolean useExtendedKeyManager(X509KeyManager keyManager) {
/* 570 */     return (PlatformDependent.javaVersion() >= 7 && keyManager instanceof javax.net.ssl.X509ExtendedKeyManager);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int refCnt() {
/* 575 */     return this.refCnt.refCnt();
/*     */   }
/*     */ 
/*     */   
/*     */   public final ReferenceCounted retain() {
/* 580 */     this.refCnt.retain();
/* 581 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ReferenceCounted retain(int increment) {
/* 586 */     this.refCnt.retain(increment);
/* 587 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ReferenceCounted touch() {
/* 592 */     this.refCnt.touch();
/* 593 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ReferenceCounted touch(Object hint) {
/* 598 */     this.refCnt.touch(hint);
/* 599 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean release() {
/* 604 */     return this.refCnt.release();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean release(int decrement) {
/* 609 */     return this.refCnt.release(decrement);
/*     */   }
/*     */   
/*     */   static abstract class AbstractCertificateVerifier extends CertificateVerifier {
/*     */     private final OpenSslEngineMap engineMap;
/*     */     
/*     */     AbstractCertificateVerifier(OpenSslEngineMap engineMap) {
/* 616 */       this.engineMap = engineMap;
/*     */     }
/*     */ 
/*     */     
/*     */     public final int verify(long ssl, byte[][] chain, String auth) {
/* 621 */       X509Certificate[] peerCerts = ReferenceCountedOpenSslContext.certificates(chain);
/* 622 */       ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
/*     */       try {
/* 624 */         verify(engine, peerCerts, auth);
/* 625 */         return CertificateVerifier.X509_V_OK;
/* 626 */       } catch (Throwable cause) {
/* 627 */         ReferenceCountedOpenSslContext.logger.debug("verification of certificate failed", cause);
/* 628 */         SSLHandshakeException e = new SSLHandshakeException("General OpenSslEngine problem");
/* 629 */         e.initCause(cause);
/* 630 */         engine.handshakeException = e;
/*     */ 
/*     */         
/* 633 */         if (cause instanceof OpenSslCertificateException)
/*     */         {
/*     */           
/* 636 */           return ((OpenSslCertificateException)cause).errorCode();
/*     */         }
/* 638 */         if (cause instanceof java.security.cert.CertificateExpiredException) {
/* 639 */           return CertificateVerifier.X509_V_ERR_CERT_HAS_EXPIRED;
/*     */         }
/* 641 */         if (cause instanceof java.security.cert.CertificateNotYetValidException) {
/* 642 */           return CertificateVerifier.X509_V_ERR_CERT_NOT_YET_VALID;
/*     */         }
/* 644 */         if (PlatformDependent.javaVersion() >= 7) {
/* 645 */           if (cause instanceof java.security.cert.CertificateRevokedException) {
/* 646 */             return CertificateVerifier.X509_V_ERR_CERT_REVOKED;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 652 */           Throwable wrapped = cause.getCause();
/* 653 */           while (wrapped != null) {
/* 654 */             if (wrapped instanceof CertPathValidatorException) {
/* 655 */               CertPathValidatorException ex = (CertPathValidatorException)wrapped;
/* 656 */               CertPathValidatorException.Reason reason = ex.getReason();
/* 657 */               if (reason == CertPathValidatorException.BasicReason.EXPIRED) {
/* 658 */                 return CertificateVerifier.X509_V_ERR_CERT_HAS_EXPIRED;
/*     */               }
/* 660 */               if (reason == CertPathValidatorException.BasicReason.NOT_YET_VALID) {
/* 661 */                 return CertificateVerifier.X509_V_ERR_CERT_NOT_YET_VALID;
/*     */               }
/* 663 */               if (reason == CertPathValidatorException.BasicReason.REVOKED) {
/* 664 */                 return CertificateVerifier.X509_V_ERR_CERT_REVOKED;
/*     */               }
/*     */             } 
/* 667 */             wrapped = wrapped.getCause();
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 672 */         return CertificateVerifier.X509_V_ERR_UNSPECIFIED;
/*     */       } 
/*     */     }
/*     */     
/*     */     abstract void verify(ReferenceCountedOpenSslEngine param1ReferenceCountedOpenSslEngine, X509Certificate[] param1ArrayOfX509Certificate, String param1String) throws Exception;
/*     */   }
/*     */   
/*     */   private static final class DefaultOpenSslEngineMap
/*     */     implements OpenSslEngineMap {
/* 681 */     private final Map<Long, ReferenceCountedOpenSslEngine> engines = PlatformDependent.newConcurrentHashMap();
/*     */ 
/*     */     
/*     */     public ReferenceCountedOpenSslEngine remove(long ssl) {
/* 685 */       return this.engines.remove(Long.valueOf(ssl));
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(ReferenceCountedOpenSslEngine engine) {
/* 690 */       this.engines.put(Long.valueOf(engine.sslPointer()), engine);
/*     */     }
/*     */ 
/*     */     
/*     */     public ReferenceCountedOpenSslEngine get(long ssl) {
/* 695 */       return this.engines.get(Long.valueOf(ssl));
/*     */     }
/*     */     
/*     */     private DefaultOpenSslEngineMap() {}
/*     */   }
/*     */   
/*     */   static void setKeyMaterial(long ctx, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword) throws SSLException {
/* 702 */     long keyBio = 0L;
/* 703 */     long keyCertChainBio = 0L;
/* 704 */     long keyCertChainBio2 = 0L;
/* 705 */     PemEncoded encoded = null;
/*     */     
/*     */     try {
/* 708 */       encoded = PemX509Certificate.toPEM(ByteBufAllocator.DEFAULT, true, keyCertChain);
/* 709 */       keyCertChainBio = toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
/* 710 */       keyCertChainBio2 = toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
/*     */       
/* 712 */       if (key != null) {
/* 713 */         keyBio = toBIO(key);
/*     */       }
/*     */       
/* 716 */       SSLContext.setCertificateBio(ctx, keyCertChainBio, keyBio, (keyPassword == null) ? "" : keyPassword);
/*     */ 
/*     */ 
/*     */       
/* 720 */       SSLContext.setCertificateChainBio(ctx, keyCertChainBio2, true);
/* 721 */     } catch (SSLException e) {
/* 722 */       throw e;
/* 723 */     } catch (Exception e) {
/* 724 */       throw new SSLException("failed to set certificate and key", e);
/*     */     } finally {
/* 726 */       freeBio(keyBio);
/* 727 */       freeBio(keyCertChainBio);
/* 728 */       freeBio(keyCertChainBio2);
/* 729 */       if (encoded != null) {
/* 730 */         encoded.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   static void freeBio(long bio) {
/* 736 */     if (bio != 0L) {
/* 737 */       SSL.freeBIO(bio);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long toBIO(PrivateKey key) throws Exception {
/* 746 */     if (key == null) {
/* 747 */       return 0L;
/*     */     }
/*     */     
/* 750 */     ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
/* 751 */     PemEncoded pem = PemPrivateKey.toPEM(allocator, true, key);
/*     */     try {
/* 753 */       return toBIO(allocator, pem.retain());
/*     */     } finally {
/* 755 */       pem.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long toBIO(X509Certificate... certChain) throws Exception {
/* 764 */     if (certChain == null) {
/* 765 */       return 0L;
/*     */     }
/*     */     
/* 768 */     if (certChain.length == 0) {
/* 769 */       throw new IllegalArgumentException("certChain can't be empty");
/*     */     }
/*     */     
/* 772 */     ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
/* 773 */     PemEncoded pem = PemX509Certificate.toPEM(allocator, true, certChain);
/*     */     try {
/* 775 */       return toBIO(allocator, pem.retain());
/*     */     } finally {
/* 777 */       pem.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static long toBIO(ByteBufAllocator allocator, PemEncoded pem) throws Exception {
/*     */     try {
/* 785 */       ByteBuf content = pem.content();
/*     */       
/* 787 */       if (content.isDirect()) {
/* 788 */         return newBIO(content.retainedSlice());
/*     */       }
/*     */       
/* 791 */       ByteBuf buffer = allocator.directBuffer(content.readableBytes());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 807 */       pem.release();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static long newBIO(ByteBuf buffer) throws Exception {
/*     */     try {
/* 813 */       long bio = SSL.newMemBIO();
/* 814 */       int readable = buffer.readableBytes();
/* 815 */       if (SSL.bioWrite(bio, OpenSsl.memoryAddress(buffer) + buffer.readerIndex(), readable) != readable) {
/* 816 */         SSL.freeBIO(bio);
/* 817 */         throw new IllegalStateException("Could not write data to memory BIO");
/*     */       } 
/* 819 */       return bio;
/*     */     } finally {
/* 821 */       buffer.release();
/*     */     } 
/*     */   }
/*     */   
/*     */   abstract OpenSslKeyMaterialManager keyMaterialManager();
/*     */   
/*     */   public abstract OpenSslSessionContext sessionContext();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\ReferenceCountedOpenSslContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */