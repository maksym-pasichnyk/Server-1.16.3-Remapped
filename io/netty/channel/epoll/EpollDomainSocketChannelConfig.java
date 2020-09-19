/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
/*     */ import io.netty.channel.unix.DomainSocketChannelConfig;
/*     */ import io.netty.channel.unix.DomainSocketReadMode;
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
/*     */ public final class EpollDomainSocketChannelConfig
/*     */   extends EpollChannelConfig
/*     */   implements DomainSocketChannelConfig
/*     */ {
/*  30 */   private volatile DomainSocketReadMode mode = DomainSocketReadMode.BYTES;
/*     */   
/*     */   EpollDomainSocketChannelConfig(AbstractEpollChannel channel) {
/*  33 */     super(channel);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  38 */     return getOptions(super.getOptions(), new ChannelOption[] { EpollChannelOption.DOMAIN_SOCKET_READ_MODE });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  44 */     if (option == EpollChannelOption.DOMAIN_SOCKET_READ_MODE) {
/*  45 */       return (T)getReadMode();
/*     */     }
/*  47 */     return super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  52 */     validate(option, value);
/*     */     
/*  54 */     if (option == EpollChannelOption.DOMAIN_SOCKET_READ_MODE) {
/*  55 */       setReadMode((DomainSocketReadMode)value);
/*     */     } else {
/*  57 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/*  60 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollDomainSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/*  66 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/*  67 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDomainSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/*  72 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDomainSocketChannelConfig setWriteSpinCount(int writeSpinCount) {
/*  78 */     super.setWriteSpinCount(writeSpinCount);
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDomainSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/*  84 */     super.setRecvByteBufAllocator(allocator);
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDomainSocketChannelConfig setAllocator(ByteBufAllocator allocator) {
/*  90 */     super.setAllocator(allocator);
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDomainSocketChannelConfig setAutoClose(boolean autoClose) {
/*  96 */     super.setAutoClose(autoClose);
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDomainSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 102 */     super.setMessageSizeEstimator(estimator);
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollDomainSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 109 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollDomainSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 116 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDomainSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 122 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDomainSocketChannelConfig setAutoRead(boolean autoRead) {
/* 128 */     super.setAutoRead(autoRead);
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDomainSocketChannelConfig setEpollMode(EpollMode mode) {
/* 134 */     super.setEpollMode(mode);
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDomainSocketChannelConfig setReadMode(DomainSocketReadMode mode) {
/* 140 */     if (mode == null) {
/* 141 */       throw new NullPointerException("mode");
/*     */     }
/* 143 */     this.mode = mode;
/* 144 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainSocketReadMode getReadMode() {
/* 149 */     return this.mode;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollDomainSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */