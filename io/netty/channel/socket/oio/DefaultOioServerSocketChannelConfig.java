/*     */ package io.netty.channel.socket.oio;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.PreferHeapByteBufAllocator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
/*     */ import io.netty.channel.socket.DefaultServerSocketChannelConfig;
/*     */ import io.netty.channel.socket.ServerSocketChannel;
/*     */ import io.netty.channel.socket.ServerSocketChannelConfig;
/*     */ import java.io.IOException;
/*     */ import java.net.ServerSocket;
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
/*     */ public class DefaultOioServerSocketChannelConfig
/*     */   extends DefaultServerSocketChannelConfig
/*     */   implements OioServerSocketChannelConfig
/*     */ {
/*     */   @Deprecated
/*     */   public DefaultOioServerSocketChannelConfig(ServerSocketChannel channel, ServerSocket javaSocket) {
/*  42 */     super(channel, javaSocket);
/*  43 */     setAllocator((ByteBufAllocator)new PreferHeapByteBufAllocator(getAllocator()));
/*     */   }
/*     */   
/*     */   DefaultOioServerSocketChannelConfig(OioServerSocketChannel channel, ServerSocket javaSocket) {
/*  47 */     super(channel, javaSocket);
/*  48 */     setAllocator((ByteBufAllocator)new PreferHeapByteBufAllocator(getAllocator()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  53 */     return getOptions(super
/*  54 */         .getOptions(), new ChannelOption[] { ChannelOption.SO_TIMEOUT });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  60 */     if (option == ChannelOption.SO_TIMEOUT) {
/*  61 */       return (T)Integer.valueOf(getSoTimeout());
/*     */     }
/*  63 */     return (T)super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  68 */     validate(option, value);
/*     */     
/*  70 */     if (option == ChannelOption.SO_TIMEOUT) {
/*  71 */       setSoTimeout(((Integer)value).intValue());
/*     */     } else {
/*  73 */       return super.setOption(option, value);
/*     */     } 
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setSoTimeout(int timeout) {
/*     */     try {
/*  81 */       this.javaSocket.setSoTimeout(timeout);
/*  82 */     } catch (IOException e) {
/*  83 */       throw new ChannelException(e);
/*     */     } 
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSoTimeout() {
/*     */     try {
/*  91 */       return this.javaSocket.getSoTimeout();
/*  92 */     } catch (IOException e) {
/*  93 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setBacklog(int backlog) {
/*  99 */     super.setBacklog(backlog);
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setReuseAddress(boolean reuseAddress) {
/* 105 */     super.setReuseAddress(reuseAddress);
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/* 111 */     super.setReceiveBufferSize(receiveBufferSize);
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
/* 117 */     super.setPerformancePreferences(connectionTime, latency, bandwidth);
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 123 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public OioServerSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 130 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 131 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 136 */     super.setWriteSpinCount(writeSpinCount);
/* 137 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 142 */     super.setAllocator(allocator);
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 148 */     super.setRecvByteBufAllocator(allocator);
/* 149 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setAutoRead(boolean autoRead) {
/* 154 */     super.setAutoRead(autoRead);
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void autoReadCleared() {
/* 160 */     if (this.channel instanceof OioServerSocketChannel) {
/* 161 */       ((OioServerSocketChannel)this.channel).clearReadPending0();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setAutoClose(boolean autoClose) {
/* 167 */     super.setAutoClose(autoClose);
/* 168 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 173 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 174 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 179 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 180 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 185 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 191 */     super.setMessageSizeEstimator(estimator);
/* 192 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\socket\oio\DefaultOioServerSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */