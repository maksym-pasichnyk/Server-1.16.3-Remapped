/*     */ package io.netty.channel.kqueue;
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
/*     */ import io.netty.channel.unix.UnixChannelOption;
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
/*     */ public final class KQueueDatagramChannelConfig
/*     */   extends KQueueChannelConfig
/*     */   implements DatagramChannelConfig
/*     */ {
/*  47 */   private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR = (RecvByteBufAllocator)new FixedRecvByteBufAllocator(2048);
/*     */   private final KQueueDatagramChannel datagramChannel;
/*     */   private boolean activeOnOpen;
/*     */   
/*     */   KQueueDatagramChannelConfig(KQueueDatagramChannel channel) {
/*  52 */     super(channel);
/*  53 */     this.datagramChannel = channel;
/*  54 */     setRecvByteBufAllocator(DEFAULT_RCVBUF_ALLOCATOR);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  60 */     return getOptions(super
/*  61 */         .getOptions(), new ChannelOption[] { ChannelOption.SO_BROADCAST, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.IP_MULTICAST_LOOP_DISABLED, ChannelOption.IP_MULTICAST_ADDR, ChannelOption.IP_MULTICAST_IF, ChannelOption.IP_MULTICAST_TTL, ChannelOption.IP_TOS, ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION, UnixChannelOption.SO_REUSEPORT });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  70 */     if (option == ChannelOption.SO_BROADCAST) {
/*  71 */       return (T)Boolean.valueOf(isBroadcast());
/*     */     }
/*  73 */     if (option == ChannelOption.SO_RCVBUF) {
/*  74 */       return (T)Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  76 */     if (option == ChannelOption.SO_SNDBUF) {
/*  77 */       return (T)Integer.valueOf(getSendBufferSize());
/*     */     }
/*  79 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  80 */       return (T)Boolean.valueOf(isReuseAddress());
/*     */     }
/*  82 */     if (option == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
/*  83 */       return (T)Boolean.valueOf(isLoopbackModeDisabled());
/*     */     }
/*  85 */     if (option == ChannelOption.IP_MULTICAST_ADDR) {
/*  86 */       return (T)getInterface();
/*     */     }
/*  88 */     if (option == ChannelOption.IP_MULTICAST_IF) {
/*  89 */       return (T)getNetworkInterface();
/*     */     }
/*  91 */     if (option == ChannelOption.IP_MULTICAST_TTL) {
/*  92 */       return (T)Integer.valueOf(getTimeToLive());
/*     */     }
/*  94 */     if (option == ChannelOption.IP_TOS) {
/*  95 */       return (T)Integer.valueOf(getTrafficClass());
/*     */     }
/*  97 */     if (option == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
/*  98 */       return (T)Boolean.valueOf(this.activeOnOpen);
/*     */     }
/* 100 */     if (option == UnixChannelOption.SO_REUSEPORT) {
/* 101 */       return (T)Boolean.valueOf(isReusePort());
/*     */     }
/* 103 */     return super.getOption(option);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/* 109 */     validate(option, value);
/*     */     
/* 111 */     if (option == ChannelOption.SO_BROADCAST) {
/* 112 */       setBroadcast(((Boolean)value).booleanValue());
/* 113 */     } else if (option == ChannelOption.SO_RCVBUF) {
/* 114 */       setReceiveBufferSize(((Integer)value).intValue());
/* 115 */     } else if (option == ChannelOption.SO_SNDBUF) {
/* 116 */       setSendBufferSize(((Integer)value).intValue());
/* 117 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/* 118 */       setReuseAddress(((Boolean)value).booleanValue());
/* 119 */     } else if (option == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
/* 120 */       setLoopbackModeDisabled(((Boolean)value).booleanValue());
/* 121 */     } else if (option == ChannelOption.IP_MULTICAST_ADDR) {
/* 122 */       setInterface((InetAddress)value);
/* 123 */     } else if (option == ChannelOption.IP_MULTICAST_IF) {
/* 124 */       setNetworkInterface((NetworkInterface)value);
/* 125 */     } else if (option == ChannelOption.IP_MULTICAST_TTL) {
/* 126 */       setTimeToLive(((Integer)value).intValue());
/* 127 */     } else if (option == ChannelOption.IP_TOS) {
/* 128 */       setTrafficClass(((Integer)value).intValue());
/* 129 */     } else if (option == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
/* 130 */       setActiveOnOpen(((Boolean)value).booleanValue());
/* 131 */     } else if (option == UnixChannelOption.SO_REUSEPORT) {
/* 132 */       setReusePort(((Boolean)value).booleanValue());
/*     */     } else {
/* 134 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/* 137 */     return true;
/*     */   }
/*     */   
/*     */   private void setActiveOnOpen(boolean activeOnOpen) {
/* 141 */     if (this.channel.isRegistered()) {
/* 142 */       throw new IllegalStateException("Can only changed before channel was registered");
/*     */     }
/* 144 */     this.activeOnOpen = activeOnOpen;
/*     */   }
/*     */   
/*     */   boolean getActiveOnOpen() {
/* 148 */     return this.activeOnOpen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReusePort() {
/*     */     try {
/* 156 */       return this.datagramChannel.socket.isReusePort();
/* 157 */     } catch (IOException e) {
/* 158 */       throw new ChannelException(e);
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
/*     */   public KQueueDatagramChannelConfig setReusePort(boolean reusePort) {
/*     */     try {
/* 171 */       this.datagramChannel.socket.setReusePort(reusePort);
/* 172 */       return this;
/* 173 */     } catch (IOException e) {
/* 174 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setRcvAllocTransportProvidesGuess(boolean transportProvidesGuess) {
/* 180 */     super.setRcvAllocTransportProvidesGuess(transportProvidesGuess);
/* 181 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 186 */     super.setMessageSizeEstimator(estimator);
/* 187 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueDatagramChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 193 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 194 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueDatagramChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 200 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 201 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 206 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 207 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setAutoClose(boolean autoClose) {
/* 212 */     super.setAutoClose(autoClose);
/* 213 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setAutoRead(boolean autoRead) {
/* 218 */     super.setAutoRead(autoRead);
/* 219 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 224 */     super.setRecvByteBufAllocator(allocator);
/* 225 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 230 */     super.setWriteSpinCount(writeSpinCount);
/* 231 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 236 */     super.setAllocator(allocator);
/* 237 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 242 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 243 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueDatagramChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 249 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 250 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSendBufferSize() {
/*     */     try {
/* 256 */       return this.datagramChannel.socket.getSendBufferSize();
/* 257 */     } catch (IOException e) {
/* 258 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setSendBufferSize(int sendBufferSize) {
/*     */     try {
/* 265 */       this.datagramChannel.socket.setSendBufferSize(sendBufferSize);
/* 266 */       return this;
/* 267 */     } catch (IOException e) {
/* 268 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReceiveBufferSize() {
/*     */     try {
/* 275 */       return this.datagramChannel.socket.getReceiveBufferSize();
/* 276 */     } catch (IOException e) {
/* 277 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/*     */     try {
/* 284 */       this.datagramChannel.socket.setReceiveBufferSize(receiveBufferSize);
/* 285 */       return this;
/* 286 */     } catch (IOException e) {
/* 287 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTrafficClass() {
/*     */     try {
/* 294 */       return this.datagramChannel.socket.getTrafficClass();
/* 295 */     } catch (IOException e) {
/* 296 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setTrafficClass(int trafficClass) {
/*     */     try {
/* 303 */       this.datagramChannel.socket.setTrafficClass(trafficClass);
/* 304 */       return this;
/* 305 */     } catch (IOException e) {
/* 306 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReuseAddress() {
/*     */     try {
/* 313 */       return this.datagramChannel.socket.isReuseAddress();
/* 314 */     } catch (IOException e) {
/* 315 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setReuseAddress(boolean reuseAddress) {
/*     */     try {
/* 322 */       this.datagramChannel.socket.setReuseAddress(reuseAddress);
/* 323 */       return this;
/* 324 */     } catch (IOException e) {
/* 325 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBroadcast() {
/*     */     try {
/* 332 */       return this.datagramChannel.socket.isBroadcast();
/* 333 */     } catch (IOException e) {
/* 334 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setBroadcast(boolean broadcast) {
/*     */     try {
/* 341 */       this.datagramChannel.socket.setBroadcast(broadcast);
/* 342 */       return this;
/* 343 */     } catch (IOException e) {
/* 344 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLoopbackModeDisabled() {
/* 350 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setLoopbackModeDisabled(boolean loopbackModeDisabled) {
/* 355 */     throw new UnsupportedOperationException("Multicast not supported");
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTimeToLive() {
/* 360 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setTimeToLive(int ttl) {
/* 365 */     throw new UnsupportedOperationException("Multicast not supported");
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getInterface() {
/* 370 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setInterface(InetAddress interfaceAddress) {
/* 375 */     throw new UnsupportedOperationException("Multicast not supported");
/*     */   }
/*     */ 
/*     */   
/*     */   public NetworkInterface getNetworkInterface() {
/* 380 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDatagramChannelConfig setNetworkInterface(NetworkInterface networkInterface) {
/* 385 */     throw new UnsupportedOperationException("Multicast not supported");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueDatagramChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */