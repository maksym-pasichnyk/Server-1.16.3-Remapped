/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
/*     */ import io.netty.channel.socket.ServerSocketChannelConfig;
/*     */ import io.netty.channel.unix.UnixChannelOption;
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
/*     */ public class KQueueServerSocketChannelConfig
/*     */   extends KQueueServerChannelConfig
/*     */   implements ServerSocketChannelConfig
/*     */ {
/*     */   KQueueServerSocketChannelConfig(KQueueServerSocketChannel channel) {
/*  36 */     super(channel);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  41 */     setReuseAddress(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  46 */     return getOptions(super.getOptions(), new ChannelOption[] { UnixChannelOption.SO_REUSEPORT, KQueueChannelOption.SO_ACCEPTFILTER });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  52 */     if (option == UnixChannelOption.SO_REUSEPORT) {
/*  53 */       return (T)Boolean.valueOf(isReusePort());
/*     */     }
/*  55 */     if (option == KQueueChannelOption.SO_ACCEPTFILTER) {
/*  56 */       return (T)getAcceptFilter();
/*     */     }
/*  58 */     return super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  63 */     validate(option, value);
/*     */     
/*  65 */     if (option == UnixChannelOption.SO_REUSEPORT) {
/*  66 */       setReusePort(((Boolean)value).booleanValue());
/*  67 */     } else if (option == KQueueChannelOption.SO_ACCEPTFILTER) {
/*  68 */       setAcceptFilter((AcceptFilter)value);
/*     */     } else {
/*  70 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   public KQueueServerSocketChannelConfig setReusePort(boolean reusePort) {
/*     */     try {
/*  78 */       this.channel.socket.setReusePort(reusePort);
/*  79 */       return this;
/*  80 */     } catch (IOException e) {
/*  81 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isReusePort() {
/*     */     try {
/*  87 */       return this.channel.socket.isReusePort();
/*  88 */     } catch (IOException e) {
/*  89 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public KQueueServerSocketChannelConfig setAcceptFilter(AcceptFilter acceptFilter) {
/*     */     try {
/*  95 */       this.channel.socket.setAcceptFilter(acceptFilter);
/*  96 */       return this;
/*  97 */     } catch (IOException e) {
/*  98 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public AcceptFilter getAcceptFilter() {
/*     */     try {
/* 104 */       return this.channel.socket.getAcceptFilter();
/* 105 */     } catch (IOException e) {
/* 106 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerSocketChannelConfig setRcvAllocTransportProvidesGuess(boolean transportProvidesGuess) {
/* 112 */     super.setRcvAllocTransportProvidesGuess(transportProvidesGuess);
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerSocketChannelConfig setReuseAddress(boolean reuseAddress) {
/* 118 */     super.setReuseAddress(reuseAddress);
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerSocketChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/* 124 */     super.setReceiveBufferSize(receiveBufferSize);
/* 125 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerSocketChannelConfig setBacklog(int backlog) {
/* 135 */     super.setBacklog(backlog);
/* 136 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 141 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueServerSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 148 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 149 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerSocketChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 154 */     super.setWriteSpinCount(writeSpinCount);
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerSocketChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 160 */     super.setAllocator(allocator);
/* 161 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 166 */     super.setRecvByteBufAllocator(allocator);
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerSocketChannelConfig setAutoRead(boolean autoRead) {
/* 172 */     super.setAutoRead(autoRead);
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueServerSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 179 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 180 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueServerSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 186 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 187 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 192 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 198 */     super.setMessageSizeEstimator(estimator);
/* 199 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueServerSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */