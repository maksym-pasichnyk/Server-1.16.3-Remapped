/*      */ package io.netty.handler.codec.http2;
/*      */ 
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.channel.Channel;
/*      */ import io.netty.channel.ChannelConfig;
/*      */ import io.netty.channel.ChannelFuture;
/*      */ import io.netty.channel.ChannelFutureListener;
/*      */ import io.netty.channel.ChannelHandler;
/*      */ import io.netty.channel.ChannelHandlerContext;
/*      */ import io.netty.channel.ChannelId;
/*      */ import io.netty.channel.ChannelMetadata;
/*      */ import io.netty.channel.ChannelOutboundBuffer;
/*      */ import io.netty.channel.ChannelOutboundInvoker;
/*      */ import io.netty.channel.ChannelPipeline;
/*      */ import io.netty.channel.ChannelProgressivePromise;
/*      */ import io.netty.channel.ChannelPromise;
/*      */ import io.netty.channel.DefaultChannelConfig;
/*      */ import io.netty.channel.DefaultChannelPipeline;
/*      */ import io.netty.channel.DefaultMaxMessagesRecvByteBufAllocator;
/*      */ import io.netty.channel.EventLoop;
/*      */ import io.netty.channel.MessageSizeEstimator;
/*      */ import io.netty.channel.RecvByteBufAllocator;
/*      */ import io.netty.channel.VoidChannelPromise;
/*      */ import io.netty.channel.WriteBufferWaterMark;
/*      */ import io.netty.util.DefaultAttributeMap;
/*      */ import io.netty.util.ReferenceCountUtil;
/*      */ import io.netty.util.concurrent.Future;
/*      */ import io.netty.util.concurrent.GenericFutureListener;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.ThrowableUtil;
/*      */ import java.net.SocketAddress;
/*      */ import java.nio.channels.ClosedChannelException;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.Queue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Http2MultiplexCodec
/*      */   extends Http2FrameCodec
/*      */ {
/*  103 */   private static final ChannelFutureListener CHILD_CHANNEL_REGISTRATION_LISTENER = new ChannelFutureListener()
/*      */     {
/*      */       public void operationComplete(ChannelFuture future) throws Exception {
/*  106 */         Http2MultiplexCodec.registerDone(future);
/*      */       }
/*      */     };
/*      */   
/*  110 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
/*  111 */   private static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), DefaultHttp2StreamChannel.Http2ChannelUnsafe.class, "write(...)");
/*      */ 
/*      */   
/*      */   private static final int MIN_HTTP2_FRAME_SIZE = 9;
/*      */ 
/*      */   
/*      */   private final ChannelHandler inboundStreamHandler;
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class FlowControlledFrameSizeEstimator
/*      */     implements MessageSizeEstimator
/*      */   {
/*  124 */     static final FlowControlledFrameSizeEstimator INSTANCE = new FlowControlledFrameSizeEstimator();
/*      */     
/*  126 */     static final MessageSizeEstimator.Handle HANDLE_INSTANCE = new MessageSizeEstimator.Handle()
/*      */       {
/*      */         public int size(Object msg) {
/*  129 */           return (msg instanceof Http2DataFrame) ? 
/*      */             
/*  131 */             (int)Math.min(2147483647L, ((Http2DataFrame)msg).initialFlowControlledBytes() + 9L) : 9;
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */     
/*      */     public MessageSizeEstimator.Handle newHandle() {
/*  138 */       return HANDLE_INSTANCE;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class Http2StreamChannelRecvByteBufAllocator extends DefaultMaxMessagesRecvByteBufAllocator {
/*      */     private Http2StreamChannelRecvByteBufAllocator() {}
/*      */     
/*      */     public DefaultMaxMessagesRecvByteBufAllocator.MaxMessageHandle newHandle() {
/*  146 */       return new DefaultMaxMessagesRecvByteBufAllocator.MaxMessageHandle()
/*      */         {
/*      */           public int guess() {
/*  149 */             return 1024;
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  157 */   private int initialOutboundStreamWindow = 65535;
/*      */ 
/*      */   
/*      */   private boolean parentReadInProgress;
/*      */   
/*      */   private int idCount;
/*      */   
/*      */   private DefaultHttp2StreamChannel head;
/*      */   
/*      */   private DefaultHttp2StreamChannel tail;
/*      */   
/*      */   volatile ChannelHandlerContext ctx;
/*      */ 
/*      */   
/*      */   Http2MultiplexCodec(Http2ConnectionEncoder encoder, Http2ConnectionDecoder decoder, Http2Settings initialSettings, ChannelHandler inboundStreamHandler) {
/*  172 */     super(encoder, decoder, initialSettings);
/*  173 */     this.inboundStreamHandler = inboundStreamHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void registerDone(ChannelFuture future) {
/*  180 */     if (!future.isSuccess()) {
/*  181 */       Channel childChannel = future.channel();
/*  182 */       if (childChannel.isRegistered()) {
/*  183 */         childChannel.close();
/*      */       } else {
/*  185 */         childChannel.unsafe().closeForcibly();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void handlerAdded0(ChannelHandlerContext ctx) throws Exception {
/*  192 */     if (ctx.executor() != ctx.channel().eventLoop()) {
/*  193 */       throw new IllegalStateException("EventExecutor must be EventLoop of Channel");
/*      */     }
/*  195 */     this.ctx = ctx;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
/*  200 */     super.handlerRemoved0(ctx);
/*      */ 
/*      */     
/*  203 */     DefaultHttp2StreamChannel ch = this.head;
/*  204 */     while (ch != null) {
/*  205 */       DefaultHttp2StreamChannel curr = ch;
/*  206 */       ch = curr.next;
/*  207 */       curr.next = null;
/*      */     } 
/*  209 */     this.head = this.tail = null;
/*      */   }
/*      */ 
/*      */   
/*      */   Http2MultiplexCodecStream newStream() {
/*  214 */     return new Http2MultiplexCodecStream();
/*      */   }
/*      */ 
/*      */   
/*      */   final void onHttp2Frame(ChannelHandlerContext ctx, Http2Frame frame) {
/*  219 */     if (frame instanceof Http2StreamFrame) {
/*  220 */       Http2StreamFrame streamFrame = (Http2StreamFrame)frame;
/*  221 */       onHttp2StreamFrame(((Http2MultiplexCodecStream)streamFrame.stream()).channel, streamFrame);
/*  222 */     } else if (frame instanceof Http2GoAwayFrame) {
/*  223 */       onHttp2GoAwayFrame(ctx, (Http2GoAwayFrame)frame);
/*      */       
/*  225 */       ctx.fireChannelRead(frame);
/*  226 */     } else if (frame instanceof Http2SettingsFrame) {
/*  227 */       Http2Settings settings = ((Http2SettingsFrame)frame).settings();
/*  228 */       if (settings.initialWindowSize() != null) {
/*  229 */         this.initialOutboundStreamWindow = settings.initialWindowSize().intValue();
/*      */       }
/*      */       
/*  232 */       ctx.fireChannelRead(frame);
/*      */     } else {
/*      */       
/*  235 */       ctx.fireChannelRead(frame);
/*      */     } 
/*      */   }
/*      */   final void onHttp2StreamStateChanged(ChannelHandlerContext ctx, Http2FrameStream stream) {
/*      */     ChannelFuture future;
/*      */     DefaultHttp2StreamChannel channel;
/*  241 */     Http2MultiplexCodecStream s = (Http2MultiplexCodecStream)stream;
/*      */     
/*  243 */     switch (stream.state()) {
/*      */       case READ_PROCESSED_BUT_STOP_READING:
/*      */       case READ_PROCESSED_OK_TO_PROCESS_MORE:
/*  246 */         if (s.channel != null) {
/*      */           break;
/*      */         }
/*      */ 
/*      */         
/*  251 */         future = ctx.channel().eventLoop().register(new DefaultHttp2StreamChannel(s, false));
/*  252 */         if (future.isDone()) {
/*  253 */           registerDone(future); break;
/*      */         } 
/*  255 */         future.addListener((GenericFutureListener)CHILD_CHANNEL_REGISTRATION_LISTENER);
/*      */         break;
/*      */       
/*      */       case READ_IGNORED_CHANNEL_INACTIVE:
/*  259 */         channel = s.channel;
/*  260 */         if (channel != null) {
/*  261 */           channel.streamClosed();
/*      */         }
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void onHttp2StreamWritabilityChanged(ChannelHandlerContext ctx, Http2FrameStream stream, boolean writable) {
/*  272 */     ((Http2MultiplexCodecStream)stream).channel.writabilityChanged(writable);
/*      */   }
/*      */ 
/*      */   
/*      */   final Http2StreamChannel newOutboundStream() {
/*  277 */     return new DefaultHttp2StreamChannel(newStream(), true);
/*      */   }
/*      */ 
/*      */   
/*      */   final void onHttp2FrameStreamException(ChannelHandlerContext ctx, Http2FrameStreamException cause) {
/*  282 */     Http2FrameStream stream = cause.stream();
/*  283 */     DefaultHttp2StreamChannel childChannel = ((Http2MultiplexCodecStream)stream).channel;
/*      */     
/*      */     try {
/*  286 */       childChannel.pipeline().fireExceptionCaught(cause.getCause());
/*      */     } finally {
/*  288 */       childChannel.unsafe().closeForcibly();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void onHttp2StreamFrame(DefaultHttp2StreamChannel childChannel, Http2StreamFrame frame) {
/*  293 */     switch (childChannel.fireChildRead(frame)) {
/*      */       case READ_PROCESSED_BUT_STOP_READING:
/*  295 */         childChannel.fireChildReadComplete();
/*      */       
/*      */       case READ_PROCESSED_OK_TO_PROCESS_MORE:
/*  298 */         addChildChannelToReadPendingQueue(childChannel);
/*      */       
/*      */       case READ_IGNORED_CHANNEL_INACTIVE:
/*      */       case READ_QUEUED:
/*      */         return;
/*      */     } 
/*      */     
/*  305 */     throw new Error();
/*      */   }
/*      */ 
/*      */   
/*      */   final void addChildChannelToReadPendingQueue(DefaultHttp2StreamChannel childChannel) {
/*  310 */     if (!childChannel.fireChannelReadPending) {
/*  311 */       assert childChannel.next == null;
/*      */       
/*  313 */       if (this.tail == null) {
/*  314 */         assert this.head == null;
/*  315 */         this.tail = this.head = childChannel;
/*      */       } else {
/*  317 */         this.tail.next = childChannel;
/*  318 */         this.tail = childChannel;
/*      */       } 
/*  320 */       childChannel.fireChannelReadPending = true;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void onHttp2GoAwayFrame(ChannelHandlerContext ctx, final Http2GoAwayFrame goAwayFrame) {
/*      */     try {
/*  326 */       forEachActiveStream(new Http2FrameStreamVisitor()
/*      */           {
/*      */             public boolean visit(Http2FrameStream stream) {
/*  329 */               int streamId = stream.id();
/*  330 */               Http2MultiplexCodec.DefaultHttp2StreamChannel childChannel = ((Http2MultiplexCodec.Http2MultiplexCodecStream)stream).channel;
/*  331 */               if (streamId > goAwayFrame.lastStreamId() && Http2MultiplexCodec.this.connection().local().isValidStreamId(streamId)) {
/*  332 */                 childChannel.pipeline().fireUserEventTriggered(goAwayFrame.retainedDuplicate());
/*      */               }
/*  334 */               return true;
/*      */             }
/*      */           });
/*  337 */     } catch (Http2Exception e) {
/*  338 */       ctx.fireExceptionCaught(e);
/*  339 */       ctx.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
/*  348 */     this.parentReadInProgress = false;
/*  349 */     onChannelReadComplete(ctx);
/*  350 */     channelReadComplete0(ctx);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*  355 */     this.parentReadInProgress = true;
/*  356 */     super.channelRead(ctx, msg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void onChannelReadComplete(ChannelHandlerContext ctx) {
/*      */     try {
/*  364 */       DefaultHttp2StreamChannel current = this.head;
/*  365 */       while (current != null) {
/*  366 */         DefaultHttp2StreamChannel childChannel = current;
/*  367 */         if (childChannel.fireChannelReadPending) {
/*      */           
/*  369 */           childChannel.fireChannelReadPending = false;
/*  370 */           childChannel.fireChildReadComplete();
/*      */         } 
/*  372 */         childChannel.next = null;
/*  373 */         current = current.next;
/*      */       } 
/*      */     } finally {
/*  376 */       this.tail = this.head = null;
/*      */ 
/*      */       
/*  379 */       flush0(ctx);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void flush0(ChannelHandlerContext ctx) {
/*  385 */     flush(ctx);
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
/*      */   boolean onBytesConsumed(ChannelHandlerContext ctx, Http2FrameStream stream, int bytes) throws Http2Exception {
/*  400 */     return consumeBytes(stream.id(), bytes);
/*      */   }
/*      */   
/*      */   static class Http2MultiplexCodecStream
/*      */     extends Http2FrameCodec.DefaultHttp2FrameStream {
/*      */     Http2MultiplexCodec.DefaultHttp2StreamChannel channel;
/*      */   }
/*      */   
/*      */   private enum ReadState {
/*  409 */     READ_QUEUED,
/*  410 */     READ_IGNORED_CHANNEL_INACTIVE,
/*  411 */     READ_PROCESSED_BUT_STOP_READING,
/*  412 */     READ_PROCESSED_OK_TO_PROCESS_MORE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean initialWritability(Http2FrameCodec.DefaultHttp2FrameStream stream) {
/*  419 */     return (!Http2CodecUtil.isStreamIdValid(stream.id()) || isWritable(stream));
/*      */   }
/*      */   
/*      */   private final class DefaultHttp2StreamChannel
/*      */     extends DefaultAttributeMap implements Http2StreamChannel {
/*  424 */     private final Http2StreamChannelConfig config = new Http2StreamChannelConfig(this);
/*  425 */     private final Http2ChannelUnsafe unsafe = new Http2ChannelUnsafe();
/*      */     
/*      */     private final ChannelId channelId;
/*      */     
/*      */     private final ChannelPipeline pipeline;
/*      */     
/*      */     private final Http2FrameCodec.DefaultHttp2FrameStream stream;
/*      */     
/*      */     private final ChannelPromise closePromise;
/*      */     
/*      */     private final boolean outbound;
/*      */     
/*      */     private volatile boolean registered;
/*      */     
/*      */     private volatile boolean writable;
/*      */     
/*      */     private boolean outboundClosed;
/*      */     
/*      */     private boolean closePending;
/*      */     
/*      */     private boolean readInProgress;
/*      */     
/*      */     private Queue<Object> inboundBuffer;
/*      */     
/*      */     private boolean firstFrameWritten;
/*      */     
/*      */     private boolean streamClosedWithoutError;
/*      */     private boolean inFireChannelReadComplete;
/*      */     boolean fireChannelReadPending;
/*      */     DefaultHttp2StreamChannel next;
/*      */     
/*      */     DefaultHttp2StreamChannel(Http2FrameCodec.DefaultHttp2FrameStream stream, boolean outbound) {
/*  457 */       this.stream = stream;
/*  458 */       this.outbound = outbound;
/*  459 */       this.writable = Http2MultiplexCodec.this.initialWritability(stream);
/*  460 */       ((Http2MultiplexCodec.Http2MultiplexCodecStream)stream).channel = this;
/*  461 */       this.pipeline = (ChannelPipeline)new DefaultChannelPipeline(this)
/*      */         {
/*      */           protected void incrementPendingOutboundBytes(long size) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           protected void decrementPendingOutboundBytes(long size) {}
/*      */         };
/*  472 */       this.closePromise = this.pipeline.newPromise();
/*  473 */       this.channelId = new Http2StreamChannelId(parent().id(), ++Http2MultiplexCodec.this.idCount);
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2FrameStream stream() {
/*  478 */       return this.stream;
/*      */     }
/*      */     
/*      */     void streamClosed() {
/*  482 */       this.streamClosedWithoutError = true;
/*  483 */       if (this.readInProgress) {
/*      */         
/*  485 */         unsafe().closeForcibly();
/*      */       } else {
/*  487 */         this.closePending = true;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelMetadata metadata() {
/*  493 */       return Http2MultiplexCodec.METADATA;
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelConfig config() {
/*  498 */       return (ChannelConfig)this.config;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isOpen() {
/*  503 */       return !this.closePromise.isDone();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/*  508 */       return isOpen();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isWritable() {
/*  513 */       return this.writable;
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelId id() {
/*  518 */       return this.channelId;
/*      */     }
/*      */ 
/*      */     
/*      */     public EventLoop eventLoop() {
/*  523 */       return parent().eventLoop();
/*      */     }
/*      */ 
/*      */     
/*      */     public Channel parent() {
/*  528 */       return Http2MultiplexCodec.this.ctx.channel();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isRegistered() {
/*  533 */       return this.registered;
/*      */     }
/*      */ 
/*      */     
/*      */     public SocketAddress localAddress() {
/*  538 */       return parent().localAddress();
/*      */     }
/*      */ 
/*      */     
/*      */     public SocketAddress remoteAddress() {
/*  543 */       return parent().remoteAddress();
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture closeFuture() {
/*  548 */       return (ChannelFuture)this.closePromise;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public long bytesBeforeUnwritable() {
/*  554 */       return config().getWriteBufferHighWaterMark();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public long bytesBeforeWritable() {
/*  560 */       return 0L;
/*      */     }
/*      */ 
/*      */     
/*      */     public Channel.Unsafe unsafe() {
/*  565 */       return this.unsafe;
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelPipeline pipeline() {
/*  570 */       return this.pipeline;
/*      */     }
/*      */ 
/*      */     
/*      */     public ByteBufAllocator alloc() {
/*  575 */       return config().getAllocator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Channel read() {
/*  580 */       pipeline().read();
/*  581 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public Channel flush() {
/*  586 */       pipeline().flush();
/*  587 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture bind(SocketAddress localAddress) {
/*  592 */       return pipeline().bind(localAddress);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture connect(SocketAddress remoteAddress) {
/*  597 */       return pipeline().connect(remoteAddress);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
/*  602 */       return pipeline().connect(remoteAddress, localAddress);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture disconnect() {
/*  607 */       return pipeline().disconnect();
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture close() {
/*  612 */       return pipeline().close();
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture deregister() {
/*  617 */       return pipeline().deregister();
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
/*  622 */       return pipeline().bind(localAddress, promise);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
/*  627 */       return pipeline().connect(remoteAddress, promise);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/*  632 */       return pipeline().connect(remoteAddress, localAddress, promise);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture disconnect(ChannelPromise promise) {
/*  637 */       return pipeline().disconnect(promise);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture close(ChannelPromise promise) {
/*  642 */       return pipeline().close(promise);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture deregister(ChannelPromise promise) {
/*  647 */       return pipeline().deregister(promise);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture write(Object msg) {
/*  652 */       return pipeline().write(msg);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture write(Object msg, ChannelPromise promise) {
/*  657 */       return pipeline().write(msg, promise);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
/*  662 */       return pipeline().writeAndFlush(msg, promise);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture writeAndFlush(Object msg) {
/*  667 */       return pipeline().writeAndFlush(msg);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelPromise newPromise() {
/*  672 */       return pipeline().newPromise();
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelProgressivePromise newProgressivePromise() {
/*  677 */       return pipeline().newProgressivePromise();
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture newSucceededFuture() {
/*  682 */       return pipeline().newSucceededFuture();
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelFuture newFailedFuture(Throwable cause) {
/*  687 */       return pipeline().newFailedFuture(cause);
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelPromise voidPromise() {
/*  692 */       return pipeline().voidPromise();
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  697 */       return id().hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  702 */       return (this == o);
/*      */     }
/*      */ 
/*      */     
/*      */     public int compareTo(Channel o) {
/*  707 */       if (this == o) {
/*  708 */         return 0;
/*      */       }
/*      */       
/*  711 */       return id().compareTo(o.id());
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  716 */       return parent().toString() + "(H2 - " + this.stream + ')';
/*      */     }
/*      */     
/*      */     void writabilityChanged(boolean writable) {
/*  720 */       assert eventLoop().inEventLoop();
/*  721 */       if (writable != this.writable && isActive()) {
/*      */         
/*  723 */         this.writable = writable;
/*  724 */         pipeline().fireChannelWritabilityChanged();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Http2MultiplexCodec.ReadState fireChildRead(Http2Frame frame) {
/*  733 */       assert eventLoop().inEventLoop();
/*  734 */       if (!isActive()) {
/*  735 */         ReferenceCountUtil.release(frame);
/*  736 */         return Http2MultiplexCodec.ReadState.READ_IGNORED_CHANNEL_INACTIVE;
/*      */       } 
/*  738 */       if (this.readInProgress && (this.inboundBuffer == null || this.inboundBuffer.isEmpty())) {
/*      */ 
/*      */         
/*  741 */         RecvByteBufAllocator.ExtendedHandle allocHandle = this.unsafe.recvBufAllocHandle();
/*  742 */         this.unsafe.doRead0(frame, (RecvByteBufAllocator.Handle)allocHandle);
/*  743 */         return allocHandle.continueReading() ? Http2MultiplexCodec.ReadState.READ_PROCESSED_OK_TO_PROCESS_MORE : Http2MultiplexCodec.ReadState.READ_PROCESSED_BUT_STOP_READING;
/*      */       } 
/*      */       
/*  746 */       if (this.inboundBuffer == null) {
/*  747 */         this.inboundBuffer = new ArrayDeque(4);
/*      */       }
/*  749 */       this.inboundBuffer.add(frame);
/*  750 */       return Http2MultiplexCodec.ReadState.READ_QUEUED;
/*      */     }
/*      */ 
/*      */     
/*      */     void fireChildReadComplete() {
/*  755 */       assert eventLoop().inEventLoop();
/*      */       try {
/*  757 */         if (this.readInProgress) {
/*  758 */           this.inFireChannelReadComplete = true;
/*  759 */           this.readInProgress = false;
/*  760 */           unsafe().recvBufAllocHandle().readComplete();
/*  761 */           pipeline().fireChannelReadComplete();
/*      */         } 
/*      */       } finally {
/*  764 */         this.inFireChannelReadComplete = false;
/*      */       } 
/*      */     }
/*      */     
/*      */     private final class Http2ChannelUnsafe implements Channel.Unsafe {
/*  769 */       private final VoidChannelPromise unsafeVoidPromise = new VoidChannelPromise(Http2MultiplexCodec.DefaultHttp2StreamChannel.this, false);
/*      */       
/*      */       private RecvByteBufAllocator.ExtendedHandle recvHandle;
/*      */       
/*      */       private boolean writeDoneAndNoFlush;
/*      */       
/*      */       private boolean closeInitiated;
/*      */ 
/*      */       
/*      */       public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/*  779 */         if (!promise.setUncancellable()) {
/*      */           return;
/*      */         }
/*  782 */         promise.setFailure(new UnsupportedOperationException());
/*      */       }
/*      */ 
/*      */       
/*      */       public RecvByteBufAllocator.ExtendedHandle recvBufAllocHandle() {
/*  787 */         if (this.recvHandle == null) {
/*  788 */           this.recvHandle = (RecvByteBufAllocator.ExtendedHandle)Http2MultiplexCodec.DefaultHttp2StreamChannel.this.config().getRecvByteBufAllocator().newHandle();
/*      */         }
/*  790 */         return this.recvHandle;
/*      */       }
/*      */ 
/*      */       
/*      */       public SocketAddress localAddress() {
/*  795 */         return Http2MultiplexCodec.DefaultHttp2StreamChannel.this.parent().unsafe().localAddress();
/*      */       }
/*      */ 
/*      */       
/*      */       public SocketAddress remoteAddress() {
/*  800 */         return Http2MultiplexCodec.DefaultHttp2StreamChannel.this.parent().unsafe().remoteAddress();
/*      */       }
/*      */ 
/*      */       
/*      */       public void register(EventLoop eventLoop, ChannelPromise promise) {
/*  805 */         if (!promise.setUncancellable()) {
/*      */           return;
/*      */         }
/*  808 */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.registered) {
/*  809 */           throw new UnsupportedOperationException("Re-register is not supported");
/*      */         }
/*      */         
/*  812 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.registered = true;
/*      */         
/*  814 */         if (!Http2MultiplexCodec.DefaultHttp2StreamChannel.this.outbound)
/*      */         {
/*  816 */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().addLast(new ChannelHandler[] { Http2MultiplexCodec.access$700(this.this$1.this$0) });
/*      */         }
/*      */         
/*  819 */         promise.setSuccess();
/*      */         
/*  821 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelRegistered();
/*  822 */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.isActive()) {
/*  823 */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelActive();
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*      */       public void bind(SocketAddress localAddress, ChannelPromise promise) {
/*  829 */         if (!promise.setUncancellable()) {
/*      */           return;
/*      */         }
/*  832 */         promise.setFailure(new UnsupportedOperationException());
/*      */       }
/*      */ 
/*      */       
/*      */       public void disconnect(ChannelPromise promise) {
/*  837 */         close(promise);
/*      */       }
/*      */ 
/*      */       
/*      */       public void close(final ChannelPromise promise) {
/*  842 */         if (!promise.setUncancellable()) {
/*      */           return;
/*      */         }
/*  845 */         if (this.closeInitiated) {
/*  846 */           if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePromise.isDone()) {
/*      */             
/*  848 */             promise.setSuccess();
/*  849 */           } else if (!(promise instanceof VoidChannelPromise)) {
/*      */             
/*  851 */             Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePromise.addListener((GenericFutureListener)new ChannelFutureListener()
/*      */                 {
/*      */                   public void operationComplete(ChannelFuture future) throws Exception {
/*  854 */                     promise.setSuccess();
/*      */                   }
/*      */                 });
/*      */           } 
/*      */           return;
/*      */         } 
/*  860 */         this.closeInitiated = true;
/*      */         
/*  862 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePending = false;
/*  863 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.fireChannelReadPending = false;
/*      */ 
/*      */ 
/*      */         
/*  867 */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.parent().isActive() && !Http2MultiplexCodec.DefaultHttp2StreamChannel.this.streamClosedWithoutError && Http2CodecUtil.isStreamIdValid(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream().id())) {
/*  868 */           Http2StreamFrame resetFrame = (new DefaultHttp2ResetFrame(Http2Error.CANCEL)).stream(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream());
/*  869 */           write(resetFrame, Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe().voidPromise());
/*  870 */           flush();
/*      */         } 
/*      */         
/*  873 */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer != null) {
/*      */           while (true) {
/*  875 */             Object msg = Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer.poll();
/*  876 */             if (msg == null) {
/*      */               break;
/*      */             }
/*  879 */             ReferenceCountUtil.release(msg);
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/*  884 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.outboundClosed = true;
/*  885 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePromise.setSuccess();
/*  886 */         promise.setSuccess();
/*      */         
/*  888 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelInactive();
/*  889 */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.isRegistered()) {
/*  890 */           deregister(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe().voidPromise());
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*      */       public void closeForcibly() {
/*  896 */         close(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe().voidPromise());
/*      */       }
/*      */ 
/*      */       
/*      */       public void deregister(ChannelPromise promise) {
/*  901 */         if (!promise.setUncancellable()) {
/*      */           return;
/*      */         }
/*  904 */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.registered) {
/*  905 */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.registered = true;
/*  906 */           promise.setSuccess();
/*  907 */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelUnregistered();
/*      */         } else {
/*  909 */           promise.setFailure(new IllegalStateException("Not registered"));
/*      */         } 
/*      */       }
/*      */       
/*      */       public void beginRead() {
/*      */         Object m;
/*  915 */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.readInProgress || !Http2MultiplexCodec.DefaultHttp2StreamChannel.this.isActive()) {
/*      */           return;
/*      */         }
/*  918 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.readInProgress = true;
/*      */         
/*  920 */         RecvByteBufAllocator.Handle allocHandle = Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe().recvBufAllocHandle();
/*  921 */         allocHandle.reset(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.config());
/*  922 */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer == null || Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer.isEmpty()) {
/*  923 */           if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePending) {
/*  924 */             Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe.closeForcibly();
/*      */           }
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/*      */         boolean continueReading;
/*      */         
/*      */         do {
/*  933 */           m = Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer.poll();
/*  934 */           if (m == null) {
/*  935 */             boolean bool = false;
/*      */             break;
/*      */           } 
/*  938 */           doRead0((Http2Frame)m, allocHandle);
/*  939 */         } while (continueReading = allocHandle.continueReading());
/*      */         
/*  941 */         if (continueReading && Http2MultiplexCodec.this.parentReadInProgress) {
/*      */ 
/*      */           
/*  944 */           Http2MultiplexCodec.this.addChildChannelToReadPendingQueue(Http2MultiplexCodec.DefaultHttp2StreamChannel.this);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  949 */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.readInProgress = false;
/*  950 */           allocHandle.readComplete();
/*  951 */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelReadComplete();
/*  952 */           flush();
/*  953 */           if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePending) {
/*  954 */             Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe.closeForcibly();
/*      */           }
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*      */       void doRead0(Http2Frame frame, RecvByteBufAllocator.Handle allocHandle) {
/*  961 */         int numBytesToBeConsumed = 0;
/*  962 */         if (frame instanceof Http2DataFrame) {
/*  963 */           numBytesToBeConsumed = ((Http2DataFrame)frame).initialFlowControlledBytes();
/*  964 */           allocHandle.lastBytesRead(numBytesToBeConsumed);
/*      */         } else {
/*  966 */           allocHandle.lastBytesRead(9);
/*      */         } 
/*  968 */         allocHandle.incMessagesRead(1);
/*  969 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelRead(frame);
/*      */         
/*  971 */         if (numBytesToBeConsumed != 0) {
/*      */           try {
/*  973 */             this.writeDoneAndNoFlush |= Http2MultiplexCodec.this.onBytesConsumed(Http2MultiplexCodec.this.ctx, Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream, numBytesToBeConsumed);
/*  974 */           } catch (Http2Exception e) {
/*  975 */             Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireExceptionCaught(e);
/*      */           } 
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void write(Object msg, final ChannelPromise promise) {
/*  983 */         if (!promise.setUncancellable()) {
/*  984 */           ReferenceCountUtil.release(msg);
/*      */           
/*      */           return;
/*      */         } 
/*  988 */         if (!Http2MultiplexCodec.DefaultHttp2StreamChannel.this.isActive() || (Http2MultiplexCodec.DefaultHttp2StreamChannel.this
/*      */           
/*  990 */           .outboundClosed && (msg instanceof Http2HeadersFrame || msg instanceof Http2DataFrame))) {
/*  991 */           ReferenceCountUtil.release(msg);
/*  992 */           promise.setFailure(Http2MultiplexCodec.CLOSED_CHANNEL_EXCEPTION);
/*      */           
/*      */           return;
/*      */         } 
/*      */         try {
/*  997 */           if (msg instanceof Http2StreamFrame) {
/*  998 */             Http2StreamFrame frame = validateStreamFrame((Http2StreamFrame)msg).stream(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream());
/*  999 */             if (!Http2MultiplexCodec.DefaultHttp2StreamChannel.this.firstFrameWritten && !Http2CodecUtil.isStreamIdValid(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream().id())) {
/* 1000 */               if (!(frame instanceof Http2HeadersFrame)) {
/* 1001 */                 ReferenceCountUtil.release(frame);
/* 1002 */                 promise.setFailure(new IllegalArgumentException("The first frame must be a headers frame. Was: " + frame
/*      */                       
/* 1004 */                       .name()));
/*      */                 return;
/*      */               } 
/* 1007 */               Http2MultiplexCodec.DefaultHttp2StreamChannel.this.firstFrameWritten = true;
/* 1008 */               ChannelFuture channelFuture = write0(frame);
/* 1009 */               if (channelFuture.isDone()) {
/* 1010 */                 firstWriteComplete(channelFuture, promise);
/*      */               } else {
/* 1012 */                 channelFuture.addListener((GenericFutureListener)new ChannelFutureListener()
/*      */                     {
/*      */                       public void operationComplete(ChannelFuture future) throws Exception {
/* 1015 */                         Http2MultiplexCodec.DefaultHttp2StreamChannel.Http2ChannelUnsafe.this.firstWriteComplete(future, promise);
/*      */                       }
/*      */                     });
/*      */               } 
/*      */               return;
/*      */             } 
/*      */           } else {
/* 1022 */             String msgStr = msg.toString();
/* 1023 */             ReferenceCountUtil.release(msg);
/* 1024 */             promise.setFailure(new IllegalArgumentException("Message must be an " + 
/* 1025 */                   StringUtil.simpleClassName(Http2StreamFrame.class) + ": " + msgStr));
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/* 1030 */           ChannelFuture future = write0(msg);
/* 1031 */           if (future.isDone()) {
/* 1032 */             writeComplete(future, promise);
/*      */           } else {
/* 1034 */             future.addListener((GenericFutureListener)new ChannelFutureListener()
/*      */                 {
/*      */                   public void operationComplete(ChannelFuture future) throws Exception {
/* 1037 */                     Http2MultiplexCodec.DefaultHttp2StreamChannel.Http2ChannelUnsafe.this.writeComplete(future, promise);
/*      */                   }
/*      */                 });
/*      */           } 
/* 1041 */         } catch (Throwable t) {
/* 1042 */           promise.tryFailure(t);
/*      */         } finally {
/* 1044 */           this.writeDoneAndNoFlush = true;
/*      */         } 
/*      */       }
/*      */       
/*      */       private void firstWriteComplete(ChannelFuture future, ChannelPromise promise) {
/* 1049 */         Throwable cause = future.cause();
/* 1050 */         if (cause == null) {
/*      */ 
/*      */           
/* 1053 */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.writabilityChanged(Http2MultiplexCodec.this.isWritable(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream));
/* 1054 */           promise.setSuccess();
/*      */         } else {
/* 1056 */           promise.setFailure(wrapStreamClosedError(cause));
/*      */           
/* 1058 */           closeForcibly();
/*      */         } 
/*      */       }
/*      */       
/*      */       private void writeComplete(ChannelFuture future, ChannelPromise promise) {
/* 1063 */         Throwable cause = future.cause();
/* 1064 */         if (cause == null) {
/* 1065 */           promise.setSuccess();
/*      */         } else {
/* 1067 */           Throwable error = wrapStreamClosedError(cause);
/* 1068 */           promise.setFailure(error);
/*      */           
/* 1070 */           if (error instanceof ClosedChannelException) {
/* 1071 */             if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.config.isAutoClose()) {
/*      */               
/* 1073 */               closeForcibly();
/*      */             } else {
/* 1075 */               Http2MultiplexCodec.DefaultHttp2StreamChannel.this.outboundClosed = true;
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       private Throwable wrapStreamClosedError(Throwable cause) {
/* 1084 */         if (cause instanceof Http2Exception && ((Http2Exception)cause).error() == Http2Error.STREAM_CLOSED) {
/* 1085 */           return (new ClosedChannelException()).initCause(cause);
/*      */         }
/* 1087 */         return cause;
/*      */       }
/*      */       
/*      */       private Http2StreamFrame validateStreamFrame(Http2StreamFrame frame) {
/* 1091 */         if (frame.stream() != null && frame.stream() != Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream) {
/* 1092 */           String msgString = frame.toString();
/* 1093 */           ReferenceCountUtil.release(frame);
/* 1094 */           throw new IllegalArgumentException("Stream " + frame
/* 1095 */               .stream() + " must not be set on the frame: " + msgString);
/*      */         } 
/* 1097 */         return frame;
/*      */       }
/*      */       
/*      */       private ChannelFuture write0(Object msg) {
/* 1101 */         ChannelPromise promise = Http2MultiplexCodec.this.ctx.newPromise();
/* 1102 */         Http2MultiplexCodec.this.write(Http2MultiplexCodec.this.ctx, msg, promise);
/* 1103 */         return (ChannelFuture)promise;
/*      */       }
/*      */ 
/*      */       
/*      */       public void flush() {
/* 1108 */         if (!this.writeDoneAndNoFlush) {
/*      */           return;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1117 */           if (!Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inFireChannelReadComplete) {
/* 1118 */             Http2MultiplexCodec.this.flush0(Http2MultiplexCodec.this.ctx);
/*      */           }
/*      */         } finally {
/* 1121 */           this.writeDoneAndNoFlush = false;
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*      */       public ChannelPromise voidPromise() {
/* 1127 */         return (ChannelPromise)this.unsafeVoidPromise;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public ChannelOutboundBuffer outboundBuffer() {
/* 1133 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       private Http2ChannelUnsafe() {}
/*      */     }
/*      */ 
/*      */     
/*      */     private final class Http2StreamChannelConfig
/*      */       extends DefaultChannelConfig
/*      */     {
/*      */       Http2StreamChannelConfig(Channel channel) {
/* 1145 */         super(channel);
/* 1146 */         setRecvByteBufAllocator((RecvByteBufAllocator)new Http2MultiplexCodec.Http2StreamChannelRecvByteBufAllocator());
/*      */       }
/*      */ 
/*      */       
/*      */       public int getWriteBufferHighWaterMark() {
/* 1151 */         return Math.min(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.parent().config().getWriteBufferHighWaterMark(), Http2MultiplexCodec.this.initialOutboundStreamWindow);
/*      */       }
/*      */ 
/*      */       
/*      */       public int getWriteBufferLowWaterMark() {
/* 1156 */         return Math.min(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.parent().config().getWriteBufferLowWaterMark(), Http2MultiplexCodec.this.initialOutboundStreamWindow);
/*      */       }
/*      */ 
/*      */       
/*      */       public MessageSizeEstimator getMessageSizeEstimator() {
/* 1161 */         return Http2MultiplexCodec.FlowControlledFrameSizeEstimator.INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public WriteBufferWaterMark getWriteBufferWaterMark() {
/* 1166 */         int mark = getWriteBufferHighWaterMark();
/* 1167 */         return new WriteBufferWaterMark(mark, mark);
/*      */       }
/*      */ 
/*      */       
/*      */       public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 1172 */         throw new UnsupportedOperationException();
/*      */       }
/*      */ 
/*      */       
/*      */       @Deprecated
/*      */       public ChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 1178 */         throw new UnsupportedOperationException();
/*      */       }
/*      */ 
/*      */       
/*      */       @Deprecated
/*      */       public ChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 1184 */         throw new UnsupportedOperationException();
/*      */       }
/*      */ 
/*      */       
/*      */       public ChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 1189 */         throw new UnsupportedOperationException();
/*      */       }
/*      */ 
/*      */       
/*      */       public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 1194 */         if (!(allocator.newHandle() instanceof RecvByteBufAllocator.ExtendedHandle)) {
/* 1195 */           throw new IllegalArgumentException("allocator.newHandle() must return an object of type: " + RecvByteBufAllocator.ExtendedHandle.class);
/*      */         }
/*      */         
/* 1198 */         super.setRecvByteBufAllocator(allocator);
/* 1199 */         return (ChannelConfig)this;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private final class Http2ChannelUnsafe implements Channel.Unsafe {
/*      */     private final VoidChannelPromise unsafeVoidPromise = new VoidChannelPromise(Http2MultiplexCodec.DefaultHttp2StreamChannel.this, false);
/*      */     private RecvByteBufAllocator.ExtendedHandle recvHandle;
/*      */     private boolean writeDoneAndNoFlush;
/*      */     private boolean closeInitiated;
/*      */     
/*      */     public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/*      */       if (!promise.setUncancellable())
/*      */         return; 
/*      */       promise.setFailure(new UnsupportedOperationException());
/*      */     }
/*      */     
/*      */     public RecvByteBufAllocator.ExtendedHandle recvBufAllocHandle() {
/*      */       if (this.recvHandle == null)
/*      */         this.recvHandle = (RecvByteBufAllocator.ExtendedHandle)Http2MultiplexCodec.DefaultHttp2StreamChannel.this.config().getRecvByteBufAllocator().newHandle(); 
/*      */       return this.recvHandle;
/*      */     }
/*      */     
/*      */     public SocketAddress localAddress() {
/*      */       return Http2MultiplexCodec.DefaultHttp2StreamChannel.this.parent().unsafe().localAddress();
/*      */     }
/*      */     
/*      */     public SocketAddress remoteAddress() {
/*      */       return Http2MultiplexCodec.DefaultHttp2StreamChannel.this.parent().unsafe().remoteAddress();
/*      */     }
/*      */     
/*      */     public void register(EventLoop eventLoop, ChannelPromise promise) {
/*      */       if (!promise.setUncancellable())
/*      */         return; 
/*      */       if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.registered)
/*      */         throw new UnsupportedOperationException("Re-register is not supported"); 
/*      */       Http2MultiplexCodec.DefaultHttp2StreamChannel.this.registered = true;
/*      */       if (!Http2MultiplexCodec.DefaultHttp2StreamChannel.this.outbound)
/*      */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().addLast(new ChannelHandler[] { Http2MultiplexCodec.access$700(this.this$1.this$0) }); 
/*      */       promise.setSuccess();
/*      */       Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelRegistered();
/*      */       if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.isActive())
/*      */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelActive(); 
/*      */     }
/*      */     
/*      */     public void bind(SocketAddress localAddress, ChannelPromise promise) {
/*      */       if (!promise.setUncancellable())
/*      */         return; 
/*      */       promise.setFailure(new UnsupportedOperationException());
/*      */     }
/*      */     
/*      */     public void disconnect(ChannelPromise promise) {
/*      */       close(promise);
/*      */     }
/*      */     
/*      */     public void close(final ChannelPromise promise) {
/*      */       if (!promise.setUncancellable())
/*      */         return; 
/*      */       if (this.closeInitiated) {
/*      */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePromise.isDone()) {
/*      */           promise.setSuccess();
/*      */         } else if (!(promise instanceof VoidChannelPromise)) {
/*      */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePromise.addListener((GenericFutureListener)new ChannelFutureListener() {
/*      */                 public void operationComplete(ChannelFuture future) throws Exception {
/*      */                   promise.setSuccess();
/*      */                 }
/*      */               });
/*      */         } 
/*      */         return;
/*      */       } 
/*      */       this.closeInitiated = true;
/*      */       Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePending = false;
/*      */       Http2MultiplexCodec.DefaultHttp2StreamChannel.this.fireChannelReadPending = false;
/*      */       if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.parent().isActive() && !Http2MultiplexCodec.DefaultHttp2StreamChannel.this.streamClosedWithoutError && Http2CodecUtil.isStreamIdValid(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream().id())) {
/*      */         Http2StreamFrame resetFrame = (new DefaultHttp2ResetFrame(Http2Error.CANCEL)).stream(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream());
/*      */         write(resetFrame, Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe().voidPromise());
/*      */         flush();
/*      */       } 
/*      */       if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer != null)
/*      */         while (true) {
/*      */           Object msg = Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer.poll();
/*      */           if (msg == null)
/*      */             break; 
/*      */           ReferenceCountUtil.release(msg);
/*      */         }  
/*      */       Http2MultiplexCodec.DefaultHttp2StreamChannel.this.outboundClosed = true;
/*      */       Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePromise.setSuccess();
/*      */       promise.setSuccess();
/*      */       Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelInactive();
/*      */       if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.isRegistered())
/*      */         deregister(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe().voidPromise()); 
/*      */     }
/*      */     
/*      */     public void closeForcibly() {
/*      */       close(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe().voidPromise());
/*      */     }
/*      */     
/*      */     public void deregister(ChannelPromise promise) {
/*      */       if (!promise.setUncancellable())
/*      */         return; 
/*      */       if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.registered) {
/*      */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.registered = true;
/*      */         promise.setSuccess();
/*      */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelUnregistered();
/*      */       } else {
/*      */         promise.setFailure(new IllegalStateException("Not registered"));
/*      */       } 
/*      */     }
/*      */     
/*      */     public void beginRead() {
/*      */       Object m;
/*      */       if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.readInProgress || !Http2MultiplexCodec.DefaultHttp2StreamChannel.this.isActive())
/*      */         return; 
/*      */       Http2MultiplexCodec.DefaultHttp2StreamChannel.this.readInProgress = true;
/*      */       RecvByteBufAllocator.Handle allocHandle = Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe().recvBufAllocHandle();
/*      */       allocHandle.reset(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.config());
/*      */       if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer == null || Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer.isEmpty()) {
/*      */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePending)
/*      */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe.closeForcibly(); 
/*      */         return;
/*      */       } 
/*      */       boolean continueReading;
/*      */       do {
/*      */         m = Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer.poll();
/*      */         if (m == null) {
/*      */           boolean bool = false;
/*      */           break;
/*      */         } 
/*      */         doRead0((Http2Frame)m, allocHandle);
/*      */       } while (continueReading = allocHandle.continueReading());
/*      */       if (continueReading && Http2MultiplexCodec.this.parentReadInProgress) {
/*      */         Http2MultiplexCodec.this.addChildChannelToReadPendingQueue(Http2MultiplexCodec.DefaultHttp2StreamChannel.this);
/*      */       } else {
/*      */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.readInProgress = false;
/*      */         allocHandle.readComplete();
/*      */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelReadComplete();
/*      */         flush();
/*      */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePending)
/*      */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe.closeForcibly(); 
/*      */       } 
/*      */     }
/*      */     
/*      */     void doRead0(Http2Frame frame, RecvByteBufAllocator.Handle allocHandle) {
/*      */       int numBytesToBeConsumed = 0;
/*      */       if (frame instanceof Http2DataFrame) {
/*      */         numBytesToBeConsumed = ((Http2DataFrame)frame).initialFlowControlledBytes();
/*      */         allocHandle.lastBytesRead(numBytesToBeConsumed);
/*      */       } else {
/*      */         allocHandle.lastBytesRead(9);
/*      */       } 
/*      */       allocHandle.incMessagesRead(1);
/*      */       Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelRead(frame);
/*      */       if (numBytesToBeConsumed != 0)
/*      */         try {
/*      */           this.writeDoneAndNoFlush |= Http2MultiplexCodec.this.onBytesConsumed(Http2MultiplexCodec.this.ctx, Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream, numBytesToBeConsumed);
/*      */         } catch (Http2Exception e) {
/*      */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireExceptionCaught(e);
/*      */         }  
/*      */     }
/*      */     
/*      */     public void write(Object msg, final ChannelPromise promise) {
/*      */       if (!promise.setUncancellable()) {
/*      */         ReferenceCountUtil.release(msg);
/*      */         return;
/*      */       } 
/*      */       if (!Http2MultiplexCodec.DefaultHttp2StreamChannel.this.isActive() || (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.outboundClosed && (msg instanceof Http2HeadersFrame || msg instanceof Http2DataFrame))) {
/*      */         ReferenceCountUtil.release(msg);
/*      */         promise.setFailure(Http2MultiplexCodec.CLOSED_CHANNEL_EXCEPTION);
/*      */         return;
/*      */       } 
/*      */       try {
/*      */         if (msg instanceof Http2StreamFrame) {
/*      */           Http2StreamFrame frame = validateStreamFrame((Http2StreamFrame)msg).stream(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream());
/*      */           if (!Http2MultiplexCodec.DefaultHttp2StreamChannel.this.firstFrameWritten && !Http2CodecUtil.isStreamIdValid(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream().id())) {
/*      */             if (!(frame instanceof Http2HeadersFrame)) {
/*      */               ReferenceCountUtil.release(frame);
/*      */               promise.setFailure(new IllegalArgumentException("The first frame must be a headers frame. Was: " + frame.name()));
/*      */               return;
/*      */             } 
/*      */             Http2MultiplexCodec.DefaultHttp2StreamChannel.this.firstFrameWritten = true;
/*      */             ChannelFuture channelFuture = write0(frame);
/*      */             if (channelFuture.isDone()) {
/*      */               firstWriteComplete(channelFuture, promise);
/*      */             } else {
/*      */               channelFuture.addListener((GenericFutureListener)new ChannelFutureListener() {
/*      */                     public void operationComplete(ChannelFuture future) throws Exception {
/*      */                       Http2MultiplexCodec.DefaultHttp2StreamChannel.Http2ChannelUnsafe.this.firstWriteComplete(future, promise);
/*      */                     }
/*      */                   });
/*      */             } 
/*      */             return;
/*      */           } 
/*      */         } else {
/*      */           String msgStr = msg.toString();
/*      */           ReferenceCountUtil.release(msg);
/*      */           promise.setFailure(new IllegalArgumentException("Message must be an " + StringUtil.simpleClassName(Http2StreamFrame.class) + ": " + msgStr));
/*      */           return;
/*      */         } 
/*      */         ChannelFuture future = write0(msg);
/*      */         if (future.isDone()) {
/*      */           writeComplete(future, promise);
/*      */         } else {
/*      */           future.addListener((GenericFutureListener)new ChannelFutureListener() {
/*      */                 public void operationComplete(ChannelFuture future) throws Exception {
/*      */                   Http2MultiplexCodec.DefaultHttp2StreamChannel.Http2ChannelUnsafe.this.writeComplete(future, promise);
/*      */                 }
/*      */               });
/*      */         } 
/*      */       } catch (Throwable t) {
/*      */         promise.tryFailure(t);
/*      */       } finally {
/*      */         this.writeDoneAndNoFlush = true;
/*      */       } 
/*      */     }
/*      */     
/*      */     private void firstWriteComplete(ChannelFuture future, ChannelPromise promise) {
/*      */       Throwable cause = future.cause();
/*      */       if (cause == null) {
/*      */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.writabilityChanged(Http2MultiplexCodec.this.isWritable(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream));
/*      */         promise.setSuccess();
/*      */       } else {
/*      */         promise.setFailure(wrapStreamClosedError(cause));
/*      */         closeForcibly();
/*      */       } 
/*      */     }
/*      */     
/*      */     private void writeComplete(ChannelFuture future, ChannelPromise promise) {
/*      */       Throwable cause = future.cause();
/*      */       if (cause == null) {
/*      */         promise.setSuccess();
/*      */       } else {
/*      */         Throwable error = wrapStreamClosedError(cause);
/*      */         promise.setFailure(error);
/*      */         if (error instanceof ClosedChannelException)
/*      */           if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.config.isAutoClose()) {
/*      */             closeForcibly();
/*      */           } else {
/*      */             Http2MultiplexCodec.DefaultHttp2StreamChannel.this.outboundClosed = true;
/*      */           }  
/*      */       } 
/*      */     }
/*      */     
/*      */     private Throwable wrapStreamClosedError(Throwable cause) {
/*      */       if (cause instanceof Http2Exception && ((Http2Exception)cause).error() == Http2Error.STREAM_CLOSED)
/*      */         return (new ClosedChannelException()).initCause(cause); 
/*      */       return cause;
/*      */     }
/*      */     
/*      */     private Http2StreamFrame validateStreamFrame(Http2StreamFrame frame) {
/*      */       if (frame.stream() != null && frame.stream() != Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream) {
/*      */         String msgString = frame.toString();
/*      */         ReferenceCountUtil.release(frame);
/*      */         throw new IllegalArgumentException("Stream " + frame.stream() + " must not be set on the frame: " + msgString);
/*      */       } 
/*      */       return frame;
/*      */     }
/*      */     
/*      */     private ChannelFuture write0(Object msg) {
/*      */       ChannelPromise promise = Http2MultiplexCodec.this.ctx.newPromise();
/*      */       Http2MultiplexCodec.this.write(Http2MultiplexCodec.this.ctx, msg, promise);
/*      */       return (ChannelFuture)promise;
/*      */     }
/*      */     
/*      */     public void flush() {
/*      */       if (!this.writeDoneAndNoFlush)
/*      */         return; 
/*      */       try {
/*      */         if (!Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inFireChannelReadComplete)
/*      */           Http2MultiplexCodec.this.flush0(Http2MultiplexCodec.this.ctx); 
/*      */       } finally {
/*      */         this.writeDoneAndNoFlush = false;
/*      */       } 
/*      */     }
/*      */     
/*      */     public ChannelPromise voidPromise() {
/*      */       return (ChannelPromise)this.unsafeVoidPromise;
/*      */     }
/*      */     
/*      */     public ChannelOutboundBuffer outboundBuffer() {
/*      */       return null;
/*      */     }
/*      */     
/*      */     private Http2ChannelUnsafe() {}
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2MultiplexCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */