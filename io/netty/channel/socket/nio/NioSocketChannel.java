/*     */ package io.netty.channel.socket.nio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.nio.AbstractNioByteChannel;
/*     */ import io.netty.channel.nio.AbstractNioChannel;
/*     */ import io.netty.channel.nio.NioEventLoop;
/*     */ import io.netty.channel.socket.DefaultSocketChannelConfig;
/*     */ import io.netty.channel.socket.ServerSocketChannel;
/*     */ import io.netty.channel.socket.SocketChannel;
/*     */ import io.netty.channel.socket.SocketChannelConfig;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.GlobalEventExecutor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.concurrent.Executor;
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
/*     */ public class NioSocketChannel
/*     */   extends AbstractNioByteChannel
/*     */   implements SocketChannel
/*     */ {
/*  55 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioSocketChannel.class);
/*  56 */   private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
/*     */ 
/*     */ 
/*     */   
/*     */   private final SocketChannelConfig config;
/*     */ 
/*     */ 
/*     */   
/*     */   private static SocketChannel newSocket(SelectorProvider provider) {
/*     */     try {
/*  66 */       return provider.openSocketChannel();
/*  67 */     } catch (IOException e) {
/*  68 */       throw new ChannelException("Failed to open a socket.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSocketChannel() {
/*  78 */     this(DEFAULT_SELECTOR_PROVIDER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSocketChannel(SelectorProvider provider) {
/*  85 */     this(newSocket(provider));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSocketChannel(SocketChannel socket) {
/*  92 */     this((Channel)null, socket);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSocketChannel(Channel parent, SocketChannel socket) {
/* 102 */     super(parent, socket);
/* 103 */     this.config = (SocketChannelConfig)new NioSocketChannelConfig(this, socket.socket());
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannel parent() {
/* 108 */     return (ServerSocketChannel)super.parent();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig config() {
/* 113 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketChannel javaChannel() {
/* 118 */     return (SocketChannel)super.javaChannel();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 123 */     SocketChannel ch = javaChannel();
/* 124 */     return (ch.isOpen() && ch.isConnected());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOutputShutdown() {
/* 129 */     return (javaChannel().socket().isOutputShutdown() || !isActive());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInputShutdown() {
/* 134 */     return (javaChannel().socket().isInputShutdown() || !isActive());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/* 139 */     Socket socket = javaChannel().socket();
/* 140 */     return ((socket.isInputShutdown() && socket.isOutputShutdown()) || !isActive());
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress localAddress() {
/* 145 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress remoteAddress() {
/* 150 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void doShutdownOutput() throws Exception {
/* 156 */     if (PlatformDependent.javaVersion() >= 7) {
/* 157 */       javaChannel().shutdownOutput();
/*     */     } else {
/* 159 */       javaChannel().socket().shutdownOutput();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdownOutput() {
/* 165 */     return shutdownOutput(newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdownOutput(final ChannelPromise promise) {
/* 170 */     NioEventLoop nioEventLoop = eventLoop();
/* 171 */     if (nioEventLoop.inEventLoop()) {
/* 172 */       ((AbstractChannel.AbstractUnsafe)unsafe()).shutdownOutput(promise);
/*     */     } else {
/* 174 */       nioEventLoop.execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 177 */               ((AbstractChannel.AbstractUnsafe)NioSocketChannel.this.unsafe()).shutdownOutput(promise);
/*     */             }
/*     */           });
/*     */     } 
/* 181 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdownInput() {
/* 186 */     return shutdownInput(newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isInputShutdown0() {
/* 191 */     return isInputShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdownInput(final ChannelPromise promise) {
/* 196 */     NioEventLoop nioEventLoop = eventLoop();
/* 197 */     if (nioEventLoop.inEventLoop()) {
/* 198 */       shutdownInput0(promise);
/*     */     } else {
/* 200 */       nioEventLoop.execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 203 */               NioSocketChannel.this.shutdownInput0(promise);
/*     */             }
/*     */           });
/*     */     } 
/* 207 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdown() {
/* 212 */     return shutdown(newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdown(final ChannelPromise promise) {
/* 217 */     ChannelFuture shutdownOutputFuture = shutdownOutput();
/* 218 */     if (shutdownOutputFuture.isDone()) {
/* 219 */       shutdownOutputDone(shutdownOutputFuture, promise);
/*     */     } else {
/* 221 */       shutdownOutputFuture.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture shutdownOutputFuture) throws Exception {
/* 224 */               NioSocketChannel.this.shutdownOutputDone(shutdownOutputFuture, promise);
/*     */             }
/*     */           });
/*     */     } 
/* 228 */     return (ChannelFuture)promise;
/*     */   }
/*     */   
/*     */   private void shutdownOutputDone(final ChannelFuture shutdownOutputFuture, final ChannelPromise promise) {
/* 232 */     ChannelFuture shutdownInputFuture = shutdownInput();
/* 233 */     if (shutdownInputFuture.isDone()) {
/* 234 */       shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*     */     } else {
/* 236 */       shutdownInputFuture.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture shutdownInputFuture) throws Exception {
/* 239 */               NioSocketChannel.shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void shutdownDone(ChannelFuture shutdownOutputFuture, ChannelFuture shutdownInputFuture, ChannelPromise promise) {
/* 248 */     Throwable shutdownOutputCause = shutdownOutputFuture.cause();
/* 249 */     Throwable shutdownInputCause = shutdownInputFuture.cause();
/* 250 */     if (shutdownOutputCause != null) {
/* 251 */       if (shutdownInputCause != null) {
/* 252 */         logger.debug("Exception suppressed because a previous exception occurred.", shutdownInputCause);
/*     */       }
/*     */       
/* 255 */       promise.setFailure(shutdownOutputCause);
/* 256 */     } else if (shutdownInputCause != null) {
/* 257 */       promise.setFailure(shutdownInputCause);
/*     */     } else {
/* 259 */       promise.setSuccess();
/*     */     } 
/*     */   }
/*     */   private void shutdownInput0(ChannelPromise promise) {
/*     */     try {
/* 264 */       shutdownInput0();
/* 265 */       promise.setSuccess();
/* 266 */     } catch (Throwable t) {
/* 267 */       promise.setFailure(t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shutdownInput0() throws Exception {
/* 272 */     if (PlatformDependent.javaVersion() >= 7) {
/* 273 */       javaChannel().shutdownInput();
/*     */     } else {
/* 275 */       javaChannel().socket().shutdownInput();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress localAddress0() {
/* 281 */     return javaChannel().socket().getLocalSocketAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress remoteAddress0() {
/* 286 */     return javaChannel().socket().getRemoteSocketAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 291 */     doBind0(localAddress);
/*     */   }
/*     */   
/*     */   private void doBind0(SocketAddress localAddress) throws Exception {
/* 295 */     if (PlatformDependent.javaVersion() >= 7) {
/* 296 */       SocketUtils.bind(javaChannel(), localAddress);
/*     */     } else {
/* 298 */       SocketUtils.bind(javaChannel().socket(), localAddress);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/* 304 */     if (localAddress != null) {
/* 305 */       doBind0(localAddress);
/*     */     }
/*     */     
/* 308 */     boolean success = false;
/*     */     try {
/* 310 */       boolean connected = SocketUtils.connect(javaChannel(), remoteAddress);
/* 311 */       if (!connected) {
/* 312 */         selectionKey().interestOps(8);
/*     */       }
/* 314 */       success = true;
/* 315 */       return connected;
/*     */     } finally {
/* 317 */       if (!success) {
/* 318 */         doClose();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doFinishConnect() throws Exception {
/* 325 */     if (!javaChannel().finishConnect()) {
/* 326 */       throw new Error();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 332 */     doClose();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 337 */     super.doClose();
/* 338 */     javaChannel().close();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int doReadBytes(ByteBuf byteBuf) throws Exception {
/* 343 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/* 344 */     allocHandle.attemptedBytesRead(byteBuf.writableBytes());
/* 345 */     return byteBuf.writeBytes(javaChannel(), allocHandle.attemptedBytesRead());
/*     */   }
/*     */ 
/*     */   
/*     */   protected int doWriteBytes(ByteBuf buf) throws Exception {
/* 350 */     int expectedWrittenBytes = buf.readableBytes();
/* 351 */     return buf.readBytes(javaChannel(), expectedWrittenBytes);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long doWriteFileRegion(FileRegion region) throws Exception {
/* 356 */     long position = region.transferred();
/* 357 */     return region.transferTo(javaChannel(), position);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void adjustMaxBytesPerGatheringWrite(int attempted, int written, int oldMaxBytesPerGatheringWrite) {
/* 364 */     if (attempted == written) {
/* 365 */       if (attempted << 1 > oldMaxBytesPerGatheringWrite) {
/* 366 */         ((NioSocketChannelConfig)this.config).setMaxBytesPerGatheringWrite(attempted << 1);
/*     */       }
/* 368 */     } else if (attempted > 4096 && written < attempted >>> 1) {
/* 369 */       ((NioSocketChannelConfig)this.config).setMaxBytesPerGatheringWrite(attempted >>> 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/* 375 */     SocketChannel ch = javaChannel();
/* 376 */     int writeSpinCount = config().getWriteSpinCount(); do {
/*     */       ByteBuffer buffer; long attemptedBytes; int i, j; long localWrittenBytes;
/* 378 */       if (in.isEmpty()) {
/*     */         
/* 380 */         clearOpWrite();
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 386 */       int maxBytesPerGatheringWrite = ((NioSocketChannelConfig)this.config).getMaxBytesPerGatheringWrite();
/* 387 */       ByteBuffer[] nioBuffers = in.nioBuffers(1024, maxBytesPerGatheringWrite);
/* 388 */       int nioBufferCnt = in.nioBufferCount();
/*     */ 
/*     */ 
/*     */       
/* 392 */       switch (nioBufferCnt) {
/*     */         
/*     */         case 0:
/* 395 */           writeSpinCount -= doWrite0(in);
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 1:
/* 401 */           buffer = nioBuffers[0];
/* 402 */           i = buffer.remaining();
/* 403 */           j = ch.write(buffer);
/* 404 */           if (j <= 0) {
/* 405 */             incompleteWrite(true);
/*     */             return;
/*     */           } 
/* 408 */           adjustMaxBytesPerGatheringWrite(i, j, maxBytesPerGatheringWrite);
/* 409 */           in.removeBytes(j);
/* 410 */           writeSpinCount--;
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         default:
/* 417 */           attemptedBytes = in.nioBufferSize();
/* 418 */           localWrittenBytes = ch.write(nioBuffers, 0, nioBufferCnt);
/* 419 */           if (localWrittenBytes <= 0L) {
/* 420 */             incompleteWrite(true);
/*     */             
/*     */             return;
/*     */           } 
/* 424 */           adjustMaxBytesPerGatheringWrite((int)attemptedBytes, (int)localWrittenBytes, maxBytesPerGatheringWrite);
/*     */           
/* 426 */           in.removeBytes(localWrittenBytes);
/* 427 */           writeSpinCount--;
/*     */           break;
/*     */       } 
/*     */     
/* 431 */     } while (writeSpinCount > 0);
/*     */     
/* 433 */     incompleteWrite((writeSpinCount < 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractNioChannel.AbstractNioUnsafe newUnsafe() {
/* 438 */     return (AbstractNioChannel.AbstractNioUnsafe)new NioSocketChannelUnsafe();
/*     */   }
/*     */   private final class NioSocketChannelUnsafe extends AbstractNioByteChannel.NioByteUnsafe { private NioSocketChannelUnsafe() {
/* 441 */       super(NioSocketChannel.this);
/*     */     }
/*     */     protected Executor prepareToClose() {
/*     */       try {
/* 445 */         if (NioSocketChannel.this.javaChannel().isOpen() && NioSocketChannel.this.config().getSoLinger() > 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 450 */           NioSocketChannel.this.doDeregister();
/* 451 */           return (Executor)GlobalEventExecutor.INSTANCE;
/*     */         } 
/* 453 */       } catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 458 */       return null;
/*     */     } }
/*     */ 
/*     */   
/*     */   private final class NioSocketChannelConfig extends DefaultSocketChannelConfig {
/* 463 */     private volatile int maxBytesPerGatheringWrite = Integer.MAX_VALUE;
/*     */     
/*     */     private NioSocketChannelConfig(NioSocketChannel channel, Socket javaSocket) {
/* 466 */       super(channel, javaSocket);
/* 467 */       calculateMaxBytesPerGatheringWrite();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void autoReadCleared() {
/* 472 */       NioSocketChannel.this.clearReadPending();
/*     */     }
/*     */ 
/*     */     
/*     */     public NioSocketChannelConfig setSendBufferSize(int sendBufferSize) {
/* 477 */       super.setSendBufferSize(sendBufferSize);
/* 478 */       calculateMaxBytesPerGatheringWrite();
/* 479 */       return this;
/*     */     }
/*     */     
/*     */     void setMaxBytesPerGatheringWrite(int maxBytesPerGatheringWrite) {
/* 483 */       this.maxBytesPerGatheringWrite = maxBytesPerGatheringWrite;
/*     */     }
/*     */     
/*     */     int getMaxBytesPerGatheringWrite() {
/* 487 */       return this.maxBytesPerGatheringWrite;
/*     */     }
/*     */ 
/*     */     
/*     */     private void calculateMaxBytesPerGatheringWrite() {
/* 492 */       int newSendBufferSize = getSendBufferSize() << 1;
/* 493 */       if (newSendBufferSize > 0)
/* 494 */         setMaxBytesPerGatheringWrite(getSendBufferSize() << 1); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\socket\nio\NioSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */