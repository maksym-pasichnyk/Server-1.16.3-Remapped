/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.AbstractCoalescingBufferQueue;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.CoalescingBufferQueue;
/*     */ import io.netty.handler.codec.http.HttpStatusClass;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.ArrayDeque;
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
/*     */ public class DefaultHttp2ConnectionEncoder
/*     */   implements Http2ConnectionEncoder
/*     */ {
/*     */   private final Http2FrameWriter frameWriter;
/*     */   private final Http2Connection connection;
/*     */   private Http2LifecycleManager lifecycleManager;
/*  46 */   private final ArrayDeque<Http2Settings> outstandingLocalSettingsQueue = new ArrayDeque<Http2Settings>(4);
/*     */   
/*     */   public DefaultHttp2ConnectionEncoder(Http2Connection connection, Http2FrameWriter frameWriter) {
/*  49 */     this.connection = (Http2Connection)ObjectUtil.checkNotNull(connection, "connection");
/*  50 */     this.frameWriter = (Http2FrameWriter)ObjectUtil.checkNotNull(frameWriter, "frameWriter");
/*  51 */     if (connection.remote().flowController() == null) {
/*  52 */       connection.remote().flowController(new DefaultHttp2RemoteFlowController(connection));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void lifecycleManager(Http2LifecycleManager lifecycleManager) {
/*  58 */     this.lifecycleManager = (Http2LifecycleManager)ObjectUtil.checkNotNull(lifecycleManager, "lifecycleManager");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameWriter frameWriter() {
/*  63 */     return this.frameWriter;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Connection connection() {
/*  68 */     return this.connection;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Http2RemoteFlowController flowController() {
/*  73 */     return connection().remote().flowController();
/*     */   }
/*     */ 
/*     */   
/*     */   public void remoteSettings(Http2Settings settings) throws Http2Exception {
/*  78 */     Boolean pushEnabled = settings.pushEnabled();
/*  79 */     Http2FrameWriter.Configuration config = configuration();
/*  80 */     Http2HeadersEncoder.Configuration outboundHeaderConfig = config.headersConfiguration();
/*  81 */     Http2FrameSizePolicy outboundFrameSizePolicy = config.frameSizePolicy();
/*  82 */     if (pushEnabled != null) {
/*  83 */       if (!this.connection.isServer() && pushEnabled.booleanValue()) {
/*  84 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Client received a value of ENABLE_PUSH specified to other than 0", new Object[0]);
/*     */       }
/*     */       
/*  87 */       this.connection.remote().allowPushTo(pushEnabled.booleanValue());
/*     */     } 
/*     */     
/*  90 */     Long maxConcurrentStreams = settings.maxConcurrentStreams();
/*  91 */     if (maxConcurrentStreams != null) {
/*  92 */       this.connection.local().maxActiveStreams((int)Math.min(maxConcurrentStreams.longValue(), 2147483647L));
/*     */     }
/*     */     
/*  95 */     Long headerTableSize = settings.headerTableSize();
/*  96 */     if (headerTableSize != null) {
/*  97 */       outboundHeaderConfig.maxHeaderTableSize((int)Math.min(headerTableSize.longValue(), 2147483647L));
/*     */     }
/*     */     
/* 100 */     Long maxHeaderListSize = settings.maxHeaderListSize();
/* 101 */     if (maxHeaderListSize != null) {
/* 102 */       outboundHeaderConfig.maxHeaderListSize(maxHeaderListSize.longValue());
/*     */     }
/*     */     
/* 105 */     Integer maxFrameSize = settings.maxFrameSize();
/* 106 */     if (maxFrameSize != null) {
/* 107 */       outboundFrameSizePolicy.maxFrameSize(maxFrameSize.intValue());
/*     */     }
/*     */     
/* 110 */     Integer initialWindowSize = settings.initialWindowSize();
/* 111 */     if (initialWindowSize != null) {
/* 112 */       flowController().initialWindowSize(initialWindowSize.intValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream, ChannelPromise promise) {
/*     */     try {
/* 121 */       Http2Stream stream = requireStream(streamId);
/*     */ 
/*     */       
/* 124 */       switch (stream.state()) {
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
/*     */         case OPEN:
/*     */         case HALF_CLOSED_REMOTE:
/* 138 */           flowController().addFlowControlled(stream, new FlowControlledData(stream, data, padding, endOfStream, promise));
/*     */           
/* 140 */           return (ChannelFuture)promise;
/*     */       } 
/*     */       throw new IllegalStateException("Stream " + stream.id() + " in unexpected state " + stream.state());
/*     */     } catch (Throwable e) {
/*     */       data.release();
/*     */       return (ChannelFuture)promise.setFailure(e);
/* 146 */     }  } public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise) { return writeHeaders(ctx, streamId, headers, 0, (short)16, false, padding, endStream, promise); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validateHeadersSentState(Http2Stream stream, Http2Headers headers, boolean isServer, boolean endOfStream) {
/* 151 */     boolean isInformational = (isServer && HttpStatusClass.valueOf(headers.status()) == HttpStatusClass.INFORMATIONAL);
/* 152 */     if (((isInformational || !endOfStream) && stream.isHeadersSent()) || stream.isTrailersSent()) {
/* 153 */       throw new IllegalStateException("Stream " + stream.id() + " sent too many headers EOS: " + endOfStream);
/*     */     }
/* 155 */     return isInformational;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise) {
/*     */     try {
/* 163 */       Http2Stream stream = this.connection.stream(streamId);
/* 164 */       if (stream == null) {
/*     */         try {
/* 166 */           stream = this.connection.local().createStream(streamId, endOfStream);
/* 167 */         } catch (Http2Exception cause) {
/* 168 */           if (this.connection.remote().mayHaveCreatedStream(streamId)) {
/* 169 */             promise.tryFailure(new IllegalStateException("Stream no longer exists: " + streamId, cause));
/* 170 */             return (ChannelFuture)promise;
/*     */           } 
/* 172 */           throw cause;
/*     */         } 
/*     */       } else {
/* 175 */         switch (stream.state()) {
/*     */           case RESERVED_LOCAL:
/* 177 */             stream.open(endOfStream);
/*     */             break;
/*     */           
/*     */           case OPEN:
/*     */           case HALF_CLOSED_REMOTE:
/*     */             break;
/*     */           default:
/* 184 */             throw new IllegalStateException("Stream " + stream.id() + " in unexpected state " + stream
/* 185 */                 .state());
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       } 
/* 191 */       Http2RemoteFlowController flowController = flowController();
/* 192 */       if (!endOfStream || !flowController.hasFlowControlled(stream)) {
/* 193 */         boolean isInformational = validateHeadersSentState(stream, headers, this.connection.isServer(), endOfStream);
/* 194 */         if (endOfStream) {
/* 195 */           final Http2Stream finalStream = stream;
/* 196 */           ChannelFutureListener closeStreamLocalListener = new ChannelFutureListener()
/*     */             {
/*     */               public void operationComplete(ChannelFuture future) throws Exception {
/* 199 */                 DefaultHttp2ConnectionEncoder.this.lifecycleManager.closeStreamLocal(finalStream, future);
/*     */               }
/*     */             };
/* 202 */           promise = promise.unvoid().addListener((GenericFutureListener)closeStreamLocalListener);
/*     */         } 
/*     */         
/* 205 */         ChannelFuture future = this.frameWriter.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream, promise);
/*     */ 
/*     */         
/* 208 */         Throwable failureCause = future.cause();
/* 209 */         if (failureCause == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 215 */           stream.headersSent(isInformational);
/*     */           
/* 217 */           if (!future.isSuccess())
/*     */           {
/* 219 */             notifyLifecycleManagerOnError(future, ctx);
/*     */           }
/*     */         } else {
/* 222 */           this.lifecycleManager.onError(ctx, true, failureCause);
/*     */         } 
/*     */         
/* 225 */         return future;
/*     */       } 
/*     */       
/* 228 */       flowController.addFlowControlled(stream, new FlowControlledHeaders(stream, headers, streamDependency, weight, exclusive, padding, true, promise));
/*     */ 
/*     */       
/* 231 */       return (ChannelFuture)promise;
/*     */     }
/* 233 */     catch (Throwable t) {
/* 234 */       this.lifecycleManager.onError(ctx, true, t);
/* 235 */       promise.tryFailure(t);
/* 236 */       return (ChannelFuture)promise;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writePriority(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive, ChannelPromise promise) {
/* 243 */     return this.frameWriter.writePriority(ctx, streamId, streamDependency, weight, exclusive, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
/* 250 */     return this.lifecycleManager.resetStream(ctx, streamId, errorCode, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeSettings(ChannelHandlerContext ctx, Http2Settings settings, ChannelPromise promise) {
/* 256 */     this.outstandingLocalSettingsQueue.add(settings);
/*     */     try {
/* 258 */       Boolean pushEnabled = settings.pushEnabled();
/* 259 */       if (pushEnabled != null && this.connection.isServer()) {
/* 260 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server sending SETTINGS frame with ENABLE_PUSH specified", new Object[0]);
/*     */       }
/* 262 */     } catch (Throwable e) {
/* 263 */       return (ChannelFuture)promise.setFailure(e);
/*     */     } 
/*     */     
/* 266 */     return this.frameWriter.writeSettings(ctx, settings, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture writeSettingsAck(ChannelHandlerContext ctx, ChannelPromise promise) {
/* 271 */     return this.frameWriter.writeSettingsAck(ctx, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture writePing(ChannelHandlerContext ctx, boolean ack, long data, ChannelPromise promise) {
/* 276 */     return this.frameWriter.writePing(ctx, ack, data, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writePushPromise(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding, ChannelPromise promise) {
/*     */     try {
/* 283 */       if (this.connection.goAwayReceived()) {
/* 284 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Sending PUSH_PROMISE after GO_AWAY received.", new Object[0]);
/*     */       }
/*     */       
/* 287 */       Http2Stream stream = requireStream(streamId);
/*     */       
/* 289 */       this.connection.local().reservePushStream(promisedStreamId, stream);
/*     */       
/* 291 */       ChannelFuture future = this.frameWriter.writePushPromise(ctx, streamId, promisedStreamId, headers, padding, promise);
/*     */ 
/*     */       
/* 294 */       Throwable failureCause = future.cause();
/* 295 */       if (failureCause == null) {
/*     */ 
/*     */         
/* 298 */         stream.pushPromiseSent();
/*     */         
/* 300 */         if (!future.isSuccess())
/*     */         {
/* 302 */           notifyLifecycleManagerOnError(future, ctx);
/*     */         }
/*     */       } else {
/* 305 */         this.lifecycleManager.onError(ctx, true, failureCause);
/*     */       } 
/* 307 */       return future;
/* 308 */     } catch (Throwable t) {
/* 309 */       this.lifecycleManager.onError(ctx, true, t);
/* 310 */       promise.tryFailure(t);
/* 311 */       return (ChannelFuture)promise;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeGoAway(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelPromise promise) {
/* 318 */     return this.lifecycleManager.goAway(ctx, lastStreamId, errorCode, debugData, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeWindowUpdate(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement, ChannelPromise promise) {
/* 324 */     return (ChannelFuture)promise.setFailure(new UnsupportedOperationException("Use the Http2[Inbound|Outbound]FlowController objects to control window sizes"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload, ChannelPromise promise) {
/* 331 */     return this.frameWriter.writeFrame(ctx, frameType, streamId, flags, payload, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 336 */     this.frameWriter.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Settings pollSentSettings() {
/* 341 */     return this.outstandingLocalSettingsQueue.poll();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameWriter.Configuration configuration() {
/* 346 */     return this.frameWriter.configuration();
/*     */   }
/*     */   
/*     */   private Http2Stream requireStream(int streamId) {
/* 350 */     Http2Stream stream = this.connection.stream(streamId);
/* 351 */     if (stream == null) {
/*     */       String message;
/* 353 */       if (this.connection.streamMayHaveExisted(streamId)) {
/* 354 */         message = "Stream no longer exists: " + streamId;
/*     */       } else {
/* 356 */         message = "Stream does not exist: " + streamId;
/*     */       } 
/* 358 */       throw new IllegalArgumentException(message);
/*     */     } 
/* 360 */     return stream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class FlowControlledData
/*     */     extends FlowControlledBase
/*     */   {
/*     */     private final CoalescingBufferQueue queue;
/*     */ 
/*     */ 
/*     */     
/*     */     private int dataSize;
/*     */ 
/*     */ 
/*     */     
/*     */     FlowControlledData(Http2Stream stream, ByteBuf buf, int padding, boolean endOfStream, ChannelPromise promise) {
/* 378 */       super(stream, padding, endOfStream, promise);
/* 379 */       this.queue = new CoalescingBufferQueue(promise.channel());
/* 380 */       this.queue.add(buf, promise);
/* 381 */       this.dataSize = this.queue.readableBytes();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 386 */       return this.dataSize + this.padding;
/*     */     }
/*     */ 
/*     */     
/*     */     public void error(ChannelHandlerContext ctx, Throwable cause) {
/* 391 */       this.queue.releaseAndFailAll(cause);
/*     */ 
/*     */       
/* 394 */       DefaultHttp2ConnectionEncoder.this.lifecycleManager.onError(ctx, true, cause);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(ChannelHandlerContext ctx, int allowedBytes) {
/* 399 */       int queuedData = this.queue.readableBytes();
/* 400 */       if (!this.endOfStream) {
/* 401 */         if (queuedData == 0) {
/*     */ 
/*     */ 
/*     */           
/* 405 */           ChannelPromise channelPromise = ctx.newPromise().addListener((GenericFutureListener)this);
/* 406 */           ctx.write(this.queue.remove(0, channelPromise), channelPromise);
/*     */           
/*     */           return;
/*     */         } 
/* 410 */         if (allowedBytes == 0) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 416 */       int writableData = Math.min(queuedData, allowedBytes);
/* 417 */       ChannelPromise writePromise = ctx.newPromise().addListener((GenericFutureListener)this);
/* 418 */       ByteBuf toWrite = this.queue.remove(writableData, writePromise);
/* 419 */       this.dataSize = this.queue.readableBytes();
/*     */ 
/*     */       
/* 422 */       int writablePadding = Math.min(allowedBytes - writableData, this.padding);
/* 423 */       this.padding -= writablePadding;
/*     */ 
/*     */       
/* 426 */       DefaultHttp2ConnectionEncoder.this.frameWriter().writeData(ctx, this.stream.id(), toWrite, writablePadding, (this.endOfStream && 
/* 427 */           size() == 0), writePromise);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean merge(ChannelHandlerContext ctx, Http2RemoteFlowController.FlowControlled next) {
/*     */       FlowControlledData nextData;
/* 433 */       if (FlowControlledData.class != next.getClass() || Integer.MAX_VALUE - (nextData = (FlowControlledData)next)
/* 434 */         .size() < size()) {
/* 435 */         return false;
/*     */       }
/* 437 */       nextData.queue.copyTo((AbstractCoalescingBufferQueue)this.queue);
/* 438 */       this.dataSize = this.queue.readableBytes();
/*     */       
/* 440 */       this.padding = Math.max(this.padding, nextData.padding);
/* 441 */       this.endOfStream = nextData.endOfStream;
/* 442 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyLifecycleManagerOnError(ChannelFuture future, final ChannelHandlerContext ctx) {
/* 447 */     future.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture future) throws Exception {
/* 450 */             Throwable cause = future.cause();
/* 451 */             if (cause != null) {
/* 452 */               DefaultHttp2ConnectionEncoder.this.lifecycleManager.onError(ctx, true, cause);
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private final class FlowControlledHeaders
/*     */     extends FlowControlledBase
/*     */   {
/*     */     private final Http2Headers headers;
/*     */     
/*     */     private final int streamDependency;
/*     */     
/*     */     private final short weight;
/*     */     
/*     */     private final boolean exclusive;
/*     */     
/*     */     FlowControlledHeaders(Http2Stream stream, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise) {
/* 471 */       super(stream, padding, endOfStream, promise);
/* 472 */       this.headers = headers;
/* 473 */       this.streamDependency = streamDependency;
/* 474 */       this.weight = weight;
/* 475 */       this.exclusive = exclusive;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 480 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void error(ChannelHandlerContext ctx, Throwable cause) {
/* 485 */       if (ctx != null) {
/* 486 */         DefaultHttp2ConnectionEncoder.this.lifecycleManager.onError(ctx, true, cause);
/*     */       }
/* 488 */       this.promise.tryFailure(cause);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(ChannelHandlerContext ctx, int allowedBytes) {
/* 493 */       boolean isInformational = DefaultHttp2ConnectionEncoder.validateHeadersSentState(this.stream, this.headers, DefaultHttp2ConnectionEncoder.this.connection.isServer(), this.endOfStream);
/* 494 */       if (this.promise.isVoid()) {
/* 495 */         this.promise = ctx.newPromise();
/*     */       }
/* 497 */       this.promise.addListener((GenericFutureListener)this);
/*     */       
/* 499 */       ChannelFuture f = DefaultHttp2ConnectionEncoder.this.frameWriter.writeHeaders(ctx, this.stream.id(), this.headers, this.streamDependency, this.weight, this.exclusive, this.padding, this.endOfStream, this.promise);
/*     */ 
/*     */       
/* 502 */       Throwable failureCause = f.cause();
/* 503 */       if (failureCause == null)
/*     */       {
/*     */         
/* 506 */         this.stream.headersSent(isInformational);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean merge(ChannelHandlerContext ctx, Http2RemoteFlowController.FlowControlled next) {
/* 512 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract class FlowControlledBase
/*     */     implements Http2RemoteFlowController.FlowControlled, ChannelFutureListener
/*     */   {
/*     */     protected final Http2Stream stream;
/*     */     
/*     */     protected ChannelPromise promise;
/*     */     
/*     */     protected boolean endOfStream;
/*     */     protected int padding;
/*     */     
/*     */     FlowControlledBase(Http2Stream stream, int padding, boolean endOfStream, ChannelPromise promise) {
/* 528 */       if (padding < 0) {
/* 529 */         throw new IllegalArgumentException("padding must be >= 0");
/*     */       }
/* 531 */       this.padding = padding;
/* 532 */       this.endOfStream = endOfStream;
/* 533 */       this.stream = stream;
/* 534 */       this.promise = promise;
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeComplete() {
/* 539 */       if (this.endOfStream) {
/* 540 */         DefaultHttp2ConnectionEncoder.this.lifecycleManager.closeStreamLocal(this.stream, (ChannelFuture)this.promise);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void operationComplete(ChannelFuture future) throws Exception {
/* 546 */       if (!future.isSuccess())
/* 547 */         error(DefaultHttp2ConnectionEncoder.this.flowController().channelHandlerContext(), future.cause()); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2ConnectionEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */