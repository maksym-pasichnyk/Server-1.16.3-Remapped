/*      */ package io.netty.handler.ssl;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.ByteBufUtil;
/*      */ import io.netty.buffer.CompositeByteBuf;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.channel.AbstractCoalescingBufferQueue;
/*      */ import io.netty.channel.Channel;
/*      */ import io.netty.channel.ChannelException;
/*      */ import io.netty.channel.ChannelFuture;
/*      */ import io.netty.channel.ChannelFutureListener;
/*      */ import io.netty.channel.ChannelHandlerContext;
/*      */ import io.netty.channel.ChannelOutboundHandler;
/*      */ import io.netty.channel.ChannelOutboundInvoker;
/*      */ import io.netty.channel.ChannelPromise;
/*      */ import io.netty.channel.ChannelPromiseNotifier;
/*      */ import io.netty.handler.codec.ByteToMessageDecoder;
/*      */ import io.netty.handler.codec.UnsupportedMessageTypeException;
/*      */ import io.netty.util.ReferenceCountUtil;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import io.netty.util.concurrent.DefaultPromise;
/*      */ import io.netty.util.concurrent.EventExecutor;
/*      */ import io.netty.util.concurrent.Future;
/*      */ import io.netty.util.concurrent.FutureListener;
/*      */ import io.netty.util.concurrent.GenericFutureListener;
/*      */ import io.netty.util.concurrent.ImmediateExecutor;
/*      */ import io.netty.util.concurrent.Promise;
/*      */ import io.netty.util.concurrent.ScheduledFuture;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.ThrowableUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.net.SocketAddress;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.ClosedChannelException;
/*      */ import java.nio.channels.DatagramChannel;
/*      */ import java.nio.channels.SocketChannel;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.CountDownLatch;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.ScheduledFuture;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLEngineResult;
/*      */ import javax.net.ssl.SSLException;
/*      */ import javax.net.ssl.SSLSession;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SslHandler
/*      */   extends ByteToMessageDecoder
/*      */   implements ChannelOutboundHandler
/*      */ {
/*  168 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SslHandler.class);
/*      */   
/*  170 */   private static final Pattern IGNORABLE_CLASS_IN_STACK = Pattern.compile("^.*(?:Socket|Datagram|Sctp|Udt)Channel.*$");
/*      */   
/*  172 */   private static final Pattern IGNORABLE_ERROR_MESSAGE = Pattern.compile("^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  180 */   private static final SSLException SSLENGINE_CLOSED = (SSLException)ThrowableUtil.unknownStackTrace(new SSLException("SSLEngine closed already"), SslHandler.class, "wrap(...)");
/*      */   
/*  182 */   private static final SSLException HANDSHAKE_TIMED_OUT = (SSLException)ThrowableUtil.unknownStackTrace(new SSLException("handshake timed out"), SslHandler.class, "handshake(...)");
/*      */   
/*  184 */   private static final ClosedChannelException CHANNEL_CLOSED = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), SslHandler.class, "channelInactive(...)");
/*      */   private static final int MAX_PLAINTEXT_LENGTH = 16384;
/*      */   private volatile ChannelHandlerContext ctx;
/*      */   private final SSLEngine engine;
/*      */   private final SslEngineType engineType;
/*      */   private final Executor delegatedTaskExecutor;
/*      */   private final boolean jdkCompatibilityMode;
/*      */   
/*      */   private enum SslEngineType
/*      */   {
/*  194 */     TCNATIVE(true, ByteToMessageDecoder.COMPOSITE_CUMULATOR)
/*      */     {
/*      */       SSLEngineResult unwrap(SslHandler handler, ByteBuf in, int readerIndex, int len, ByteBuf out) throws SSLException {
/*      */         SSLEngineResult result;
/*  198 */         int nioBufferCount = in.nioBufferCount();
/*  199 */         int writerIndex = out.writerIndex();
/*      */         
/*  201 */         if (nioBufferCount > 1) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  207 */           ReferenceCountedOpenSslEngine opensslEngine = (ReferenceCountedOpenSslEngine)handler.engine;
/*      */           try {
/*  209 */             handler.singleBuffer[0] = SslHandler.toByteBuffer(out, writerIndex, out
/*  210 */                 .writableBytes());
/*  211 */             result = opensslEngine.unwrap(in.nioBuffers(readerIndex, len), handler.singleBuffer);
/*      */           } finally {
/*  213 */             handler.singleBuffer[0] = null;
/*      */           } 
/*      */         } else {
/*  216 */           result = handler.engine.unwrap(SslHandler.toByteBuffer(in, readerIndex, len), SslHandler
/*  217 */               .toByteBuffer(out, writerIndex, out.writableBytes()));
/*      */         } 
/*  219 */         out.writerIndex(writerIndex + result.bytesProduced());
/*  220 */         return result;
/*      */       }
/*      */ 
/*      */       
/*      */       int getPacketBufferSize(SslHandler handler) {
/*  225 */         return ((ReferenceCountedOpenSslEngine)handler.engine).maxEncryptedPacketLength0();
/*      */       }
/*      */ 
/*      */       
/*      */       int calculateWrapBufferCapacity(SslHandler handler, int pendingBytes, int numComponents) {
/*  230 */         return ((ReferenceCountedOpenSslEngine)handler.engine).calculateMaxLengthForWrap(pendingBytes, numComponents);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       int calculatePendingData(SslHandler handler, int guess) {
/*  236 */         int sslPending = ((ReferenceCountedOpenSslEngine)handler.engine).sslPending();
/*  237 */         return (sslPending > 0) ? sslPending : guess;
/*      */       }
/*      */ 
/*      */       
/*      */       boolean jdkCompatibilityMode(SSLEngine engine) {
/*  242 */         return ((ReferenceCountedOpenSslEngine)engine).jdkCompatibilityMode;
/*      */       }
/*      */     },
/*  245 */     CONSCRYPT(true, ByteToMessageDecoder.COMPOSITE_CUMULATOR)
/*      */     {
/*      */       SSLEngineResult unwrap(SslHandler handler, ByteBuf in, int readerIndex, int len, ByteBuf out) throws SSLException {
/*      */         SSLEngineResult result;
/*  249 */         int nioBufferCount = in.nioBufferCount();
/*  250 */         int writerIndex = out.writerIndex();
/*      */         
/*  252 */         if (nioBufferCount > 1) {
/*      */ 
/*      */           
/*      */           try {
/*      */             
/*  257 */             handler.singleBuffer[0] = SslHandler.toByteBuffer(out, writerIndex, out.writableBytes());
/*  258 */             result = ((ConscryptAlpnSslEngine)handler.engine).unwrap(in
/*  259 */                 .nioBuffers(readerIndex, len), handler
/*  260 */                 .singleBuffer);
/*      */           } finally {
/*  262 */             handler.singleBuffer[0] = null;
/*      */           } 
/*      */         } else {
/*  265 */           result = handler.engine.unwrap(SslHandler.toByteBuffer(in, readerIndex, len), SslHandler
/*  266 */               .toByteBuffer(out, writerIndex, out.writableBytes()));
/*      */         } 
/*  268 */         out.writerIndex(writerIndex + result.bytesProduced());
/*  269 */         return result;
/*      */       }
/*      */ 
/*      */       
/*      */       int calculateWrapBufferCapacity(SslHandler handler, int pendingBytes, int numComponents) {
/*  274 */         return ((ConscryptAlpnSslEngine)handler.engine).calculateOutNetBufSize(pendingBytes, numComponents);
/*      */       }
/*      */ 
/*      */       
/*      */       int calculatePendingData(SslHandler handler, int guess) {
/*  279 */         return guess;
/*      */       }
/*      */ 
/*      */       
/*      */       boolean jdkCompatibilityMode(SSLEngine engine) {
/*  284 */         return true;
/*      */       }
/*      */     },
/*  287 */     JDK(false, ByteToMessageDecoder.MERGE_CUMULATOR)
/*      */     {
/*      */       SSLEngineResult unwrap(SslHandler handler, ByteBuf in, int readerIndex, int len, ByteBuf out) throws SSLException
/*      */       {
/*  291 */         int writerIndex = out.writerIndex();
/*  292 */         ByteBuffer inNioBuffer = SslHandler.toByteBuffer(in, readerIndex, len);
/*  293 */         int position = inNioBuffer.position();
/*  294 */         SSLEngineResult result = handler.engine.unwrap(inNioBuffer, SslHandler
/*  295 */             .toByteBuffer(out, writerIndex, out.writableBytes()));
/*  296 */         out.writerIndex(writerIndex + result.bytesProduced());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  304 */         if (result.bytesConsumed() == 0) {
/*  305 */           int consumed = inNioBuffer.position() - position;
/*  306 */           if (consumed != result.bytesConsumed())
/*      */           {
/*  308 */             return new SSLEngineResult(result
/*  309 */                 .getStatus(), result.getHandshakeStatus(), consumed, result.bytesProduced());
/*      */           }
/*      */         } 
/*  312 */         return result;
/*      */       }
/*      */ 
/*      */       
/*      */       int calculateWrapBufferCapacity(SslHandler handler, int pendingBytes, int numComponents) {
/*  317 */         return handler.engine.getSession().getPacketBufferSize();
/*      */       }
/*      */ 
/*      */       
/*      */       int calculatePendingData(SslHandler handler, int guess) {
/*  322 */         return guess;
/*      */       }
/*      */ 
/*      */       
/*      */       boolean jdkCompatibilityMode(SSLEngine engine) {
/*  327 */         return true;
/*      */       } };
/*      */     final boolean wantsDirectBuffer;
/*      */     
/*      */     static SslEngineType forEngine(SSLEngine engine) {
/*  332 */       return (engine instanceof ReferenceCountedOpenSslEngine) ? TCNATIVE : ((engine instanceof ConscryptAlpnSslEngine) ? CONSCRYPT : JDK);
/*      */     }
/*      */     final ByteToMessageDecoder.Cumulator cumulator;
/*      */     
/*      */     SslEngineType(boolean wantsDirectBuffer, ByteToMessageDecoder.Cumulator cumulator) {
/*  337 */       this.wantsDirectBuffer = wantsDirectBuffer;
/*  338 */       this.cumulator = cumulator;
/*      */     }
/*      */     
/*      */     int getPacketBufferSize(SslHandler handler) {
/*  342 */       return handler.engine.getSession().getPacketBufferSize();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract SSLEngineResult unwrap(SslHandler param1SslHandler, ByteBuf param1ByteBuf1, int param1Int1, int param1Int2, ByteBuf param1ByteBuf2) throws SSLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract int calculateWrapBufferCapacity(SslHandler param1SslHandler, int param1Int1, int param1Int2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract int calculatePendingData(SslHandler param1SslHandler, int param1Int);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract boolean jdkCompatibilityMode(SSLEngine param1SSLEngine);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  385 */   private final ByteBuffer[] singleBuffer = new ByteBuffer[1];
/*      */   
/*      */   private final boolean startTls;
/*      */   private boolean sentFirstMessage;
/*      */   private boolean flushedBeforeHandshake;
/*      */   private boolean readDuringHandshake;
/*      */   private boolean handshakeStarted;
/*      */   private SslHandlerCoalescingBufferQueue pendingUnencryptedWrites;
/*  393 */   private Promise<Channel> handshakePromise = (Promise<Channel>)new LazyChannelPromise();
/*  394 */   private final LazyChannelPromise sslClosePromise = new LazyChannelPromise();
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean needsFlush;
/*      */ 
/*      */   
/*      */   private boolean outboundClosed;
/*      */ 
/*      */   
/*      */   private boolean closeNotify;
/*      */ 
/*      */   
/*      */   private int packetLength;
/*      */ 
/*      */   
/*      */   private boolean firedChannelRead;
/*      */ 
/*      */   
/*  413 */   private volatile long handshakeTimeoutMillis = 10000L;
/*  414 */   private volatile long closeNotifyFlushTimeoutMillis = 3000L;
/*      */   private volatile long closeNotifyReadTimeoutMillis;
/*  416 */   volatile int wrapDataSize = 16384;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SslHandler(SSLEngine engine) {
/*  424 */     this(engine, false);
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
/*      */   public SslHandler(SSLEngine engine, boolean startTls) {
/*  436 */     this(engine, startTls, (Executor)ImmediateExecutor.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public SslHandler(SSLEngine engine, Executor delegatedTaskExecutor) {
/*  444 */     this(engine, false, delegatedTaskExecutor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public SslHandler(SSLEngine engine, boolean startTls, Executor delegatedTaskExecutor) {
/*  452 */     if (engine == null) {
/*  453 */       throw new NullPointerException("engine");
/*      */     }
/*  455 */     if (delegatedTaskExecutor == null) {
/*  456 */       throw new NullPointerException("delegatedTaskExecutor");
/*      */     }
/*  458 */     this.engine = engine;
/*  459 */     this.engineType = SslEngineType.forEngine(engine);
/*  460 */     this.delegatedTaskExecutor = delegatedTaskExecutor;
/*  461 */     this.startTls = startTls;
/*  462 */     this.jdkCompatibilityMode = this.engineType.jdkCompatibilityMode(engine);
/*  463 */     setCumulator(this.engineType.cumulator);
/*      */   }
/*      */   
/*      */   public long getHandshakeTimeoutMillis() {
/*  467 */     return this.handshakeTimeoutMillis;
/*      */   }
/*      */   
/*      */   public void setHandshakeTimeout(long handshakeTimeout, TimeUnit unit) {
/*  471 */     if (unit == null) {
/*  472 */       throw new NullPointerException("unit");
/*      */     }
/*      */     
/*  475 */     setHandshakeTimeoutMillis(unit.toMillis(handshakeTimeout));
/*      */   }
/*      */   
/*      */   public void setHandshakeTimeoutMillis(long handshakeTimeoutMillis) {
/*  479 */     if (handshakeTimeoutMillis < 0L) {
/*  480 */       throw new IllegalArgumentException("handshakeTimeoutMillis: " + handshakeTimeoutMillis + " (expected: >= 0)");
/*      */     }
/*      */     
/*  483 */     this.handshakeTimeoutMillis = handshakeTimeoutMillis;
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
/*      */   public final void setWrapDataSize(int wrapDataSize) {
/*  508 */     this.wrapDataSize = wrapDataSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long getCloseNotifyTimeoutMillis() {
/*  516 */     return getCloseNotifyFlushTimeoutMillis();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setCloseNotifyTimeout(long closeNotifyTimeout, TimeUnit unit) {
/*  524 */     setCloseNotifyFlushTimeout(closeNotifyTimeout, unit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setCloseNotifyTimeoutMillis(long closeNotifyFlushTimeoutMillis) {
/*  532 */     setCloseNotifyFlushTimeoutMillis(closeNotifyFlushTimeoutMillis);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getCloseNotifyFlushTimeoutMillis() {
/*  541 */     return this.closeNotifyFlushTimeoutMillis;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCloseNotifyFlushTimeout(long closeNotifyFlushTimeout, TimeUnit unit) {
/*  550 */     setCloseNotifyFlushTimeoutMillis(unit.toMillis(closeNotifyFlushTimeout));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCloseNotifyFlushTimeoutMillis(long closeNotifyFlushTimeoutMillis) {
/*  557 */     if (closeNotifyFlushTimeoutMillis < 0L) {
/*  558 */       throw new IllegalArgumentException("closeNotifyFlushTimeoutMillis: " + closeNotifyFlushTimeoutMillis + " (expected: >= 0)");
/*      */     }
/*      */     
/*  561 */     this.closeNotifyFlushTimeoutMillis = closeNotifyFlushTimeoutMillis;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getCloseNotifyReadTimeoutMillis() {
/*  570 */     return this.closeNotifyReadTimeoutMillis;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCloseNotifyReadTimeout(long closeNotifyReadTimeout, TimeUnit unit) {
/*  579 */     setCloseNotifyReadTimeoutMillis(unit.toMillis(closeNotifyReadTimeout));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCloseNotifyReadTimeoutMillis(long closeNotifyReadTimeoutMillis) {
/*  586 */     if (closeNotifyReadTimeoutMillis < 0L) {
/*  587 */       throw new IllegalArgumentException("closeNotifyReadTimeoutMillis: " + closeNotifyReadTimeoutMillis + " (expected: >= 0)");
/*      */     }
/*      */     
/*  590 */     this.closeNotifyReadTimeoutMillis = closeNotifyReadTimeoutMillis;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SSLEngine engine() {
/*  597 */     return this.engine;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String applicationProtocol() {
/*  606 */     SSLEngine engine = engine();
/*  607 */     if (!(engine instanceof ApplicationProtocolAccessor)) {
/*  608 */       return null;
/*      */     }
/*      */     
/*  611 */     return ((ApplicationProtocolAccessor)engine).getNegotiatedApplicationProtocol();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Future<Channel> handshakeFuture() {
/*  621 */     return (Future<Channel>)this.handshakePromise;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ChannelFuture close() {
/*  632 */     return close(this.ctx.newPromise());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ChannelFuture close(final ChannelPromise promise) {
/*  642 */     final ChannelHandlerContext ctx = this.ctx;
/*  643 */     ctx.executor().execute(new Runnable()
/*      */         {
/*      */           public void run() {
/*  646 */             SslHandler.this.outboundClosed = true;
/*  647 */             SslHandler.this.engine.closeOutbound();
/*      */             try {
/*  649 */               SslHandler.this.flush(ctx, promise);
/*  650 */             } catch (Exception e) {
/*  651 */               if (!promise.tryFailure(e)) {
/*  652 */                 SslHandler.logger.warn("{} flush() raised a masked exception.", ctx.channel(), e);
/*      */               }
/*      */             } 
/*      */           }
/*      */         });
/*      */     
/*  658 */     return (ChannelFuture)promise;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Future<Channel> sslCloseFuture() {
/*  669 */     return (Future<Channel>)this.sslClosePromise;
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
/*  674 */     if (!this.pendingUnencryptedWrites.isEmpty())
/*      */     {
/*  676 */       this.pendingUnencryptedWrites.releaseAndFailAll((ChannelOutboundInvoker)ctx, (Throwable)new ChannelException("Pending write on removal of SslHandler"));
/*      */     }
/*      */     
/*  679 */     this.pendingUnencryptedWrites = null;
/*  680 */     if (this.engine instanceof ReferenceCounted) {
/*  681 */       ((ReferenceCounted)this.engine).release();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
/*  687 */     ctx.bind(localAddress, promise);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
/*  693 */     ctx.connect(remoteAddress, localAddress, promise);
/*      */   }
/*      */ 
/*      */   
/*      */   public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/*  698 */     ctx.deregister(promise);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/*  704 */     closeOutboundAndChannel(ctx, promise, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/*  710 */     closeOutboundAndChannel(ctx, promise, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public void read(ChannelHandlerContext ctx) throws Exception {
/*  715 */     if (!this.handshakePromise.isDone()) {
/*  716 */       this.readDuringHandshake = true;
/*      */     }
/*      */     
/*  719 */     ctx.read();
/*      */   }
/*      */   
/*      */   private static IllegalStateException newPendingWritesNullException() {
/*  723 */     return new IllegalStateException("pendingUnencryptedWrites is null, handlerRemoved0 called?");
/*      */   }
/*      */ 
/*      */   
/*      */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/*  728 */     if (!(msg instanceof ByteBuf)) {
/*  729 */       UnsupportedMessageTypeException exception = new UnsupportedMessageTypeException(msg, new Class[] { ByteBuf.class });
/*  730 */       ReferenceCountUtil.safeRelease(msg);
/*  731 */       promise.setFailure((Throwable)exception);
/*  732 */     } else if (this.pendingUnencryptedWrites == null) {
/*  733 */       ReferenceCountUtil.safeRelease(msg);
/*  734 */       promise.setFailure(newPendingWritesNullException());
/*      */     } else {
/*  736 */       this.pendingUnencryptedWrites.add((ByteBuf)msg, promise);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void flush(ChannelHandlerContext ctx) throws Exception {
/*  744 */     if (this.startTls && !this.sentFirstMessage) {
/*  745 */       this.sentFirstMessage = true;
/*  746 */       this.pendingUnencryptedWrites.writeAndRemoveAll(ctx);
/*  747 */       forceFlush(ctx);
/*      */       
/*      */       return;
/*      */     } 
/*      */     try {
/*  752 */       wrapAndFlush(ctx);
/*  753 */     } catch (Throwable cause) {
/*  754 */       setHandshakeFailure(ctx, cause);
/*  755 */       PlatformDependent.throwException(cause);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void wrapAndFlush(ChannelHandlerContext ctx) throws SSLException {
/*  760 */     if (this.pendingUnencryptedWrites.isEmpty())
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  765 */       this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, ctx.newPromise());
/*      */     }
/*  767 */     if (!this.handshakePromise.isDone()) {
/*  768 */       this.flushedBeforeHandshake = true;
/*      */     }
/*      */     try {
/*  771 */       wrap(ctx, false);
/*      */     }
/*      */     finally {
/*      */       
/*  775 */       forceFlush(ctx);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void wrap(ChannelHandlerContext ctx, boolean inUnwrap) throws SSLException {
/*  781 */     ByteBuf out = null;
/*  782 */     ChannelPromise promise = null;
/*  783 */     ByteBufAllocator alloc = ctx.alloc();
/*  784 */     boolean needUnwrap = false;
/*  785 */     ByteBuf buf = null;
/*      */     try {
/*  787 */       int wrapDataSize = this.wrapDataSize;
/*      */ 
/*      */       
/*  790 */       while (!ctx.isRemoved()) {
/*  791 */         promise = ctx.newPromise();
/*      */ 
/*      */         
/*  794 */         buf = (wrapDataSize > 0) ? this.pendingUnencryptedWrites.remove(alloc, wrapDataSize, promise) : this.pendingUnencryptedWrites.removeFirst(promise);
/*  795 */         if (buf == null) {
/*      */           break;
/*      */         }
/*      */         
/*  799 */         if (out == null) {
/*  800 */           out = allocateOutNetBuf(ctx, buf.readableBytes(), buf.nioBufferCount());
/*      */         }
/*      */         
/*  803 */         SSLEngineResult result = wrap(alloc, this.engine, buf, out);
/*      */         
/*  805 */         if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
/*  806 */           buf.release();
/*  807 */           buf = null;
/*  808 */           promise.tryFailure(SSLENGINE_CLOSED);
/*  809 */           promise = null;
/*      */ 
/*      */           
/*  812 */           this.pendingUnencryptedWrites.releaseAndFailAll((ChannelOutboundInvoker)ctx, SSLENGINE_CLOSED);
/*      */           return;
/*      */         } 
/*  815 */         if (buf.isReadable()) {
/*  816 */           this.pendingUnencryptedWrites.addFirst(buf, promise);
/*      */ 
/*      */           
/*  819 */           promise = null;
/*      */         } else {
/*  821 */           buf.release();
/*      */         } 
/*  823 */         buf = null;
/*      */         
/*  825 */         switch (result.getHandshakeStatus()) {
/*      */           case BUFFER_OVERFLOW:
/*  827 */             runDelegatedTasks();
/*      */             continue;
/*      */           case CLOSED:
/*  830 */             setHandshakeSuccess();
/*      */           
/*      */           case null:
/*  833 */             setHandshakeSuccessIfStillHandshaking();
/*      */           
/*      */           case null:
/*  836 */             finishWrap(ctx, out, promise, inUnwrap, false);
/*  837 */             promise = null;
/*  838 */             out = null;
/*      */             continue;
/*      */           case null:
/*  841 */             needUnwrap = true;
/*      */             return;
/*      */         } 
/*  844 */         throw new IllegalStateException("Unknown handshake status: " + result
/*  845 */             .getHandshakeStatus());
/*      */       }
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  851 */       if (buf != null) {
/*  852 */         buf.release();
/*      */       }
/*  854 */       finishWrap(ctx, out, promise, inUnwrap, needUnwrap);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void finishWrap(ChannelHandlerContext ctx, ByteBuf out, ChannelPromise promise, boolean inUnwrap, boolean needUnwrap) {
/*  860 */     if (out == null) {
/*  861 */       out = Unpooled.EMPTY_BUFFER;
/*  862 */     } else if (!out.isReadable()) {
/*  863 */       out.release();
/*  864 */       out = Unpooled.EMPTY_BUFFER;
/*      */     } 
/*      */     
/*  867 */     if (promise != null) {
/*  868 */       ctx.write(out, promise);
/*      */     } else {
/*  870 */       ctx.write(out);
/*      */     } 
/*      */     
/*  873 */     if (inUnwrap) {
/*  874 */       this.needsFlush = true;
/*      */     }
/*      */     
/*  877 */     if (needUnwrap)
/*      */     {
/*      */       
/*  880 */       readIfNeeded(ctx);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean wrapNonAppData(ChannelHandlerContext ctx, boolean inUnwrap) throws SSLException {
/*  891 */     ByteBuf out = null;
/*  892 */     ByteBufAllocator alloc = ctx.alloc();
/*      */ 
/*      */     
/*      */     try {
/*  896 */       while (!ctx.isRemoved()) {
/*  897 */         boolean bool; if (out == null)
/*      */         {
/*      */ 
/*      */           
/*  901 */           out = allocateOutNetBuf(ctx, 2048, 1);
/*      */         }
/*  903 */         SSLEngineResult result = wrap(alloc, this.engine, Unpooled.EMPTY_BUFFER, out);
/*      */         
/*  905 */         if (result.bytesProduced() > 0) {
/*  906 */           ctx.write(out);
/*  907 */           if (inUnwrap) {
/*  908 */             this.needsFlush = true;
/*      */           }
/*  910 */           out = null;
/*      */         } 
/*      */         
/*  913 */         switch (result.getHandshakeStatus()) {
/*      */           case CLOSED:
/*  915 */             setHandshakeSuccess();
/*  916 */             bool = false; return bool;
/*      */           case BUFFER_OVERFLOW:
/*  918 */             runDelegatedTasks();
/*      */             break;
/*      */           case null:
/*  921 */             if (inUnwrap) {
/*      */ 
/*      */ 
/*      */               
/*  925 */               bool = false; return bool;
/*      */             } 
/*      */             
/*  928 */             unwrapNonAppData(ctx);
/*      */             break;
/*      */           case null:
/*      */             break;
/*      */           case null:
/*  933 */             setHandshakeSuccessIfStillHandshaking();
/*      */ 
/*      */             
/*  936 */             if (!inUnwrap) {
/*  937 */               unwrapNonAppData(ctx);
/*      */             }
/*  939 */             bool = true; return bool;
/*      */           default:
/*  941 */             throw new IllegalStateException("Unknown handshake status: " + result.getHandshakeStatus());
/*      */         } 
/*      */         
/*  944 */         if (result.bytesProduced() == 0) {
/*      */           break;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  950 */         if (result.bytesConsumed() == 0 && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     } finally {
/*  955 */       if (out != null) {
/*  956 */         out.release();
/*      */       }
/*      */     } 
/*  959 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private SSLEngineResult wrap(ByteBufAllocator alloc, SSLEngine engine, ByteBuf in, ByteBuf out) throws SSLException {
/*  964 */     ByteBuf newDirectIn = null; try {
/*      */       ByteBuffer[] in0; SSLEngineResult result;
/*  966 */       int readerIndex = in.readerIndex();
/*  967 */       int readableBytes = in.readableBytes();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  972 */       if (in.isDirect() || !this.engineType.wantsDirectBuffer) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  977 */         if (!(in instanceof CompositeByteBuf) && in.nioBufferCount() == 1) {
/*  978 */           in0 = this.singleBuffer;
/*      */ 
/*      */           
/*  981 */           in0[0] = in.internalNioBuffer(readerIndex, readableBytes);
/*      */         } else {
/*  983 */           in0 = in.nioBuffers();
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  989 */         newDirectIn = alloc.directBuffer(readableBytes);
/*  990 */         newDirectIn.writeBytes(in, readerIndex, readableBytes);
/*  991 */         in0 = this.singleBuffer;
/*  992 */         in0[0] = newDirectIn.internalNioBuffer(newDirectIn.readerIndex(), readableBytes);
/*      */       } 
/*      */       
/*      */       while (true) {
/*  996 */         ByteBuffer out0 = out.nioBuffer(out.writerIndex(), out.writableBytes());
/*  997 */         result = engine.wrap(in0, out0);
/*  998 */         in.skipBytes(result.bytesConsumed());
/*  999 */         out.writerIndex(out.writerIndex() + result.bytesProduced());
/*      */         
/* 1001 */         switch (result.getStatus()) {
/*      */           case BUFFER_OVERFLOW:
/* 1003 */             out.ensureWritable(engine.getSession().getPacketBufferSize()); continue;
/*      */         }  break;
/*      */       } 
/* 1006 */       return result;
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/* 1011 */       this.singleBuffer[0] = null;
/*      */       
/* 1013 */       if (newDirectIn != null) {
/* 1014 */         newDirectIn.release();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 1023 */     setHandshakeFailure(ctx, CHANNEL_CLOSED, !this.outboundClosed, this.handshakeStarted, false);
/*      */ 
/*      */     
/* 1026 */     notifyClosePromise(CHANNEL_CLOSED);
/*      */     
/* 1028 */     super.channelInactive(ctx);
/*      */   }
/*      */ 
/*      */   
/*      */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 1033 */     if (ignoreException(cause)) {
/*      */ 
/*      */       
/* 1036 */       if (logger.isDebugEnabled()) {
/* 1037 */         logger.debug("{} Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred while writing close_notify in response to the peer's close_notify", ctx
/*      */             
/* 1039 */             .channel(), cause);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1044 */       if (ctx.channel().isActive()) {
/* 1045 */         ctx.close();
/*      */       }
/*      */     } else {
/* 1048 */       ctx.fireExceptionCaught(cause);
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
/*      */   
/*      */   private boolean ignoreException(Throwable t) {
/* 1062 */     if (!(t instanceof SSLException) && t instanceof java.io.IOException && this.sslClosePromise.isDone()) {
/* 1063 */       String message = t.getMessage();
/*      */ 
/*      */ 
/*      */       
/* 1067 */       if (message != null && IGNORABLE_ERROR_MESSAGE.matcher(message).matches()) {
/* 1068 */         return true;
/*      */       }
/*      */ 
/*      */       
/* 1072 */       StackTraceElement[] elements = t.getStackTrace();
/* 1073 */       for (StackTraceElement element : elements) {
/* 1074 */         String classname = element.getClassName();
/* 1075 */         String methodname = element.getMethodName();
/*      */ 
/*      */         
/* 1078 */         if (!classname.startsWith("io.netty."))
/*      */         {
/*      */ 
/*      */ 
/*      */           
/* 1083 */           if ("read".equals(methodname)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1089 */             if (IGNORABLE_CLASS_IN_STACK.matcher(classname).matches()) {
/* 1090 */               return true;
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             try {
/* 1097 */               Class<?> clazz = PlatformDependent.getClassLoader(getClass()).loadClass(classname);
/*      */               
/* 1099 */               if (SocketChannel.class.isAssignableFrom(clazz) || DatagramChannel.class
/* 1100 */                 .isAssignableFrom(clazz)) {
/* 1101 */                 return true;
/*      */               }
/*      */ 
/*      */               
/* 1105 */               if (PlatformDependent.javaVersion() >= 7 && "com.sun.nio.sctp.SctpChannel"
/* 1106 */                 .equals(clazz.getSuperclass().getName())) {
/* 1107 */                 return true;
/*      */               }
/* 1109 */             } catch (Throwable cause) {
/* 1110 */               logger.debug("Unexpected exception while loading class {} classname {}", new Object[] {
/* 1111 */                     getClass(), classname, cause });
/*      */             } 
/*      */           }  } 
/*      */       } 
/*      */     } 
/* 1116 */     return false;
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
/*      */   public static boolean isEncrypted(ByteBuf buffer) {
/* 1132 */     if (buffer.readableBytes() < 5) {
/* 1133 */       throw new IllegalArgumentException("buffer must have at least 5 readable bytes");
/*      */     }
/*      */     
/* 1136 */     return (SslUtils.getEncryptedPacketLength(buffer, buffer.readerIndex()) != -2);
/*      */   }
/*      */   
/*      */   private void decodeJdkCompatible(ChannelHandlerContext ctx, ByteBuf in) throws NotSslRecordException {
/* 1140 */     int packetLength = this.packetLength;
/*      */     
/* 1142 */     if (packetLength > 0) {
/* 1143 */       if (in.readableBytes() < packetLength) {
/*      */         return;
/*      */       }
/*      */     } else {
/*      */       
/* 1148 */       int readableBytes = in.readableBytes();
/* 1149 */       if (readableBytes < 5) {
/*      */         return;
/*      */       }
/* 1152 */       packetLength = SslUtils.getEncryptedPacketLength(in, in.readerIndex());
/* 1153 */       if (packetLength == -2) {
/*      */ 
/*      */         
/* 1156 */         NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
/* 1157 */         in.skipBytes(in.readableBytes());
/*      */ 
/*      */ 
/*      */         
/* 1161 */         setHandshakeFailure(ctx, e);
/*      */         
/* 1163 */         throw e;
/*      */       } 
/* 1165 */       assert packetLength > 0;
/* 1166 */       if (packetLength > readableBytes) {
/*      */         
/* 1168 */         this.packetLength = packetLength;
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*      */     
/* 1175 */     this.packetLength = 0;
/*      */     try {
/* 1177 */       int bytesConsumed = unwrap(ctx, in, in.readerIndex(), packetLength);
/* 1178 */       assert bytesConsumed == packetLength || this.engine.isInboundDone() : "we feed the SSLEngine a packets worth of data: " + packetLength + " but it only consumed: " + bytesConsumed;
/*      */ 
/*      */       
/* 1181 */       in.skipBytes(bytesConsumed);
/* 1182 */     } catch (Throwable cause) {
/* 1183 */       handleUnwrapThrowable(ctx, cause);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void decodeNonJdkCompatible(ChannelHandlerContext ctx, ByteBuf in) {
/*      */     try {
/* 1189 */       in.skipBytes(unwrap(ctx, in, in.readerIndex(), in.readableBytes()));
/* 1190 */     } catch (Throwable cause) {
/* 1191 */       handleUnwrapThrowable(ctx, cause);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleUnwrapThrowable(ChannelHandlerContext ctx, Throwable cause) {
/*      */     try {
/* 1201 */       if (this.handshakePromise.tryFailure(cause)) {
/* 1202 */         ctx.fireUserEventTriggered(new SslHandshakeCompletionEvent(cause));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1207 */       wrapAndFlush(ctx);
/* 1208 */     } catch (SSLException ex) {
/* 1209 */       logger.debug("SSLException during trying to call SSLEngine.wrap(...) because of an previous SSLException, ignoring...", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1213 */       setHandshakeFailure(ctx, cause, true, false, true);
/*      */     } 
/* 1215 */     PlatformDependent.throwException(cause);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws SSLException {
/* 1220 */     if (this.jdkCompatibilityMode) {
/* 1221 */       decodeJdkCompatible(ctx, in);
/*      */     } else {
/* 1223 */       decodeNonJdkCompatible(ctx, in);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
/* 1230 */     discardSomeReadBytes();
/*      */     
/* 1232 */     flushIfNeeded(ctx);
/* 1233 */     readIfNeeded(ctx);
/*      */     
/* 1235 */     this.firedChannelRead = false;
/* 1236 */     ctx.fireChannelReadComplete();
/*      */   }
/*      */ 
/*      */   
/*      */   private void readIfNeeded(ChannelHandlerContext ctx) {
/* 1241 */     if (!ctx.channel().config().isAutoRead() && (!this.firedChannelRead || !this.handshakePromise.isDone()))
/*      */     {
/*      */       
/* 1244 */       ctx.read();
/*      */     }
/*      */   }
/*      */   
/*      */   private void flushIfNeeded(ChannelHandlerContext ctx) {
/* 1249 */     if (this.needsFlush) {
/* 1250 */       forceFlush(ctx);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void unwrapNonAppData(ChannelHandlerContext ctx) throws SSLException {
/* 1258 */     unwrap(ctx, Unpooled.EMPTY_BUFFER, 0, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int unwrap(ChannelHandlerContext ctx, ByteBuf packet, int offset, int length) throws SSLException {
/* 1266 */     int originalLength = length;
/* 1267 */     boolean wrapLater = false;
/* 1268 */     boolean notifyClosure = false;
/* 1269 */     int overflowReadableBytes = -1;
/* 1270 */     ByteBuf decodeOut = allocate(ctx, length);
/*      */ 
/*      */     
/*      */     try {
/* 1274 */       while (!ctx.isRemoved()) {
/* 1275 */         int readableBytes, previousOverflowReadableBytes, bufferSize; SSLEngineResult result = this.engineType.unwrap(this, packet, offset, length, decodeOut);
/* 1276 */         SSLEngineResult.Status status = result.getStatus();
/* 1277 */         SSLEngineResult.HandshakeStatus handshakeStatus = result.getHandshakeStatus();
/* 1278 */         int produced = result.bytesProduced();
/* 1279 */         int consumed = result.bytesConsumed();
/*      */ 
/*      */         
/* 1282 */         offset += consumed;
/* 1283 */         length -= consumed;
/*      */         
/* 1285 */         switch (status) {
/*      */           case BUFFER_OVERFLOW:
/* 1287 */             readableBytes = decodeOut.readableBytes();
/* 1288 */             previousOverflowReadableBytes = overflowReadableBytes;
/* 1289 */             overflowReadableBytes = readableBytes;
/* 1290 */             bufferSize = this.engine.getSession().getApplicationBufferSize() - readableBytes;
/* 1291 */             if (readableBytes > 0) {
/* 1292 */               this.firedChannelRead = true;
/* 1293 */               ctx.fireChannelRead(decodeOut);
/*      */ 
/*      */               
/* 1296 */               decodeOut = null;
/* 1297 */               if (bufferSize <= 0)
/*      */               {
/*      */ 
/*      */ 
/*      */                 
/* 1302 */                 bufferSize = this.engine.getSession().getApplicationBufferSize();
/*      */               }
/*      */             } else {
/*      */               
/* 1306 */               decodeOut.release();
/* 1307 */               decodeOut = null;
/*      */             } 
/* 1309 */             if (readableBytes == 0 && previousOverflowReadableBytes == 0)
/*      */             {
/*      */               
/* 1312 */               throw new IllegalStateException("Two consecutive overflows but no content was consumed. " + SSLSession.class
/* 1313 */                   .getSimpleName() + " getApplicationBufferSize: " + this.engine
/* 1314 */                   .getSession().getApplicationBufferSize() + " maybe too small.");
/*      */             }
/*      */ 
/*      */ 
/*      */             
/* 1319 */             decodeOut = allocate(ctx, this.engineType.calculatePendingData(this, bufferSize));
/*      */             continue;
/*      */           
/*      */           case CLOSED:
/* 1323 */             notifyClosure = true;
/* 1324 */             overflowReadableBytes = -1;
/*      */             break;
/*      */           default:
/* 1327 */             overflowReadableBytes = -1;
/*      */             break;
/*      */         } 
/*      */         
/* 1331 */         switch (handshakeStatus) {
/*      */           case null:
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case null:
/* 1338 */             if (wrapNonAppData(ctx, true) && length == 0) {
/*      */               break;
/*      */             }
/*      */             break;
/*      */           case BUFFER_OVERFLOW:
/* 1343 */             runDelegatedTasks();
/*      */             break;
/*      */           case CLOSED:
/* 1346 */             setHandshakeSuccess();
/* 1347 */             wrapLater = true;
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case null:
/* 1362 */             if (setHandshakeSuccessIfStillHandshaking()) {
/* 1363 */               wrapLater = true;
/*      */               continue;
/*      */             } 
/* 1366 */             if (this.flushedBeforeHandshake) {
/*      */ 
/*      */ 
/*      */               
/* 1370 */               this.flushedBeforeHandshake = false;
/* 1371 */               wrapLater = true;
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/* 1376 */             if (length == 0) {
/*      */               break;
/*      */             }
/*      */             break;
/*      */           default:
/* 1381 */             throw new IllegalStateException("unknown handshake status: " + handshakeStatus);
/*      */         } 
/*      */         
/* 1384 */         if (status == SSLEngineResult.Status.BUFFER_UNDERFLOW || (consumed == 0 && produced == 0)) {
/* 1385 */           if (handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP)
/*      */           {
/*      */             
/* 1388 */             readIfNeeded(ctx);
/*      */           }
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */       
/* 1395 */       if (wrapLater) {
/* 1396 */         wrap(ctx, true);
/*      */       }
/*      */       
/* 1399 */       if (notifyClosure) {
/* 1400 */         notifyClosePromise((Throwable)null);
/*      */       }
/*      */     } finally {
/* 1403 */       if (decodeOut != null) {
/* 1404 */         if (decodeOut.isReadable()) {
/* 1405 */           this.firedChannelRead = true;
/*      */           
/* 1407 */           ctx.fireChannelRead(decodeOut);
/*      */         } else {
/* 1409 */           decodeOut.release();
/*      */         } 
/*      */       }
/*      */     } 
/* 1413 */     return originalLength - length;
/*      */   }
/*      */   
/*      */   private static ByteBuffer toByteBuffer(ByteBuf out, int index, int len) {
/* 1417 */     return (out.nioBufferCount() == 1) ? out.internalNioBuffer(index, len) : out
/* 1418 */       .nioBuffer(index, len);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void runDelegatedTasks() {
/* 1428 */     if (this.delegatedTaskExecutor == ImmediateExecutor.INSTANCE) {
/*      */       while (true) {
/* 1430 */         Runnable task = this.engine.getDelegatedTask();
/* 1431 */         if (task == null) {
/*      */           break;
/*      */         }
/*      */         
/* 1435 */         task.run();
/*      */       } 
/*      */     } else {
/* 1438 */       final List<Runnable> tasks = new ArrayList<Runnable>(2);
/*      */       while (true) {
/* 1440 */         Runnable task = this.engine.getDelegatedTask();
/* 1441 */         if (task == null) {
/*      */           break;
/*      */         }
/*      */         
/* 1445 */         tasks.add(task);
/*      */       } 
/*      */       
/* 1448 */       if (tasks.isEmpty()) {
/*      */         return;
/*      */       }
/*      */       
/* 1452 */       final CountDownLatch latch = new CountDownLatch(1);
/* 1453 */       this.delegatedTaskExecutor.execute(new Runnable()
/*      */           {
/*      */             public void run() {
/*      */               try {
/* 1457 */                 for (Runnable task : tasks) {
/* 1458 */                   task.run();
/*      */                 }
/* 1460 */               } catch (Exception e) {
/* 1461 */                 SslHandler.this.ctx.fireExceptionCaught(e);
/*      */               } finally {
/* 1463 */                 latch.countDown();
/*      */               } 
/*      */             }
/*      */           });
/*      */       
/* 1468 */       boolean interrupted = false;
/* 1469 */       while (latch.getCount() != 0L) {
/*      */         try {
/* 1471 */           latch.await();
/* 1472 */         } catch (InterruptedException e) {
/*      */           
/* 1474 */           interrupted = true;
/*      */         } 
/*      */       } 
/*      */       
/* 1478 */       if (interrupted) {
/* 1479 */         Thread.currentThread().interrupt();
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
/*      */   private boolean setHandshakeSuccessIfStillHandshaking() {
/* 1492 */     if (!this.handshakePromise.isDone()) {
/* 1493 */       setHandshakeSuccess();
/* 1494 */       return true;
/*      */     } 
/* 1496 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setHandshakeSuccess() {
/* 1503 */     this.handshakePromise.trySuccess(this.ctx.channel());
/*      */     
/* 1505 */     if (logger.isDebugEnabled()) {
/* 1506 */       logger.debug("{} HANDSHAKEN: {}", this.ctx.channel(), this.engine.getSession().getCipherSuite());
/*      */     }
/* 1508 */     this.ctx.fireUserEventTriggered(SslHandshakeCompletionEvent.SUCCESS);
/*      */     
/* 1510 */     if (this.readDuringHandshake && !this.ctx.channel().config().isAutoRead()) {
/* 1511 */       this.readDuringHandshake = false;
/* 1512 */       this.ctx.read();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setHandshakeFailure(ChannelHandlerContext ctx, Throwable cause) {
/* 1520 */     setHandshakeFailure(ctx, cause, true, true, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setHandshakeFailure(ChannelHandlerContext ctx, Throwable cause, boolean closeInbound, boolean notify, boolean alwaysFlushAndClose) {
/*      */     try {
/* 1531 */       this.outboundClosed = true;
/* 1532 */       this.engine.closeOutbound();
/*      */       
/* 1534 */       if (closeInbound) {
/*      */         try {
/* 1536 */           this.engine.closeInbound();
/* 1537 */         } catch (SSLException e) {
/* 1538 */           if (logger.isDebugEnabled()) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1543 */             String msg = e.getMessage();
/* 1544 */             if (msg == null || !msg.contains("possible truncation attack")) {
/* 1545 */               logger.debug("{} SSLEngine.closeInbound() raised an exception.", ctx.channel(), e);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/* 1550 */       if (this.handshakePromise.tryFailure(cause) || alwaysFlushAndClose) {
/* 1551 */         SslUtils.handleHandshakeFailure(ctx, cause, notify);
/*      */       }
/*      */     } finally {
/*      */       
/* 1555 */       releaseAndFailAll(cause);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void releaseAndFailAll(Throwable cause) {
/* 1560 */     if (this.pendingUnencryptedWrites != null) {
/* 1561 */       this.pendingUnencryptedWrites.releaseAndFailAll((ChannelOutboundInvoker)this.ctx, cause);
/*      */     }
/*      */   }
/*      */   
/*      */   private void notifyClosePromise(Throwable cause) {
/* 1566 */     if (cause == null) {
/* 1567 */       if (this.sslClosePromise.trySuccess(this.ctx.channel())) {
/* 1568 */         this.ctx.fireUserEventTriggered(SslCloseCompletionEvent.SUCCESS);
/*      */       }
/*      */     }
/* 1571 */     else if (this.sslClosePromise.tryFailure(cause)) {
/* 1572 */       this.ctx.fireUserEventTriggered(new SslCloseCompletionEvent(cause));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void closeOutboundAndChannel(ChannelHandlerContext ctx, final ChannelPromise promise, boolean disconnect) throws Exception {
/* 1579 */     this.outboundClosed = true;
/* 1580 */     this.engine.closeOutbound();
/*      */     
/* 1582 */     if (!ctx.channel().isActive()) {
/* 1583 */       if (disconnect) {
/* 1584 */         ctx.disconnect(promise);
/*      */       } else {
/* 1586 */         ctx.close(promise);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/* 1591 */     ChannelPromise closeNotifyPromise = ctx.newPromise();
/*      */     try {
/* 1593 */       flush(ctx, closeNotifyPromise);
/*      */     } finally {
/* 1595 */       if (!this.closeNotify) {
/* 1596 */         this.closeNotify = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1605 */         safeClose(ctx, (ChannelFuture)closeNotifyPromise, ctx.newPromise().addListener((GenericFutureListener)new ChannelPromiseNotifier(false, new ChannelPromise[] { promise })));
/*      */       }
/*      */       else {
/*      */         
/* 1609 */         this.sslClosePromise.addListener((GenericFutureListener)new FutureListener<Channel>()
/*      */             {
/*      */               public void operationComplete(Future<Channel> future) {
/* 1612 */                 promise.setSuccess();
/*      */               }
/*      */             });
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void flush(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 1620 */     if (this.pendingUnencryptedWrites != null) {
/* 1621 */       this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, promise);
/*      */     } else {
/* 1623 */       promise.setFailure(newPendingWritesNullException());
/*      */     } 
/* 1625 */     flush(ctx);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/* 1630 */     this.ctx = ctx;
/*      */     
/* 1632 */     this.pendingUnencryptedWrites = new SslHandlerCoalescingBufferQueue(ctx.channel(), 16);
/* 1633 */     if (ctx.channel().isActive()) {
/* 1634 */       startHandshakeProcessing();
/*      */     }
/*      */   }
/*      */   
/*      */   private void startHandshakeProcessing() {
/* 1639 */     this.handshakeStarted = true;
/* 1640 */     if (this.engine.getUseClientMode()) {
/*      */ 
/*      */ 
/*      */       
/* 1644 */       handshake((Promise<Channel>)null);
/*      */     } else {
/* 1646 */       applyHandshakeTimeout((Promise<Channel>)null);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Future<Channel> renegotiate() {
/* 1654 */     ChannelHandlerContext ctx = this.ctx;
/* 1655 */     if (ctx == null) {
/* 1656 */       throw new IllegalStateException();
/*      */     }
/*      */     
/* 1659 */     return renegotiate(ctx.executor().newPromise());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Future<Channel> renegotiate(final Promise<Channel> promise) {
/* 1666 */     if (promise == null) {
/* 1667 */       throw new NullPointerException("promise");
/*      */     }
/*      */     
/* 1670 */     ChannelHandlerContext ctx = this.ctx;
/* 1671 */     if (ctx == null) {
/* 1672 */       throw new IllegalStateException();
/*      */     }
/*      */     
/* 1675 */     EventExecutor executor = ctx.executor();
/* 1676 */     if (!executor.inEventLoop()) {
/* 1677 */       executor.execute(new Runnable()
/*      */           {
/*      */             public void run() {
/* 1680 */               SslHandler.this.handshake(promise);
/*      */             }
/*      */           });
/* 1683 */       return (Future<Channel>)promise;
/*      */     } 
/*      */     
/* 1686 */     handshake(promise);
/* 1687 */     return (Future<Channel>)promise;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void handshake(final Promise<Channel> newHandshakePromise) {
/*      */     Promise<Channel> p;
/* 1699 */     if (newHandshakePromise != null)
/* 1700 */     { Promise<Channel> oldHandshakePromise = this.handshakePromise;
/* 1701 */       if (!oldHandshakePromise.isDone()) {
/*      */ 
/*      */         
/* 1704 */         oldHandshakePromise.addListener((GenericFutureListener)new FutureListener<Channel>()
/*      */             {
/*      */               public void operationComplete(Future<Channel> future) throws Exception {
/* 1707 */                 if (future.isSuccess()) {
/* 1708 */                   newHandshakePromise.setSuccess(future.getNow());
/*      */                 } else {
/* 1710 */                   newHandshakePromise.setFailure(future.cause());
/*      */                 } 
/*      */               }
/*      */             });
/*      */         
/*      */         return;
/*      */       } 
/* 1717 */       this.handshakePromise = p = newHandshakePromise; }
/* 1718 */     else { if (this.engine.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1724 */       p = this.handshakePromise;
/* 1725 */       assert !p.isDone(); }
/*      */ 
/*      */ 
/*      */     
/* 1729 */     ChannelHandlerContext ctx = this.ctx;
/*      */     try {
/* 1731 */       this.engine.beginHandshake();
/* 1732 */       wrapNonAppData(ctx, false);
/* 1733 */     } catch (Throwable e) {
/* 1734 */       setHandshakeFailure(ctx, e);
/*      */     } finally {
/* 1736 */       forceFlush(ctx);
/*      */     } 
/* 1738 */     applyHandshakeTimeout(p);
/*      */   }
/*      */   
/*      */   private void applyHandshakeTimeout(Promise<Channel> p) {
/* 1742 */     final Promise<Channel> promise = (p == null) ? this.handshakePromise : p;
/*      */     
/* 1744 */     long handshakeTimeoutMillis = this.handshakeTimeoutMillis;
/* 1745 */     if (handshakeTimeoutMillis <= 0L || promise.isDone()) {
/*      */       return;
/*      */     }
/*      */     
/* 1749 */     final ScheduledFuture timeoutFuture = this.ctx.executor().schedule(new Runnable()
/*      */         {
/*      */           public void run() {
/* 1752 */             if (promise.isDone()) {
/*      */               return;
/*      */             }
/*      */             try {
/* 1756 */               if (SslHandler.this.handshakePromise.tryFailure(SslHandler.HANDSHAKE_TIMED_OUT)) {
/* 1757 */                 SslUtils.handleHandshakeFailure(SslHandler.this.ctx, SslHandler.HANDSHAKE_TIMED_OUT, true);
/*      */               }
/*      */             } finally {
/* 1760 */               SslHandler.this.releaseAndFailAll(SslHandler.HANDSHAKE_TIMED_OUT);
/*      */             } 
/*      */           }
/*      */         }handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
/*      */ 
/*      */     
/* 1766 */     promise.addListener((GenericFutureListener)new FutureListener<Channel>()
/*      */         {
/*      */           public void operationComplete(Future<Channel> f) throws Exception {
/* 1769 */             timeoutFuture.cancel(false);
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   private void forceFlush(ChannelHandlerContext ctx) {
/* 1775 */     this.needsFlush = false;
/* 1776 */     ctx.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void channelActive(ChannelHandlerContext ctx) throws Exception {
/* 1784 */     if (!this.startTls) {
/* 1785 */       startHandshakeProcessing();
/*      */     }
/* 1787 */     ctx.fireChannelActive();
/*      */   }
/*      */ 
/*      */   
/*      */   private void safeClose(final ChannelHandlerContext ctx, final ChannelFuture flushFuture, final ChannelPromise promise) {
/*      */     final ScheduledFuture<?> timeoutFuture;
/* 1793 */     if (!ctx.channel().isActive()) {
/* 1794 */       ctx.close(promise);
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1799 */     if (!flushFuture.isDone()) {
/* 1800 */       long closeNotifyTimeout = this.closeNotifyFlushTimeoutMillis;
/* 1801 */       if (closeNotifyTimeout > 0L) {
/*      */         
/* 1803 */         ScheduledFuture scheduledFuture = ctx.executor().schedule(new Runnable()
/*      */             {
/*      */               public void run()
/*      */               {
/* 1807 */                 if (!flushFuture.isDone()) {
/* 1808 */                   SslHandler.logger.warn("{} Last write attempt timed out; force-closing the connection.", ctx
/* 1809 */                       .channel());
/* 1810 */                   SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise);
/*      */                 } 
/*      */               }
/*      */             }closeNotifyTimeout, TimeUnit.MILLISECONDS);
/*      */       } else {
/* 1815 */         timeoutFuture = null;
/*      */       } 
/*      */     } else {
/* 1818 */       timeoutFuture = null;
/*      */     } 
/*      */ 
/*      */     
/* 1822 */     flushFuture.addListener((GenericFutureListener)new ChannelFutureListener()
/*      */         {
/*      */           public void operationComplete(ChannelFuture f) throws Exception
/*      */           {
/* 1826 */             if (timeoutFuture != null) {
/* 1827 */               timeoutFuture.cancel(false);
/*      */             }
/* 1829 */             final long closeNotifyReadTimeout = SslHandler.this.closeNotifyReadTimeoutMillis;
/* 1830 */             if (closeNotifyReadTimeout <= 0L) {
/*      */ 
/*      */               
/* 1833 */               SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise);
/*      */             } else {
/*      */               final ScheduledFuture<?> closeNotifyReadTimeoutFuture;
/*      */               
/* 1837 */               if (!SslHandler.this.sslClosePromise.isDone()) {
/* 1838 */                 ScheduledFuture scheduledFuture = ctx.executor().schedule(new Runnable()
/*      */                     {
/*      */                       public void run() {
/* 1841 */                         if (!SslHandler.this.sslClosePromise.isDone()) {
/* 1842 */                           SslHandler.logger.debug("{} did not receive close_notify in {}ms; force-closing the connection.", ctx
/*      */                               
/* 1844 */                               .channel(), Long.valueOf(closeNotifyReadTimeout));
/*      */ 
/*      */                           
/* 1847 */                           SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise);
/*      */                         } 
/*      */                       }
/*      */                     }closeNotifyReadTimeout, TimeUnit.MILLISECONDS);
/*      */               } else {
/* 1852 */                 closeNotifyReadTimeoutFuture = null;
/*      */               } 
/*      */ 
/*      */               
/* 1856 */               SslHandler.this.sslClosePromise.addListener((GenericFutureListener)new FutureListener<Channel>()
/*      */                   {
/*      */                     public void operationComplete(Future<Channel> future) throws Exception {
/* 1859 */                       if (closeNotifyReadTimeoutFuture != null) {
/* 1860 */                         closeNotifyReadTimeoutFuture.cancel(false);
/*      */                       }
/* 1862 */                       SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise);
/*      */                     }
/*      */                   });
/*      */             } 
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addCloseListener(ChannelFuture future, ChannelPromise promise) {
/* 1877 */     future.addListener((GenericFutureListener)new ChannelPromiseNotifier(false, new ChannelPromise[] { promise }));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteBuf allocate(ChannelHandlerContext ctx, int capacity) {
/* 1885 */     ByteBufAllocator alloc = ctx.alloc();
/* 1886 */     if (this.engineType.wantsDirectBuffer) {
/* 1887 */       return alloc.directBuffer(capacity);
/*      */     }
/* 1889 */     return alloc.buffer(capacity);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteBuf allocateOutNetBuf(ChannelHandlerContext ctx, int pendingBytes, int numComponents) {
/* 1898 */     return allocate(ctx, this.engineType.calculateWrapBufferCapacity(this, pendingBytes, numComponents));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class SslHandlerCoalescingBufferQueue
/*      */     extends AbstractCoalescingBufferQueue
/*      */   {
/*      */     SslHandlerCoalescingBufferQueue(Channel channel, int initSize) {
/* 1909 */       super(channel, initSize);
/*      */     }
/*      */ 
/*      */     
/*      */     protected ByteBuf compose(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf next) {
/* 1914 */       int wrapDataSize = SslHandler.this.wrapDataSize;
/* 1915 */       if (cumulation instanceof CompositeByteBuf) {
/* 1916 */         CompositeByteBuf composite = (CompositeByteBuf)cumulation;
/* 1917 */         int numComponents = composite.numComponents();
/* 1918 */         if (numComponents == 0 || 
/* 1919 */           !SslHandler.attemptCopyToCumulation(composite.internalComponent(numComponents - 1), next, wrapDataSize)) {
/* 1920 */           composite.addComponent(true, next);
/*      */         }
/* 1922 */         return (ByteBuf)composite;
/*      */       } 
/* 1924 */       return SslHandler.attemptCopyToCumulation(cumulation, next, wrapDataSize) ? cumulation : 
/* 1925 */         copyAndCompose(alloc, cumulation, next);
/*      */     }
/*      */ 
/*      */     
/*      */     protected ByteBuf composeFirst(ByteBufAllocator allocator, ByteBuf first) {
/* 1930 */       if (first instanceof CompositeByteBuf) {
/* 1931 */         CompositeByteBuf composite = (CompositeByteBuf)first;
/* 1932 */         first = allocator.directBuffer(composite.readableBytes());
/*      */         try {
/* 1934 */           first.writeBytes((ByteBuf)composite);
/* 1935 */         } catch (Throwable cause) {
/* 1936 */           first.release();
/* 1937 */           PlatformDependent.throwException(cause);
/*      */         } 
/* 1939 */         composite.release();
/*      */       } 
/* 1941 */       return first;
/*      */     }
/*      */ 
/*      */     
/*      */     protected ByteBuf removeEmptyValue() {
/* 1946 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   private static boolean attemptCopyToCumulation(ByteBuf cumulation, ByteBuf next, int wrapDataSize) {
/* 1951 */     int inReadableBytes = next.readableBytes();
/* 1952 */     int cumulationCapacity = cumulation.capacity();
/* 1953 */     if (wrapDataSize - cumulation.readableBytes() >= inReadableBytes && ((cumulation
/*      */ 
/*      */ 
/*      */       
/* 1957 */       .isWritable(inReadableBytes) && cumulationCapacity >= wrapDataSize) || (cumulationCapacity < wrapDataSize && 
/*      */       
/* 1959 */       ByteBufUtil.ensureWritableSuccess(cumulation.ensureWritable(inReadableBytes, false))))) {
/* 1960 */       cumulation.writeBytes(next);
/* 1961 */       next.release();
/* 1962 */       return true;
/*      */     } 
/* 1964 */     return false;
/*      */   }
/*      */   
/*      */   private final class LazyChannelPromise extends DefaultPromise<Channel> {
/*      */     private LazyChannelPromise() {}
/*      */     
/*      */     protected EventExecutor executor() {
/* 1971 */       if (SslHandler.this.ctx == null) {
/* 1972 */         throw new IllegalStateException();
/*      */       }
/* 1974 */       return SslHandler.this.ctx.executor();
/*      */     }
/*      */ 
/*      */     
/*      */     protected void checkDeadLock() {
/* 1979 */       if (SslHandler.this.ctx == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1988 */       super.checkDeadLock();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\SslHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */