/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
/*     */ import io.netty.channel.socket.SocketChannelConfig;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EpollSocketChannelConfig
/*     */   extends EpollChannelConfig
/*     */   implements SocketChannelConfig
/*     */ {
/*     */   private final EpollSocketChannel channel;
/*     */   private volatile boolean allowHalfClosure;
/*     */   
/*     */   EpollSocketChannelConfig(EpollSocketChannel channel) {
/*  48 */     super(channel);
/*     */     
/*  50 */     this.channel = channel;
/*  51 */     if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
/*  52 */       setTcpNoDelay(true);
/*     */     }
/*  54 */     calculateMaxBytesPerGatheringWrite();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  59 */     return getOptions(super
/*  60 */         .getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE, EpollChannelOption.TCP_CORK, EpollChannelOption.TCP_NOTSENT_LOWAT, EpollChannelOption.TCP_KEEPCNT, EpollChannelOption.TCP_KEEPIDLE, EpollChannelOption.TCP_KEEPINTVL, EpollChannelOption.TCP_MD5SIG, EpollChannelOption.TCP_QUICKACK, EpollChannelOption.IP_TRANSPARENT, EpollChannelOption.TCP_FASTOPEN_CONNECT });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  71 */     if (option == ChannelOption.SO_RCVBUF) {
/*  72 */       return (T)Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  74 */     if (option == ChannelOption.SO_SNDBUF) {
/*  75 */       return (T)Integer.valueOf(getSendBufferSize());
/*     */     }
/*  77 */     if (option == ChannelOption.TCP_NODELAY) {
/*  78 */       return (T)Boolean.valueOf(isTcpNoDelay());
/*     */     }
/*  80 */     if (option == ChannelOption.SO_KEEPALIVE) {
/*  81 */       return (T)Boolean.valueOf(isKeepAlive());
/*     */     }
/*  83 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  84 */       return (T)Boolean.valueOf(isReuseAddress());
/*     */     }
/*  86 */     if (option == ChannelOption.SO_LINGER) {
/*  87 */       return (T)Integer.valueOf(getSoLinger());
/*     */     }
/*  89 */     if (option == ChannelOption.IP_TOS) {
/*  90 */       return (T)Integer.valueOf(getTrafficClass());
/*     */     }
/*  92 */     if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
/*  93 */       return (T)Boolean.valueOf(isAllowHalfClosure());
/*     */     }
/*  95 */     if (option == EpollChannelOption.TCP_CORK) {
/*  96 */       return (T)Boolean.valueOf(isTcpCork());
/*     */     }
/*  98 */     if (option == EpollChannelOption.TCP_NOTSENT_LOWAT) {
/*  99 */       return (T)Long.valueOf(getTcpNotSentLowAt());
/*     */     }
/* 101 */     if (option == EpollChannelOption.TCP_KEEPIDLE) {
/* 102 */       return (T)Integer.valueOf(getTcpKeepIdle());
/*     */     }
/* 104 */     if (option == EpollChannelOption.TCP_KEEPINTVL) {
/* 105 */       return (T)Integer.valueOf(getTcpKeepIntvl());
/*     */     }
/* 107 */     if (option == EpollChannelOption.TCP_KEEPCNT) {
/* 108 */       return (T)Integer.valueOf(getTcpKeepCnt());
/*     */     }
/* 110 */     if (option == EpollChannelOption.TCP_USER_TIMEOUT) {
/* 111 */       return (T)Integer.valueOf(getTcpUserTimeout());
/*     */     }
/* 113 */     if (option == EpollChannelOption.TCP_QUICKACK) {
/* 114 */       return (T)Boolean.valueOf(isTcpQuickAck());
/*     */     }
/* 116 */     if (option == EpollChannelOption.IP_TRANSPARENT) {
/* 117 */       return (T)Boolean.valueOf(isIpTransparent());
/*     */     }
/* 119 */     if (option == EpollChannelOption.TCP_FASTOPEN_CONNECT) {
/* 120 */       return (T)Boolean.valueOf(isTcpFastOpenConnect());
/*     */     }
/* 122 */     return super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/* 127 */     validate(option, value);
/*     */     
/* 129 */     if (option == ChannelOption.SO_RCVBUF) {
/* 130 */       setReceiveBufferSize(((Integer)value).intValue());
/* 131 */     } else if (option == ChannelOption.SO_SNDBUF) {
/* 132 */       setSendBufferSize(((Integer)value).intValue());
/* 133 */     } else if (option == ChannelOption.TCP_NODELAY) {
/* 134 */       setTcpNoDelay(((Boolean)value).booleanValue());
/* 135 */     } else if (option == ChannelOption.SO_KEEPALIVE) {
/* 136 */       setKeepAlive(((Boolean)value).booleanValue());
/* 137 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/* 138 */       setReuseAddress(((Boolean)value).booleanValue());
/* 139 */     } else if (option == ChannelOption.SO_LINGER) {
/* 140 */       setSoLinger(((Integer)value).intValue());
/* 141 */     } else if (option == ChannelOption.IP_TOS) {
/* 142 */       setTrafficClass(((Integer)value).intValue());
/* 143 */     } else if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
/* 144 */       setAllowHalfClosure(((Boolean)value).booleanValue());
/* 145 */     } else if (option == EpollChannelOption.TCP_CORK) {
/* 146 */       setTcpCork(((Boolean)value).booleanValue());
/* 147 */     } else if (option == EpollChannelOption.TCP_NOTSENT_LOWAT) {
/* 148 */       setTcpNotSentLowAt(((Long)value).longValue());
/* 149 */     } else if (option == EpollChannelOption.TCP_KEEPIDLE) {
/* 150 */       setTcpKeepIdle(((Integer)value).intValue());
/* 151 */     } else if (option == EpollChannelOption.TCP_KEEPCNT) {
/* 152 */       setTcpKeepCnt(((Integer)value).intValue());
/* 153 */     } else if (option == EpollChannelOption.TCP_KEEPINTVL) {
/* 154 */       setTcpKeepIntvl(((Integer)value).intValue());
/* 155 */     } else if (option == EpollChannelOption.TCP_USER_TIMEOUT) {
/* 156 */       setTcpUserTimeout(((Integer)value).intValue());
/* 157 */     } else if (option == EpollChannelOption.IP_TRANSPARENT) {
/* 158 */       setIpTransparent(((Boolean)value).booleanValue());
/* 159 */     } else if (option == EpollChannelOption.TCP_MD5SIG) {
/*     */       
/* 161 */       Map<InetAddress, byte[]> m = (Map<InetAddress, byte[]>)value;
/* 162 */       setTcpMd5Sig(m);
/* 163 */     } else if (option == EpollChannelOption.TCP_QUICKACK) {
/* 164 */       setTcpQuickAck(((Boolean)value).booleanValue());
/* 165 */     } else if (option == EpollChannelOption.TCP_FASTOPEN_CONNECT) {
/* 166 */       setTcpFastOpenConnect(((Boolean)value).booleanValue());
/*     */     } else {
/* 168 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReceiveBufferSize() {
/*     */     try {
/* 177 */       return this.channel.socket.getReceiveBufferSize();
/* 178 */     } catch (IOException e) {
/* 179 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSendBufferSize() {
/*     */     try {
/* 186 */       return this.channel.socket.getSendBufferSize();
/* 187 */     } catch (IOException e) {
/* 188 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSoLinger() {
/*     */     try {
/* 195 */       return this.channel.socket.getSoLinger();
/* 196 */     } catch (IOException e) {
/* 197 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTrafficClass() {
/*     */     try {
/* 204 */       return this.channel.socket.getTrafficClass();
/* 205 */     } catch (IOException e) {
/* 206 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isKeepAlive() {
/*     */     try {
/* 213 */       return this.channel.socket.isKeepAlive();
/* 214 */     } catch (IOException e) {
/* 215 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReuseAddress() {
/*     */     try {
/* 222 */       return this.channel.socket.isReuseAddress();
/* 223 */     } catch (IOException e) {
/* 224 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTcpNoDelay() {
/*     */     try {
/* 231 */       return this.channel.socket.isTcpNoDelay();
/* 232 */     } catch (IOException e) {
/* 233 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTcpCork() {
/*     */     try {
/* 242 */       return this.channel.socket.isTcpCork();
/* 243 */     } catch (IOException e) {
/* 244 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTcpNotSentLowAt() {
/*     */     try {
/* 254 */       return this.channel.socket.getTcpNotSentLowAt();
/* 255 */     } catch (IOException e) {
/* 256 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTcpKeepIdle() {
/*     */     try {
/* 265 */       return this.channel.socket.getTcpKeepIdle();
/* 266 */     } catch (IOException e) {
/* 267 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTcpKeepIntvl() {
/*     */     try {
/* 276 */       return this.channel.socket.getTcpKeepIntvl();
/* 277 */     } catch (IOException e) {
/* 278 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTcpKeepCnt() {
/*     */     try {
/* 287 */       return this.channel.socket.getTcpKeepCnt();
/* 288 */     } catch (IOException e) {
/* 289 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTcpUserTimeout() {
/*     */     try {
/* 298 */       return this.channel.socket.getTcpUserTimeout();
/* 299 */     } catch (IOException e) {
/* 300 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setKeepAlive(boolean keepAlive) {
/*     */     try {
/* 307 */       this.channel.socket.setKeepAlive(keepAlive);
/* 308 */       return this;
/* 309 */     } catch (IOException e) {
/* 310 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
/* 317 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/*     */     try {
/* 323 */       this.channel.socket.setReceiveBufferSize(receiveBufferSize);
/* 324 */       return this;
/* 325 */     } catch (IOException e) {
/* 326 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setReuseAddress(boolean reuseAddress) {
/*     */     try {
/* 333 */       this.channel.socket.setReuseAddress(reuseAddress);
/* 334 */       return this;
/* 335 */     } catch (IOException e) {
/* 336 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setSendBufferSize(int sendBufferSize) {
/*     */     try {
/* 343 */       this.channel.socket.setSendBufferSize(sendBufferSize);
/* 344 */       calculateMaxBytesPerGatheringWrite();
/* 345 */       return this;
/* 346 */     } catch (IOException e) {
/* 347 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setSoLinger(int soLinger) {
/*     */     try {
/* 354 */       this.channel.socket.setSoLinger(soLinger);
/* 355 */       return this;
/* 356 */     } catch (IOException e) {
/* 357 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setTcpNoDelay(boolean tcpNoDelay) {
/*     */     try {
/* 364 */       this.channel.socket.setTcpNoDelay(tcpNoDelay);
/* 365 */       return this;
/* 366 */     } catch (IOException e) {
/* 367 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setTcpCork(boolean tcpCork) {
/*     */     try {
/* 376 */       this.channel.socket.setTcpCork(tcpCork);
/* 377 */       return this;
/* 378 */     } catch (IOException e) {
/* 379 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setTcpNotSentLowAt(long tcpNotSentLowAt) {
/*     */     try {
/* 389 */       this.channel.socket.setTcpNotSentLowAt(tcpNotSentLowAt);
/* 390 */       return this;
/* 391 */     } catch (IOException e) {
/* 392 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setTrafficClass(int trafficClass) {
/*     */     try {
/* 399 */       this.channel.socket.setTrafficClass(trafficClass);
/* 400 */       return this;
/* 401 */     } catch (IOException e) {
/* 402 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setTcpKeepIdle(int seconds) {
/*     */     try {
/* 411 */       this.channel.socket.setTcpKeepIdle(seconds);
/* 412 */       return this;
/* 413 */     } catch (IOException e) {
/* 414 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setTcpKeepIntvl(int seconds) {
/*     */     try {
/* 423 */       this.channel.socket.setTcpKeepIntvl(seconds);
/* 424 */       return this;
/* 425 */     } catch (IOException e) {
/* 426 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollSocketChannelConfig setTcpKeepCntl(int probes) {
/* 435 */     return setTcpKeepCnt(probes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setTcpKeepCnt(int probes) {
/*     */     try {
/* 443 */       this.channel.socket.setTcpKeepCnt(probes);
/* 444 */       return this;
/* 445 */     } catch (IOException e) {
/* 446 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setTcpUserTimeout(int milliseconds) {
/*     */     try {
/* 455 */       this.channel.socket.setTcpUserTimeout(milliseconds);
/* 456 */       return this;
/* 457 */     } catch (IOException e) {
/* 458 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIpTransparent() {
/*     */     try {
/* 468 */       return this.channel.socket.isIpTransparent();
/* 469 */     } catch (IOException e) {
/* 470 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setIpTransparent(boolean transparent) {
/*     */     try {
/* 480 */       this.channel.socket.setIpTransparent(transparent);
/* 481 */       return this;
/* 482 */     } catch (IOException e) {
/* 483 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setTcpMd5Sig(Map<InetAddress, byte[]> keys) {
/*     */     try {
/* 494 */       this.channel.setTcpMd5Sig(keys);
/* 495 */       return this;
/* 496 */     } catch (IOException e) {
/* 497 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setTcpQuickAck(boolean quickAck) {
/*     */     try {
/* 507 */       this.channel.socket.setTcpQuickAck(quickAck);
/* 508 */       return this;
/* 509 */     } catch (IOException e) {
/* 510 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTcpQuickAck() {
/*     */     try {
/* 520 */       return this.channel.socket.isTcpQuickAck();
/* 521 */     } catch (IOException e) {
/* 522 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setTcpFastOpenConnect(boolean fastOpenConnect) {
/*     */     try {
/* 534 */       this.channel.socket.setTcpFastOpenConnect(fastOpenConnect);
/* 535 */       return this;
/* 536 */     } catch (IOException e) {
/* 537 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTcpFastOpenConnect() {
/*     */     try {
/* 546 */       return this.channel.socket.isTcpFastOpenConnect();
/* 547 */     } catch (IOException e) {
/* 548 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllowHalfClosure() {
/* 554 */     return this.allowHalfClosure;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setAllowHalfClosure(boolean allowHalfClosure) {
/* 559 */     this.allowHalfClosure = allowHalfClosure;
/* 560 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 565 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 566 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 572 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 573 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 578 */     super.setWriteSpinCount(writeSpinCount);
/* 579 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 584 */     super.setAllocator(allocator);
/* 585 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 590 */     super.setRecvByteBufAllocator(allocator);
/* 591 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setAutoRead(boolean autoRead) {
/* 596 */     super.setAutoRead(autoRead);
/* 597 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setAutoClose(boolean autoClose) {
/* 602 */     super.setAutoClose(autoClose);
/* 603 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 609 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 610 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 616 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 617 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 622 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 623 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 628 */     super.setMessageSizeEstimator(estimator);
/* 629 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig setEpollMode(EpollMode mode) {
/* 634 */     super.setEpollMode(mode);
/* 635 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private void calculateMaxBytesPerGatheringWrite() {
/* 640 */     int newSendBufferSize = getSendBufferSize() << 1;
/* 641 */     if (newSendBufferSize > 0)
/* 642 */       setMaxBytesPerGatheringWrite((getSendBufferSize() << 1)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */