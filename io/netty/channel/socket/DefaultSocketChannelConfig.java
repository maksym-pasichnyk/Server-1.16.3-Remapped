/*     */ package io.netty.channel.socket;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
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
/*     */ public class DefaultSocketChannelConfig
/*     */   extends DefaultChannelConfig
/*     */   implements SocketChannelConfig
/*     */ {
/*     */   protected final Socket javaSocket;
/*     */   private volatile boolean allowHalfClosure;
/*     */   
/*     */   public DefaultSocketChannelConfig(SocketChannel channel, Socket javaSocket) {
/*  46 */     super(channel);
/*  47 */     if (javaSocket == null) {
/*  48 */       throw new NullPointerException("javaSocket");
/*     */     }
/*  50 */     this.javaSocket = javaSocket;
/*     */ 
/*     */     
/*  53 */     if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
/*     */       try {
/*  55 */         setTcpNoDelay(true);
/*  56 */       } catch (Exception exception) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  64 */     return getOptions(super
/*  65 */         .getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  73 */     if (option == ChannelOption.SO_RCVBUF) {
/*  74 */       return (T)Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  76 */     if (option == ChannelOption.SO_SNDBUF) {
/*  77 */       return (T)Integer.valueOf(getSendBufferSize());
/*     */     }
/*  79 */     if (option == ChannelOption.TCP_NODELAY) {
/*  80 */       return (T)Boolean.valueOf(isTcpNoDelay());
/*     */     }
/*  82 */     if (option == ChannelOption.SO_KEEPALIVE) {
/*  83 */       return (T)Boolean.valueOf(isKeepAlive());
/*     */     }
/*  85 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  86 */       return (T)Boolean.valueOf(isReuseAddress());
/*     */     }
/*  88 */     if (option == ChannelOption.SO_LINGER) {
/*  89 */       return (T)Integer.valueOf(getSoLinger());
/*     */     }
/*  91 */     if (option == ChannelOption.IP_TOS) {
/*  92 */       return (T)Integer.valueOf(getTrafficClass());
/*     */     }
/*  94 */     if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
/*  95 */       return (T)Boolean.valueOf(isAllowHalfClosure());
/*     */     }
/*     */     
/*  98 */     return (T)super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/* 103 */     validate(option, value);
/*     */     
/* 105 */     if (option == ChannelOption.SO_RCVBUF) {
/* 106 */       setReceiveBufferSize(((Integer)value).intValue());
/* 107 */     } else if (option == ChannelOption.SO_SNDBUF) {
/* 108 */       setSendBufferSize(((Integer)value).intValue());
/* 109 */     } else if (option == ChannelOption.TCP_NODELAY) {
/* 110 */       setTcpNoDelay(((Boolean)value).booleanValue());
/* 111 */     } else if (option == ChannelOption.SO_KEEPALIVE) {
/* 112 */       setKeepAlive(((Boolean)value).booleanValue());
/* 113 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/* 114 */       setReuseAddress(((Boolean)value).booleanValue());
/* 115 */     } else if (option == ChannelOption.SO_LINGER) {
/* 116 */       setSoLinger(((Integer)value).intValue());
/* 117 */     } else if (option == ChannelOption.IP_TOS) {
/* 118 */       setTrafficClass(((Integer)value).intValue());
/* 119 */     } else if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
/* 120 */       setAllowHalfClosure(((Boolean)value).booleanValue());
/*     */     } else {
/* 122 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/* 125 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReceiveBufferSize() {
/*     */     try {
/* 131 */       return this.javaSocket.getReceiveBufferSize();
/* 132 */     } catch (SocketException e) {
/* 133 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSendBufferSize() {
/*     */     try {
/* 140 */       return this.javaSocket.getSendBufferSize();
/* 141 */     } catch (SocketException e) {
/* 142 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSoLinger() {
/*     */     try {
/* 149 */       return this.javaSocket.getSoLinger();
/* 150 */     } catch (SocketException e) {
/* 151 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTrafficClass() {
/*     */     try {
/* 158 */       return this.javaSocket.getTrafficClass();
/* 159 */     } catch (SocketException e) {
/* 160 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isKeepAlive() {
/*     */     try {
/* 167 */       return this.javaSocket.getKeepAlive();
/* 168 */     } catch (SocketException e) {
/* 169 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReuseAddress() {
/*     */     try {
/* 176 */       return this.javaSocket.getReuseAddress();
/* 177 */     } catch (SocketException e) {
/* 178 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTcpNoDelay() {
/*     */     try {
/* 185 */       return this.javaSocket.getTcpNoDelay();
/* 186 */     } catch (SocketException e) {
/* 187 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setKeepAlive(boolean keepAlive) {
/*     */     try {
/* 194 */       this.javaSocket.setKeepAlive(keepAlive);
/* 195 */     } catch (SocketException e) {
/* 196 */       throw new ChannelException(e);
/*     */     } 
/* 198 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
/* 204 */     this.javaSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
/* 205 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/*     */     try {
/* 211 */       this.javaSocket.setReceiveBufferSize(receiveBufferSize);
/* 212 */     } catch (SocketException e) {
/* 213 */       throw new ChannelException(e);
/*     */     } 
/* 215 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setReuseAddress(boolean reuseAddress) {
/*     */     try {
/* 221 */       this.javaSocket.setReuseAddress(reuseAddress);
/* 222 */     } catch (SocketException e) {
/* 223 */       throw new ChannelException(e);
/*     */     } 
/* 225 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setSendBufferSize(int sendBufferSize) {
/*     */     try {
/* 231 */       this.javaSocket.setSendBufferSize(sendBufferSize);
/* 232 */     } catch (SocketException e) {
/* 233 */       throw new ChannelException(e);
/*     */     } 
/* 235 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setSoLinger(int soLinger) {
/*     */     try {
/* 241 */       if (soLinger < 0) {
/* 242 */         this.javaSocket.setSoLinger(false, 0);
/*     */       } else {
/* 244 */         this.javaSocket.setSoLinger(true, soLinger);
/*     */       } 
/* 246 */     } catch (SocketException e) {
/* 247 */       throw new ChannelException(e);
/*     */     } 
/* 249 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setTcpNoDelay(boolean tcpNoDelay) {
/*     */     try {
/* 255 */       this.javaSocket.setTcpNoDelay(tcpNoDelay);
/* 256 */     } catch (SocketException e) {
/* 257 */       throw new ChannelException(e);
/*     */     } 
/* 259 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setTrafficClass(int trafficClass) {
/*     */     try {
/* 265 */       this.javaSocket.setTrafficClass(trafficClass);
/* 266 */     } catch (SocketException e) {
/* 267 */       throw new ChannelException(e);
/*     */     } 
/* 269 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllowHalfClosure() {
/* 274 */     return this.allowHalfClosure;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setAllowHalfClosure(boolean allowHalfClosure) {
/* 279 */     this.allowHalfClosure = allowHalfClosure;
/* 280 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 285 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 286 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 292 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 293 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 298 */     super.setWriteSpinCount(writeSpinCount);
/* 299 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 304 */     super.setAllocator(allocator);
/* 305 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 310 */     super.setRecvByteBufAllocator(allocator);
/* 311 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setAutoRead(boolean autoRead) {
/* 316 */     super.setAutoRead(autoRead);
/* 317 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setAutoClose(boolean autoClose) {
/* 322 */     super.setAutoClose(autoClose);
/* 323 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 328 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 329 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 334 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 335 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 340 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 341 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 346 */     super.setMessageSizeEstimator(estimator);
/* 347 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\socket\DefaultSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */