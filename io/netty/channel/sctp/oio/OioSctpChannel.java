/*     */ package io.netty.channel.sctp.oio;
/*     */ 
/*     */ import com.sun.nio.sctp.Association;
/*     */ import com.sun.nio.sctp.MessageInfo;
/*     */ import com.sun.nio.sctp.NotificationHandler;
/*     */ import com.sun.nio.sctp.SctpChannel;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.oio.AbstractOioMessageChannel;
/*     */ import io.netty.channel.sctp.DefaultSctpChannelConfig;
/*     */ import io.netty.channel.sctp.SctpChannel;
/*     */ import io.netty.channel.sctp.SctpChannelConfig;
/*     */ import io.netty.channel.sctp.SctpMessage;
/*     */ import io.netty.channel.sctp.SctpNotificationHandler;
/*     */ import io.netty.channel.sctp.SctpServerChannel;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class OioSctpChannel
/*     */   extends AbstractOioMessageChannel
/*     */   implements SctpChannel
/*     */ {
/*  66 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OioSctpChannel.class);
/*     */   
/*  68 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*  69 */   private static final String EXPECTED_TYPE = " (expected: " + StringUtil.simpleClassName(SctpMessage.class) + ')';
/*     */   
/*     */   private final SctpChannel ch;
/*     */   
/*     */   private final SctpChannelConfig config;
/*     */   
/*     */   private final Selector readSelector;
/*     */   private final Selector writeSelector;
/*     */   private final Selector connectSelector;
/*     */   private final NotificationHandler<?> notificationHandler;
/*     */   
/*     */   private static SctpChannel openChannel() {
/*     */     try {
/*  82 */       return SctpChannel.open();
/*  83 */     } catch (IOException e) {
/*  84 */       throw new ChannelException("Failed to open a sctp channel.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OioSctpChannel() {
/*  92 */     this(openChannel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OioSctpChannel(SctpChannel ch) {
/* 101 */     this((Channel)null, ch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OioSctpChannel(Channel parent, SctpChannel ch) {
/* 112 */     super(parent);
/* 113 */     this.ch = ch;
/* 114 */     boolean success = false;
/*     */     try {
/* 116 */       ch.configureBlocking(false);
/* 117 */       this.readSelector = Selector.open();
/* 118 */       this.writeSelector = Selector.open();
/* 119 */       this.connectSelector = Selector.open();
/*     */       
/* 121 */       ch.register(this.readSelector, 1);
/* 122 */       ch.register(this.writeSelector, 4);
/* 123 */       ch.register(this.connectSelector, 8);
/*     */       
/* 125 */       this.config = (SctpChannelConfig)new OioSctpChannelConfig(this, ch);
/* 126 */       this.notificationHandler = (NotificationHandler<?>)new SctpNotificationHandler(this);
/* 127 */       success = true;
/* 128 */     } catch (Exception e) {
/* 129 */       throw new ChannelException("failed to initialize a sctp channel", e);
/*     */     } finally {
/* 131 */       if (!success) {
/*     */         try {
/* 133 */           ch.close();
/* 134 */         } catch (IOException e) {
/* 135 */           logger.warn("Failed to close a sctp channel.", e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress localAddress() {
/* 143 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress remoteAddress() {
/* 148 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannel parent() {
/* 153 */     return (SctpServerChannel)super.parent();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/* 158 */     return METADATA;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig config() {
/* 163 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 168 */     return this.ch.isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int doReadMessages(List<Object> msgs) throws Exception {
/* 173 */     if (!this.readSelector.isOpen()) {
/* 174 */       return 0;
/*     */     }
/*     */     
/* 177 */     int readMessages = 0;
/*     */     
/* 179 */     int selectedKeys = this.readSelector.select(1000L);
/* 180 */     boolean keysSelected = (selectedKeys > 0);
/*     */     
/* 182 */     if (!keysSelected) {
/* 183 */       return readMessages;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     this.readSelector.selectedKeys().clear();
/* 191 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/* 192 */     ByteBuf buffer = allocHandle.allocate(config().getAllocator());
/* 193 */     boolean free = true;
/*     */     
/*     */     try {
/* 196 */       ByteBuffer data = buffer.nioBuffer(buffer.writerIndex(), buffer.writableBytes());
/* 197 */       MessageInfo messageInfo = this.ch.receive(data, (Object)null, this.notificationHandler);
/* 198 */       if (messageInfo == null) {
/* 199 */         return readMessages;
/*     */       }
/*     */       
/* 202 */       data.flip();
/* 203 */       allocHandle.lastBytesRead(data.remaining());
/* 204 */       msgs.add(new SctpMessage(messageInfo, buffer
/* 205 */             .writerIndex(buffer.writerIndex() + allocHandle.lastBytesRead())));
/* 206 */       free = false;
/* 207 */       readMessages++;
/* 208 */     } catch (Throwable cause) {
/* 209 */       PlatformDependent.throwException(cause);
/*     */     } finally {
/* 211 */       if (free) {
/* 212 */         buffer.release();
/*     */       }
/*     */     } 
/* 215 */     return readMessages;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/* 220 */     if (!this.writeSelector.isOpen()) {
/*     */       return;
/*     */     }
/* 223 */     int size = in.size();
/* 224 */     int selectedKeys = this.writeSelector.select(1000L);
/* 225 */     if (selectedKeys > 0) {
/* 226 */       Set<SelectionKey> writableKeys = this.writeSelector.selectedKeys();
/* 227 */       if (writableKeys.isEmpty()) {
/*     */         return;
/*     */       }
/* 230 */       Iterator<SelectionKey> writableKeysIt = writableKeys.iterator();
/* 231 */       int written = 0; do {
/*     */         ByteBuffer nioData;
/* 233 */         if (written == size) {
/*     */           return;
/*     */         }
/*     */         
/* 237 */         writableKeysIt.next();
/* 238 */         writableKeysIt.remove();
/*     */         
/* 240 */         SctpMessage packet = (SctpMessage)in.current();
/* 241 */         if (packet == null) {
/*     */           return;
/*     */         }
/*     */         
/* 245 */         ByteBuf data = packet.content();
/* 246 */         int dataLen = data.readableBytes();
/*     */ 
/*     */         
/* 249 */         if (data.nioBufferCount() != -1) {
/* 250 */           nioData = data.nioBuffer();
/*     */         } else {
/* 252 */           nioData = ByteBuffer.allocate(dataLen);
/* 253 */           data.getBytes(data.readerIndex(), nioData);
/* 254 */           nioData.flip();
/*     */         } 
/*     */         
/* 257 */         MessageInfo mi = MessageInfo.createOutgoing(association(), null, packet.streamIdentifier());
/* 258 */         mi.payloadProtocolID(packet.protocolIdentifier());
/* 259 */         mi.streamNumber(packet.streamIdentifier());
/* 260 */         mi.unordered(packet.isUnordered());
/*     */         
/* 262 */         this.ch.send(nioData, mi);
/* 263 */         written++;
/* 264 */         in.remove();
/*     */       }
/* 266 */       while (writableKeysIt.hasNext());
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) throws Exception {
/* 275 */     if (msg instanceof SctpMessage) {
/* 276 */       return msg;
/*     */     }
/*     */     
/* 279 */     throw new UnsupportedOperationException("unsupported message type: " + 
/* 280 */         StringUtil.simpleClassName(msg) + EXPECTED_TYPE);
/*     */   }
/*     */ 
/*     */   
/*     */   public Association association() {
/*     */     try {
/* 286 */       return this.ch.association();
/* 287 */     } catch (IOException ignored) {
/* 288 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 294 */     return (isOpen() && association() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress localAddress0() {
/*     */     try {
/* 300 */       Iterator<SocketAddress> i = this.ch.getAllLocalAddresses().iterator();
/* 301 */       if (i.hasNext()) {
/* 302 */         return i.next();
/*     */       }
/* 304 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 307 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<InetSocketAddress> allLocalAddresses() {
/*     */     try {
/* 313 */       Set<SocketAddress> allLocalAddresses = this.ch.getAllLocalAddresses();
/* 314 */       Set<InetSocketAddress> addresses = new LinkedHashSet<InetSocketAddress>(allLocalAddresses.size());
/* 315 */       for (SocketAddress socketAddress : allLocalAddresses) {
/* 316 */         addresses.add((InetSocketAddress)socketAddress);
/*     */       }
/* 318 */       return addresses;
/* 319 */     } catch (Throwable ignored) {
/* 320 */       return Collections.emptySet();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress remoteAddress0() {
/*     */     try {
/* 327 */       Iterator<SocketAddress> i = this.ch.getRemoteAddresses().iterator();
/* 328 */       if (i.hasNext()) {
/* 329 */         return i.next();
/*     */       }
/* 331 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 334 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<InetSocketAddress> allRemoteAddresses() {
/*     */     try {
/* 340 */       Set<SocketAddress> allLocalAddresses = this.ch.getRemoteAddresses();
/* 341 */       Set<InetSocketAddress> addresses = new LinkedHashSet<InetSocketAddress>(allLocalAddresses.size());
/* 342 */       for (SocketAddress socketAddress : allLocalAddresses) {
/* 343 */         addresses.add((InetSocketAddress)socketAddress);
/*     */       }
/* 345 */       return addresses;
/* 346 */     } catch (Throwable ignored) {
/* 347 */       return Collections.emptySet();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 353 */     this.ch.bind(localAddress);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/* 359 */     if (localAddress != null) {
/* 360 */       this.ch.bind(localAddress);
/*     */     }
/*     */     
/* 363 */     boolean success = false;
/*     */     try {
/* 365 */       this.ch.connect(remoteAddress);
/* 366 */       boolean finishConnect = false;
/* 367 */       while (!finishConnect) {
/* 368 */         if (this.connectSelector.select(1000L) >= 0) {
/* 369 */           Set<SelectionKey> selectionKeys = this.connectSelector.selectedKeys();
/* 370 */           for (SelectionKey key : selectionKeys) {
/* 371 */             if (key.isConnectable()) {
/* 372 */               selectionKeys.clear();
/* 373 */               finishConnect = true;
/*     */               break;
/*     */             } 
/*     */           } 
/* 377 */           selectionKeys.clear();
/*     */         } 
/*     */       } 
/* 380 */       success = this.ch.finishConnect();
/*     */     } finally {
/* 382 */       if (!success) {
/* 383 */         doClose();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 390 */     doClose();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 395 */     closeSelector("read", this.readSelector);
/* 396 */     closeSelector("write", this.writeSelector);
/* 397 */     closeSelector("connect", this.connectSelector);
/* 398 */     this.ch.close();
/*     */   }
/*     */   
/*     */   private static void closeSelector(String selectorName, Selector selector) {
/*     */     try {
/* 403 */       selector.close();
/* 404 */     } catch (IOException e) {
/* 405 */       logger.warn("Failed to close a " + selectorName + " selector.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture bindAddress(InetAddress localAddress) {
/* 411 */     return bindAddress(localAddress, newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture bindAddress(final InetAddress localAddress, final ChannelPromise promise) {
/* 416 */     if (eventLoop().inEventLoop()) {
/*     */       try {
/* 418 */         this.ch.bindAddress(localAddress);
/* 419 */         promise.setSuccess();
/* 420 */       } catch (Throwable t) {
/* 421 */         promise.setFailure(t);
/*     */       } 
/*     */     } else {
/* 424 */       eventLoop().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 427 */               OioSctpChannel.this.bindAddress(localAddress, promise);
/*     */             }
/*     */           });
/*     */     } 
/* 431 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture unbindAddress(InetAddress localAddress) {
/* 436 */     return unbindAddress(localAddress, newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture unbindAddress(final InetAddress localAddress, final ChannelPromise promise) {
/* 441 */     if (eventLoop().inEventLoop()) {
/*     */       try {
/* 443 */         this.ch.unbindAddress(localAddress);
/* 444 */         promise.setSuccess();
/* 445 */       } catch (Throwable t) {
/* 446 */         promise.setFailure(t);
/*     */       } 
/*     */     } else {
/* 449 */       eventLoop().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 452 */               OioSctpChannel.this.unbindAddress(localAddress, promise);
/*     */             }
/*     */           });
/*     */     } 
/* 456 */     return (ChannelFuture)promise;
/*     */   }
/*     */   
/*     */   private final class OioSctpChannelConfig extends DefaultSctpChannelConfig {
/*     */     private OioSctpChannelConfig(OioSctpChannel channel, SctpChannel javaChannel) {
/* 461 */       super(channel, javaChannel);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void autoReadCleared() {
/* 466 */       OioSctpChannel.this.clearReadPending();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\sctp\oio\OioSctpChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */