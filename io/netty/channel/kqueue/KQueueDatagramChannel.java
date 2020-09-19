/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.AddressedEnvelope;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultAddressedEnvelope;
/*     */ import io.netty.channel.socket.DatagramChannel;
/*     */ import io.netty.channel.socket.DatagramChannelConfig;
/*     */ import io.netty.channel.socket.DatagramPacket;
/*     */ import io.netty.channel.unix.DatagramSocketAddress;
/*     */ import io.netty.channel.unix.IovArray;
/*     */ import io.netty.channel.unix.UnixChannelUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.nio.ByteBuffer;
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
/*     */ public final class KQueueDatagramChannel
/*     */   extends AbstractKQueueChannel
/*     */   implements DatagramChannel
/*     */ {
/*  50 */   private static final ChannelMetadata METADATA = new ChannelMetadata(true);
/*  51 */   private static final String EXPECTED_TYPES = " (expected: " + 
/*  52 */     StringUtil.simpleClassName(DatagramPacket.class) + ", " + 
/*  53 */     StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + 
/*  54 */     StringUtil.simpleClassName(ByteBuf.class) + ", " + 
/*  55 */     StringUtil.simpleClassName(InetSocketAddress.class) + ">, " + 
/*  56 */     StringUtil.simpleClassName(ByteBuf.class) + ')';
/*     */   
/*     */   private volatile boolean connected;
/*     */   private final KQueueDatagramChannelConfig config;
/*     */   
/*     */   public KQueueDatagramChannel() {
/*  62 */     super((Channel)null, BsdSocket.newSocketDgram(), false);
/*  63 */     this.config = new KQueueDatagramChannelConfig(this);
/*     */   }
/*     */   
/*     */   public KQueueDatagramChannel(int fd) {
/*  67 */     this(new BsdSocket(fd), true);
/*     */   }
/*     */   
/*     */   KQueueDatagramChannel(BsdSocket socket, boolean active) {
/*  71 */     super((Channel)null, socket, active);
/*  72 */     this.config = new KQueueDatagramChannelConfig(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress remoteAddress() {
/*  77 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress localAddress() {
/*  82 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/*  87 */     return METADATA;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/*  93 */     return (this.socket.isOpen() && ((this.config.getActiveOnOpen() && isRegistered()) || this.active));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConnected() {
/*  98 */     return this.connected;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress) {
/* 103 */     return joinGroup(multicastAddress, newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, ChannelPromise promise) {
/*     */     try {
/* 109 */       return joinGroup(multicastAddress, 
/*     */           
/* 111 */           NetworkInterface.getByInetAddress(localAddress().getAddress()), (InetAddress)null, promise);
/*     */     }
/* 113 */     catch (SocketException e) {
/* 114 */       promise.setFailure(e);
/*     */       
/* 116 */       return (ChannelFuture)promise;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
/* 122 */     return joinGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
/* 129 */     return joinGroup(multicastAddress.getAddress(), networkInterface, (InetAddress)null, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
/* 135 */     return joinGroup(multicastAddress, networkInterface, source, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
/* 143 */     if (multicastAddress == null) {
/* 144 */       throw new NullPointerException("multicastAddress");
/*     */     }
/*     */     
/* 147 */     if (networkInterface == null) {
/* 148 */       throw new NullPointerException("networkInterface");
/*     */     }
/*     */     
/* 151 */     promise.setFailure(new UnsupportedOperationException("Multicast not supported"));
/* 152 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress) {
/* 157 */     return leaveGroup(multicastAddress, newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise promise) {
/*     */     try {
/* 163 */       return leaveGroup(multicastAddress, 
/* 164 */           NetworkInterface.getByInetAddress(localAddress().getAddress()), (InetAddress)null, promise);
/* 165 */     } catch (SocketException e) {
/* 166 */       promise.setFailure(e);
/*     */       
/* 168 */       return (ChannelFuture)promise;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
/* 174 */     return leaveGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
/* 181 */     return leaveGroup(multicastAddress.getAddress(), networkInterface, (InetAddress)null, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
/* 187 */     return leaveGroup(multicastAddress, networkInterface, source, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
/* 194 */     if (multicastAddress == null) {
/* 195 */       throw new NullPointerException("multicastAddress");
/*     */     }
/* 197 */     if (networkInterface == null) {
/* 198 */       throw new NullPointerException("networkInterface");
/*     */     }
/*     */     
/* 201 */     promise.setFailure(new UnsupportedOperationException("Multicast not supported"));
/*     */     
/* 203 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock) {
/* 210 */     return block(multicastAddress, networkInterface, sourceToBlock, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock, ChannelPromise promise) {
/* 217 */     if (multicastAddress == null) {
/* 218 */       throw new NullPointerException("multicastAddress");
/*     */     }
/* 220 */     if (sourceToBlock == null) {
/* 221 */       throw new NullPointerException("sourceToBlock");
/*     */     }
/*     */     
/* 224 */     if (networkInterface == null) {
/* 225 */       throw new NullPointerException("networkInterface");
/*     */     }
/* 227 */     promise.setFailure(new UnsupportedOperationException("Multicast not supported"));
/* 228 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock) {
/* 233 */     return block(multicastAddress, sourceToBlock, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock, ChannelPromise promise) {
/*     */     try {
/* 240 */       return block(multicastAddress, 
/*     */           
/* 242 */           NetworkInterface.getByInetAddress(localAddress().getAddress()), sourceToBlock, promise);
/*     */     }
/* 244 */     catch (Throwable e) {
/* 245 */       promise.setFailure(e);
/*     */       
/* 247 */       return (ChannelFuture)promise;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe() {
/* 252 */     return new KQueueDatagramChannelUnsafe();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 257 */     super.doBind(localAddress);
/* 258 */     this.active = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/*     */     while (true) {
/* 264 */       Object msg = in.current();
/* 265 */       if (msg == null) {
/*     */         
/* 267 */         writeFilter(false);
/*     */         
/*     */         break;
/*     */       } 
/*     */       try {
/* 272 */         boolean done = false;
/* 273 */         for (int i = config().getWriteSpinCount(); i > 0; i--) {
/* 274 */           if (doWriteMessage(msg)) {
/* 275 */             done = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 280 */         if (done) {
/* 281 */           in.remove();
/*     */           continue;
/*     */         } 
/* 284 */         writeFilter(true);
/*     */         
/*     */         break;
/* 287 */       } catch (IOException e) {
/*     */ 
/*     */ 
/*     */         
/* 291 */         in.remove(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private boolean doWriteMessage(Object msg) throws Exception {
/*     */     ByteBuf data;
/*     */     InetSocketAddress remoteAddress;
/*     */     long writtenBytes;
/* 299 */     if (msg instanceof AddressedEnvelope) {
/*     */       
/* 301 */       AddressedEnvelope<ByteBuf, InetSocketAddress> envelope = (AddressedEnvelope<ByteBuf, InetSocketAddress>)msg;
/*     */       
/* 303 */       data = (ByteBuf)envelope.content();
/* 304 */       remoteAddress = (InetSocketAddress)envelope.recipient();
/*     */     } else {
/* 306 */       data = (ByteBuf)msg;
/* 307 */       remoteAddress = null;
/*     */     } 
/*     */     
/* 310 */     int dataLen = data.readableBytes();
/* 311 */     if (dataLen == 0) {
/* 312 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 316 */     if (data.hasMemoryAddress()) {
/* 317 */       long memoryAddress = data.memoryAddress();
/* 318 */       if (remoteAddress == null) {
/* 319 */         writtenBytes = this.socket.writeAddress(memoryAddress, data.readerIndex(), data.writerIndex());
/*     */       } else {
/* 321 */         writtenBytes = this.socket.sendToAddress(memoryAddress, data.readerIndex(), data.writerIndex(), remoteAddress
/* 322 */             .getAddress(), remoteAddress.getPort());
/*     */       } 
/* 324 */     } else if (data.nioBufferCount() > 1) {
/* 325 */       IovArray array = ((KQueueEventLoop)eventLoop()).cleanArray();
/* 326 */       array.add(data);
/* 327 */       int cnt = array.count();
/* 328 */       assert cnt != 0;
/*     */       
/* 330 */       if (remoteAddress == null) {
/* 331 */         writtenBytes = this.socket.writevAddresses(array.memoryAddress(0), cnt);
/*     */       } else {
/* 333 */         writtenBytes = this.socket.sendToAddresses(array.memoryAddress(0), cnt, remoteAddress
/* 334 */             .getAddress(), remoteAddress.getPort());
/*     */       } 
/*     */     } else {
/* 337 */       ByteBuffer nioData = data.internalNioBuffer(data.readerIndex(), data.readableBytes());
/* 338 */       if (remoteAddress == null) {
/* 339 */         writtenBytes = this.socket.write(nioData, nioData.position(), nioData.limit());
/*     */       } else {
/* 341 */         writtenBytes = this.socket.sendTo(nioData, nioData.position(), nioData.limit(), remoteAddress
/* 342 */             .getAddress(), remoteAddress.getPort());
/*     */       } 
/*     */     } 
/*     */     
/* 346 */     return (writtenBytes > 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) {
/* 351 */     if (msg instanceof DatagramPacket) {
/* 352 */       DatagramPacket packet = (DatagramPacket)msg;
/* 353 */       ByteBuf content = (ByteBuf)packet.content();
/* 354 */       return UnixChannelUtil.isBufferCopyNeededForWrite(content) ? new DatagramPacket(
/* 355 */           newDirectBuffer(packet, content), (InetSocketAddress)packet.recipient()) : msg;
/*     */     } 
/*     */     
/* 358 */     if (msg instanceof ByteBuf) {
/* 359 */       ByteBuf buf = (ByteBuf)msg;
/* 360 */       return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? newDirectBuffer(buf) : buf;
/*     */     } 
/*     */     
/* 363 */     if (msg instanceof AddressedEnvelope) {
/*     */       
/* 365 */       AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope<Object, SocketAddress>)msg;
/* 366 */       if (e.content() instanceof ByteBuf && (e
/* 367 */         .recipient() == null || e.recipient() instanceof InetSocketAddress)) {
/*     */         
/* 369 */         ByteBuf content = (ByteBuf)e.content();
/* 370 */         return UnixChannelUtil.isBufferCopyNeededForWrite(content) ? new DefaultAddressedEnvelope(
/*     */             
/* 372 */             newDirectBuffer(e, content), e.recipient()) : e;
/*     */       } 
/*     */     } 
/*     */     
/* 376 */     throw new UnsupportedOperationException("unsupported message type: " + 
/* 377 */         StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig config() {
/* 382 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 387 */     this.socket.disconnect();
/* 388 */     this.connected = this.active = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/* 393 */     if (super.doConnect(remoteAddress, localAddress)) {
/* 394 */       this.connected = true;
/* 395 */       return true;
/*     */     } 
/* 397 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 402 */     super.doClose();
/* 403 */     this.connected = false;
/*     */   }
/*     */   
/*     */   final class KQueueDatagramChannelUnsafe
/*     */     extends AbstractKQueueChannel.AbstractKQueueUnsafe
/*     */   {
/*     */     void readReady(KQueueRecvByteAllocatorHandle allocHandle) {
/* 410 */       assert KQueueDatagramChannel.this.eventLoop().inEventLoop();
/* 411 */       DatagramChannelConfig config = KQueueDatagramChannel.this.config();
/* 412 */       if (KQueueDatagramChannel.this.shouldBreakReadReady((ChannelConfig)config)) {
/* 413 */         clearReadFilter0();
/*     */         return;
/*     */       } 
/* 416 */       ChannelPipeline pipeline = KQueueDatagramChannel.this.pipeline();
/* 417 */       ByteBufAllocator allocator = config.getAllocator();
/* 418 */       allocHandle.reset((ChannelConfig)config);
/* 419 */       readReadyBefore();
/*     */       
/* 421 */       Throwable exception = null;
/*     */       try {
/* 423 */         ByteBuf data = null; try {
/*     */           do {
/*     */             DatagramSocketAddress remoteAddress;
/* 426 */             data = allocHandle.allocate(allocator);
/* 427 */             allocHandle.attemptedBytesRead(data.writableBytes());
/*     */             
/* 429 */             if (data.hasMemoryAddress()) {
/*     */               
/* 431 */               remoteAddress = KQueueDatagramChannel.this.socket.recvFromAddress(data.memoryAddress(), data.writerIndex(), data
/* 432 */                   .capacity());
/*     */             } else {
/* 434 */               ByteBuffer nioData = data.internalNioBuffer(data.writerIndex(), data.writableBytes());
/* 435 */               remoteAddress = KQueueDatagramChannel.this.socket.recvFrom(nioData, nioData.position(), nioData.limit());
/*     */             } 
/*     */             
/* 438 */             if (remoteAddress == null) {
/* 439 */               allocHandle.lastBytesRead(-1);
/* 440 */               data.release();
/* 441 */               data = null;
/*     */               
/*     */               break;
/*     */             } 
/* 445 */             allocHandle.incMessagesRead(1);
/* 446 */             allocHandle.lastBytesRead(remoteAddress.receivedAmount());
/* 447 */             data.writerIndex(data.writerIndex() + allocHandle.lastBytesRead());
/*     */             
/* 449 */             this.readPending = false;
/* 450 */             pipeline.fireChannelRead(new DatagramPacket(data, (InetSocketAddress)
/* 451 */                   localAddress(), (InetSocketAddress)remoteAddress));
/*     */             
/* 453 */             data = null;
/* 454 */           } while (allocHandle.continueReading());
/* 455 */         } catch (Throwable t) {
/* 456 */           if (data != null) {
/* 457 */             data.release();
/*     */           }
/* 459 */           exception = t;
/*     */         } 
/*     */         
/* 462 */         allocHandle.readComplete();
/* 463 */         pipeline.fireChannelReadComplete();
/*     */         
/* 465 */         if (exception != null) {
/* 466 */           pipeline.fireExceptionCaught(exception);
/*     */         }
/*     */       } finally {
/* 469 */         readReadyFinally((ChannelConfig)config);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueDatagramChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */