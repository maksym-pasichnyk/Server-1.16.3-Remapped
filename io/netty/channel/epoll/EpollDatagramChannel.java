/*     */ package io.netty.channel.epoll;
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
/*     */ public final class EpollDatagramChannel
/*     */   extends AbstractEpollChannel
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
/*     */   private final EpollDatagramChannelConfig config;
/*     */   private volatile boolean connected;
/*     */   
/*     */   public EpollDatagramChannel() {
/*  62 */     super(LinuxSocket.newSocketDgram(), Native.EPOLLIN);
/*  63 */     this.config = new EpollDatagramChannelConfig(this);
/*     */   }
/*     */   
/*     */   public EpollDatagramChannel(int fd) {
/*  67 */     this(new LinuxSocket(fd));
/*     */   }
/*     */   
/*     */   EpollDatagramChannel(LinuxSocket fd) {
/*  71 */     super((Channel)null, fd, Native.EPOLLIN, true);
/*  72 */     this.config = new EpollDatagramChannelConfig(this);
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
/* 112 */     } catch (SocketException e) {
/* 113 */       promise.setFailure(e);
/*     */       
/* 115 */       return (ChannelFuture)promise;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
/* 121 */     return joinGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
/* 128 */     return joinGroup(multicastAddress.getAddress(), networkInterface, (InetAddress)null, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
/* 134 */     return joinGroup(multicastAddress, networkInterface, source, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
/* 142 */     if (multicastAddress == null) {
/* 143 */       throw new NullPointerException("multicastAddress");
/*     */     }
/*     */     
/* 146 */     if (networkInterface == null) {
/* 147 */       throw new NullPointerException("networkInterface");
/*     */     }
/*     */     
/* 150 */     promise.setFailure(new UnsupportedOperationException("Multicast not supported"));
/* 151 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress) {
/* 156 */     return leaveGroup(multicastAddress, newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise promise) {
/*     */     try {
/* 162 */       return leaveGroup(multicastAddress, 
/* 163 */           NetworkInterface.getByInetAddress(localAddress().getAddress()), (InetAddress)null, promise);
/* 164 */     } catch (SocketException e) {
/* 165 */       promise.setFailure(e);
/*     */       
/* 167 */       return (ChannelFuture)promise;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
/* 173 */     return leaveGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
/* 180 */     return leaveGroup(multicastAddress.getAddress(), networkInterface, (InetAddress)null, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
/* 186 */     return leaveGroup(multicastAddress, networkInterface, source, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
/* 193 */     if (multicastAddress == null) {
/* 194 */       throw new NullPointerException("multicastAddress");
/*     */     }
/* 196 */     if (networkInterface == null) {
/* 197 */       throw new NullPointerException("networkInterface");
/*     */     }
/*     */     
/* 200 */     promise.setFailure(new UnsupportedOperationException("Multicast not supported"));
/*     */     
/* 202 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock) {
/* 209 */     return block(multicastAddress, networkInterface, sourceToBlock, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock, ChannelPromise promise) {
/* 216 */     if (multicastAddress == null) {
/* 217 */       throw new NullPointerException("multicastAddress");
/*     */     }
/* 219 */     if (sourceToBlock == null) {
/* 220 */       throw new NullPointerException("sourceToBlock");
/*     */     }
/*     */     
/* 223 */     if (networkInterface == null) {
/* 224 */       throw new NullPointerException("networkInterface");
/*     */     }
/* 226 */     promise.setFailure(new UnsupportedOperationException("Multicast not supported"));
/* 227 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock) {
/* 232 */     return block(multicastAddress, sourceToBlock, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock, ChannelPromise promise) {
/*     */     try {
/* 239 */       return block(multicastAddress, 
/*     */           
/* 241 */           NetworkInterface.getByInetAddress(localAddress().getAddress()), sourceToBlock, promise);
/*     */     }
/* 243 */     catch (Throwable e) {
/* 244 */       promise.setFailure(e);
/*     */       
/* 246 */       return (ChannelFuture)promise;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
/* 251 */     return new EpollDatagramChannelUnsafe();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 256 */     super.doBind(localAddress);
/* 257 */     this.active = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/*     */     while (true) {
/* 263 */       Object msg = in.current();
/* 264 */       if (msg == null) {
/*     */         
/* 266 */         clearFlag(Native.EPOLLOUT);
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/*     */       try {
/* 272 */         if (Native.IS_SUPPORTING_SENDMMSG && in.size() > 1) {
/* 273 */           NativeDatagramPacketArray array = NativeDatagramPacketArray.getInstance(in);
/* 274 */           int cnt = array.count();
/*     */           
/* 276 */           if (cnt >= 1) {
/*     */             
/* 278 */             int offset = 0;
/* 279 */             NativeDatagramPacketArray.NativeDatagramPacket[] packets = array.packets();
/*     */             
/* 281 */             while (cnt > 0) {
/* 282 */               int send = Native.sendmmsg(this.socket.intValue(), packets, offset, cnt);
/* 283 */               if (send == 0) {
/*     */                 
/* 285 */                 setFlag(Native.EPOLLOUT);
/*     */                 return;
/*     */               } 
/* 288 */               for (int j = 0; j < send; j++) {
/* 289 */                 in.remove();
/*     */               }
/* 291 */               cnt -= send;
/* 292 */               offset += send;
/*     */             } 
/*     */             continue;
/*     */           } 
/*     */         } 
/* 297 */         boolean done = false;
/* 298 */         for (int i = config().getWriteSpinCount(); i > 0; i--) {
/* 299 */           if (doWriteMessage(msg)) {
/* 300 */             done = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 305 */         if (done) {
/* 306 */           in.remove();
/*     */           continue;
/*     */         } 
/* 309 */         setFlag(Native.EPOLLOUT);
/*     */         
/*     */         break;
/* 312 */       } catch (IOException e) {
/*     */ 
/*     */ 
/*     */         
/* 316 */         in.remove(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private boolean doWriteMessage(Object msg) throws Exception {
/*     */     ByteBuf data;
/*     */     InetSocketAddress remoteAddress;
/*     */     long writtenBytes;
/* 324 */     if (msg instanceof AddressedEnvelope) {
/*     */       
/* 326 */       AddressedEnvelope<ByteBuf, InetSocketAddress> envelope = (AddressedEnvelope<ByteBuf, InetSocketAddress>)msg;
/*     */       
/* 328 */       data = (ByteBuf)envelope.content();
/* 329 */       remoteAddress = (InetSocketAddress)envelope.recipient();
/*     */     } else {
/* 331 */       data = (ByteBuf)msg;
/* 332 */       remoteAddress = null;
/*     */     } 
/*     */     
/* 335 */     int dataLen = data.readableBytes();
/* 336 */     if (dataLen == 0) {
/* 337 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 341 */     if (data.hasMemoryAddress()) {
/* 342 */       long memoryAddress = data.memoryAddress();
/* 343 */       if (remoteAddress == null) {
/* 344 */         writtenBytes = this.socket.writeAddress(memoryAddress, data.readerIndex(), data.writerIndex());
/*     */       } else {
/* 346 */         writtenBytes = this.socket.sendToAddress(memoryAddress, data.readerIndex(), data.writerIndex(), remoteAddress
/* 347 */             .getAddress(), remoteAddress.getPort());
/*     */       } 
/* 349 */     } else if (data.nioBufferCount() > 1) {
/* 350 */       IovArray array = ((EpollEventLoop)eventLoop()).cleanArray();
/* 351 */       array.add(data);
/* 352 */       int cnt = array.count();
/* 353 */       assert cnt != 0;
/*     */       
/* 355 */       if (remoteAddress == null) {
/* 356 */         writtenBytes = this.socket.writevAddresses(array.memoryAddress(0), cnt);
/*     */       } else {
/* 358 */         writtenBytes = this.socket.sendToAddresses(array.memoryAddress(0), cnt, remoteAddress
/* 359 */             .getAddress(), remoteAddress.getPort());
/*     */       } 
/*     */     } else {
/* 362 */       ByteBuffer nioData = data.internalNioBuffer(data.readerIndex(), data.readableBytes());
/* 363 */       if (remoteAddress == null) {
/* 364 */         writtenBytes = this.socket.write(nioData, nioData.position(), nioData.limit());
/*     */       } else {
/* 366 */         writtenBytes = this.socket.sendTo(nioData, nioData.position(), nioData.limit(), remoteAddress
/* 367 */             .getAddress(), remoteAddress.getPort());
/*     */       } 
/*     */     } 
/*     */     
/* 371 */     return (writtenBytes > 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) {
/* 376 */     if (msg instanceof DatagramPacket) {
/* 377 */       DatagramPacket packet = (DatagramPacket)msg;
/* 378 */       ByteBuf content = (ByteBuf)packet.content();
/* 379 */       return UnixChannelUtil.isBufferCopyNeededForWrite(content) ? new DatagramPacket(
/* 380 */           newDirectBuffer(packet, content), (InetSocketAddress)packet.recipient()) : msg;
/*     */     } 
/*     */     
/* 383 */     if (msg instanceof ByteBuf) {
/* 384 */       ByteBuf buf = (ByteBuf)msg;
/* 385 */       return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? newDirectBuffer(buf) : buf;
/*     */     } 
/*     */     
/* 388 */     if (msg instanceof AddressedEnvelope) {
/*     */       
/* 390 */       AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope<Object, SocketAddress>)msg;
/* 391 */       if (e.content() instanceof ByteBuf && (e
/* 392 */         .recipient() == null || e.recipient() instanceof InetSocketAddress)) {
/*     */         
/* 394 */         ByteBuf content = (ByteBuf)e.content();
/* 395 */         return UnixChannelUtil.isBufferCopyNeededForWrite(content) ? new DefaultAddressedEnvelope(
/*     */             
/* 397 */             newDirectBuffer(e, content), e.recipient()) : e;
/*     */       } 
/*     */     } 
/*     */     
/* 401 */     throw new UnsupportedOperationException("unsupported message type: " + 
/* 402 */         StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig config() {
/* 407 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 412 */     this.socket.disconnect();
/* 413 */     this.connected = this.active = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/* 418 */     if (super.doConnect(remoteAddress, localAddress)) {
/* 419 */       this.connected = true;
/* 420 */       return true;
/*     */     } 
/* 422 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 427 */     super.doClose();
/* 428 */     this.connected = false;
/*     */   }
/*     */   
/*     */   final class EpollDatagramChannelUnsafe
/*     */     extends AbstractEpollChannel.AbstractEpollUnsafe
/*     */   {
/*     */     void epollInReady() {
/* 435 */       assert EpollDatagramChannel.this.eventLoop().inEventLoop();
/* 436 */       DatagramChannelConfig config = EpollDatagramChannel.this.config();
/* 437 */       if (EpollDatagramChannel.this.shouldBreakEpollInReady((ChannelConfig)config)) {
/* 438 */         clearEpollIn0();
/*     */         return;
/*     */       } 
/* 441 */       EpollRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
/* 442 */       allocHandle.edgeTriggered(EpollDatagramChannel.this.isFlagSet(Native.EPOLLET));
/*     */       
/* 444 */       ChannelPipeline pipeline = EpollDatagramChannel.this.pipeline();
/* 445 */       ByteBufAllocator allocator = config.getAllocator();
/* 446 */       allocHandle.reset((ChannelConfig)config);
/* 447 */       epollInBefore();
/*     */       
/* 449 */       Throwable exception = null;
/*     */       try {
/* 451 */         ByteBuf data = null; try {
/*     */           do {
/*     */             DatagramSocketAddress remoteAddress; InetSocketAddress inetSocketAddress;
/* 454 */             data = allocHandle.allocate(allocator);
/* 455 */             allocHandle.attemptedBytesRead(data.writableBytes());
/*     */             
/* 457 */             if (data.hasMemoryAddress()) {
/*     */               
/* 459 */               remoteAddress = EpollDatagramChannel.this.socket.recvFromAddress(data.memoryAddress(), data.writerIndex(), data
/* 460 */                   .capacity());
/*     */             } else {
/* 462 */               ByteBuffer nioData = data.internalNioBuffer(data.writerIndex(), data.writableBytes());
/* 463 */               remoteAddress = EpollDatagramChannel.this.socket.recvFrom(nioData, nioData.position(), nioData.limit());
/*     */             } 
/*     */             
/* 466 */             if (remoteAddress == null) {
/* 467 */               allocHandle.lastBytesRead(-1);
/* 468 */               data.release();
/* 469 */               data = null;
/*     */               
/*     */               break;
/*     */             } 
/* 473 */             DatagramSocketAddress datagramSocketAddress1 = remoteAddress.localAddress();
/* 474 */             if (datagramSocketAddress1 == null) {
/* 475 */               inetSocketAddress = (InetSocketAddress)localAddress();
/*     */             }
/*     */             
/* 478 */             allocHandle.incMessagesRead(1);
/* 479 */             allocHandle.lastBytesRead(remoteAddress.receivedAmount());
/* 480 */             data.writerIndex(data.writerIndex() + allocHandle.lastBytesRead());
/*     */             
/* 482 */             this.readPending = false;
/* 483 */             pipeline.fireChannelRead(new DatagramPacket(data, inetSocketAddress, (InetSocketAddress)remoteAddress));
/*     */ 
/*     */             
/* 486 */             data = null;
/* 487 */           } while (allocHandle.continueReading());
/* 488 */         } catch (Throwable t) {
/* 489 */           if (data != null) {
/* 490 */             data.release();
/*     */           }
/* 492 */           exception = t;
/*     */         } 
/*     */         
/* 495 */         allocHandle.readComplete();
/* 496 */         pipeline.fireChannelReadComplete();
/*     */         
/* 498 */         if (exception != null) {
/* 499 */           pipeline.fireExceptionCaught(exception);
/*     */         }
/*     */       } finally {
/* 502 */         epollInFinally((ChannelConfig)config);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollDatagramChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */