/*     */ package io.netty.channel.socket;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
/*     */ import io.netty.util.NetUtil;
/*     */ import java.net.ServerSocket;
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
/*     */ public class DefaultServerSocketChannelConfig
/*     */   extends DefaultChannelConfig
/*     */   implements ServerSocketChannelConfig
/*     */ {
/*     */   protected final ServerSocket javaSocket;
/*  42 */   private volatile int backlog = NetUtil.SOMAXCONN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultServerSocketChannelConfig(ServerSocketChannel channel, ServerSocket javaSocket) {
/*  48 */     super((Channel)channel);
/*  49 */     if (javaSocket == null) {
/*  50 */       throw new NullPointerException("javaSocket");
/*     */     }
/*  52 */     this.javaSocket = javaSocket;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  57 */     return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_REUSEADDR, ChannelOption.SO_BACKLOG });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  63 */     if (option == ChannelOption.SO_RCVBUF) {
/*  64 */       return (T)Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  66 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  67 */       return (T)Boolean.valueOf(isReuseAddress());
/*     */     }
/*  69 */     if (option == ChannelOption.SO_BACKLOG) {
/*  70 */       return (T)Integer.valueOf(getBacklog());
/*     */     }
/*     */     
/*  73 */     return (T)super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  78 */     validate(option, value);
/*     */     
/*  80 */     if (option == ChannelOption.SO_RCVBUF) {
/*  81 */       setReceiveBufferSize(((Integer)value).intValue());
/*  82 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/*  83 */       setReuseAddress(((Boolean)value).booleanValue());
/*  84 */     } else if (option == ChannelOption.SO_BACKLOG) {
/*  85 */       setBacklog(((Integer)value).intValue());
/*     */     } else {
/*  87 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/*  90 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReuseAddress() {
/*     */     try {
/*  96 */       return this.javaSocket.getReuseAddress();
/*  97 */     } catch (SocketException e) {
/*  98 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig setReuseAddress(boolean reuseAddress) {
/*     */     try {
/* 105 */       this.javaSocket.setReuseAddress(reuseAddress);
/* 106 */     } catch (SocketException e) {
/* 107 */       throw new ChannelException(e);
/*     */     } 
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReceiveBufferSize() {
/*     */     try {
/* 115 */       return this.javaSocket.getReceiveBufferSize();
/* 116 */     } catch (SocketException e) {
/* 117 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/*     */     try {
/* 124 */       this.javaSocket.setReceiveBufferSize(receiveBufferSize);
/* 125 */     } catch (SocketException e) {
/* 126 */       throw new ChannelException(e);
/*     */     } 
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
/* 133 */     this.javaSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBacklog() {
/* 139 */     return this.backlog;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig setBacklog(int backlog) {
/* 144 */     if (backlog < 0) {
/* 145 */       throw new IllegalArgumentException("backlog: " + backlog);
/*     */     }
/* 147 */     this.backlog = backlog;
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 153 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ServerSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 160 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 161 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 166 */     super.setWriteSpinCount(writeSpinCount);
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 172 */     super.setAllocator(allocator);
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 178 */     super.setRecvByteBufAllocator(allocator);
/* 179 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig setAutoRead(boolean autoRead) {
/* 184 */     super.setAutoRead(autoRead);
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 190 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 191 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 196 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 202 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 203 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 208 */     super.setMessageSizeEstimator(estimator);
/* 209 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\socket\DefaultServerSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */