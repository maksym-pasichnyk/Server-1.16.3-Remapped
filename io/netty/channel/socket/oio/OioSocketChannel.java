/*     */ package io.netty.channel.socket.oio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.ConnectTimeoutException;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.oio.OioByteStreamChannel;
/*     */ import io.netty.channel.socket.ServerSocketChannel;
/*     */ import io.netty.channel.socket.SocketChannel;
/*     */ import io.netty.channel.socket.SocketChannelConfig;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketTimeoutException;
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
/*     */ public class OioSocketChannel
/*     */   extends OioByteStreamChannel
/*     */   implements SocketChannel
/*     */ {
/*  45 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OioSocketChannel.class);
/*     */ 
/*     */   
/*     */   private final Socket socket;
/*     */   
/*     */   private final OioSocketChannelConfig config;
/*     */ 
/*     */   
/*     */   public OioSocketChannel() {
/*  54 */     this(new Socket());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OioSocketChannel(Socket socket) {
/*  63 */     this((Channel)null, socket);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OioSocketChannel(Channel parent, Socket socket) {
/*  74 */     super(parent);
/*  75 */     this.socket = socket;
/*  76 */     this.config = new DefaultOioSocketChannelConfig(this, socket);
/*     */     
/*  78 */     boolean success = false;
/*     */     try {
/*  80 */       if (socket.isConnected()) {
/*  81 */         activate(socket.getInputStream(), socket.getOutputStream());
/*     */       }
/*  83 */       socket.setSoTimeout(1000);
/*  84 */       success = true;
/*  85 */     } catch (Exception e) {
/*  86 */       throw new ChannelException("failed to initialize a socket", e);
/*     */     } finally {
/*  88 */       if (!success) {
/*     */         try {
/*  90 */           socket.close();
/*  91 */         } catch (IOException e) {
/*  92 */           logger.warn("Failed to close a socket.", e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannel parent() {
/* 100 */     return (ServerSocketChannel)super.parent();
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig config() {
/* 105 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 110 */     return !this.socket.isClosed();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 115 */     return (!this.socket.isClosed() && this.socket.isConnected());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOutputShutdown() {
/* 120 */     return (this.socket.isOutputShutdown() || !isActive());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInputShutdown() {
/* 125 */     return (this.socket.isInputShutdown() || !isActive());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/* 130 */     return ((this.socket.isInputShutdown() && this.socket.isOutputShutdown()) || !isActive());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void doShutdownOutput() throws Exception {
/* 136 */     shutdownOutput0();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdownOutput() {
/* 141 */     return shutdownOutput(newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdownInput() {
/* 146 */     return shutdownInput(newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdown() {
/* 151 */     return shutdown(newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   protected int doReadBytes(ByteBuf buf) throws Exception {
/* 156 */     if (this.socket.isClosed()) {
/* 157 */       return -1;
/*     */     }
/*     */     try {
/* 160 */       return super.doReadBytes(buf);
/* 161 */     } catch (SocketTimeoutException ignored) {
/* 162 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdownOutput(final ChannelPromise promise) {
/* 168 */     EventLoop loop = eventLoop();
/* 169 */     if (loop.inEventLoop()) {
/* 170 */       shutdownOutput0(promise);
/*     */     } else {
/* 172 */       loop.execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 175 */               OioSocketChannel.this.shutdownOutput0(promise);
/*     */             }
/*     */           });
/*     */     } 
/* 179 */     return (ChannelFuture)promise;
/*     */   }
/*     */   
/*     */   private void shutdownOutput0(ChannelPromise promise) {
/*     */     try {
/* 184 */       shutdownOutput0();
/* 185 */       promise.setSuccess();
/* 186 */     } catch (Throwable t) {
/* 187 */       promise.setFailure(t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shutdownOutput0() throws IOException {
/* 192 */     this.socket.shutdownOutput();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdownInput(final ChannelPromise promise) {
/* 197 */     EventLoop loop = eventLoop();
/* 198 */     if (loop.inEventLoop()) {
/* 199 */       shutdownInput0(promise);
/*     */     } else {
/* 201 */       loop.execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 204 */               OioSocketChannel.this.shutdownInput0(promise);
/*     */             }
/*     */           });
/*     */     } 
/* 208 */     return (ChannelFuture)promise;
/*     */   }
/*     */   
/*     */   private void shutdownInput0(ChannelPromise promise) {
/*     */     try {
/* 213 */       this.socket.shutdownInput();
/* 214 */       promise.setSuccess();
/* 215 */     } catch (Throwable t) {
/* 216 */       promise.setFailure(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdown(final ChannelPromise promise) {
/* 222 */     ChannelFuture shutdownOutputFuture = shutdownOutput();
/* 223 */     if (shutdownOutputFuture.isDone()) {
/* 224 */       shutdownOutputDone(shutdownOutputFuture, promise);
/*     */     } else {
/* 226 */       shutdownOutputFuture.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture shutdownOutputFuture) throws Exception {
/* 229 */               OioSocketChannel.this.shutdownOutputDone(shutdownOutputFuture, promise);
/*     */             }
/*     */           });
/*     */     } 
/* 233 */     return (ChannelFuture)promise;
/*     */   }
/*     */   
/*     */   private void shutdownOutputDone(final ChannelFuture shutdownOutputFuture, final ChannelPromise promise) {
/* 237 */     ChannelFuture shutdownInputFuture = shutdownInput();
/* 238 */     if (shutdownInputFuture.isDone()) {
/* 239 */       shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*     */     } else {
/* 241 */       shutdownInputFuture.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture shutdownInputFuture) throws Exception {
/* 244 */               OioSocketChannel.shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void shutdownDone(ChannelFuture shutdownOutputFuture, ChannelFuture shutdownInputFuture, ChannelPromise promise) {
/* 253 */     Throwable shutdownOutputCause = shutdownOutputFuture.cause();
/* 254 */     Throwable shutdownInputCause = shutdownInputFuture.cause();
/* 255 */     if (shutdownOutputCause != null) {
/* 256 */       if (shutdownInputCause != null) {
/* 257 */         logger.debug("Exception suppressed because a previous exception occurred.", shutdownInputCause);
/*     */       }
/*     */       
/* 260 */       promise.setFailure(shutdownOutputCause);
/* 261 */     } else if (shutdownInputCause != null) {
/* 262 */       promise.setFailure(shutdownInputCause);
/*     */     } else {
/* 264 */       promise.setSuccess();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress localAddress() {
/* 270 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress remoteAddress() {
/* 275 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress localAddress0() {
/* 280 */     return this.socket.getLocalSocketAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress remoteAddress0() {
/* 285 */     return this.socket.getRemoteSocketAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 290 */     SocketUtils.bind(this.socket, localAddress);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/* 296 */     if (localAddress != null) {
/* 297 */       SocketUtils.bind(this.socket, localAddress);
/*     */     }
/*     */     
/* 300 */     boolean success = false;
/*     */     try {
/* 302 */       SocketUtils.connect(this.socket, remoteAddress, config().getConnectTimeoutMillis());
/* 303 */       activate(this.socket.getInputStream(), this.socket.getOutputStream());
/* 304 */       success = true;
/* 305 */     } catch (SocketTimeoutException e) {
/* 306 */       ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
/* 307 */       cause.setStackTrace(e.getStackTrace());
/* 308 */       throw cause;
/*     */     } finally {
/* 310 */       if (!success) {
/* 311 */         doClose();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 318 */     doClose();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 323 */     this.socket.close();
/*     */   }
/*     */   
/*     */   protected boolean checkInputShutdown() {
/* 327 */     if (isInputShutdown()) {
/*     */       try {
/* 329 */         Thread.sleep(config().getSoTimeout());
/* 330 */       } catch (Throwable throwable) {}
/*     */ 
/*     */       
/* 333 */       return true;
/*     */     } 
/* 335 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void setReadPending(boolean readPending) {
/* 341 */     super.setReadPending(readPending);
/*     */   }
/*     */   
/*     */   final void clearReadPending0() {
/* 345 */     clearReadPending();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\socket\oio\OioSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */