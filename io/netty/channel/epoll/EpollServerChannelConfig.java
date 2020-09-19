/*     */ package io.netty.channel.epoll;
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
/*     */ public class EpollServerChannelConfig
/*     */   extends EpollChannelConfig
/*     */   implements ServerSocketChannelConfig
/*     */ {
/*     */   protected final AbstractEpollChannel channel;
/*  36 */   private volatile int backlog = NetUtil.SOMAXCONN;
/*     */   private volatile int pendingFastOpenRequestsThreshold;
/*     */   
/*     */   EpollServerChannelConfig(AbstractEpollChannel channel) {
/*  40 */     super(channel);
/*  41 */     this.channel = channel;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  46 */     return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_REUSEADDR, ChannelOption.SO_BACKLOG, EpollChannelOption.TCP_FASTOPEN });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  52 */     if (option == ChannelOption.SO_RCVBUF) {
/*  53 */       return (T)Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  55 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  56 */       return (T)Boolean.valueOf(isReuseAddress());
/*     */     }
/*  58 */     if (option == ChannelOption.SO_BACKLOG) {
/*  59 */       return (T)Integer.valueOf(getBacklog());
/*     */     }
/*  61 */     if (option == EpollChannelOption.TCP_FASTOPEN) {
/*  62 */       return (T)Integer.valueOf(getTcpFastopen());
/*     */     }
/*  64 */     return super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  69 */     validate(option, value);
/*     */     
/*  71 */     if (option == ChannelOption.SO_RCVBUF) {
/*  72 */       setReceiveBufferSize(((Integer)value).intValue());
/*  73 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/*  74 */       setReuseAddress(((Boolean)value).booleanValue());
/*  75 */     } else if (option == ChannelOption.SO_BACKLOG) {
/*  76 */       setBacklog(((Integer)value).intValue());
/*  77 */     } else if (option == EpollChannelOption.TCP_FASTOPEN) {
/*  78 */       setTcpFastopen(((Integer)value).intValue());
/*     */     } else {
/*  80 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/*  83 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isReuseAddress() {
/*     */     try {
/*  88 */       return this.channel.socket.isReuseAddress();
/*  89 */     } catch (IOException e) {
/*  90 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setReuseAddress(boolean reuseAddress) {
/*     */     try {
/*  96 */       this.channel.socket.setReuseAddress(reuseAddress);
/*  97 */       return this;
/*  98 */     } catch (IOException e) {
/*  99 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getReceiveBufferSize() {
/*     */     try {
/* 105 */       return this.channel.socket.getReceiveBufferSize();
/* 106 */     } catch (IOException e) {
/* 107 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/*     */     try {
/* 113 */       this.channel.socket.setReceiveBufferSize(receiveBufferSize);
/* 114 */       return this;
/* 115 */     } catch (IOException e) {
/* 116 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getBacklog() {
/* 121 */     return this.backlog;
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setBacklog(int backlog) {
/* 125 */     if (backlog < 0) {
/* 126 */       throw new IllegalArgumentException("backlog: " + backlog);
/*     */     }
/* 128 */     this.backlog = backlog;
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTcpFastopen() {
/* 138 */     return this.pendingFastOpenRequestsThreshold;
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
/*     */   
/*     */   public EpollServerChannelConfig setTcpFastopen(int pendingFastOpenRequestsThreshold) {
/* 151 */     if (this.pendingFastOpenRequestsThreshold < 0) {
/* 152 */       throw new IllegalArgumentException("pendingFastOpenRequestsThreshold: " + pendingFastOpenRequestsThreshold);
/*     */     }
/* 154 */     this.pendingFastOpenRequestsThreshold = pendingFastOpenRequestsThreshold;
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollServerChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollServerChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 165 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollServerChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 172 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollServerChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 178 */     super.setWriteSpinCount(writeSpinCount);
/* 179 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollServerChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 184 */     super.setAllocator(allocator);
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 190 */     super.setRecvByteBufAllocator(allocator);
/* 191 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollServerChannelConfig setAutoRead(boolean autoRead) {
/* 196 */     super.setAutoRead(autoRead);
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollServerChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 203 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 204 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollServerChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 210 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 211 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollServerChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 216 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 217 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 222 */     super.setMessageSizeEstimator(estimator);
/* 223 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollServerChannelConfig setEpollMode(EpollMode mode) {
/* 228 */     super.setEpollMode(mode);
/* 229 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollServerChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */