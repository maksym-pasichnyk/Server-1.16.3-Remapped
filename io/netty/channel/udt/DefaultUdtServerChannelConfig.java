/*     */ package io.netty.channel.udt;
/*     */ 
/*     */ import com.barchart.udt.nio.ChannelUDT;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class DefaultUdtServerChannelConfig
/*     */   extends DefaultUdtChannelConfig
/*     */   implements UdtServerChannelConfig
/*     */ {
/*  39 */   private volatile int backlog = 64;
/*     */ 
/*     */   
/*     */   public DefaultUdtServerChannelConfig(UdtChannel channel, ChannelUDT channelUDT, boolean apply) throws IOException {
/*  43 */     super(channel, channelUDT, apply);
/*  44 */     if (apply) {
/*  45 */       apply(channelUDT);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void apply(ChannelUDT channelUDT) throws IOException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBacklog() {
/*  56 */     return this.backlog;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  62 */     if (option == ChannelOption.SO_BACKLOG) {
/*  63 */       return (T)Integer.valueOf(getBacklog());
/*     */     }
/*  65 */     return super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  70 */     return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_BACKLOG });
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setBacklog(int backlog) {
/*  75 */     this.backlog = backlog;
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  81 */     validate(option, value);
/*  82 */     if (option == ChannelOption.SO_BACKLOG) {
/*  83 */       setBacklog(((Integer)value).intValue());
/*     */     } else {
/*  85 */       return super.setOption(option, value);
/*     */     } 
/*  87 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setProtocolReceiveBufferSize(int protocolReceiveBufferSize) {
/*  93 */     super.setProtocolReceiveBufferSize(protocolReceiveBufferSize);
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setProtocolSendBufferSize(int protocolSendBufferSize) {
/* 100 */     super.setProtocolSendBufferSize(protocolSendBufferSize);
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/* 107 */     super.setReceiveBufferSize(receiveBufferSize);
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setReuseAddress(boolean reuseAddress) {
/* 113 */     super.setReuseAddress(reuseAddress);
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setSendBufferSize(int sendBufferSize) {
/* 119 */     super.setSendBufferSize(sendBufferSize);
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setSoLinger(int soLinger) {
/* 125 */     super.setSoLinger(soLinger);
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setSystemReceiveBufferSize(int systemSendBufferSize) {
/* 132 */     super.setSystemReceiveBufferSize(systemSendBufferSize);
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setSystemSendBufferSize(int systemReceiveBufferSize) {
/* 139 */     super.setSystemSendBufferSize(systemReceiveBufferSize);
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 145 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 146 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public UdtServerChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 152 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 153 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 158 */     super.setWriteSpinCount(writeSpinCount);
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 164 */     super.setAllocator(allocator);
/* 165 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 170 */     super.setRecvByteBufAllocator(allocator);
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setAutoRead(boolean autoRead) {
/* 176 */     super.setAutoRead(autoRead);
/* 177 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setAutoClose(boolean autoClose) {
/* 182 */     super.setAutoClose(autoClose);
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 188 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 189 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 194 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 195 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 200 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 201 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 206 */     super.setMessageSizeEstimator(estimator);
/* 207 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\udt\DefaultUdtServerChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */