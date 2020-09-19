/*     */ package io.netty.channel.socket;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.FixedRecvByteBufAllocator;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MulticastSocket;
/*     */ import java.net.NetworkInterface;
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
/*     */ public class DefaultDatagramChannelConfig
/*     */   extends DefaultChannelConfig
/*     */   implements DatagramChannelConfig
/*     */ {
/*  45 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultDatagramChannelConfig.class);
/*     */ 
/*     */   
/*     */   private final DatagramSocket javaSocket;
/*     */   
/*     */   private volatile boolean activeOnOpen;
/*     */ 
/*     */   
/*     */   public DefaultDatagramChannelConfig(DatagramChannel channel, DatagramSocket javaSocket) {
/*  54 */     super(channel, (RecvByteBufAllocator)new FixedRecvByteBufAllocator(2048));
/*  55 */     if (javaSocket == null) {
/*  56 */       throw new NullPointerException("javaSocket");
/*     */     }
/*  58 */     this.javaSocket = javaSocket;
/*     */   }
/*     */   
/*     */   protected final DatagramSocket javaSocket() {
/*  62 */     return this.javaSocket;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  68 */     return getOptions(super
/*  69 */         .getOptions(), new ChannelOption[] { ChannelOption.SO_BROADCAST, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.IP_MULTICAST_LOOP_DISABLED, ChannelOption.IP_MULTICAST_ADDR, ChannelOption.IP_MULTICAST_IF, ChannelOption.IP_MULTICAST_TTL, ChannelOption.IP_TOS, ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  77 */     if (option == ChannelOption.SO_BROADCAST) {
/*  78 */       return (T)Boolean.valueOf(isBroadcast());
/*     */     }
/*  80 */     if (option == ChannelOption.SO_RCVBUF) {
/*  81 */       return (T)Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  83 */     if (option == ChannelOption.SO_SNDBUF) {
/*  84 */       return (T)Integer.valueOf(getSendBufferSize());
/*     */     }
/*  86 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  87 */       return (T)Boolean.valueOf(isReuseAddress());
/*     */     }
/*  89 */     if (option == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
/*  90 */       return (T)Boolean.valueOf(isLoopbackModeDisabled());
/*     */     }
/*  92 */     if (option == ChannelOption.IP_MULTICAST_ADDR) {
/*  93 */       return (T)getInterface();
/*     */     }
/*  95 */     if (option == ChannelOption.IP_MULTICAST_IF) {
/*  96 */       return (T)getNetworkInterface();
/*     */     }
/*  98 */     if (option == ChannelOption.IP_MULTICAST_TTL) {
/*  99 */       return (T)Integer.valueOf(getTimeToLive());
/*     */     }
/* 101 */     if (option == ChannelOption.IP_TOS) {
/* 102 */       return (T)Integer.valueOf(getTrafficClass());
/*     */     }
/* 104 */     if (option == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
/* 105 */       return (T)Boolean.valueOf(this.activeOnOpen);
/*     */     }
/* 107 */     return (T)super.getOption(option);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/* 113 */     validate(option, value);
/*     */     
/* 115 */     if (option == ChannelOption.SO_BROADCAST) {
/* 116 */       setBroadcast(((Boolean)value).booleanValue());
/* 117 */     } else if (option == ChannelOption.SO_RCVBUF) {
/* 118 */       setReceiveBufferSize(((Integer)value).intValue());
/* 119 */     } else if (option == ChannelOption.SO_SNDBUF) {
/* 120 */       setSendBufferSize(((Integer)value).intValue());
/* 121 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/* 122 */       setReuseAddress(((Boolean)value).booleanValue());
/* 123 */     } else if (option == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
/* 124 */       setLoopbackModeDisabled(((Boolean)value).booleanValue());
/* 125 */     } else if (option == ChannelOption.IP_MULTICAST_ADDR) {
/* 126 */       setInterface((InetAddress)value);
/* 127 */     } else if (option == ChannelOption.IP_MULTICAST_IF) {
/* 128 */       setNetworkInterface((NetworkInterface)value);
/* 129 */     } else if (option == ChannelOption.IP_MULTICAST_TTL) {
/* 130 */       setTimeToLive(((Integer)value).intValue());
/* 131 */     } else if (option == ChannelOption.IP_TOS) {
/* 132 */       setTrafficClass(((Integer)value).intValue());
/* 133 */     } else if (option == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
/* 134 */       setActiveOnOpen(((Boolean)value).booleanValue());
/*     */     } else {
/* 136 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/* 139 */     return true;
/*     */   }
/*     */   
/*     */   private void setActiveOnOpen(boolean activeOnOpen) {
/* 143 */     if (this.channel.isRegistered()) {
/* 144 */       throw new IllegalStateException("Can only changed before channel was registered");
/*     */     }
/* 146 */     this.activeOnOpen = activeOnOpen;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBroadcast() {
/*     */     try {
/* 152 */       return this.javaSocket.getBroadcast();
/* 153 */     } catch (SocketException e) {
/* 154 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setBroadcast(boolean broadcast) {
/*     */     try {
/* 162 */       if (broadcast && 
/* 163 */         !this.javaSocket.getLocalAddress().isAnyLocalAddress() && 
/* 164 */         !PlatformDependent.isWindows() && !PlatformDependent.maybeSuperUser())
/*     */       {
/*     */         
/* 167 */         logger.warn("A non-root user can't receive a broadcast packet if the socket is not bound to a wildcard address; setting the SO_BROADCAST flag anyway as requested on the socket which is bound to " + this.javaSocket
/*     */ 
/*     */ 
/*     */             
/* 171 */             .getLocalSocketAddress() + '.');
/*     */       }
/*     */       
/* 174 */       this.javaSocket.setBroadcast(broadcast);
/* 175 */     } catch (SocketException e) {
/* 176 */       throw new ChannelException(e);
/*     */     } 
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getInterface() {
/* 183 */     if (this.javaSocket instanceof MulticastSocket) {
/*     */       try {
/* 185 */         return ((MulticastSocket)this.javaSocket).getInterface();
/* 186 */       } catch (SocketException e) {
/* 187 */         throw new ChannelException(e);
/*     */       } 
/*     */     }
/* 190 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setInterface(InetAddress interfaceAddress) {
/* 196 */     if (this.javaSocket instanceof MulticastSocket) {
/*     */       try {
/* 198 */         ((MulticastSocket)this.javaSocket).setInterface(interfaceAddress);
/* 199 */       } catch (SocketException e) {
/* 200 */         throw new ChannelException(e);
/*     */       } 
/*     */     } else {
/* 203 */       throw new UnsupportedOperationException();
/*     */     } 
/* 205 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLoopbackModeDisabled() {
/* 210 */     if (this.javaSocket instanceof MulticastSocket) {
/*     */       try {
/* 212 */         return ((MulticastSocket)this.javaSocket).getLoopbackMode();
/* 213 */       } catch (SocketException e) {
/* 214 */         throw new ChannelException(e);
/*     */       } 
/*     */     }
/* 217 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setLoopbackModeDisabled(boolean loopbackModeDisabled) {
/* 223 */     if (this.javaSocket instanceof MulticastSocket) {
/*     */       try {
/* 225 */         ((MulticastSocket)this.javaSocket).setLoopbackMode(loopbackModeDisabled);
/* 226 */       } catch (SocketException e) {
/* 227 */         throw new ChannelException(e);
/*     */       } 
/*     */     } else {
/* 230 */       throw new UnsupportedOperationException();
/*     */     } 
/* 232 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NetworkInterface getNetworkInterface() {
/* 237 */     if (this.javaSocket instanceof MulticastSocket) {
/*     */       try {
/* 239 */         return ((MulticastSocket)this.javaSocket).getNetworkInterface();
/* 240 */       } catch (SocketException e) {
/* 241 */         throw new ChannelException(e);
/*     */       } 
/*     */     }
/* 244 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setNetworkInterface(NetworkInterface networkInterface) {
/* 250 */     if (this.javaSocket instanceof MulticastSocket) {
/*     */       try {
/* 252 */         ((MulticastSocket)this.javaSocket).setNetworkInterface(networkInterface);
/* 253 */       } catch (SocketException e) {
/* 254 */         throw new ChannelException(e);
/*     */       } 
/*     */     } else {
/* 257 */       throw new UnsupportedOperationException();
/*     */     } 
/* 259 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReuseAddress() {
/*     */     try {
/* 265 */       return this.javaSocket.getReuseAddress();
/* 266 */     } catch (SocketException e) {
/* 267 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setReuseAddress(boolean reuseAddress) {
/*     */     try {
/* 274 */       this.javaSocket.setReuseAddress(reuseAddress);
/* 275 */     } catch (SocketException e) {
/* 276 */       throw new ChannelException(e);
/*     */     } 
/* 278 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReceiveBufferSize() {
/*     */     try {
/* 284 */       return this.javaSocket.getReceiveBufferSize();
/* 285 */     } catch (SocketException e) {
/* 286 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/*     */     try {
/* 293 */       this.javaSocket.setReceiveBufferSize(receiveBufferSize);
/* 294 */     } catch (SocketException e) {
/* 295 */       throw new ChannelException(e);
/*     */     } 
/* 297 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSendBufferSize() {
/*     */     try {
/* 303 */       return this.javaSocket.getSendBufferSize();
/* 304 */     } catch (SocketException e) {
/* 305 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setSendBufferSize(int sendBufferSize) {
/*     */     try {
/* 312 */       this.javaSocket.setSendBufferSize(sendBufferSize);
/* 313 */     } catch (SocketException e) {
/* 314 */       throw new ChannelException(e);
/*     */     } 
/* 316 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTimeToLive() {
/* 321 */     if (this.javaSocket instanceof MulticastSocket) {
/*     */       try {
/* 323 */         return ((MulticastSocket)this.javaSocket).getTimeToLive();
/* 324 */       } catch (IOException e) {
/* 325 */         throw new ChannelException(e);
/*     */       } 
/*     */     }
/* 328 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setTimeToLive(int ttl) {
/* 334 */     if (this.javaSocket instanceof MulticastSocket) {
/*     */       try {
/* 336 */         ((MulticastSocket)this.javaSocket).setTimeToLive(ttl);
/* 337 */       } catch (IOException e) {
/* 338 */         throw new ChannelException(e);
/*     */       } 
/*     */     } else {
/* 341 */       throw new UnsupportedOperationException();
/*     */     } 
/* 343 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTrafficClass() {
/*     */     try {
/* 349 */       return this.javaSocket.getTrafficClass();
/* 350 */     } catch (SocketException e) {
/* 351 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setTrafficClass(int trafficClass) {
/*     */     try {
/* 358 */       this.javaSocket.setTrafficClass(trafficClass);
/* 359 */     } catch (SocketException e) {
/* 360 */       throw new ChannelException(e);
/*     */     } 
/* 362 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 367 */     super.setWriteSpinCount(writeSpinCount);
/* 368 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 373 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 374 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DatagramChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 380 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 381 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 386 */     super.setAllocator(allocator);
/* 387 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 392 */     super.setRecvByteBufAllocator(allocator);
/* 393 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setAutoRead(boolean autoRead) {
/* 398 */     super.setAutoRead(autoRead);
/* 399 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setAutoClose(boolean autoClose) {
/* 404 */     super.setAutoClose(autoClose);
/* 405 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 410 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 411 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 416 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 417 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 422 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 423 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 428 */     super.setMessageSizeEstimator(estimator);
/* 429 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\socket\DefaultDatagramChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */