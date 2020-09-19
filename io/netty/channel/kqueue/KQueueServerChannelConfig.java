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
/*     */ import io.netty.util.NetUtil;
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
/*     */ public class KQueueServerChannelConfig
/*     */   extends KQueueChannelConfig
/*     */   implements ServerSocketChannelConfig
/*     */ {
/*     */   protected final AbstractKQueueChannel channel;
/*  38 */   private volatile int backlog = NetUtil.SOMAXCONN;
/*     */   
/*     */   KQueueServerChannelConfig(AbstractKQueueChannel channel) {
/*  41 */     super(channel);
/*  42 */     this.channel = channel;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  47 */     return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_REUSEADDR, ChannelOption.SO_BACKLOG });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  53 */     if (option == ChannelOption.SO_RCVBUF) {
/*  54 */       return (T)Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  56 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  57 */       return (T)Boolean.valueOf(isReuseAddress());
/*     */     }
/*  59 */     if (option == ChannelOption.SO_BACKLOG) {
/*  60 */       return (T)Integer.valueOf(getBacklog());
/*     */     }
/*  62 */     return super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  67 */     validate(option, value);
/*     */     
/*  69 */     if (option == ChannelOption.SO_RCVBUF) {
/*  70 */       setReceiveBufferSize(((Integer)value).intValue());
/*  71 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/*  72 */       setReuseAddress(((Boolean)value).booleanValue());
/*  73 */     } else if (option == ChannelOption.SO_BACKLOG) {
/*  74 */       setBacklog(((Integer)value).intValue());
/*     */     } else {
/*  76 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isReuseAddress() {
/*     */     try {
/*  84 */       return this.channel.socket.isReuseAddress();
/*  85 */     } catch (IOException e) {
/*  86 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public KQueueServerChannelConfig setReuseAddress(boolean reuseAddress) {
/*     */     try {
/*  92 */       this.channel.socket.setReuseAddress(reuseAddress);
/*  93 */       return this;
/*  94 */     } catch (IOException e) {
/*  95 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getReceiveBufferSize() {
/*     */     try {
/* 101 */       return this.channel.socket.getReceiveBufferSize();
/* 102 */     } catch (IOException e) {
/* 103 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public KQueueServerChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/*     */     try {
/* 109 */       this.channel.socket.setReceiveBufferSize(receiveBufferSize);
/* 110 */       return this;
/* 111 */     } catch (IOException e) {
/* 112 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getBacklog() {
/* 117 */     return this.backlog;
/*     */   }
/*     */   
/*     */   public KQueueServerChannelConfig setBacklog(int backlog) {
/* 121 */     if (backlog < 0) {
/* 122 */       throw new IllegalArgumentException("backlog: " + backlog);
/*     */     }
/* 124 */     this.backlog = backlog;
/* 125 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerChannelConfig setRcvAllocTransportProvidesGuess(boolean transportProvidesGuess) {
/* 130 */     super.setRcvAllocTransportProvidesGuess(transportProvidesGuess);
/* 131 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
/* 136 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 141 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueServerChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 148 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 149 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 154 */     super.setWriteSpinCount(writeSpinCount);
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 160 */     super.setAllocator(allocator);
/* 161 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 166 */     super.setRecvByteBufAllocator(allocator);
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerChannelConfig setAutoRead(boolean autoRead) {
/* 172 */     super.setAutoRead(autoRead);
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueServerChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 179 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 180 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueServerChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 186 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 187 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 192 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 198 */     super.setMessageSizeEstimator(estimator);
/* 199 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueServerChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */