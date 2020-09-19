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
/*     */ import io.netty.channel.socket.DefaultSocketChannelConfig;
/*     */ import io.netty.channel.socket.SocketChannel;
/*     */ import io.netty.channel.socket.SocketChannelConfig;
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
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
/*     */ public class DefaultOioSocketChannelConfig
/*     */   extends DefaultSocketChannelConfig
/*     */   implements OioSocketChannelConfig
/*     */ {
/*     */   @Deprecated
/*     */   public DefaultOioSocketChannelConfig(SocketChannel channel, Socket javaSocket) {
/*  40 */     super(channel, javaSocket);
/*  41 */     setAllocator((ByteBufAllocator)new PreferHeapByteBufAllocator(getAllocator()));
/*     */   }
/*     */   
/*     */   DefaultOioSocketChannelConfig(OioSocketChannel channel, Socket javaSocket) {
/*  45 */     super(channel, javaSocket);
/*  46 */     setAllocator((ByteBufAllocator)new PreferHeapByteBufAllocator(getAllocator()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  51 */     return getOptions(super
/*  52 */         .getOptions(), new ChannelOption[] { ChannelOption.SO_TIMEOUT });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  58 */     if (option == ChannelOption.SO_TIMEOUT) {
/*  59 */       return (T)Integer.valueOf(getSoTimeout());
/*     */     }
/*  61 */     return (T)super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  66 */     validate(option, value);
/*     */     
/*  68 */     if (option == ChannelOption.SO_TIMEOUT) {
/*  69 */       setSoTimeout(((Integer)value).intValue());
/*     */     } else {
/*  71 */       return super.setOption(option, value);
/*     */     } 
/*  73 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setSoTimeout(int timeout) {
/*     */     try {
/*  79 */       this.javaSocket.setSoTimeout(timeout);
/*  80 */     } catch (IOException e) {
/*  81 */       throw new ChannelException(e);
/*     */     } 
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSoTimeout() {
/*     */     try {
/*  89 */       return this.javaSocket.getSoTimeout();
/*  90 */     } catch (IOException e) {
/*  91 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setTcpNoDelay(boolean tcpNoDelay) {
/*  97 */     super.setTcpNoDelay(tcpNoDelay);
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setSoLinger(int soLinger) {
/* 103 */     super.setSoLinger(soLinger);
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setSendBufferSize(int sendBufferSize) {
/* 109 */     super.setSendBufferSize(sendBufferSize);
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/* 115 */     super.setReceiveBufferSize(receiveBufferSize);
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setKeepAlive(boolean keepAlive) {
/* 121 */     super.setKeepAlive(keepAlive);
/* 122 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setTrafficClass(int trafficClass) {
/* 127 */     super.setTrafficClass(trafficClass);
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setReuseAddress(boolean reuseAddress) {
/* 133 */     super.setReuseAddress(reuseAddress);
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
/* 139 */     super.setPerformancePreferences(connectionTime, latency, bandwidth);
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setAllowHalfClosure(boolean allowHalfClosure) {
/* 145 */     super.setAllowHalfClosure(allowHalfClosure);
/* 146 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 151 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public OioSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 158 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 164 */     super.setWriteSpinCount(writeSpinCount);
/* 165 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 170 */     super.setAllocator(allocator);
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 176 */     super.setRecvByteBufAllocator(allocator);
/* 177 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setAutoRead(boolean autoRead) {
/* 182 */     super.setAutoRead(autoRead);
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void autoReadCleared() {
/* 188 */     if (this.channel instanceof OioSocketChannel) {
/* 189 */       ((OioSocketChannel)this.channel).clearReadPending0();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setAutoClose(boolean autoClose) {
/* 195 */     super.setAutoClose(autoClose);
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 201 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 202 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 207 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 208 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 213 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 219 */     super.setMessageSizeEstimator(estimator);
/* 220 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\socket\oio\DefaultOioSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */