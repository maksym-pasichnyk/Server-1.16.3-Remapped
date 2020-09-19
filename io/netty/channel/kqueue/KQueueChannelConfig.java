/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
/*     */ import io.netty.channel.unix.Limits;
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
/*     */ public class KQueueChannelConfig
/*     */   extends DefaultChannelConfig
/*     */ {
/*     */   final AbstractKQueueChannel channel;
/*     */   private volatile boolean transportProvidesGuess;
/*  36 */   private volatile long maxBytesPerGatheringWrite = Limits.SSIZE_MAX;
/*     */   
/*     */   KQueueChannelConfig(AbstractKQueueChannel channel) {
/*  39 */     super((Channel)channel);
/*  40 */     this.channel = channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  46 */     return getOptions(super.getOptions(), new ChannelOption[] { KQueueChannelOption.RCV_ALLOC_TRANSPORT_PROVIDES_GUESS });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  52 */     if (option == KQueueChannelOption.RCV_ALLOC_TRANSPORT_PROVIDES_GUESS) {
/*  53 */       return (T)Boolean.valueOf(getRcvAllocTransportProvidesGuess());
/*     */     }
/*  55 */     return (T)super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  60 */     validate(option, value);
/*     */     
/*  62 */     if (option == KQueueChannelOption.RCV_ALLOC_TRANSPORT_PROVIDES_GUESS) {
/*  63 */       setRcvAllocTransportProvidesGuess(((Boolean)value).booleanValue());
/*     */     } else {
/*  65 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KQueueChannelConfig setRcvAllocTransportProvidesGuess(boolean transportProvidesGuess) {
/*  76 */     this.transportProvidesGuess = transportProvidesGuess;
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRcvAllocTransportProvidesGuess() {
/*  85 */     return this.transportProvidesGuess;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/*  90 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/*  97 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 103 */     super.setWriteSpinCount(writeSpinCount);
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 109 */     super.setAllocator(allocator);
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 115 */     if (!(allocator.newHandle() instanceof RecvByteBufAllocator.ExtendedHandle)) {
/* 116 */       throw new IllegalArgumentException("allocator.newHandle() must return an object of type: " + RecvByteBufAllocator.ExtendedHandle.class);
/*     */     }
/*     */     
/* 119 */     super.setRecvByteBufAllocator(allocator);
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueChannelConfig setAutoRead(boolean autoRead) {
/* 125 */     super.setAutoRead(autoRead);
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 132 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 139 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 145 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 146 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 151 */     super.setMessageSizeEstimator(estimator);
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void autoReadCleared() {
/* 157 */     this.channel.clearReadFilter();
/*     */   }
/*     */   
/*     */   final void setMaxBytesPerGatheringWrite(long maxBytesPerGatheringWrite) {
/* 161 */     this.maxBytesPerGatheringWrite = Math.min(Limits.SSIZE_MAX, maxBytesPerGatheringWrite);
/*     */   }
/*     */   
/*     */   final long getMaxBytesPerGatheringWrite() {
/* 165 */     return this.maxBytesPerGatheringWrite;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */