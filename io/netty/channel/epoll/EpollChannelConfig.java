/*     */ package io.netty.channel.epoll;
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
/*     */ import io.netty.channel.unix.Limits;
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
/*     */ public class EpollChannelConfig
/*     */   extends DefaultChannelConfig
/*     */ {
/*     */   final AbstractEpollChannel channel;
/*  33 */   private volatile long maxBytesPerGatheringWrite = Limits.SSIZE_MAX;
/*     */   
/*     */   EpollChannelConfig(AbstractEpollChannel channel) {
/*  36 */     super((Channel)channel);
/*  37 */     this.channel = channel;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  42 */     return getOptions(super.getOptions(), new ChannelOption[] { EpollChannelOption.EPOLL_MODE });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  48 */     if (option == EpollChannelOption.EPOLL_MODE) {
/*  49 */       return (T)getEpollMode();
/*     */     }
/*  51 */     return (T)super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  56 */     validate(option, value);
/*  57 */     if (option == EpollChannelOption.EPOLL_MODE) {
/*  58 */       setEpollMode((EpollMode)value);
/*     */     } else {
/*  60 */       return super.setOption(option, value);
/*     */     } 
/*  62 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/*  67 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/*  74 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/*  75 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollChannelConfig setWriteSpinCount(int writeSpinCount) {
/*  80 */     super.setWriteSpinCount(writeSpinCount);
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollChannelConfig setAllocator(ByteBufAllocator allocator) {
/*  86 */     super.setAllocator(allocator);
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/*  92 */     if (!(allocator.newHandle() instanceof RecvByteBufAllocator.ExtendedHandle)) {
/*  93 */       throw new IllegalArgumentException("allocator.newHandle() must return an object of type: " + RecvByteBufAllocator.ExtendedHandle.class);
/*     */     }
/*     */     
/*  96 */     super.setRecvByteBufAllocator(allocator);
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollChannelConfig setAutoRead(boolean autoRead) {
/* 102 */     super.setAutoRead(autoRead);
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 109 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 116 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 122 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 128 */     super.setMessageSizeEstimator(estimator);
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollMode getEpollMode() {
/* 139 */     return this.channel.isFlagSet(Native.EPOLLET) ? EpollMode.EDGE_TRIGGERED : EpollMode.LEVEL_TRIGGERED;
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
/*     */   public EpollChannelConfig setEpollMode(EpollMode mode) {
/* 152 */     if (mode == null) {
/* 153 */       throw new NullPointerException("mode");
/*     */     }
/*     */     try {
/* 156 */       switch (mode) {
/*     */         case EDGE_TRIGGERED:
/* 158 */           checkChannelNotRegistered();
/* 159 */           this.channel.setFlag(Native.EPOLLET);
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
/* 171 */           return this;case LEVEL_TRIGGERED: checkChannelNotRegistered(); this.channel.clearFlag(Native.EPOLLET); return this;
/*     */       }  throw new Error();
/*     */     } catch (IOException e) {
/*     */       throw new ChannelException(e);
/* 175 */     }  } private void checkChannelNotRegistered() { if (this.channel.isRegistered()) {
/* 176 */       throw new IllegalStateException("EpollMode can only be changed before channel is registered");
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void autoReadCleared() {
/* 182 */     this.channel.clearEpollIn();
/*     */   }
/*     */   
/*     */   final void setMaxBytesPerGatheringWrite(long maxBytesPerGatheringWrite) {
/* 186 */     this.maxBytesPerGatheringWrite = maxBytesPerGatheringWrite;
/*     */   }
/*     */   
/*     */   final long getMaxBytesPerGatheringWrite() {
/* 190 */     return this.maxBytesPerGatheringWrite;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */