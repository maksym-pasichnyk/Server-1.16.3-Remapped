/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.FixedRecvByteBufAllocator;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
/*     */ import io.netty.channel.socket.DatagramChannelConfig;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
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
/*     */ public final class EpollDatagramChannelConfig
/*     */   extends EpollChannelConfig
/*     */   implements DatagramChannelConfig
/*     */ {
/*  33 */   private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR = (RecvByteBufAllocator)new FixedRecvByteBufAllocator(2048);
/*     */   private final EpollDatagramChannel datagramChannel;
/*     */   private boolean activeOnOpen;
/*     */   
/*     */   EpollDatagramChannelConfig(EpollDatagramChannel channel) {
/*  38 */     super(channel);
/*  39 */     this.datagramChannel = channel;
/*  40 */     setRecvByteBufAllocator(DEFAULT_RCVBUF_ALLOCATOR);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  46 */     return getOptions(super
/*  47 */         .getOptions(), new ChannelOption[] { ChannelOption.SO_BROADCAST, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.IP_MULTICAST_LOOP_DISABLED, ChannelOption.IP_MULTICAST_ADDR, ChannelOption.IP_MULTICAST_IF, ChannelOption.IP_MULTICAST_TTL, ChannelOption.IP_TOS, ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION, EpollChannelOption.SO_REUSEPORT, EpollChannelOption.IP_TRANSPARENT, EpollChannelOption.IP_RECVORIGDSTADDR });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  59 */     if (option == ChannelOption.SO_BROADCAST) {
/*  60 */       return (T)Boolean.valueOf(isBroadcast());
/*     */     }
/*  62 */     if (option == ChannelOption.SO_RCVBUF) {
/*  63 */       return (T)Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  65 */     if (option == ChannelOption.SO_SNDBUF) {
/*  66 */       return (T)Integer.valueOf(getSendBufferSize());
/*     */     }
/*  68 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  69 */       return (T)Boolean.valueOf(isReuseAddress());
/*     */     }
/*  71 */     if (option == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
/*  72 */       return (T)Boolean.valueOf(isLoopbackModeDisabled());
/*     */     }
/*  74 */     if (option == ChannelOption.IP_MULTICAST_ADDR) {
/*  75 */       return (T)getInterface();
/*     */     }
/*  77 */     if (option == ChannelOption.IP_MULTICAST_IF) {
/*  78 */       return (T)getNetworkInterface();
/*     */     }
/*  80 */     if (option == ChannelOption.IP_MULTICAST_TTL) {
/*  81 */       return (T)Integer.valueOf(getTimeToLive());
/*     */     }
/*  83 */     if (option == ChannelOption.IP_TOS) {
/*  84 */       return (T)Integer.valueOf(getTrafficClass());
/*     */     }
/*  86 */     if (option == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
/*  87 */       return (T)Boolean.valueOf(this.activeOnOpen);
/*     */     }
/*  89 */     if (option == EpollChannelOption.SO_REUSEPORT) {
/*  90 */       return (T)Boolean.valueOf(isReusePort());
/*     */     }
/*  92 */     if (option == EpollChannelOption.IP_TRANSPARENT) {
/*  93 */       return (T)Boolean.valueOf(isIpTransparent());
/*     */     }
/*  95 */     if (option == EpollChannelOption.IP_RECVORIGDSTADDR) {
/*  96 */       return (T)Boolean.valueOf(isIpRecvOrigDestAddr());
/*     */     }
/*  98 */     return super.getOption(option);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/* 104 */     validate(option, value);
/*     */     
/* 106 */     if (option == ChannelOption.SO_BROADCAST) {
/* 107 */       setBroadcast(((Boolean)value).booleanValue());
/* 108 */     } else if (option == ChannelOption.SO_RCVBUF) {
/* 109 */       setReceiveBufferSize(((Integer)value).intValue());
/* 110 */     } else if (option == ChannelOption.SO_SNDBUF) {
/* 111 */       setSendBufferSize(((Integer)value).intValue());
/* 112 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/* 113 */       setReuseAddress(((Boolean)value).booleanValue());
/* 114 */     } else if (option == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
/* 115 */       setLoopbackModeDisabled(((Boolean)value).booleanValue());
/* 116 */     } else if (option == ChannelOption.IP_MULTICAST_ADDR) {
/* 117 */       setInterface((InetAddress)value);
/* 118 */     } else if (option == ChannelOption.IP_MULTICAST_IF) {
/* 119 */       setNetworkInterface((NetworkInterface)value);
/* 120 */     } else if (option == ChannelOption.IP_MULTICAST_TTL) {
/* 121 */       setTimeToLive(((Integer)value).intValue());
/* 122 */     } else if (option == ChannelOption.IP_TOS) {
/* 123 */       setTrafficClass(((Integer)value).intValue());
/* 124 */     } else if (option == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
/* 125 */       setActiveOnOpen(((Boolean)value).booleanValue());
/* 126 */     } else if (option == EpollChannelOption.SO_REUSEPORT) {
/* 127 */       setReusePort(((Boolean)value).booleanValue());
/* 128 */     } else if (option == EpollChannelOption.IP_TRANSPARENT) {
/* 129 */       setIpTransparent(((Boolean)value).booleanValue());
/* 130 */     } else if (option == EpollChannelOption.IP_RECVORIGDSTADDR) {
/* 131 */       setIpRecvOrigDestAddr(((Boolean)value).booleanValue());
/*     */     } else {
/* 133 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   private void setActiveOnOpen(boolean activeOnOpen) {
/* 140 */     if (this.channel.isRegistered()) {
/* 141 */       throw new IllegalStateException("Can only changed before channel was registered");
/*     */     }
/* 143 */     this.activeOnOpen = activeOnOpen;
/*     */   }
/*     */   
/*     */   boolean getActiveOnOpen() {
/* 147 */     return this.activeOnOpen;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 152 */     super.setMessageSizeEstimator(estimator);
/* 153 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollDatagramChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 159 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollDatagramChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 166 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 172 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setAutoClose(boolean autoClose) {
/* 178 */     super.setAutoClose(autoClose);
/* 179 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setAutoRead(boolean autoRead) {
/* 184 */     super.setAutoRead(autoRead);
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 190 */     super.setRecvByteBufAllocator(allocator);
/* 191 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 196 */     super.setWriteSpinCount(writeSpinCount);
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 202 */     super.setAllocator(allocator);
/* 203 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 208 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 209 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollDatagramChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 215 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSendBufferSize() {
/*     */     try {
/* 222 */       return this.datagramChannel.socket.getSendBufferSize();
/* 223 */     } catch (IOException e) {
/* 224 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setSendBufferSize(int sendBufferSize) {
/*     */     try {
/* 231 */       this.datagramChannel.socket.setSendBufferSize(sendBufferSize);
/* 232 */       return this;
/* 233 */     } catch (IOException e) {
/* 234 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReceiveBufferSize() {
/*     */     try {
/* 241 */       return this.datagramChannel.socket.getReceiveBufferSize();
/* 242 */     } catch (IOException e) {
/* 243 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/*     */     try {
/* 250 */       this.datagramChannel.socket.setReceiveBufferSize(receiveBufferSize);
/* 251 */       return this;
/* 252 */     } catch (IOException e) {
/* 253 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTrafficClass() {
/*     */     try {
/* 260 */       return this.datagramChannel.socket.getTrafficClass();
/* 261 */     } catch (IOException e) {
/* 262 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setTrafficClass(int trafficClass) {
/*     */     try {
/* 269 */       this.datagramChannel.socket.setTrafficClass(trafficClass);
/* 270 */       return this;
/* 271 */     } catch (IOException e) {
/* 272 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReuseAddress() {
/*     */     try {
/* 279 */       return this.datagramChannel.socket.isReuseAddress();
/* 280 */     } catch (IOException e) {
/* 281 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setReuseAddress(boolean reuseAddress) {
/*     */     try {
/* 288 */       this.datagramChannel.socket.setReuseAddress(reuseAddress);
/* 289 */       return this;
/* 290 */     } catch (IOException e) {
/* 291 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBroadcast() {
/*     */     try {
/* 298 */       return this.datagramChannel.socket.isBroadcast();
/* 299 */     } catch (IOException e) {
/* 300 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setBroadcast(boolean broadcast) {
/*     */     try {
/* 307 */       this.datagramChannel.socket.setBroadcast(broadcast);
/* 308 */       return this;
/* 309 */     } catch (IOException e) {
/* 310 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLoopbackModeDisabled() {
/* 316 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setLoopbackModeDisabled(boolean loopbackModeDisabled) {
/* 321 */     throw new UnsupportedOperationException("Multicast not supported");
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTimeToLive() {
/* 326 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setTimeToLive(int ttl) {
/* 331 */     throw new UnsupportedOperationException("Multicast not supported");
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getInterface() {
/* 336 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setInterface(InetAddress interfaceAddress) {
/* 341 */     throw new UnsupportedOperationException("Multicast not supported");
/*     */   }
/*     */ 
/*     */   
/*     */   public NetworkInterface getNetworkInterface() {
/* 346 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setNetworkInterface(NetworkInterface networkInterface) {
/* 351 */     throw new UnsupportedOperationException("Multicast not supported");
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setEpollMode(EpollMode mode) {
/* 356 */     super.setEpollMode(mode);
/* 357 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReusePort() {
/*     */     try {
/* 365 */       return this.datagramChannel.socket.isReusePort();
/* 366 */     } catch (IOException e) {
/* 367 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setReusePort(boolean reusePort) {
/*     */     try {
/* 380 */       this.datagramChannel.socket.setReusePort(reusePort);
/* 381 */       return this;
/* 382 */     } catch (IOException e) {
/* 383 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIpTransparent() {
/*     */     try {
/* 393 */       return this.datagramChannel.socket.isIpTransparent();
/* 394 */     } catch (IOException e) {
/* 395 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setIpTransparent(boolean ipTransparent) {
/*     */     try {
/* 405 */       this.datagramChannel.socket.setIpTransparent(ipTransparent);
/* 406 */       return this;
/* 407 */     } catch (IOException e) {
/* 408 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIpRecvOrigDestAddr() {
/*     */     try {
/* 418 */       return this.datagramChannel.socket.isIpRecvOrigDestAddr();
/* 419 */     } catch (IOException e) {
/* 420 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollDatagramChannelConfig setIpRecvOrigDestAddr(boolean ipTransparent) {
/*     */     try {
/* 430 */       this.datagramChannel.socket.setIpRecvOrigDestAddr(ipTransparent);
/* 431 */       return this;
/* 432 */     } catch (IOException e) {
/* 433 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollDatagramChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */