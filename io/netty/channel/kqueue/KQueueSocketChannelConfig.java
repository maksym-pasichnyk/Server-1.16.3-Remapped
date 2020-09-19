/*     */ package io.netty.channel.kqueue;
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
/*     */ 
/*     */ public final class KQueueSocketChannelConfig
/*     */   extends KQueueChannelConfig
/*     */   implements SocketChannelConfig
/*     */ {
/*     */   private final KQueueSocketChannel channel;
/*     */   private volatile boolean allowHalfClosure;
/*     */   
/*     */   KQueueSocketChannelConfig(KQueueSocketChannel channel) {
/*  48 */     super(channel);
/*  49 */     this.channel = channel;
/*  50 */     if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
/*  51 */       setTcpNoDelay(true);
/*     */     }
/*  53 */     calculateMaxBytesPerGatheringWrite();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  58 */     return getOptions(super
/*  59 */         .getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE, KQueueChannelOption.SO_SNDLOWAT, KQueueChannelOption.TCP_NOPUSH });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  67 */     if (option == ChannelOption.SO_RCVBUF) {
/*  68 */       return (T)Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  70 */     if (option == ChannelOption.SO_SNDBUF) {
/*  71 */       return (T)Integer.valueOf(getSendBufferSize());
/*     */     }
/*  73 */     if (option == ChannelOption.TCP_NODELAY) {
/*  74 */       return (T)Boolean.valueOf(isTcpNoDelay());
/*     */     }
/*  76 */     if (option == ChannelOption.SO_KEEPALIVE) {
/*  77 */       return (T)Boolean.valueOf(isKeepAlive());
/*     */     }
/*  79 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  80 */       return (T)Boolean.valueOf(isReuseAddress());
/*     */     }
/*  82 */     if (option == ChannelOption.SO_LINGER) {
/*  83 */       return (T)Integer.valueOf(getSoLinger());
/*     */     }
/*  85 */     if (option == ChannelOption.IP_TOS) {
/*  86 */       return (T)Integer.valueOf(getTrafficClass());
/*     */     }
/*  88 */     if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
/*  89 */       return (T)Boolean.valueOf(isAllowHalfClosure());
/*     */     }
/*  91 */     if (option == KQueueChannelOption.SO_SNDLOWAT) {
/*  92 */       return (T)Integer.valueOf(getSndLowAt());
/*     */     }
/*  94 */     if (option == KQueueChannelOption.TCP_NOPUSH) {
/*  95 */       return (T)Boolean.valueOf(isTcpNoPush());
/*     */     }
/*  97 */     return super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/* 102 */     validate(option, value);
/*     */     
/* 104 */     if (option == ChannelOption.SO_RCVBUF) {
/* 105 */       setReceiveBufferSize(((Integer)value).intValue());
/* 106 */     } else if (option == ChannelOption.SO_SNDBUF) {
/* 107 */       setSendBufferSize(((Integer)value).intValue());
/* 108 */     } else if (option == ChannelOption.TCP_NODELAY) {
/* 109 */       setTcpNoDelay(((Boolean)value).booleanValue());
/* 110 */     } else if (option == ChannelOption.SO_KEEPALIVE) {
/* 111 */       setKeepAlive(((Boolean)value).booleanValue());
/* 112 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/* 113 */       setReuseAddress(((Boolean)value).booleanValue());
/* 114 */     } else if (option == ChannelOption.SO_LINGER) {
/* 115 */       setSoLinger(((Integer)value).intValue());
/* 116 */     } else if (option == ChannelOption.IP_TOS) {
/* 117 */       setTrafficClass(((Integer)value).intValue());
/* 118 */     } else if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
/* 119 */       setAllowHalfClosure(((Boolean)value).booleanValue());
/* 120 */     } else if (option == KQueueChannelOption.SO_SNDLOWAT) {
/* 121 */       setSndLowAt(((Integer)value).intValue());
/* 122 */     } else if (option == KQueueChannelOption.TCP_NOPUSH) {
/* 123 */       setTcpNoPush(((Boolean)value).booleanValue());
/*     */     } else {
/* 125 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/* 128 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReceiveBufferSize() {
/*     */     try {
/* 134 */       return this.channel.socket.getReceiveBufferSize();
/* 135 */     } catch (IOException e) {
/* 136 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSendBufferSize() {
/*     */     try {
/* 143 */       return this.channel.socket.getSendBufferSize();
/* 144 */     } catch (IOException e) {
/* 145 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSoLinger() {
/*     */     try {
/* 152 */       return this.channel.socket.getSoLinger();
/* 153 */     } catch (IOException e) {
/* 154 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTrafficClass() {
/*     */     try {
/* 161 */       return this.channel.socket.getTrafficClass();
/* 162 */     } catch (IOException e) {
/* 163 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isKeepAlive() {
/*     */     try {
/* 170 */       return this.channel.socket.isKeepAlive();
/* 171 */     } catch (IOException e) {
/* 172 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReuseAddress() {
/*     */     try {
/* 179 */       return this.channel.socket.isReuseAddress();
/* 180 */     } catch (IOException e) {
/* 181 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTcpNoDelay() {
/*     */     try {
/* 188 */       return this.channel.socket.isTcpNoDelay();
/* 189 */     } catch (IOException e) {
/* 190 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getSndLowAt() {
/*     */     try {
/* 196 */       return this.channel.socket.getSndLowAt();
/* 197 */     } catch (IOException e) {
/* 198 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setSndLowAt(int sndLowAt) {
/*     */     try {
/* 204 */       this.channel.socket.setSndLowAt(sndLowAt);
/* 205 */     } catch (IOException e) {
/* 206 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isTcpNoPush() {
/*     */     try {
/* 212 */       return this.channel.socket.isTcpNoPush();
/* 213 */     } catch (IOException e) {
/* 214 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setTcpNoPush(boolean tcpNoPush) {
/*     */     try {
/* 220 */       this.channel.socket.setTcpNoPush(tcpNoPush);
/* 221 */     } catch (IOException e) {
/* 222 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setKeepAlive(boolean keepAlive) {
/*     */     try {
/* 229 */       this.channel.socket.setKeepAlive(keepAlive);
/* 230 */       return this;
/* 231 */     } catch (IOException e) {
/* 232 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/*     */     try {
/* 239 */       this.channel.socket.setReceiveBufferSize(receiveBufferSize);
/* 240 */       return this;
/* 241 */     } catch (IOException e) {
/* 242 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setReuseAddress(boolean reuseAddress) {
/*     */     try {
/* 249 */       this.channel.socket.setReuseAddress(reuseAddress);
/* 250 */       return this;
/* 251 */     } catch (IOException e) {
/* 252 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setSendBufferSize(int sendBufferSize) {
/*     */     try {
/* 259 */       this.channel.socket.setSendBufferSize(sendBufferSize);
/* 260 */       calculateMaxBytesPerGatheringWrite();
/* 261 */       return this;
/* 262 */     } catch (IOException e) {
/* 263 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setSoLinger(int soLinger) {
/*     */     try {
/* 270 */       this.channel.socket.setSoLinger(soLinger);
/* 271 */       return this;
/* 272 */     } catch (IOException e) {
/* 273 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setTcpNoDelay(boolean tcpNoDelay) {
/*     */     try {
/* 280 */       this.channel.socket.setTcpNoDelay(tcpNoDelay);
/* 281 */       return this;
/* 282 */     } catch (IOException e) {
/* 283 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setTrafficClass(int trafficClass) {
/*     */     try {
/* 290 */       this.channel.socket.setTrafficClass(trafficClass);
/* 291 */       return this;
/* 292 */     } catch (IOException e) {
/* 293 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllowHalfClosure() {
/* 299 */     return this.allowHalfClosure;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setRcvAllocTransportProvidesGuess(boolean transportProvidesGuess) {
/* 304 */     super.setRcvAllocTransportProvidesGuess(transportProvidesGuess);
/* 305 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
/* 311 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setAllowHalfClosure(boolean allowHalfClosure) {
/* 316 */     this.allowHalfClosure = allowHalfClosure;
/* 317 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 322 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 323 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 329 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 330 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 335 */     super.setWriteSpinCount(writeSpinCount);
/* 336 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 341 */     super.setAllocator(allocator);
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 347 */     super.setRecvByteBufAllocator(allocator);
/* 348 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setAutoRead(boolean autoRead) {
/* 353 */     super.setAutoRead(autoRead);
/* 354 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setAutoClose(boolean autoClose) {
/* 359 */     super.setAutoClose(autoClose);
/* 360 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 366 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 367 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 373 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 374 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 379 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 380 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 385 */     super.setMessageSizeEstimator(estimator);
/* 386 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private void calculateMaxBytesPerGatheringWrite() {
/* 391 */     int newSendBufferSize = getSendBufferSize() << 1;
/* 392 */     if (newSendBufferSize > 0)
/* 393 */       setMaxBytesPerGatheringWrite((getSendBufferSize() << 1)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */