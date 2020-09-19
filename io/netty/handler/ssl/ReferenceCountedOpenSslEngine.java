/*      */ package io.netty.handler.ssl;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.internal.tcnative.Buffer;
/*      */ import io.netty.internal.tcnative.SSL;
/*      */ import io.netty.util.AbstractReferenceCounted;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import io.netty.util.ResourceLeakDetector;
/*      */ import io.netty.util.ResourceLeakDetectorFactory;
/*      */ import io.netty.util.ResourceLeakTracker;
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.ThrowableUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ReadOnlyBufferException;
/*      */ import java.security.Principal;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLEngineResult;
/*      */ import javax.net.ssl.SSLException;
/*      */ import javax.net.ssl.SSLHandshakeException;
/*      */ import javax.net.ssl.SSLParameters;
/*      */ import javax.net.ssl.SSLPeerUnverifiedException;
/*      */ import javax.net.ssl.SSLSession;
/*      */ import javax.net.ssl.SSLSessionBindingEvent;
/*      */ import javax.net.ssl.SSLSessionBindingListener;
/*      */ import javax.net.ssl.SSLSessionContext;
/*      */ import javax.security.cert.X509Certificate;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ReferenceCountedOpenSslEngine
/*      */   extends SSLEngine
/*      */   implements ReferenceCounted, ApplicationProtocolAccessor
/*      */ {
/*   95 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountedOpenSslEngine.class);
/*      */   
/*   97 */   private static final SSLException BEGIN_HANDSHAKE_ENGINE_CLOSED = (SSLException)ThrowableUtil.unknownStackTrace(new SSLException("engine closed"), ReferenceCountedOpenSslEngine.class, "beginHandshake()");
/*      */   
/*   99 */   private static final SSLException HANDSHAKE_ENGINE_CLOSED = (SSLException)ThrowableUtil.unknownStackTrace(new SSLException("engine closed"), ReferenceCountedOpenSslEngine.class, "handshake()");
/*      */   
/*  101 */   private static final SSLException RENEGOTIATION_UNSUPPORTED = (SSLException)ThrowableUtil.unknownStackTrace(new SSLException("renegotiation unsupported"), ReferenceCountedOpenSslEngine.class, "beginHandshake()");
/*      */ 
/*      */   
/*  104 */   private static final ResourceLeakDetector<ReferenceCountedOpenSslEngine> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(ReferenceCountedOpenSslEngine.class);
/*      */   private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_SSLV2 = 0;
/*      */   private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_SSLV3 = 1;
/*      */   private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_TLSv1 = 2;
/*      */   private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_TLSv1_1 = 3;
/*      */   private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_TLSv1_2 = 4;
/*  110 */   private static final int[] OPENSSL_OP_NO_PROTOCOLS = new int[] { SSL.SSL_OP_NO_SSLv2, SSL.SSL_OP_NO_SSLv3, SSL.SSL_OP_NO_TLSv1, SSL.SSL_OP_NO_TLSv1_1, SSL.SSL_OP_NO_TLSv1_2 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int DEFAULT_HOSTNAME_VALIDATION_FLAGS = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  125 */   static final int MAX_PLAINTEXT_LENGTH = SSL.SSL_MAX_PLAINTEXT_LENGTH;
/*      */ 
/*      */ 
/*      */   
/*  129 */   private static final int MAX_RECORD_SIZE = SSL.SSL_MAX_RECORD_LENGTH;
/*      */ 
/*      */   
/*  132 */   private static final AtomicIntegerFieldUpdater<ReferenceCountedOpenSslEngine> DESTROYED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(ReferenceCountedOpenSslEngine.class, "destroyed");
/*      */   
/*      */   private static final String INVALID_CIPHER = "SSL_NULL_WITH_NULL_NULL";
/*  135 */   private static final SSLEngineResult NEED_UNWRAP_OK = new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
/*  136 */   private static final SSLEngineResult NEED_UNWRAP_CLOSED = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
/*  137 */   private static final SSLEngineResult NEED_WRAP_OK = new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
/*  138 */   private static final SSLEngineResult NEED_WRAP_CLOSED = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0); private long ssl; private long networkBIO; private boolean certificateSet; private HandshakeState handshakeState; private boolean receivedShutdown; private volatile int destroyed; private volatile String applicationProtocol; private final ResourceLeakTracker<ReferenceCountedOpenSslEngine> leak; private final AbstractReferenceCounted refCnt; private volatile ClientAuth clientAuth; private volatile long lastAccessed; private String endPointIdentificationAlgorithm; private Object algorithmConstraints; private List<String> sniHostNames; private volatile Collection<?> matchers; private boolean isInboundDone;
/*  139 */   private static final SSLEngineResult CLOSED_NOT_HANDSHAKING = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0); private boolean outboundClosed; final boolean jdkCompatibilityMode; private final boolean clientMode; private final ByteBufAllocator alloc; private final OpenSslEngineMap engineMap; private final OpenSslApplicationProtocolNegotiator apn;
/*      */   private final OpenSslSession session;
/*      */   private final Certificate[] localCerts;
/*      */   private final ByteBuffer[] singleSrcBuffer;
/*      */   private final ByteBuffer[] singleDstBuffer;
/*      */   private final OpenSslKeyMaterialManager keyMaterialManager;
/*      */   private final boolean enableOcsp;
/*      */   private int maxWrapOverhead;
/*      */   private int maxWrapBufferSize;
/*      */   SSLHandshakeException handshakeException;
/*      */   
/*  150 */   private enum HandshakeState { NOT_STARTED,
/*      */ 
/*      */ 
/*      */     
/*  154 */     STARTED_IMPLICITLY,
/*      */ 
/*      */ 
/*      */     
/*  158 */     STARTED_EXPLICITLY,
/*      */ 
/*      */ 
/*      */     
/*  162 */     FINISHED; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ReferenceCountedOpenSslEngine(ReferenceCountedOpenSslContext context, ByteBufAllocator alloc, String peerHost, int peerPort, boolean jdkCompatibilityMode, boolean leakDetection) {
/*  242 */     super(peerHost, peerPort); long finalSsl; this.handshakeState = HandshakeState.NOT_STARTED; this.refCnt = new AbstractReferenceCounted() { public ReferenceCounted touch(Object hint) { if (ReferenceCountedOpenSslEngine.this.leak != null) ReferenceCountedOpenSslEngine.this.leak.record(hint);  return ReferenceCountedOpenSslEngine.this; } protected void deallocate() { ReferenceCountedOpenSslEngine.this.shutdown(); if (ReferenceCountedOpenSslEngine.this.leak != null) { boolean closed = ReferenceCountedOpenSslEngine.this.leak.close(ReferenceCountedOpenSslEngine.this); assert closed; }  } }
/*  243 */       ; this.clientAuth = ClientAuth.NONE; this.lastAccessed = -1L; this.singleSrcBuffer = new ByteBuffer[1]; this.singleDstBuffer = new ByteBuffer[1]; OpenSsl.ensureAvailability();
/*  244 */     this.alloc = (ByteBufAllocator)ObjectUtil.checkNotNull(alloc, "alloc");
/*  245 */     this.apn = (OpenSslApplicationProtocolNegotiator)context.applicationProtocolNegotiator();
/*  246 */     this.session = new OpenSslSession(context.sessionContext());
/*  247 */     this.clientMode = context.isClient();
/*  248 */     this.engineMap = context.engineMap;
/*  249 */     this.localCerts = context.keyCertChain;
/*  250 */     this.keyMaterialManager = context.keyMaterialManager();
/*  251 */     this.enableOcsp = context.enableOcsp;
/*  252 */     this.jdkCompatibilityMode = jdkCompatibilityMode;
/*  253 */     Lock readerLock = context.ctxLock.readLock();
/*  254 */     readerLock.lock();
/*      */     
/*      */     try {
/*  257 */       finalSsl = SSL.newSSL(context.ctx, !context.isClient());
/*      */     } finally {
/*  259 */       readerLock.unlock();
/*      */     } 
/*  261 */     synchronized (this) {
/*  262 */       this.ssl = finalSsl;
/*      */       try {
/*  264 */         this.networkBIO = SSL.bioNewByteBuffer(this.ssl, context.getBioNonApplicationBufferSize());
/*      */ 
/*      */ 
/*      */         
/*  268 */         setClientAuth(this.clientMode ? ClientAuth.NONE : context.clientAuth);
/*      */         
/*  270 */         if (context.protocols != null) {
/*  271 */           setEnabledProtocols(context.protocols);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  276 */         if (this.clientMode && peerHost != null) {
/*  277 */           SSL.setTlsExtHostName(this.ssl, peerHost);
/*      */         }
/*      */         
/*  280 */         if (this.enableOcsp) {
/*  281 */           SSL.enableOcsp(this.ssl);
/*      */         }
/*      */         
/*  284 */         if (!jdkCompatibilityMode) {
/*  285 */           SSL.setMode(this.ssl, SSL.getMode(this.ssl) | SSL.SSL_MODE_ENABLE_PARTIAL_WRITE);
/*      */         }
/*      */ 
/*      */         
/*  289 */         calculateMaxWrapOverhead();
/*  290 */       } catch (Throwable cause) {
/*  291 */         SSL.freeSSL(this.ssl);
/*  292 */         PlatformDependent.throwException(cause);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  298 */     this.leak = leakDetection ? leakDetector.track(this) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOcspResponse(byte[] response) {
/*  306 */     if (!this.enableOcsp) {
/*  307 */       throw new IllegalStateException("OCSP stapling is not enabled");
/*      */     }
/*      */     
/*  310 */     if (this.clientMode) {
/*  311 */       throw new IllegalStateException("Not a server SSLEngine");
/*      */     }
/*      */     
/*  314 */     synchronized (this) {
/*  315 */       SSL.setOcspResponse(this.ssl, response);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getOcspResponse() {
/*  324 */     if (!this.enableOcsp) {
/*  325 */       throw new IllegalStateException("OCSP stapling is not enabled");
/*      */     }
/*      */     
/*  328 */     if (!this.clientMode) {
/*  329 */       throw new IllegalStateException("Not a client SSLEngine");
/*      */     }
/*      */     
/*  332 */     synchronized (this) {
/*  333 */       return SSL.getOcspResponse(this.ssl);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final int refCnt() {
/*  339 */     return this.refCnt.refCnt();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ReferenceCounted retain() {
/*  344 */     this.refCnt.retain();
/*  345 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ReferenceCounted retain(int increment) {
/*  350 */     this.refCnt.retain(increment);
/*  351 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ReferenceCounted touch() {
/*  356 */     this.refCnt.touch();
/*  357 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ReferenceCounted touch(Object hint) {
/*  362 */     this.refCnt.touch(hint);
/*  363 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean release() {
/*  368 */     return this.refCnt.release();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean release(int decrement) {
/*  373 */     return this.refCnt.release(decrement);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized SSLSession getHandshakeSession() {
/*  382 */     switch (this.handshakeState) {
/*      */       case NONE:
/*      */       case ALPN:
/*  385 */         return null;
/*      */     } 
/*  387 */     return this.session;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized long sslPointer() {
/*  397 */     return this.ssl;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized void shutdown() {
/*  404 */     if (DESTROYED_UPDATER.compareAndSet(this, 0, 1)) {
/*  405 */       this.engineMap.remove(this.ssl);
/*  406 */       SSL.freeSSL(this.ssl);
/*  407 */       this.ssl = this.networkBIO = 0L;
/*      */       
/*  409 */       this.isInboundDone = this.outboundClosed = true;
/*      */     } 
/*      */ 
/*      */     
/*  413 */     SSL.clearError();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int writePlaintextData(ByteBuffer src, int len) {
/*  422 */     int sslWrote, pos = src.position();
/*  423 */     int limit = src.limit();
/*      */ 
/*      */     
/*  426 */     if (src.isDirect()) {
/*  427 */       sslWrote = SSL.writeToSSL(this.ssl, bufferAddress(src) + pos, len);
/*  428 */       if (sslWrote > 0) {
/*  429 */         src.position(pos + sslWrote);
/*      */       }
/*      */     } else {
/*  432 */       ByteBuf buf = this.alloc.directBuffer(len);
/*      */       try {
/*  434 */         src.limit(pos + len);
/*      */         
/*  436 */         buf.setBytes(0, src);
/*  437 */         src.limit(limit);
/*      */         
/*  439 */         sslWrote = SSL.writeToSSL(this.ssl, OpenSsl.memoryAddress(buf), len);
/*  440 */         if (sslWrote > 0) {
/*  441 */           src.position(pos + sslWrote);
/*      */         } else {
/*  443 */           src.position(pos);
/*      */         } 
/*      */       } finally {
/*  446 */         buf.release();
/*      */       } 
/*      */     } 
/*  449 */     return sslWrote;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteBuf writeEncryptedData(ByteBuffer src, int len) {
/*  456 */     int pos = src.position();
/*  457 */     if (src.isDirect()) {
/*  458 */       SSL.bioSetByteBuffer(this.networkBIO, bufferAddress(src) + pos, len, false);
/*      */     } else {
/*  460 */       ByteBuf buf = this.alloc.directBuffer(len);
/*      */       try {
/*  462 */         int limit = src.limit();
/*  463 */         src.limit(pos + len);
/*  464 */         buf.writeBytes(src);
/*      */         
/*  466 */         src.position(pos);
/*  467 */         src.limit(limit);
/*      */         
/*  469 */         SSL.bioSetByteBuffer(this.networkBIO, OpenSsl.memoryAddress(buf), len, false);
/*  470 */         return buf;
/*  471 */       } catch (Throwable cause) {
/*  472 */         buf.release();
/*  473 */         PlatformDependent.throwException(cause);
/*      */       } 
/*      */     } 
/*  476 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readPlaintextData(ByteBuffer dst) {
/*  484 */     int sslRead, pos = dst.position();
/*  485 */     if (dst.isDirect()) {
/*  486 */       sslRead = SSL.readFromSSL(this.ssl, bufferAddress(dst) + pos, dst.limit() - pos);
/*  487 */       if (sslRead > 0) {
/*  488 */         dst.position(pos + sslRead);
/*      */       }
/*      */     } else {
/*  491 */       int limit = dst.limit();
/*  492 */       int len = Math.min(maxEncryptedPacketLength0(), limit - pos);
/*  493 */       ByteBuf buf = this.alloc.directBuffer(len);
/*      */       try {
/*  495 */         sslRead = SSL.readFromSSL(this.ssl, OpenSsl.memoryAddress(buf), len);
/*  496 */         if (sslRead > 0) {
/*  497 */           dst.limit(pos + sslRead);
/*  498 */           buf.getBytes(buf.readerIndex(), dst);
/*  499 */           dst.limit(limit);
/*      */         } 
/*      */       } finally {
/*  502 */         buf.release();
/*      */       } 
/*      */     } 
/*      */     
/*  506 */     return sslRead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized int maxWrapOverhead() {
/*  513 */     return this.maxWrapOverhead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized int maxEncryptedPacketLength() {
/*  520 */     return maxEncryptedPacketLength0();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final int maxEncryptedPacketLength0() {
/*  528 */     return this.maxWrapOverhead + MAX_PLAINTEXT_LENGTH;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final int calculateMaxLengthForWrap(int plaintextLength, int numComponents) {
/*  537 */     return (int)Math.min(this.maxWrapBufferSize, plaintextLength + this.maxWrapOverhead * numComponents);
/*      */   }
/*      */   
/*      */   final synchronized int sslPending() {
/*  541 */     return sslPending0();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void calculateMaxWrapOverhead() {
/*  548 */     this.maxWrapOverhead = SSL.getMaxWrapOverhead(this.ssl);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  553 */     this.maxWrapBufferSize = this.jdkCompatibilityMode ? maxEncryptedPacketLength0() : (maxEncryptedPacketLength0() << 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int sslPending0() {
/*  561 */     return (this.handshakeState != HandshakeState.FINISHED) ? 0 : SSL.sslPending(this.ssl);
/*      */   }
/*      */   
/*      */   private boolean isBytesAvailableEnoughForWrap(int bytesAvailable, int plaintextLength, int numComponents) {
/*  565 */     return (bytesAvailable - this.maxWrapOverhead * numComponents >= plaintextLength);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final SSLEngineResult wrap(ByteBuffer[] srcs, int offset, int length, ByteBuffer dst) throws SSLException {
/*  572 */     if (srcs == null) {
/*  573 */       throw new IllegalArgumentException("srcs is null");
/*      */     }
/*  575 */     if (dst == null) {
/*  576 */       throw new IllegalArgumentException("dst is null");
/*      */     }
/*      */     
/*  579 */     if (offset >= srcs.length || offset + length > srcs.length) {
/*  580 */       throw new IndexOutOfBoundsException("offset: " + offset + ", length: " + length + " (expected: offset <= offset + length <= srcs.length (" + srcs.length + "))");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  585 */     if (dst.isReadOnly()) {
/*  586 */       throw new ReadOnlyBufferException();
/*      */     }
/*      */     
/*  589 */     synchronized (this) {
/*  590 */       if (isOutboundDone())
/*      */       {
/*  592 */         return (isInboundDone() || isDestroyed()) ? CLOSED_NOT_HANDSHAKING : NEED_UNWRAP_CLOSED;
/*      */       }
/*      */       
/*  595 */       int bytesProduced = 0;
/*  596 */       ByteBuf bioReadCopyBuf = null;
/*      */       
/*      */       try {
/*  599 */         if (dst.isDirect()) {
/*  600 */           SSL.bioSetByteBuffer(this.networkBIO, bufferAddress(dst) + dst.position(), dst.remaining(), true);
/*      */         } else {
/*      */           
/*  603 */           bioReadCopyBuf = this.alloc.directBuffer(dst.remaining());
/*  604 */           SSL.bioSetByteBuffer(this.networkBIO, OpenSsl.memoryAddress(bioReadCopyBuf), bioReadCopyBuf.writableBytes(), true);
/*      */         } 
/*      */ 
/*      */         
/*  608 */         int bioLengthBefore = SSL.bioLengthByteBuffer(this.networkBIO);
/*      */ 
/*      */         
/*  611 */         if (this.outboundClosed) {
/*      */ 
/*      */           
/*  614 */           bytesProduced = SSL.bioFlushByteBuffer(this.networkBIO);
/*  615 */           if (bytesProduced <= 0) {
/*  616 */             return newResultMayFinishHandshake(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  621 */           if (!doSSLShutdown()) {
/*  622 */             return newResultMayFinishHandshake(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, bytesProduced);
/*      */           }
/*  624 */           bytesProduced = bioLengthBefore - SSL.bioLengthByteBuffer(this.networkBIO);
/*  625 */           return newResultMayFinishHandshake(SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, bytesProduced);
/*      */         } 
/*      */ 
/*      */         
/*  629 */         SSLEngineResult.HandshakeStatus status = SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */         
/*  631 */         if (this.handshakeState != HandshakeState.FINISHED) {
/*  632 */           if (this.handshakeState != HandshakeState.STARTED_EXPLICITLY)
/*      */           {
/*  634 */             this.handshakeState = HandshakeState.STARTED_IMPLICITLY;
/*      */           }
/*      */ 
/*      */           
/*  638 */           bytesProduced = SSL.bioFlushByteBuffer(this.networkBIO);
/*      */           
/*  640 */           if (bytesProduced > 0 && this.handshakeException != null)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  649 */             return newResult(SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, bytesProduced);
/*      */           }
/*      */           
/*  652 */           status = handshake();
/*      */ 
/*      */ 
/*      */           
/*  656 */           bytesProduced = bioLengthBefore - SSL.bioLengthByteBuffer(this.networkBIO);
/*      */           
/*  658 */           if (bytesProduced > 0)
/*      */           {
/*      */ 
/*      */             
/*  662 */             return newResult(mayFinishHandshake((status != SSLEngineResult.HandshakeStatus.FINISHED) ? ((bytesProduced == bioLengthBefore) ? SSLEngineResult.HandshakeStatus.NEED_WRAP : 
/*      */                   
/*  664 */                   getHandshakeStatus(SSL.bioLengthNonApplication(this.networkBIO))) : SSLEngineResult.HandshakeStatus.FINISHED), 0, bytesProduced);
/*      */           }
/*      */ 
/*      */           
/*  668 */           if (status == SSLEngineResult.HandshakeStatus.NEED_UNWRAP)
/*      */           {
/*  670 */             return isOutboundDone() ? NEED_UNWRAP_CLOSED : NEED_UNWRAP_OK;
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  675 */           if (this.outboundClosed) {
/*  676 */             bytesProduced = SSL.bioFlushByteBuffer(this.networkBIO);
/*  677 */             return newResultMayFinishHandshake(status, 0, bytesProduced);
/*      */           } 
/*      */         } 
/*      */         
/*  681 */         int endOffset = offset + length;
/*  682 */         if (this.jdkCompatibilityMode) {
/*  683 */           int srcsLen = 0;
/*  684 */           for (int i = offset; i < endOffset; i++) {
/*  685 */             ByteBuffer src = srcs[i];
/*  686 */             if (src == null) {
/*  687 */               throw new IllegalArgumentException("srcs[" + i + "] is null");
/*      */             }
/*  689 */             if (srcsLen != MAX_PLAINTEXT_LENGTH) {
/*      */ 
/*      */ 
/*      */               
/*  693 */               srcsLen += src.remaining();
/*  694 */               if (srcsLen > MAX_PLAINTEXT_LENGTH || srcsLen < 0)
/*      */               {
/*      */ 
/*      */                 
/*  698 */                 srcsLen = MAX_PLAINTEXT_LENGTH;
/*      */               }
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/*  704 */           if (!isBytesAvailableEnoughForWrap(dst.remaining(), srcsLen, 1)) {
/*  705 */             return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, getHandshakeStatus(), 0, 0);
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/*  710 */         int bytesConsumed = 0;
/*      */         
/*  712 */         bytesProduced = SSL.bioFlushByteBuffer(this.networkBIO);
/*  713 */         for (; offset < endOffset; offset++) {
/*  714 */           ByteBuffer src = srcs[offset];
/*  715 */           int remaining = src.remaining();
/*  716 */           if (remaining != 0) {
/*      */             int bytesWritten;
/*      */ 
/*      */ 
/*      */             
/*  721 */             if (this.jdkCompatibilityMode) {
/*      */ 
/*      */ 
/*      */               
/*  725 */               bytesWritten = writePlaintextData(src, Math.min(remaining, MAX_PLAINTEXT_LENGTH - bytesConsumed));
/*      */             
/*      */             }
/*      */             else {
/*      */               
/*  730 */               int availableCapacityForWrap = dst.remaining() - bytesProduced - this.maxWrapOverhead;
/*  731 */               if (availableCapacityForWrap <= 0) {
/*  732 */                 return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, getHandshakeStatus(), bytesConsumed, bytesProduced);
/*      */               }
/*      */               
/*  735 */               bytesWritten = writePlaintextData(src, Math.min(remaining, availableCapacityForWrap));
/*      */             } 
/*      */             
/*  738 */             if (bytesWritten > 0) {
/*  739 */               bytesConsumed += bytesWritten;
/*      */ 
/*      */               
/*  742 */               int pendingNow = SSL.bioLengthByteBuffer(this.networkBIO);
/*  743 */               bytesProduced += bioLengthBefore - pendingNow;
/*  744 */               bioLengthBefore = pendingNow;
/*      */               
/*  746 */               if (this.jdkCompatibilityMode || bytesProduced == dst.remaining()) {
/*  747 */                 return newResultMayFinishHandshake(status, bytesConsumed, bytesProduced);
/*      */               }
/*      */             } else {
/*  750 */               int sslError = SSL.getError(this.ssl, bytesWritten);
/*  751 */               if (sslError == SSL.SSL_ERROR_ZERO_RETURN) {
/*      */                 
/*  753 */                 if (!this.receivedShutdown) {
/*  754 */                   closeAll();
/*      */                   
/*  756 */                   bytesProduced += bioLengthBefore - SSL.bioLengthByteBuffer(this.networkBIO);
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/*  761 */                   SSLEngineResult.HandshakeStatus hs = mayFinishHandshake((status != SSLEngineResult.HandshakeStatus.FINISHED) ? (
/*  762 */                       (bytesProduced == dst.remaining()) ? SSLEngineResult.HandshakeStatus.NEED_WRAP : 
/*  763 */                       getHandshakeStatus(SSL.bioLengthNonApplication(this.networkBIO))) : SSLEngineResult.HandshakeStatus.FINISHED);
/*      */                   
/*  765 */                   return newResult(hs, bytesConsumed, bytesProduced);
/*      */                 } 
/*      */                 
/*  768 */                 return newResult(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, bytesConsumed, bytesProduced);
/*  769 */               }  if (sslError == SSL.SSL_ERROR_WANT_READ)
/*      */               {
/*      */ 
/*      */                 
/*  773 */                 return newResult(SSLEngineResult.HandshakeStatus.NEED_UNWRAP, bytesConsumed, bytesProduced); } 
/*  774 */               if (sslError == SSL.SSL_ERROR_WANT_WRITE)
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  787 */                 return newResult(SSLEngineResult.Status.BUFFER_OVERFLOW, status, bytesConsumed, bytesProduced);
/*      */               }
/*      */               
/*  790 */               throw shutdownWithError("SSL_write");
/*      */             } 
/*      */           } 
/*      */         } 
/*  794 */         return newResultMayFinishHandshake(status, bytesConsumed, bytesProduced);
/*      */       } finally {
/*  796 */         SSL.bioClearByteBuffer(this.networkBIO);
/*  797 */         if (bioReadCopyBuf == null) {
/*  798 */           dst.position(dst.position() + bytesProduced);
/*      */         } else {
/*  800 */           assert bioReadCopyBuf.readableBytes() <= dst.remaining() : "The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf;
/*      */           
/*  802 */           dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  803 */           bioReadCopyBuf.release();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private SSLEngineResult newResult(SSLEngineResult.HandshakeStatus hs, int bytesConsumed, int bytesProduced) {
/*  810 */     return newResult(SSLEngineResult.Status.OK, hs, bytesConsumed, bytesProduced);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SSLEngineResult newResult(SSLEngineResult.Status status, SSLEngineResult.HandshakeStatus hs, int bytesConsumed, int bytesProduced) {
/*  818 */     if (isOutboundDone()) {
/*  819 */       if (isInboundDone()) {
/*      */         
/*  821 */         hs = SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */ 
/*      */         
/*  824 */         shutdown();
/*      */       } 
/*  826 */       return new SSLEngineResult(SSLEngineResult.Status.CLOSED, hs, bytesConsumed, bytesProduced);
/*      */     } 
/*  828 */     return new SSLEngineResult(status, hs, bytesConsumed, bytesProduced);
/*      */   }
/*      */ 
/*      */   
/*      */   private SSLEngineResult newResultMayFinishHandshake(SSLEngineResult.HandshakeStatus hs, int bytesConsumed, int bytesProduced) throws SSLException {
/*  833 */     return newResult(mayFinishHandshake((hs != SSLEngineResult.HandshakeStatus.FINISHED) ? getHandshakeStatus() : SSLEngineResult.HandshakeStatus.FINISHED), bytesConsumed, bytesProduced);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SSLEngineResult newResultMayFinishHandshake(SSLEngineResult.Status status, SSLEngineResult.HandshakeStatus hs, int bytesConsumed, int bytesProduced) throws SSLException {
/*  840 */     return newResult(status, mayFinishHandshake((hs != SSLEngineResult.HandshakeStatus.FINISHED) ? getHandshakeStatus() : SSLEngineResult.HandshakeStatus.FINISHED), bytesConsumed, bytesProduced);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SSLException shutdownWithError(String operations) {
/*  848 */     String err = SSL.getLastError();
/*  849 */     return shutdownWithError(operations, err);
/*      */   }
/*      */   
/*      */   private SSLException shutdownWithError(String operation, String err) {
/*  853 */     if (logger.isDebugEnabled()) {
/*  854 */       logger.debug("{} failed: OpenSSL error: {}", operation, err);
/*      */     }
/*      */ 
/*      */     
/*  858 */     shutdown();
/*  859 */     if (this.handshakeState == HandshakeState.FINISHED) {
/*  860 */       return new SSLException(err);
/*      */     }
/*  862 */     return new SSLHandshakeException(err);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final SSLEngineResult unwrap(ByteBuffer[] srcs, int srcsOffset, int srcsLength, ByteBuffer[] dsts, int dstsOffset, int dstsLength) throws SSLException {
/*  870 */     if (srcs == null) {
/*  871 */       throw new NullPointerException("srcs");
/*      */     }
/*  873 */     if (srcsOffset >= srcs.length || srcsOffset + srcsLength > srcs.length)
/*      */     {
/*  875 */       throw new IndexOutOfBoundsException("offset: " + srcsOffset + ", length: " + srcsLength + " (expected: offset <= offset + length <= srcs.length (" + srcs.length + "))");
/*      */     }
/*      */ 
/*      */     
/*  879 */     if (dsts == null) {
/*  880 */       throw new IllegalArgumentException("dsts is null");
/*      */     }
/*  882 */     if (dstsOffset >= dsts.length || dstsOffset + dstsLength > dsts.length) {
/*  883 */       throw new IndexOutOfBoundsException("offset: " + dstsOffset + ", length: " + dstsLength + " (expected: offset <= offset + length <= dsts.length (" + dsts.length + "))");
/*      */     }
/*      */ 
/*      */     
/*  887 */     long capacity = 0L;
/*  888 */     int dstsEndOffset = dstsOffset + dstsLength;
/*  889 */     for (int i = dstsOffset; i < dstsEndOffset; i++) {
/*  890 */       ByteBuffer dst = dsts[i];
/*  891 */       if (dst == null) {
/*  892 */         throw new IllegalArgumentException("dsts[" + i + "] is null");
/*      */       }
/*  894 */       if (dst.isReadOnly()) {
/*  895 */         throw new ReadOnlyBufferException();
/*      */       }
/*  897 */       capacity += dst.remaining();
/*      */     } 
/*      */     
/*  900 */     int srcsEndOffset = srcsOffset + srcsLength;
/*  901 */     long len = 0L;
/*  902 */     for (int j = srcsOffset; j < srcsEndOffset; j++) {
/*  903 */       ByteBuffer src = srcs[j];
/*  904 */       if (src == null) {
/*  905 */         throw new IllegalArgumentException("srcs[" + j + "] is null");
/*      */       }
/*  907 */       len += src.remaining();
/*      */     } 
/*      */     
/*  910 */     synchronized (this) {
/*  911 */       int packetLength; if (isInboundDone()) {
/*  912 */         return (isOutboundDone() || isDestroyed()) ? CLOSED_NOT_HANDSHAKING : NEED_WRAP_CLOSED;
/*      */       }
/*      */       
/*  915 */       SSLEngineResult.HandshakeStatus status = SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */       
/*  917 */       if (this.handshakeState != HandshakeState.FINISHED) {
/*  918 */         if (this.handshakeState != HandshakeState.STARTED_EXPLICITLY)
/*      */         {
/*  920 */           this.handshakeState = HandshakeState.STARTED_IMPLICITLY;
/*      */         }
/*      */         
/*  923 */         status = handshake();
/*  924 */         if (status == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
/*  925 */           return NEED_WRAP_OK;
/*      */         }
/*      */         
/*  928 */         if (this.isInboundDone) {
/*  929 */           return NEED_WRAP_CLOSED;
/*      */         }
/*      */       } 
/*      */       
/*  933 */       int sslPending = sslPending0();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  939 */       if (this.jdkCompatibilityMode) {
/*  940 */         if (len < 5L) {
/*  941 */           return newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_UNDERFLOW, status, 0, 0);
/*      */         }
/*      */         
/*  944 */         packetLength = SslUtils.getEncryptedPacketLength(srcs, srcsOffset);
/*  945 */         if (packetLength == -2) {
/*  946 */           throw new NotSslRecordException("not an SSL/TLS record");
/*      */         }
/*      */         
/*  949 */         int packetLengthDataOnly = packetLength - 5;
/*  950 */         if (packetLengthDataOnly > capacity) {
/*      */ 
/*      */           
/*  953 */           if (packetLengthDataOnly > MAX_RECORD_SIZE)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  959 */             throw new SSLException("Illegal packet length: " + packetLengthDataOnly + " > " + this.session
/*  960 */                 .getApplicationBufferSize());
/*      */           }
/*  962 */           this.session.tryExpandApplicationBufferSize(packetLengthDataOnly);
/*      */           
/*  964 */           return newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_OVERFLOW, status, 0, 0);
/*      */         } 
/*      */         
/*  967 */         if (len < packetLength)
/*      */         {
/*      */           
/*  970 */           return newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_UNDERFLOW, status, 0, 0); } 
/*      */       } else {
/*  972 */         if (len == 0L && sslPending <= 0)
/*  973 */           return newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_UNDERFLOW, status, 0, 0); 
/*  974 */         if (capacity == 0L) {
/*  975 */           return newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_OVERFLOW, status, 0, 0);
/*      */         }
/*  977 */         packetLength = (int)Math.min(2147483647L, len);
/*      */       } 
/*      */ 
/*      */       
/*  981 */       assert srcsOffset < srcsEndOffset;
/*      */ 
/*      */       
/*  984 */       assert capacity > 0L;
/*      */ 
/*      */       
/*  987 */       int bytesProduced = 0;
/*  988 */       int bytesConsumed = 0; try {
/*      */         while (true) {
/*      */           ByteBuf bioWriteCopyBuf;
/*      */           int pendingEncryptedBytes;
/*  992 */           ByteBuffer src = srcs[srcsOffset];
/*  993 */           int remaining = src.remaining();
/*      */ 
/*      */           
/*  996 */           if (remaining == 0) {
/*  997 */             if (sslPending <= 0) {
/*      */ 
/*      */               
/* 1000 */               if (++srcsOffset >= srcsEndOffset) {
/*      */                 break;
/*      */               }
/*      */               continue;
/*      */             } 
/* 1005 */             bioWriteCopyBuf = null;
/* 1006 */             pendingEncryptedBytes = SSL.bioLengthByteBuffer(this.networkBIO);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 1011 */             pendingEncryptedBytes = Math.min(packetLength, remaining);
/* 1012 */             bioWriteCopyBuf = writeEncryptedData(src, pendingEncryptedBytes);
/*      */           } 
/*      */           
/*      */           while (true) {
/* 1016 */             ByteBuffer dst = dsts[dstsOffset];
/* 1017 */             if (!dst.hasRemaining())
/*      */             
/* 1019 */             { if (++dstsOffset >= dstsEndOffset)
/*      */               
/*      */               { 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 1076 */                 if (bioWriteCopyBuf != null)
/* 1077 */                   bioWriteCopyBuf.release();  break; }  continue; }  int bytesRead = readPlaintextData(dst); int localBytesConsumed = pendingEncryptedBytes - SSL.bioLengthByteBuffer(this.networkBIO); bytesConsumed += localBytesConsumed; packetLength -= localBytesConsumed; pendingEncryptedBytes -= localBytesConsumed; src.position(src.position() + localBytesConsumed); if (bytesRead > 0) { bytesProduced += bytesRead; if (!dst.hasRemaining()) { sslPending = sslPending0(); if (++dstsOffset >= dstsEndOffset) { SSLEngineResult sSLEngineResult1 = (sslPending > 0) ? newResult(SSLEngineResult.Status.BUFFER_OVERFLOW, status, bytesConsumed, bytesProduced) : newResultMayFinishHandshake(isInboundDone() ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK, status, bytesConsumed, bytesProduced); if (bioWriteCopyBuf != null) bioWriteCopyBuf.release();  return sSLEngineResult1; }  continue; }  if (packetLength == 0 || this.jdkCompatibilityMode) { if (bioWriteCopyBuf != null) bioWriteCopyBuf.release();  break; }  continue; }  int sslError = SSL.getError(this.ssl, bytesRead); if (sslError == SSL.SSL_ERROR_WANT_READ || sslError == SSL.SSL_ERROR_WANT_WRITE) { if (++srcsOffset >= srcsEndOffset) { if (bioWriteCopyBuf != null) bioWriteCopyBuf.release();  break; }  if (bioWriteCopyBuf != null) bioWriteCopyBuf.release();  continue; }  if (sslError == SSL.SSL_ERROR_ZERO_RETURN) { if (!this.receivedShutdown) closeAll();  SSLEngineResult sSLEngineResult1 = newResultMayFinishHandshake(isInboundDone() ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK, status, bytesConsumed, bytesProduced); if (bioWriteCopyBuf != null) bioWriteCopyBuf.release();  return sSLEngineResult1; }  SSLEngineResult sSLEngineResult = sslReadErrorResult(SSL.getLastErrorNumber(), bytesConsumed, bytesProduced); if (bioWriteCopyBuf != null) bioWriteCopyBuf.release();  return sSLEngineResult;
/*      */           } 
/*      */           break;
/*      */         } 
/*      */       } finally {
/* 1082 */         SSL.bioClearByteBuffer(this.networkBIO);
/* 1083 */         rejectRemoteInitiatedRenegotiation();
/*      */       } 
/*      */ 
/*      */       
/* 1087 */       if (!this.receivedShutdown && (SSL.getShutdown(this.ssl) & SSL.SSL_RECEIVED_SHUTDOWN) == SSL.SSL_RECEIVED_SHUTDOWN) {
/* 1088 */         closeAll();
/*      */       }
/*      */       
/* 1091 */       return newResultMayFinishHandshake(isInboundDone() ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK, status, bytesConsumed, bytesProduced);
/*      */     } 
/*      */   }
/*      */   
/*      */   private SSLEngineResult sslReadErrorResult(int err, int bytesConsumed, int bytesProduced) throws SSLException {
/* 1096 */     String errStr = SSL.getErrorString(err);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1102 */     if (SSL.bioLengthNonApplication(this.networkBIO) > 0) {
/* 1103 */       if (this.handshakeException == null && this.handshakeState != HandshakeState.FINISHED)
/*      */       {
/*      */         
/* 1106 */         this.handshakeException = new SSLHandshakeException(errStr);
/*      */       }
/* 1108 */       return new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_WRAP, bytesConsumed, bytesProduced);
/*      */     } 
/* 1110 */     throw shutdownWithError("SSL_read", errStr);
/*      */   }
/*      */   
/*      */   private void closeAll() throws SSLException {
/* 1114 */     this.receivedShutdown = true;
/* 1115 */     closeOutbound();
/* 1116 */     closeInbound();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void rejectRemoteInitiatedRenegotiation() throws SSLHandshakeException {
/* 1123 */     if (!isDestroyed() && SSL.getHandshakeCount(this.ssl) > 1) {
/*      */ 
/*      */       
/* 1126 */       shutdown();
/* 1127 */       throw new SSLHandshakeException("remote-initiated renegotiation not allowed");
/*      */     } 
/*      */   }
/*      */   
/*      */   public final SSLEngineResult unwrap(ByteBuffer[] srcs, ByteBuffer[] dsts) throws SSLException {
/* 1132 */     return unwrap(srcs, 0, srcs.length, dsts, 0, dsts.length);
/*      */   }
/*      */   
/*      */   private ByteBuffer[] singleSrcBuffer(ByteBuffer src) {
/* 1136 */     this.singleSrcBuffer[0] = src;
/* 1137 */     return this.singleSrcBuffer;
/*      */   }
/*      */   
/*      */   private void resetSingleSrcBuffer() {
/* 1141 */     this.singleSrcBuffer[0] = null;
/*      */   }
/*      */   
/*      */   private ByteBuffer[] singleDstBuffer(ByteBuffer src) {
/* 1145 */     this.singleDstBuffer[0] = src;
/* 1146 */     return this.singleDstBuffer;
/*      */   }
/*      */   
/*      */   private void resetSingleDstBuffer() {
/* 1150 */     this.singleDstBuffer[0] = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized SSLEngineResult unwrap(ByteBuffer src, ByteBuffer[] dsts, int offset, int length) throws SSLException {
/*      */     try {
/* 1157 */       return unwrap(singleSrcBuffer(src), 0, 1, dsts, offset, length);
/*      */     } finally {
/* 1159 */       resetSingleSrcBuffer();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized SSLEngineResult wrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
/*      */     try {
/* 1166 */       return wrap(singleSrcBuffer(src), dst);
/*      */     } finally {
/* 1168 */       resetSingleSrcBuffer();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized SSLEngineResult unwrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
/*      */     try {
/* 1175 */       return unwrap(singleSrcBuffer(src), singleDstBuffer(dst));
/*      */     } finally {
/* 1177 */       resetSingleSrcBuffer();
/* 1178 */       resetSingleDstBuffer();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized SSLEngineResult unwrap(ByteBuffer src, ByteBuffer[] dsts) throws SSLException {
/*      */     try {
/* 1185 */       return unwrap(singleSrcBuffer(src), dsts);
/*      */     } finally {
/* 1187 */       resetSingleSrcBuffer();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Runnable getDelegatedTask() {
/* 1195 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized void closeInbound() throws SSLException {
/* 1200 */     if (this.isInboundDone) {
/*      */       return;
/*      */     }
/*      */     
/* 1204 */     this.isInboundDone = true;
/*      */     
/* 1206 */     if (isOutboundDone())
/*      */     {
/*      */       
/* 1209 */       shutdown();
/*      */     }
/*      */     
/* 1212 */     if (this.handshakeState != HandshakeState.NOT_STARTED && !this.receivedShutdown) {
/* 1213 */       throw new SSLException("Inbound closed before receiving peer's close_notify: possible truncation attack?");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized boolean isInboundDone() {
/* 1220 */     return this.isInboundDone;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized void closeOutbound() {
/* 1225 */     if (this.outboundClosed) {
/*      */       return;
/*      */     }
/*      */     
/* 1229 */     this.outboundClosed = true;
/*      */     
/* 1231 */     if (this.handshakeState != HandshakeState.NOT_STARTED && !isDestroyed()) {
/* 1232 */       int mode = SSL.getShutdown(this.ssl);
/* 1233 */       if ((mode & SSL.SSL_SENT_SHUTDOWN) != SSL.SSL_SENT_SHUTDOWN) {
/* 1234 */         doSSLShutdown();
/*      */       }
/*      */     } else {
/*      */       
/* 1238 */       shutdown();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean doSSLShutdown() {
/* 1247 */     if (SSL.isInInit(this.ssl) != 0)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 1252 */       return false;
/*      */     }
/* 1254 */     int err = SSL.shutdownSSL(this.ssl);
/* 1255 */     if (err < 0) {
/* 1256 */       int sslErr = SSL.getError(this.ssl, err);
/* 1257 */       if (sslErr == SSL.SSL_ERROR_SYSCALL || sslErr == SSL.SSL_ERROR_SSL) {
/* 1258 */         if (logger.isDebugEnabled()) {
/* 1259 */           logger.debug("SSL_shutdown failed: OpenSSL error: {}", SSL.getLastError());
/*      */         }
/*      */         
/* 1262 */         shutdown();
/* 1263 */         return false;
/*      */       } 
/* 1265 */       SSL.clearError();
/*      */     } 
/* 1267 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized boolean isOutboundDone() {
/* 1274 */     return (this.outboundClosed && (this.networkBIO == 0L || SSL.bioLengthNonApplication(this.networkBIO) == 0));
/*      */   }
/*      */ 
/*      */   
/*      */   public final String[] getSupportedCipherSuites() {
/* 1279 */     return OpenSsl.AVAILABLE_CIPHER_SUITES.<String>toArray(new String[OpenSsl.AVAILABLE_CIPHER_SUITES.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public final String[] getEnabledCipherSuites() {
/*      */     String[] enabled;
/* 1285 */     synchronized (this) {
/* 1286 */       if (!isDestroyed()) {
/* 1287 */         enabled = SSL.getCiphers(this.ssl);
/*      */       } else {
/* 1289 */         return EmptyArrays.EMPTY_STRINGS;
/*      */       } 
/*      */     } 
/* 1292 */     if (enabled == null) {
/* 1293 */       return EmptyArrays.EMPTY_STRINGS;
/*      */     }
/* 1295 */     synchronized (this) {
/* 1296 */       for (int i = 0; i < enabled.length; i++) {
/* 1297 */         String mapped = toJavaCipherSuite(enabled[i]);
/* 1298 */         if (mapped != null) {
/* 1299 */           enabled[i] = mapped;
/*      */         }
/*      */       } 
/*      */     } 
/* 1303 */     return enabled;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setEnabledCipherSuites(String[] cipherSuites) {
/* 1309 */     ObjectUtil.checkNotNull(cipherSuites, "cipherSuites");
/*      */     
/* 1311 */     StringBuilder buf = new StringBuilder();
/* 1312 */     for (String c : cipherSuites) {
/* 1313 */       if (c == null) {
/*      */         break;
/*      */       }
/*      */       
/* 1317 */       String converted = CipherSuiteConverter.toOpenSsl(c);
/* 1318 */       if (converted == null) {
/* 1319 */         converted = c;
/*      */       }
/*      */       
/* 1322 */       if (!OpenSsl.isCipherSuiteAvailable(converted)) {
/* 1323 */         throw new IllegalArgumentException("unsupported cipher suite: " + c + '(' + converted + ')');
/*      */       }
/*      */       
/* 1326 */       buf.append(converted);
/* 1327 */       buf.append(':');
/*      */     } 
/*      */     
/* 1330 */     if (buf.length() == 0) {
/* 1331 */       throw new IllegalArgumentException("empty cipher suites");
/*      */     }
/* 1333 */     buf.setLength(buf.length() - 1);
/*      */     
/* 1335 */     String cipherSuiteSpec = buf.toString();
/*      */     
/* 1337 */     synchronized (this) {
/* 1338 */       if (!isDestroyed()) {
/*      */         try {
/* 1340 */           SSL.setCipherSuites(this.ssl, cipherSuiteSpec);
/* 1341 */         } catch (Exception e) {
/* 1342 */           throw new IllegalStateException("failed to enable cipher suites: " + cipherSuiteSpec, e);
/*      */         } 
/*      */       } else {
/* 1345 */         throw new IllegalStateException("failed to enable cipher suites: " + cipherSuiteSpec);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final String[] getSupportedProtocols() {
/* 1352 */     return OpenSsl.SUPPORTED_PROTOCOLS_SET.<String>toArray(new String[OpenSsl.SUPPORTED_PROTOCOLS_SET.size()]);
/*      */   }
/*      */   
/*      */   public final String[] getEnabledProtocols() {
/*      */     int opts;
/* 1357 */     List<String> enabled = new ArrayList<String>(6);
/*      */     
/* 1359 */     enabled.add("SSLv2Hello");
/*      */ 
/*      */     
/* 1362 */     synchronized (this) {
/* 1363 */       if (!isDestroyed()) {
/* 1364 */         opts = SSL.getOptions(this.ssl);
/*      */       } else {
/* 1366 */         return enabled.<String>toArray(new String[1]);
/*      */       } 
/*      */     } 
/* 1369 */     if (isProtocolEnabled(opts, SSL.SSL_OP_NO_TLSv1, "TLSv1")) {
/* 1370 */       enabled.add("TLSv1");
/*      */     }
/* 1372 */     if (isProtocolEnabled(opts, SSL.SSL_OP_NO_TLSv1_1, "TLSv1.1")) {
/* 1373 */       enabled.add("TLSv1.1");
/*      */     }
/* 1375 */     if (isProtocolEnabled(opts, SSL.SSL_OP_NO_TLSv1_2, "TLSv1.2")) {
/* 1376 */       enabled.add("TLSv1.2");
/*      */     }
/* 1378 */     if (isProtocolEnabled(opts, SSL.SSL_OP_NO_SSLv2, "SSLv2")) {
/* 1379 */       enabled.add("SSLv2");
/*      */     }
/* 1381 */     if (isProtocolEnabled(opts, SSL.SSL_OP_NO_SSLv3, "SSLv3")) {
/* 1382 */       enabled.add("SSLv3");
/*      */     }
/* 1384 */     return enabled.<String>toArray(new String[enabled.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isProtocolEnabled(int opts, int disableMask, String protocolString) {
/* 1390 */     return ((opts & disableMask) == 0 && OpenSsl.SUPPORTED_PROTOCOLS_SET.contains(protocolString));
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
/*      */   public final void setEnabledProtocols(String[] protocols) {
/* 1404 */     if (protocols == null)
/*      */     {
/* 1406 */       throw new IllegalArgumentException();
/*      */     }
/* 1408 */     int minProtocolIndex = OPENSSL_OP_NO_PROTOCOLS.length;
/* 1409 */     int maxProtocolIndex = 0;
/* 1410 */     for (String p : protocols) {
/* 1411 */       if (!OpenSsl.SUPPORTED_PROTOCOLS_SET.contains(p)) {
/* 1412 */         throw new IllegalArgumentException("Protocol " + p + " is not supported.");
/*      */       }
/* 1414 */       if (p.equals("SSLv2")) {
/* 1415 */         if (minProtocolIndex > 0) {
/* 1416 */           minProtocolIndex = 0;
/*      */         }
/* 1418 */         if (maxProtocolIndex < 0) {
/* 1419 */           maxProtocolIndex = 0;
/*      */         }
/* 1421 */       } else if (p.equals("SSLv3")) {
/* 1422 */         if (minProtocolIndex > 1) {
/* 1423 */           minProtocolIndex = 1;
/*      */         }
/* 1425 */         if (maxProtocolIndex < 1) {
/* 1426 */           maxProtocolIndex = 1;
/*      */         }
/* 1428 */       } else if (p.equals("TLSv1")) {
/* 1429 */         if (minProtocolIndex > 2) {
/* 1430 */           minProtocolIndex = 2;
/*      */         }
/* 1432 */         if (maxProtocolIndex < 2) {
/* 1433 */           maxProtocolIndex = 2;
/*      */         }
/* 1435 */       } else if (p.equals("TLSv1.1")) {
/* 1436 */         if (minProtocolIndex > 3) {
/* 1437 */           minProtocolIndex = 3;
/*      */         }
/* 1439 */         if (maxProtocolIndex < 3) {
/* 1440 */           maxProtocolIndex = 3;
/*      */         }
/* 1442 */       } else if (p.equals("TLSv1.2")) {
/* 1443 */         if (minProtocolIndex > 4) {
/* 1444 */           minProtocolIndex = 4;
/*      */         }
/* 1446 */         if (maxProtocolIndex < 4) {
/* 1447 */           maxProtocolIndex = 4;
/*      */         }
/*      */       } 
/*      */     } 
/* 1451 */     synchronized (this) {
/* 1452 */       if (!isDestroyed()) {
/*      */         
/* 1454 */         SSL.clearOptions(this.ssl, SSL.SSL_OP_NO_SSLv2 | SSL.SSL_OP_NO_SSLv3 | SSL.SSL_OP_NO_TLSv1 | SSL.SSL_OP_NO_TLSv1_1 | SSL.SSL_OP_NO_TLSv1_2);
/*      */ 
/*      */         
/* 1457 */         int opts = 0; int i;
/* 1458 */         for (i = 0; i < minProtocolIndex; i++) {
/* 1459 */           opts |= OPENSSL_OP_NO_PROTOCOLS[i];
/*      */         }
/* 1461 */         assert maxProtocolIndex != Integer.MAX_VALUE;
/* 1462 */         for (i = maxProtocolIndex + 1; i < OPENSSL_OP_NO_PROTOCOLS.length; i++) {
/* 1463 */           opts |= OPENSSL_OP_NO_PROTOCOLS[i];
/*      */         }
/*      */ 
/*      */         
/* 1467 */         SSL.setOptions(this.ssl, opts);
/*      */       } else {
/* 1469 */         throw new IllegalStateException("failed to enable protocols: " + Arrays.asList(protocols));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final SSLSession getSession() {
/* 1476 */     return this.session;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized void beginHandshake() throws SSLException {
/* 1481 */     switch (this.handshakeState) {
/*      */       case NPN:
/* 1483 */         checkEngineClosed(BEGIN_HANDSHAKE_ENGINE_CLOSED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1491 */         this.handshakeState = HandshakeState.STARTED_EXPLICITLY;
/* 1492 */         calculateMaxWrapOverhead();
/*      */ 
/*      */       
/*      */       case NPN_AND_ALPN:
/*      */         return;
/*      */       
/*      */       case ALPN:
/* 1499 */         throw RENEGOTIATION_UNSUPPORTED;
/*      */       case NONE:
/* 1501 */         this.handshakeState = HandshakeState.STARTED_EXPLICITLY;
/* 1502 */         handshake();
/* 1503 */         calculateMaxWrapOverhead();
/*      */     } 
/*      */     
/* 1506 */     throw new Error();
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkEngineClosed(SSLException cause) throws SSLException {
/* 1511 */     if (isDestroyed()) {
/* 1512 */       throw cause;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static SSLEngineResult.HandshakeStatus pendingStatus(int pendingStatus) {
/* 1518 */     return (pendingStatus > 0) ? SSLEngineResult.HandshakeStatus.NEED_WRAP : SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
/*      */   }
/*      */   
/*      */   private static boolean isEmpty(Object[] arr) {
/* 1522 */     return (arr == null || arr.length == 0);
/*      */   }
/*      */   
/*      */   private static boolean isEmpty(byte[] cert) {
/* 1526 */     return (cert == null || cert.length == 0);
/*      */   }
/*      */   
/*      */   private SSLEngineResult.HandshakeStatus handshake() throws SSLException {
/* 1530 */     if (this.handshakeState == HandshakeState.FINISHED) {
/* 1531 */       return SSLEngineResult.HandshakeStatus.FINISHED;
/*      */     }
/* 1533 */     checkEngineClosed(HANDSHAKE_ENGINE_CLOSED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1539 */     SSLHandshakeException exception = this.handshakeException;
/* 1540 */     if (exception != null) {
/* 1541 */       if (SSL.bioLengthNonApplication(this.networkBIO) > 0)
/*      */       {
/* 1543 */         return SSLEngineResult.HandshakeStatus.NEED_WRAP;
/*      */       }
/*      */ 
/*      */       
/* 1547 */       this.handshakeException = null;
/* 1548 */       shutdown();
/* 1549 */       throw exception;
/*      */     } 
/*      */ 
/*      */     
/* 1553 */     this.engineMap.add(this);
/* 1554 */     if (this.lastAccessed == -1L) {
/* 1555 */       this.lastAccessed = System.currentTimeMillis();
/*      */     }
/*      */     
/* 1558 */     if (!this.certificateSet && this.keyMaterialManager != null) {
/* 1559 */       this.certificateSet = true;
/* 1560 */       this.keyMaterialManager.setKeyMaterial(this);
/*      */     } 
/*      */     
/* 1563 */     int code = SSL.doHandshake(this.ssl);
/* 1564 */     if (code <= 0) {
/*      */ 
/*      */       
/* 1567 */       if (this.handshakeException != null) {
/* 1568 */         exception = this.handshakeException;
/* 1569 */         this.handshakeException = null;
/* 1570 */         shutdown();
/* 1571 */         throw exception;
/*      */       } 
/*      */       
/* 1574 */       int sslError = SSL.getError(this.ssl, code);
/* 1575 */       if (sslError == SSL.SSL_ERROR_WANT_READ || sslError == SSL.SSL_ERROR_WANT_WRITE) {
/* 1576 */         return pendingStatus(SSL.bioLengthNonApplication(this.networkBIO));
/*      */       }
/*      */       
/* 1579 */       throw shutdownWithError("SSL_do_handshake");
/*      */     } 
/*      */ 
/*      */     
/* 1583 */     this.session.handshakeFinished();
/* 1584 */     this.engineMap.remove(this.ssl);
/* 1585 */     return SSLEngineResult.HandshakeStatus.FINISHED;
/*      */   }
/*      */ 
/*      */   
/*      */   private SSLEngineResult.HandshakeStatus mayFinishHandshake(SSLEngineResult.HandshakeStatus status) throws SSLException {
/* 1590 */     if (status == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING && this.handshakeState != HandshakeState.FINISHED)
/*      */     {
/*      */       
/* 1593 */       return handshake();
/*      */     }
/* 1595 */     return status;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized SSLEngineResult.HandshakeStatus getHandshakeStatus() {
/* 1601 */     return needPendingStatus() ? pendingStatus(SSL.bioLengthNonApplication(this.networkBIO)) : SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */   }
/*      */ 
/*      */   
/*      */   private SSLEngineResult.HandshakeStatus getHandshakeStatus(int pending) {
/* 1606 */     return needPendingStatus() ? pendingStatus(pending) : SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */   }
/*      */   
/*      */   private boolean needPendingStatus() {
/* 1610 */     return (this.handshakeState != HandshakeState.NOT_STARTED && !isDestroyed() && (this.handshakeState != HandshakeState.FINISHED || 
/* 1611 */       isInboundDone() || isOutboundDone()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String toJavaCipherSuite(String openSslCipherSuite) {
/* 1618 */     if (openSslCipherSuite == null) {
/* 1619 */       return null;
/*      */     }
/*      */     
/* 1622 */     String prefix = toJavaCipherSuitePrefix(SSL.getVersion(this.ssl));
/* 1623 */     return CipherSuiteConverter.toJava(openSslCipherSuite, prefix);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String toJavaCipherSuitePrefix(String protocolVersion) {
/*      */     char c;
/* 1631 */     if (protocolVersion == null || protocolVersion.isEmpty()) {
/* 1632 */       c = Character.MIN_VALUE;
/*      */     } else {
/* 1634 */       c = protocolVersion.charAt(0);
/*      */     } 
/*      */     
/* 1637 */     switch (c) {
/*      */       case 'T':
/* 1639 */         return "TLS";
/*      */       case 'S':
/* 1641 */         return "SSL";
/*      */     } 
/* 1643 */     return "UNKNOWN";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setUseClientMode(boolean clientMode) {
/* 1649 */     if (clientMode != this.clientMode) {
/* 1650 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean getUseClientMode() {
/* 1656 */     return this.clientMode;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setNeedClientAuth(boolean b) {
/* 1661 */     setClientAuth(b ? ClientAuth.REQUIRE : ClientAuth.NONE);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean getNeedClientAuth() {
/* 1666 */     return (this.clientAuth == ClientAuth.REQUIRE);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setWantClientAuth(boolean b) {
/* 1671 */     setClientAuth(b ? ClientAuth.OPTIONAL : ClientAuth.NONE);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean getWantClientAuth() {
/* 1676 */     return (this.clientAuth == ClientAuth.OPTIONAL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized void setVerify(int verifyMode, int depth) {
/* 1685 */     SSL.setVerify(this.ssl, verifyMode, depth);
/*      */   }
/*      */   
/*      */   private void setClientAuth(ClientAuth mode) {
/* 1689 */     if (this.clientMode) {
/*      */       return;
/*      */     }
/* 1692 */     synchronized (this) {
/* 1693 */       if (this.clientAuth == mode) {
/*      */         return;
/*      */       }
/*      */       
/* 1697 */       switch (mode) {
/*      */         case NONE:
/* 1699 */           SSL.setVerify(this.ssl, 0, 10);
/*      */           break;
/*      */         case ALPN:
/* 1702 */           SSL.setVerify(this.ssl, 2, 10);
/*      */           break;
/*      */         case NPN:
/* 1705 */           SSL.setVerify(this.ssl, 1, 10);
/*      */           break;
/*      */         default:
/* 1708 */           throw new Error(mode.toString());
/*      */       } 
/* 1710 */       this.clientAuth = mode;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setEnableSessionCreation(boolean b) {
/* 1716 */     if (b) {
/* 1717 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean getEnableSessionCreation() {
/* 1723 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized SSLParameters getSSLParameters() {
/* 1728 */     SSLParameters sslParameters = super.getSSLParameters();
/*      */     
/* 1730 */     int version = PlatformDependent.javaVersion();
/* 1731 */     if (version >= 7) {
/* 1732 */       sslParameters.setEndpointIdentificationAlgorithm(this.endPointIdentificationAlgorithm);
/* 1733 */       Java7SslParametersUtils.setAlgorithmConstraints(sslParameters, this.algorithmConstraints);
/* 1734 */       if (version >= 8) {
/* 1735 */         if (this.sniHostNames != null) {
/* 1736 */           Java8SslUtils.setSniHostNames(sslParameters, this.sniHostNames);
/*      */         }
/* 1738 */         if (!isDestroyed()) {
/* 1739 */           Java8SslUtils.setUseCipherSuitesOrder(sslParameters, 
/* 1740 */               ((SSL.getOptions(this.ssl) & SSL.SSL_OP_CIPHER_SERVER_PREFERENCE) != 0));
/*      */         }
/*      */         
/* 1743 */         Java8SslUtils.setSNIMatchers(sslParameters, this.matchers);
/*      */       } 
/*      */     } 
/* 1746 */     return sslParameters;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized void setSSLParameters(SSLParameters sslParameters) {
/* 1751 */     int version = PlatformDependent.javaVersion();
/* 1752 */     if (version >= 7) {
/* 1753 */       if (sslParameters.getAlgorithmConstraints() != null) {
/* 1754 */         throw new IllegalArgumentException("AlgorithmConstraints are not supported.");
/*      */       }
/*      */       
/* 1757 */       if (version >= 8) {
/* 1758 */         if (!isDestroyed()) {
/* 1759 */           if (this.clientMode) {
/* 1760 */             List<String> sniHostNames = Java8SslUtils.getSniHostNames(sslParameters);
/* 1761 */             for (String name : sniHostNames) {
/* 1762 */               SSL.setTlsExtHostName(this.ssl, name);
/*      */             }
/* 1764 */             this.sniHostNames = sniHostNames;
/*      */           } 
/* 1766 */           if (Java8SslUtils.getUseCipherSuitesOrder(sslParameters)) {
/* 1767 */             SSL.setOptions(this.ssl, SSL.SSL_OP_CIPHER_SERVER_PREFERENCE);
/*      */           } else {
/* 1769 */             SSL.clearOptions(this.ssl, SSL.SSL_OP_CIPHER_SERVER_PREFERENCE);
/*      */           } 
/*      */         } 
/* 1772 */         this.matchers = sslParameters.getSNIMatchers();
/*      */       } 
/*      */       
/* 1775 */       String endPointIdentificationAlgorithm = sslParameters.getEndpointIdentificationAlgorithm();
/*      */       
/* 1777 */       boolean endPointVerificationEnabled = (endPointIdentificationAlgorithm != null && !endPointIdentificationAlgorithm.isEmpty());
/* 1778 */       SSL.setHostNameValidation(this.ssl, 0, endPointVerificationEnabled ? 
/* 1779 */           getPeerHost() : null);
/*      */ 
/*      */       
/* 1782 */       if (this.clientMode && endPointVerificationEnabled) {
/* 1783 */         SSL.setVerify(this.ssl, 2, -1);
/*      */       }
/*      */       
/* 1786 */       this.endPointIdentificationAlgorithm = endPointIdentificationAlgorithm;
/* 1787 */       this.algorithmConstraints = sslParameters.getAlgorithmConstraints();
/*      */     } 
/* 1789 */     super.setSSLParameters(sslParameters);
/*      */   }
/*      */   
/*      */   private boolean isDestroyed() {
/* 1793 */     return (this.destroyed != 0);
/*      */   }
/*      */   
/*      */   final boolean checkSniHostnameMatch(String hostname) {
/* 1797 */     return Java8SslUtils.checkSniHostnameMatch(this.matchers, hostname);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getNegotiatedApplicationProtocol() {
/* 1802 */     return this.applicationProtocol;
/*      */   }
/*      */   
/*      */   private static long bufferAddress(ByteBuffer b) {
/* 1806 */     assert b.isDirect();
/* 1807 */     if (PlatformDependent.hasUnsafe()) {
/* 1808 */       return PlatformDependent.directBufferAddress(b);
/*      */     }
/* 1810 */     return Buffer.address(b);
/*      */   }
/*      */ 
/*      */   
/*      */   private final class OpenSslSession
/*      */     implements SSLSession
/*      */   {
/*      */     private final OpenSslSessionContext sessionContext;
/*      */     private X509Certificate[] x509PeerCerts;
/*      */     private Certificate[] peerCerts;
/*      */     private String protocol;
/*      */     private String cipher;
/*      */     private byte[] id;
/*      */     private long creationTime;
/* 1824 */     private volatile int applicationBufferSize = ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH;
/*      */     
/*      */     private Map<String, Object> values;
/*      */ 
/*      */     
/*      */     OpenSslSession(OpenSslSessionContext sessionContext) {
/* 1830 */       this.sessionContext = sessionContext;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] getId() {
/* 1835 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 1836 */         if (this.id == null) {
/* 1837 */           return EmptyArrays.EMPTY_BYTES;
/*      */         }
/* 1839 */         return (byte[])this.id.clone();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SSLSessionContext getSessionContext() {
/* 1845 */       return this.sessionContext;
/*      */     }
/*      */ 
/*      */     
/*      */     public long getCreationTime() {
/* 1850 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 1851 */         if (this.creationTime == 0L && !ReferenceCountedOpenSslEngine.this.isDestroyed()) {
/* 1852 */           this.creationTime = SSL.getTime(ReferenceCountedOpenSslEngine.this.ssl) * 1000L;
/*      */         }
/*      */       } 
/* 1855 */       return this.creationTime;
/*      */     }
/*      */ 
/*      */     
/*      */     public long getLastAccessedTime() {
/* 1860 */       long lastAccessed = ReferenceCountedOpenSslEngine.this.lastAccessed;
/*      */       
/* 1862 */       return (lastAccessed == -1L) ? getCreationTime() : lastAccessed;
/*      */     }
/*      */ 
/*      */     
/*      */     public void invalidate() {
/* 1867 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 1868 */         if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
/* 1869 */           SSL.setTimeout(ReferenceCountedOpenSslEngine.this.ssl, 0L);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isValid() {
/* 1876 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 1877 */         if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
/* 1878 */           return (System.currentTimeMillis() - SSL.getTimeout(ReferenceCountedOpenSslEngine.this.ssl) * 1000L < SSL.getTime(ReferenceCountedOpenSslEngine.this.ssl) * 1000L);
/*      */         }
/*      */       } 
/* 1881 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void putValue(String name, Object value) {
/* 1886 */       if (name == null) {
/* 1887 */         throw new NullPointerException("name");
/*      */       }
/* 1889 */       if (value == null) {
/* 1890 */         throw new NullPointerException("value");
/*      */       }
/* 1892 */       Map<String, Object> values = this.values;
/* 1893 */       if (values == null)
/*      */       {
/* 1895 */         values = this.values = new HashMap<String, Object>(2);
/*      */       }
/* 1897 */       Object old = values.put(name, value);
/* 1898 */       if (value instanceof SSLSessionBindingListener) {
/* 1899 */         ((SSLSessionBindingListener)value).valueBound(new SSLSessionBindingEvent(this, name));
/*      */       }
/* 1901 */       notifyUnbound(old, name);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getValue(String name) {
/* 1906 */       if (name == null) {
/* 1907 */         throw new NullPointerException("name");
/*      */       }
/* 1909 */       if (this.values == null) {
/* 1910 */         return null;
/*      */       }
/* 1912 */       return this.values.get(name);
/*      */     }
/*      */ 
/*      */     
/*      */     public void removeValue(String name) {
/* 1917 */       if (name == null) {
/* 1918 */         throw new NullPointerException("name");
/*      */       }
/* 1920 */       Map<String, Object> values = this.values;
/* 1921 */       if (values == null) {
/*      */         return;
/*      */       }
/* 1924 */       Object old = values.remove(name);
/* 1925 */       notifyUnbound(old, name);
/*      */     }
/*      */ 
/*      */     
/*      */     public String[] getValueNames() {
/* 1930 */       Map<String, Object> values = this.values;
/* 1931 */       if (values == null || values.isEmpty()) {
/* 1932 */         return EmptyArrays.EMPTY_STRINGS;
/*      */       }
/* 1934 */       return (String[])values.keySet().toArray((Object[])new String[values.size()]);
/*      */     }
/*      */     
/*      */     private void notifyUnbound(Object value, String name) {
/* 1938 */       if (value instanceof SSLSessionBindingListener) {
/* 1939 */         ((SSLSessionBindingListener)value).valueUnbound(new SSLSessionBindingEvent(this, name));
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void handshakeFinished() throws SSLException {
/* 1948 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 1949 */         if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
/* 1950 */           this.id = SSL.getSessionId(ReferenceCountedOpenSslEngine.this.ssl);
/* 1951 */           this.cipher = ReferenceCountedOpenSslEngine.this.toJavaCipherSuite(SSL.getCipherForSSL(ReferenceCountedOpenSslEngine.this.ssl));
/* 1952 */           this.protocol = SSL.getVersion(ReferenceCountedOpenSslEngine.this.ssl);
/*      */           
/* 1954 */           initPeerCerts();
/* 1955 */           selectApplicationProtocol();
/* 1956 */           ReferenceCountedOpenSslEngine.this.calculateMaxWrapOverhead();
/*      */           
/* 1958 */           ReferenceCountedOpenSslEngine.this.handshakeState = ReferenceCountedOpenSslEngine.HandshakeState.FINISHED;
/*      */         } else {
/* 1960 */           throw new SSLException("Already closed");
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void initPeerCerts() {
/* 1971 */       byte[][] chain = SSL.getPeerCertChain(ReferenceCountedOpenSslEngine.this.ssl);
/* 1972 */       if (ReferenceCountedOpenSslEngine.this.clientMode) {
/* 1973 */         if (ReferenceCountedOpenSslEngine.isEmpty((Object[])chain)) {
/* 1974 */           this.peerCerts = EmptyArrays.EMPTY_CERTIFICATES;
/* 1975 */           this.x509PeerCerts = EmptyArrays.EMPTY_JAVAX_X509_CERTIFICATES;
/*      */         } else {
/* 1977 */           this.peerCerts = new Certificate[chain.length];
/* 1978 */           this.x509PeerCerts = new X509Certificate[chain.length];
/* 1979 */           initCerts(chain, 0);
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1987 */         byte[] clientCert = SSL.getPeerCertificate(ReferenceCountedOpenSslEngine.this.ssl);
/* 1988 */         if (ReferenceCountedOpenSslEngine.isEmpty(clientCert)) {
/* 1989 */           this.peerCerts = EmptyArrays.EMPTY_CERTIFICATES;
/* 1990 */           this.x509PeerCerts = EmptyArrays.EMPTY_JAVAX_X509_CERTIFICATES;
/*      */         }
/* 1992 */         else if (ReferenceCountedOpenSslEngine.isEmpty((Object[])chain)) {
/* 1993 */           this.peerCerts = new Certificate[] { new OpenSslX509Certificate(clientCert) };
/* 1994 */           this.x509PeerCerts = new X509Certificate[] { new OpenSslJavaxX509Certificate(clientCert) };
/*      */         } else {
/* 1996 */           this.peerCerts = new Certificate[chain.length + 1];
/* 1997 */           this.x509PeerCerts = new X509Certificate[chain.length + 1];
/* 1998 */           this.peerCerts[0] = new OpenSslX509Certificate(clientCert);
/* 1999 */           this.x509PeerCerts[0] = new OpenSslJavaxX509Certificate(clientCert);
/* 2000 */           initCerts(chain, 1);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void initCerts(byte[][] chain, int startPos) {
/* 2007 */       for (int i = 0; i < chain.length; i++) {
/* 2008 */         int certPos = startPos + i;
/* 2009 */         this.peerCerts[certPos] = new OpenSslX509Certificate(chain[i]);
/* 2010 */         this.x509PeerCerts[certPos] = new OpenSslJavaxX509Certificate(chain[i]);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void selectApplicationProtocol() throws SSLException {
/*      */       String applicationProtocol;
/* 2018 */       ApplicationProtocolConfig.SelectedListenerFailureBehavior behavior = ReferenceCountedOpenSslEngine.this.apn.selectedListenerFailureBehavior();
/* 2019 */       List<String> protocols = ReferenceCountedOpenSslEngine.this.apn.protocols();
/*      */       
/* 2021 */       switch (ReferenceCountedOpenSslEngine.this.apn.protocol()) {
/*      */         case NONE:
/*      */           return;
/*      */ 
/*      */         
/*      */         case ALPN:
/* 2027 */           applicationProtocol = SSL.getAlpnSelected(ReferenceCountedOpenSslEngine.this.ssl);
/* 2028 */           if (applicationProtocol != null) {
/* 2029 */             ReferenceCountedOpenSslEngine.this.applicationProtocol = selectApplicationProtocol(protocols, behavior, applicationProtocol);
/*      */           }
/*      */ 
/*      */         
/*      */         case NPN:
/* 2034 */           applicationProtocol = SSL.getNextProtoNegotiated(ReferenceCountedOpenSslEngine.this.ssl);
/* 2035 */           if (applicationProtocol != null) {
/* 2036 */             ReferenceCountedOpenSslEngine.this.applicationProtocol = selectApplicationProtocol(protocols, behavior, applicationProtocol);
/*      */           }
/*      */ 
/*      */         
/*      */         case NPN_AND_ALPN:
/* 2041 */           applicationProtocol = SSL.getAlpnSelected(ReferenceCountedOpenSslEngine.this.ssl);
/* 2042 */           if (applicationProtocol == null) {
/* 2043 */             applicationProtocol = SSL.getNextProtoNegotiated(ReferenceCountedOpenSslEngine.this.ssl);
/*      */           }
/* 2045 */           if (applicationProtocol != null) {
/* 2046 */             ReferenceCountedOpenSslEngine.this.applicationProtocol = selectApplicationProtocol(protocols, behavior, applicationProtocol);
/*      */           }
/*      */       } 
/*      */ 
/*      */       
/* 2051 */       throw new Error();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String selectApplicationProtocol(List<String> protocols, ApplicationProtocolConfig.SelectedListenerFailureBehavior behavior, String applicationProtocol) throws SSLException {
/* 2058 */       if (behavior == ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT) {
/* 2059 */         return applicationProtocol;
/*      */       }
/* 2061 */       int size = protocols.size();
/* 2062 */       assert size > 0;
/* 2063 */       if (protocols.contains(applicationProtocol)) {
/* 2064 */         return applicationProtocol;
/*      */       }
/* 2066 */       if (behavior == ApplicationProtocolConfig.SelectedListenerFailureBehavior.CHOOSE_MY_LAST_PROTOCOL) {
/* 2067 */         return protocols.get(size - 1);
/*      */       }
/* 2069 */       throw new SSLException("unknown protocol " + applicationProtocol);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
/* 2077 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 2078 */         if (ReferenceCountedOpenSslEngine.isEmpty((Object[])this.peerCerts)) {
/* 2079 */           throw new SSLPeerUnverifiedException("peer not verified");
/*      */         }
/* 2081 */         return (Certificate[])this.peerCerts.clone();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Certificate[] getLocalCertificates() {
/* 2087 */       if (ReferenceCountedOpenSslEngine.this.localCerts == null) {
/* 2088 */         return null;
/*      */       }
/* 2090 */       return (Certificate[])ReferenceCountedOpenSslEngine.this.localCerts.clone();
/*      */     }
/*      */ 
/*      */     
/*      */     public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
/* 2095 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 2096 */         if (ReferenceCountedOpenSslEngine.isEmpty((Object[])this.x509PeerCerts)) {
/* 2097 */           throw new SSLPeerUnverifiedException("peer not verified");
/*      */         }
/* 2099 */         return (X509Certificate[])this.x509PeerCerts.clone();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
/* 2105 */       Certificate[] peer = getPeerCertificates();
/*      */ 
/*      */       
/* 2108 */       return ((X509Certificate)peer[0]).getSubjectX500Principal();
/*      */     }
/*      */ 
/*      */     
/*      */     public Principal getLocalPrincipal() {
/* 2113 */       Certificate[] local = ReferenceCountedOpenSslEngine.this.localCerts;
/* 2114 */       if (local == null || local.length == 0) {
/* 2115 */         return null;
/*      */       }
/* 2117 */       return ((X509Certificate)local[0]).getIssuerX500Principal();
/*      */     }
/*      */ 
/*      */     
/*      */     public String getCipherSuite() {
/* 2122 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 2123 */         if (this.cipher == null) {
/* 2124 */           return "SSL_NULL_WITH_NULL_NULL";
/*      */         }
/* 2126 */         return this.cipher;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public String getProtocol() {
/* 2132 */       String protocol = this.protocol;
/* 2133 */       if (protocol == null) {
/* 2134 */         synchronized (ReferenceCountedOpenSslEngine.this) {
/* 2135 */           if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
/* 2136 */             protocol = SSL.getVersion(ReferenceCountedOpenSslEngine.this.ssl);
/*      */           } else {
/* 2138 */             protocol = "";
/*      */           } 
/*      */         } 
/*      */       }
/* 2142 */       return protocol;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getPeerHost() {
/* 2147 */       return ReferenceCountedOpenSslEngine.this.getPeerHost();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getPeerPort() {
/* 2152 */       return ReferenceCountedOpenSslEngine.this.getPeerPort();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getPacketBufferSize() {
/* 2157 */       return ReferenceCountedOpenSslEngine.this.maxEncryptedPacketLength();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getApplicationBufferSize() {
/* 2162 */       return this.applicationBufferSize;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void tryExpandApplicationBufferSize(int packetLengthDataOnly) {
/* 2172 */       if (packetLengthDataOnly > ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH && this.applicationBufferSize != ReferenceCountedOpenSslEngine.MAX_RECORD_SIZE)
/* 2173 */         this.applicationBufferSize = ReferenceCountedOpenSslEngine.MAX_RECORD_SIZE; 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\ReferenceCountedOpenSslEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */