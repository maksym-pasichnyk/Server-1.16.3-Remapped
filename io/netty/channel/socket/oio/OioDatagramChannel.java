/*     */ package io.netty.channel.socket.oio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.AddressedEnvelope;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.oio.AbstractOioMessageChannel;
/*     */ import io.netty.channel.socket.DatagramChannel;
/*     */ import io.netty.channel.socket.DatagramChannelConfig;
/*     */ import io.netty.channel.socket.DatagramPacket;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.MulticastSocket;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.channels.NotYetConnectedException;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
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
/*     */ public class OioDatagramChannel
/*     */   extends AbstractOioMessageChannel
/*     */   implements DatagramChannel
/*     */ {
/*  60 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OioDatagramChannel.class);
/*     */   
/*  62 */   private static final ChannelMetadata METADATA = new ChannelMetadata(true);
/*  63 */   private static final String EXPECTED_TYPES = " (expected: " + 
/*  64 */     StringUtil.simpleClassName(DatagramPacket.class) + ", " + 
/*  65 */     StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + 
/*  66 */     StringUtil.simpleClassName(ByteBuf.class) + ", " + 
/*  67 */     StringUtil.simpleClassName(SocketAddress.class) + ">, " + 
/*  68 */     StringUtil.simpleClassName(ByteBuf.class) + ')';
/*     */   
/*     */   private final MulticastSocket socket;
/*     */   private final OioDatagramChannelConfig config;
/*  72 */   private final DatagramPacket tmpPacket = new DatagramPacket(EmptyArrays.EMPTY_BYTES, 0);
/*     */   
/*     */   private static MulticastSocket newSocket() {
/*     */     try {
/*  76 */       return new MulticastSocket(null);
/*  77 */     } catch (Exception e) {
/*  78 */       throw new ChannelException("failed to create a new socket", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OioDatagramChannel() {
/*  86 */     this(newSocket());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OioDatagramChannel(MulticastSocket socket) {
/*  95 */     super(null);
/*     */     
/*  97 */     boolean success = false;
/*     */     try {
/*  99 */       socket.setSoTimeout(1000);
/* 100 */       socket.setBroadcast(false);
/* 101 */       success = true;
/* 102 */     } catch (SocketException e) {
/* 103 */       throw new ChannelException("Failed to configure the datagram socket timeout.", e);
/*     */     } finally {
/*     */       
/* 106 */       if (!success) {
/* 107 */         socket.close();
/*     */       }
/*     */     } 
/*     */     
/* 111 */     this.socket = socket;
/* 112 */     this.config = new DefaultOioDatagramChannelConfig(this, socket);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/* 117 */     return METADATA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig config() {
/* 128 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 133 */     return !this.socket.isClosed();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 139 */     return (isOpen() && ((((Boolean)this.config
/* 140 */       .getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION)).booleanValue() && isRegistered()) || this.socket
/* 141 */       .isBound()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConnected() {
/* 146 */     return this.socket.isConnected();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress localAddress0() {
/* 151 */     return this.socket.getLocalSocketAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress remoteAddress0() {
/* 156 */     return this.socket.getRemoteSocketAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 161 */     this.socket.bind(localAddress);
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress localAddress() {
/* 166 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress remoteAddress() {
/* 171 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/* 177 */     if (localAddress != null) {
/* 178 */       this.socket.bind(localAddress);
/*     */     }
/*     */     
/* 181 */     boolean success = false;
/*     */     try {
/* 183 */       this.socket.connect(remoteAddress);
/* 184 */       success = true;
/*     */     } finally {
/* 186 */       if (!success) {
/*     */         try {
/* 188 */           this.socket.close();
/* 189 */         } catch (Throwable t) {
/* 190 */           logger.warn("Failed to close a socket.", t);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 198 */     this.socket.disconnect();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 203 */     this.socket.close();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int doReadMessages(List<Object> buf) throws Exception {
/* 208 */     DatagramChannelConfig config = config();
/* 209 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/*     */     
/* 211 */     ByteBuf data = config.getAllocator().heapBuffer(allocHandle.guess());
/* 212 */     boolean free = true;
/*     */     
/*     */     try {
/* 215 */       this.tmpPacket.setAddress(null);
/* 216 */       this.tmpPacket.setData(data.array(), data.arrayOffset(), data.capacity());
/* 217 */       this.socket.receive(this.tmpPacket);
/*     */       
/* 219 */       InetSocketAddress remoteAddr = (InetSocketAddress)this.tmpPacket.getSocketAddress();
/*     */       
/* 221 */       allocHandle.lastBytesRead(this.tmpPacket.getLength());
/* 222 */       buf.add(new DatagramPacket(data.writerIndex(allocHandle.lastBytesRead()), localAddress(), remoteAddr));
/* 223 */       free = false;
/* 224 */       return 1;
/* 225 */     } catch (SocketTimeoutException e) {
/*     */       
/* 227 */       return 0;
/* 228 */     } catch (SocketException e) {
/* 229 */       if (!e.getMessage().toLowerCase(Locale.US).contains("socket closed")) {
/* 230 */         throw e;
/*     */       }
/* 232 */       return -1;
/* 233 */     } catch (Throwable cause) {
/* 234 */       PlatformDependent.throwException(cause);
/* 235 */       return -1;
/*     */     } finally {
/* 237 */       if (free)
/* 238 */         data.release(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/*     */     while (true) {
/*     */       ByteBuf data;
/*     */       SocketAddress remoteAddress;
/* 246 */       Object o = in.current();
/* 247 */       if (o == null) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 253 */       if (o instanceof AddressedEnvelope) {
/*     */         
/* 255 */         AddressedEnvelope<ByteBuf, SocketAddress> envelope = (AddressedEnvelope<ByteBuf, SocketAddress>)o;
/* 256 */         remoteAddress = envelope.recipient();
/* 257 */         data = (ByteBuf)envelope.content();
/*     */       } else {
/* 259 */         data = (ByteBuf)o;
/* 260 */         remoteAddress = null;
/*     */       } 
/*     */       
/* 263 */       int length = data.readableBytes();
/*     */       try {
/* 265 */         if (remoteAddress != null) {
/* 266 */           this.tmpPacket.setSocketAddress(remoteAddress);
/*     */         } else {
/* 268 */           if (!isConnected())
/*     */           {
/*     */             
/* 271 */             throw new NotYetConnectedException();
/*     */           }
/*     */           
/* 274 */           this.tmpPacket.setAddress(null);
/*     */         } 
/* 276 */         if (data.hasArray()) {
/* 277 */           this.tmpPacket.setData(data.array(), data.arrayOffset() + data.readerIndex(), length);
/*     */         } else {
/* 279 */           byte[] tmp = new byte[length];
/* 280 */           data.getBytes(data.readerIndex(), tmp);
/* 281 */           this.tmpPacket.setData(tmp);
/*     */         } 
/* 283 */         this.socket.send(this.tmpPacket);
/* 284 */         in.remove();
/* 285 */       } catch (Exception e) {
/*     */ 
/*     */ 
/*     */         
/* 289 */         in.remove(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) {
/* 296 */     if (msg instanceof DatagramPacket || msg instanceof ByteBuf) {
/* 297 */       return msg;
/*     */     }
/*     */     
/* 300 */     if (msg instanceof AddressedEnvelope) {
/*     */       
/* 302 */       AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope<Object, SocketAddress>)msg;
/* 303 */       if (e.content() instanceof ByteBuf) {
/* 304 */         return msg;
/*     */       }
/*     */     } 
/*     */     
/* 308 */     throw new UnsupportedOperationException("unsupported message type: " + 
/* 309 */         StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress) {
/* 314 */     return joinGroup(multicastAddress, newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, ChannelPromise promise) {
/* 319 */     ensureBound();
/*     */     try {
/* 321 */       this.socket.joinGroup(multicastAddress);
/* 322 */       promise.setSuccess();
/* 323 */     } catch (IOException e) {
/* 324 */       promise.setFailure(e);
/*     */     } 
/* 326 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
/* 331 */     return joinGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
/* 338 */     ensureBound();
/*     */     try {
/* 340 */       this.socket.joinGroup(multicastAddress, networkInterface);
/* 341 */       promise.setSuccess();
/* 342 */     } catch (IOException e) {
/* 343 */       promise.setFailure(e);
/*     */     } 
/* 345 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
/* 351 */     return newFailedFuture(new UnsupportedOperationException());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
/* 358 */     promise.setFailure(new UnsupportedOperationException());
/* 359 */     return (ChannelFuture)promise;
/*     */   }
/*     */   
/*     */   private void ensureBound() {
/* 363 */     if (!isActive()) {
/* 364 */       throw new IllegalStateException(DatagramChannel.class
/* 365 */           .getName() + " must be bound to join a group.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress) {
/* 372 */     return leaveGroup(multicastAddress, newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise promise) {
/*     */     try {
/* 378 */       this.socket.leaveGroup(multicastAddress);
/* 379 */       promise.setSuccess();
/* 380 */     } catch (IOException e) {
/* 381 */       promise.setFailure(e);
/*     */     } 
/* 383 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
/* 389 */     return leaveGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
/*     */     try {
/* 397 */       this.socket.leaveGroup(multicastAddress, networkInterface);
/* 398 */       promise.setSuccess();
/* 399 */     } catch (IOException e) {
/* 400 */       promise.setFailure(e);
/*     */     } 
/* 402 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
/* 408 */     return newFailedFuture(new UnsupportedOperationException());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
/* 415 */     promise.setFailure(new UnsupportedOperationException());
/* 416 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock) {
/* 422 */     return newFailedFuture(new UnsupportedOperationException());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock, ChannelPromise promise) {
/* 429 */     promise.setFailure(new UnsupportedOperationException());
/* 430 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock) {
/* 436 */     return newFailedFuture(new UnsupportedOperationException());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock, ChannelPromise promise) {
/* 442 */     promise.setFailure(new UnsupportedOperationException());
/* 443 */     return (ChannelFuture)promise;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\socket\oio\OioDatagramChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */