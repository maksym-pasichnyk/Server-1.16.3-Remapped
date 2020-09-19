/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
/*     */ import io.netty.channel.unix.DomainSocketChannelConfig;
/*     */ import io.netty.channel.unix.DomainSocketReadMode;
/*     */ import io.netty.channel.unix.UnixChannelOption;
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
/*     */ public final class KQueueDomainSocketChannelConfig
/*     */   extends KQueueChannelConfig
/*     */   implements DomainSocketChannelConfig
/*     */ {
/*  33 */   private volatile DomainSocketReadMode mode = DomainSocketReadMode.BYTES;
/*     */   
/*     */   KQueueDomainSocketChannelConfig(AbstractKQueueChannel channel) {
/*  36 */     super(channel);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  41 */     return getOptions(super.getOptions(), new ChannelOption[] { UnixChannelOption.DOMAIN_SOCKET_READ_MODE });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  47 */     if (option == UnixChannelOption.DOMAIN_SOCKET_READ_MODE) {
/*  48 */       return (T)getReadMode();
/*     */     }
/*  50 */     return super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  55 */     validate(option, value);
/*     */     
/*  57 */     if (option == UnixChannelOption.DOMAIN_SOCKET_READ_MODE) {
/*  58 */       setReadMode((DomainSocketReadMode)value);
/*     */     } else {
/*  60 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDomainSocketChannelConfig setRcvAllocTransportProvidesGuess(boolean transportProvidesGuess) {
/*  68 */     super.setRcvAllocTransportProvidesGuess(transportProvidesGuess);
/*  69 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueDomainSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/*  75 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDomainSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/*  81 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDomainSocketChannelConfig setWriteSpinCount(int writeSpinCount) {
/*  87 */     super.setWriteSpinCount(writeSpinCount);
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDomainSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/*  93 */     super.setRecvByteBufAllocator(allocator);
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDomainSocketChannelConfig setAllocator(ByteBufAllocator allocator) {
/*  99 */     super.setAllocator(allocator);
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDomainSocketChannelConfig setAutoClose(boolean autoClose) {
/* 105 */     super.setAutoClose(autoClose);
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDomainSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 111 */     super.setMessageSizeEstimator(estimator);
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueDomainSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 118 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueDomainSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 125 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDomainSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 131 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDomainSocketChannelConfig setAutoRead(boolean autoRead) {
/* 137 */     super.setAutoRead(autoRead);
/* 138 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDomainSocketChannelConfig setReadMode(DomainSocketReadMode mode) {
/* 143 */     if (mode == null) {
/* 144 */       throw new NullPointerException("mode");
/*     */     }
/* 146 */     this.mode = mode;
/* 147 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainSocketReadMode getReadMode() {
/* 152 */     return this.mode;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueDomainSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */