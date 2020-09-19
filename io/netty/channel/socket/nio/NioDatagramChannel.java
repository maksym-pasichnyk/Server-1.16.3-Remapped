/*     */ package io.netty.channel.socket.nio;
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
/*     */ import io.netty.channel.DefaultAddressedEnvelope;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.nio.AbstractNioMessageChannel;
/*     */ import io.netty.channel.socket.DatagramChannel;
/*     */ import io.netty.channel.socket.DatagramChannelConfig;
/*     */ import io.netty.channel.socket.DatagramPacket;
/*     */ import io.netty.channel.socket.InternetProtocolFamily;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.DatagramChannel;
/*     */ import java.nio.channels.MembershipKey;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public final class NioDatagramChannel
/*     */   extends AbstractNioMessageChannel
/*     */   implements DatagramChannel
/*     */ {
/*  65 */   private static final ChannelMetadata METADATA = new ChannelMetadata(true);
/*  66 */   private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
/*  67 */   private static final String EXPECTED_TYPES = " (expected: " + 
/*  68 */     StringUtil.simpleClassName(DatagramPacket.class) + ", " + 
/*  69 */     StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + 
/*  70 */     StringUtil.simpleClassName(ByteBuf.class) + ", " + 
/*  71 */     StringUtil.simpleClassName(SocketAddress.class) + ">, " + 
/*  72 */     StringUtil.simpleClassName(ByteBuf.class) + ')';
/*     */ 
/*     */ 
/*     */   
/*     */   private final DatagramChannelConfig config;
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<InetAddress, List<MembershipKey>> memberships;
/*     */ 
/*     */ 
/*     */   
/*     */   private static DatagramChannel newSocket(SelectorProvider provider) {
/*     */     try {
/*  86 */       return provider.openDatagramChannel();
/*  87 */     } catch (IOException e) {
/*  88 */       throw new ChannelException("Failed to open a socket.", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static DatagramChannel newSocket(SelectorProvider provider, InternetProtocolFamily ipFamily) {
/*  93 */     if (ipFamily == null) {
/*  94 */       return newSocket(provider);
/*     */     }
/*     */     
/*  97 */     checkJavaVersion();
/*     */     
/*     */     try {
/* 100 */       return provider.openDatagramChannel(ProtocolFamilyConverter.convert(ipFamily));
/* 101 */     } catch (IOException e) {
/* 102 */       throw new ChannelException("Failed to open a socket.", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void checkJavaVersion() {
/* 107 */     if (PlatformDependent.javaVersion() < 7) {
/* 108 */       throw new UnsupportedOperationException("Only supported on java 7+.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioDatagramChannel() {
/* 116 */     this(newSocket(DEFAULT_SELECTOR_PROVIDER));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioDatagramChannel(SelectorProvider provider) {
/* 124 */     this(newSocket(provider));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioDatagramChannel(InternetProtocolFamily ipFamily) {
/* 132 */     this(newSocket(DEFAULT_SELECTOR_PROVIDER, ipFamily));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioDatagramChannel(SelectorProvider provider, InternetProtocolFamily ipFamily) {
/* 141 */     this(newSocket(provider, ipFamily));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioDatagramChannel(DatagramChannel socket) {
/* 148 */     super(null, socket, 1);
/* 149 */     this.config = (DatagramChannelConfig)new NioDatagramChannelConfig(this, socket);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/* 154 */     return METADATA;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig config() {
/* 159 */     return this.config;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 165 */     DatagramChannel ch = javaChannel();
/* 166 */     return (ch.isOpen() && ((((Boolean)this.config
/* 167 */       .getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION)).booleanValue() && isRegistered()) || ch
/* 168 */       .socket().isBound()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConnected() {
/* 173 */     return javaChannel().isConnected();
/*     */   }
/*     */ 
/*     */   
/*     */   protected DatagramChannel javaChannel() {
/* 178 */     return (DatagramChannel)super.javaChannel();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress localAddress0() {
/* 183 */     return javaChannel().socket().getLocalSocketAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress remoteAddress0() {
/* 188 */     return javaChannel().socket().getRemoteSocketAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 193 */     doBind0(localAddress);
/*     */   }
/*     */   
/*     */   private void doBind0(SocketAddress localAddress) throws Exception {
/* 197 */     if (PlatformDependent.javaVersion() >= 7) {
/* 198 */       SocketUtils.bind(javaChannel(), localAddress);
/*     */     } else {
/* 200 */       javaChannel().socket().bind(localAddress);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/* 207 */     if (localAddress != null) {
/* 208 */       doBind0(localAddress);
/*     */     }
/*     */     
/* 211 */     boolean success = false;
/*     */     try {
/* 213 */       javaChannel().connect(remoteAddress);
/* 214 */       success = true;
/* 215 */       return true;
/*     */     } finally {
/* 217 */       if (!success) {
/* 218 */         doClose();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doFinishConnect() throws Exception {
/* 225 */     throw new Error();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 230 */     javaChannel().disconnect();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 235 */     javaChannel().close();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int doReadMessages(List<Object> buf) throws Exception {
/* 240 */     DatagramChannel ch = javaChannel();
/* 241 */     DatagramChannelConfig config = config();
/* 242 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/*     */     
/* 244 */     ByteBuf data = allocHandle.allocate(config.getAllocator());
/* 245 */     allocHandle.attemptedBytesRead(data.writableBytes());
/* 246 */     boolean free = true;
/*     */     try {
/* 248 */       ByteBuffer nioData = data.internalNioBuffer(data.writerIndex(), data.writableBytes());
/* 249 */       int pos = nioData.position();
/* 250 */       InetSocketAddress remoteAddress = (InetSocketAddress)ch.receive(nioData);
/* 251 */       if (remoteAddress == null) {
/* 252 */         return 0;
/*     */       }
/*     */       
/* 255 */       allocHandle.lastBytesRead(nioData.position() - pos);
/* 256 */       buf.add(new DatagramPacket(data.writerIndex(data.writerIndex() + allocHandle.lastBytesRead()), 
/* 257 */             localAddress(), remoteAddress));
/* 258 */       free = false;
/* 259 */       return 1;
/* 260 */     } catch (Throwable cause) {
/* 261 */       PlatformDependent.throwException(cause);
/* 262 */       return -1;
/*     */     } finally {
/* 264 */       if (free) {
/* 265 */         data.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception {
/*     */     SocketAddress remoteAddress;
/*     */     ByteBuf data;
/*     */     int writtenBytes;
/* 274 */     if (msg instanceof AddressedEnvelope) {
/*     */       
/* 276 */       AddressedEnvelope<ByteBuf, SocketAddress> envelope = (AddressedEnvelope<ByteBuf, SocketAddress>)msg;
/* 277 */       remoteAddress = envelope.recipient();
/* 278 */       data = (ByteBuf)envelope.content();
/*     */     } else {
/* 280 */       data = (ByteBuf)msg;
/* 281 */       remoteAddress = null;
/*     */     } 
/*     */     
/* 284 */     int dataLen = data.readableBytes();
/* 285 */     if (dataLen == 0) {
/* 286 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 290 */     ByteBuffer nioData = (data.nioBufferCount() == 1) ? data.internalNioBuffer(data.readerIndex(), dataLen) : data.nioBuffer(data.readerIndex(), dataLen);
/*     */     
/* 292 */     if (remoteAddress != null) {
/* 293 */       writtenBytes = javaChannel().send(nioData, remoteAddress);
/*     */     } else {
/* 295 */       writtenBytes = javaChannel().write(nioData);
/*     */     } 
/* 297 */     return (writtenBytes > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) {
/* 302 */     if (msg instanceof DatagramPacket) {
/* 303 */       DatagramPacket p = (DatagramPacket)msg;
/* 304 */       ByteBuf content = (ByteBuf)p.content();
/* 305 */       if (isSingleDirectBuffer(content)) {
/* 306 */         return p;
/*     */       }
/* 308 */       return new DatagramPacket(newDirectBuffer((ReferenceCounted)p, content), (InetSocketAddress)p.recipient());
/*     */     } 
/*     */     
/* 311 */     if (msg instanceof ByteBuf) {
/* 312 */       ByteBuf buf = (ByteBuf)msg;
/* 313 */       if (isSingleDirectBuffer(buf)) {
/* 314 */         return buf;
/*     */       }
/* 316 */       return newDirectBuffer(buf);
/*     */     } 
/*     */     
/* 319 */     if (msg instanceof AddressedEnvelope) {
/*     */       
/* 321 */       AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope<Object, SocketAddress>)msg;
/* 322 */       if (e.content() instanceof ByteBuf) {
/* 323 */         ByteBuf content = (ByteBuf)e.content();
/* 324 */         if (isSingleDirectBuffer(content)) {
/* 325 */           return e;
/*     */         }
/* 327 */         return new DefaultAddressedEnvelope(newDirectBuffer((ReferenceCounted)e, content), e.recipient());
/*     */       } 
/*     */     } 
/*     */     
/* 331 */     throw new UnsupportedOperationException("unsupported message type: " + 
/* 332 */         StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isSingleDirectBuffer(ByteBuf buf) {
/* 340 */     return (buf.isDirect() && buf.nioBufferCount() == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean continueOnWriteError() {
/* 348 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress localAddress() {
/* 353 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress remoteAddress() {
/* 358 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress) {
/* 363 */     return joinGroup(multicastAddress, newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, ChannelPromise promise) {
/*     */     try {
/* 369 */       return joinGroup(multicastAddress, 
/*     */           
/* 371 */           NetworkInterface.getByInetAddress(localAddress().getAddress()), (InetAddress)null, promise);
/*     */     }
/* 373 */     catch (SocketException e) {
/* 374 */       promise.setFailure(e);
/*     */       
/* 376 */       return (ChannelFuture)promise;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
/* 382 */     return joinGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
/* 389 */     return joinGroup(multicastAddress.getAddress(), networkInterface, (InetAddress)null, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
/* 395 */     return joinGroup(multicastAddress, networkInterface, source, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
/* 403 */     checkJavaVersion();
/*     */     
/* 405 */     if (multicastAddress == null) {
/* 406 */       throw new NullPointerException("multicastAddress");
/*     */     }
/*     */     
/* 409 */     if (networkInterface == null) {
/* 410 */       throw new NullPointerException("networkInterface");
/*     */     }
/*     */     
/*     */     try {
/*     */       MembershipKey key;
/* 415 */       if (source == null) {
/* 416 */         key = javaChannel().join(multicastAddress, networkInterface);
/*     */       } else {
/* 418 */         key = javaChannel().join(multicastAddress, networkInterface, source);
/*     */       } 
/*     */       
/* 421 */       synchronized (this) {
/* 422 */         List<MembershipKey> keys = null;
/* 423 */         if (this.memberships == null) {
/* 424 */           this.memberships = new HashMap<InetAddress, List<MembershipKey>>();
/*     */         } else {
/* 426 */           keys = this.memberships.get(multicastAddress);
/*     */         } 
/* 428 */         if (keys == null) {
/* 429 */           keys = new ArrayList<MembershipKey>();
/* 430 */           this.memberships.put(multicastAddress, keys);
/*     */         } 
/* 432 */         keys.add(key);
/*     */       } 
/*     */       
/* 435 */       promise.setSuccess();
/* 436 */     } catch (Throwable e) {
/* 437 */       promise.setFailure(e);
/*     */     } 
/*     */     
/* 440 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress) {
/* 445 */     return leaveGroup(multicastAddress, newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise promise) {
/*     */     try {
/* 451 */       return leaveGroup(multicastAddress, 
/* 452 */           NetworkInterface.getByInetAddress(localAddress().getAddress()), (InetAddress)null, promise);
/* 453 */     } catch (SocketException e) {
/* 454 */       promise.setFailure(e);
/*     */       
/* 456 */       return (ChannelFuture)promise;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
/* 462 */     return leaveGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
/* 469 */     return leaveGroup(multicastAddress.getAddress(), networkInterface, (InetAddress)null, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
/* 475 */     return leaveGroup(multicastAddress, networkInterface, source, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
/* 482 */     checkJavaVersion();
/*     */     
/* 484 */     if (multicastAddress == null) {
/* 485 */       throw new NullPointerException("multicastAddress");
/*     */     }
/* 487 */     if (networkInterface == null) {
/* 488 */       throw new NullPointerException("networkInterface");
/*     */     }
/*     */     
/* 491 */     synchronized (this) {
/* 492 */       if (this.memberships != null) {
/* 493 */         List<MembershipKey> keys = this.memberships.get(multicastAddress);
/* 494 */         if (keys != null) {
/* 495 */           Iterator<MembershipKey> keyIt = keys.iterator();
/*     */           
/* 497 */           while (keyIt.hasNext()) {
/* 498 */             MembershipKey key = keyIt.next();
/* 499 */             if (networkInterface.equals(key.networkInterface()) && ((
/* 500 */               source == null && key.sourceAddress() == null) || (source != null && source
/* 501 */               .equals(key.sourceAddress())))) {
/* 502 */               key.drop();
/* 503 */               keyIt.remove();
/*     */             } 
/*     */           } 
/*     */           
/* 507 */           if (keys.isEmpty()) {
/* 508 */             this.memberships.remove(multicastAddress);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 514 */     promise.setSuccess();
/* 515 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock) {
/* 525 */     return block(multicastAddress, networkInterface, sourceToBlock, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock, ChannelPromise promise) {
/* 535 */     checkJavaVersion();
/*     */     
/* 537 */     if (multicastAddress == null) {
/* 538 */       throw new NullPointerException("multicastAddress");
/*     */     }
/* 540 */     if (sourceToBlock == null) {
/* 541 */       throw new NullPointerException("sourceToBlock");
/*     */     }
/*     */     
/* 544 */     if (networkInterface == null) {
/* 545 */       throw new NullPointerException("networkInterface");
/*     */     }
/* 547 */     synchronized (this) {
/* 548 */       if (this.memberships != null) {
/* 549 */         List<MembershipKey> keys = this.memberships.get(multicastAddress);
/* 550 */         for (MembershipKey key : keys) {
/* 551 */           if (networkInterface.equals(key.networkInterface())) {
/*     */             try {
/* 553 */               key.block(sourceToBlock);
/* 554 */             } catch (IOException e) {
/* 555 */               promise.setFailure(e);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 561 */     promise.setSuccess();
/* 562 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock) {
/* 571 */     return block(multicastAddress, sourceToBlock, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock, ChannelPromise promise) {
/*     */     try {
/* 582 */       return block(multicastAddress, 
/*     */           
/* 584 */           NetworkInterface.getByInetAddress(localAddress().getAddress()), sourceToBlock, promise);
/*     */     }
/* 586 */     catch (SocketException e) {
/* 587 */       promise.setFailure(e);
/*     */       
/* 589 */       return (ChannelFuture)promise;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected void setReadPending(boolean readPending) {
/* 595 */     super.setReadPending(readPending);
/*     */   }
/*     */   
/*     */   void clearReadPending0() {
/* 599 */     clearReadPending();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean closeOnReadError(Throwable cause) {
/* 606 */     if (cause instanceof SocketException) {
/* 607 */       return false;
/*     */     }
/* 609 */     return super.closeOnReadError(cause);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\socket\nio\NioDatagramChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */