/*     */ package io.netty.channel.udt;
/*     */ 
/*     */ import com.barchart.udt.OptionUDT;
/*     */ import com.barchart.udt.SocketUDT;
/*     */ import com.barchart.udt.nio.ChannelUDT;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.DefaultChannelConfig;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class DefaultUdtChannelConfig
/*     */   extends DefaultChannelConfig
/*     */   implements UdtChannelConfig
/*     */ {
/*     */   private static final int K = 1024;
/*     */   private static final int M = 1048576;
/*  52 */   private volatile int protocolReceiveBufferSize = 10485760;
/*  53 */   private volatile int protocolSendBufferSize = 10485760;
/*     */   
/*  55 */   private volatile int systemReceiveBufferSize = 1048576;
/*  56 */   private volatile int systemSendBufferSize = 1048576;
/*     */   
/*  58 */   private volatile int allocatorReceiveBufferSize = 131072;
/*  59 */   private volatile int allocatorSendBufferSize = 131072;
/*     */ 
/*     */   
/*     */   private volatile int soLinger;
/*     */   
/*     */   private volatile boolean reuseAddress = true;
/*     */ 
/*     */   
/*     */   public DefaultUdtChannelConfig(UdtChannel channel, ChannelUDT channelUDT, boolean apply) throws IOException {
/*  68 */     super(channel);
/*  69 */     if (apply) {
/*  70 */       apply(channelUDT);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void apply(ChannelUDT channelUDT) throws IOException {
/*  75 */     SocketUDT socketUDT = channelUDT.socketUDT();
/*  76 */     socketUDT.setReuseAddress(isReuseAddress());
/*  77 */     socketUDT.setSendBufferSize(getSendBufferSize());
/*  78 */     if (getSoLinger() <= 0) {
/*  79 */       socketUDT.setSoLinger(false, 0);
/*     */     } else {
/*  81 */       socketUDT.setSoLinger(true, getSoLinger());
/*     */     } 
/*  83 */     socketUDT.setOption(OptionUDT.Protocol_Receive_Buffer_Size, 
/*  84 */         Integer.valueOf(getProtocolReceiveBufferSize()));
/*  85 */     socketUDT.setOption(OptionUDT.Protocol_Send_Buffer_Size, 
/*  86 */         Integer.valueOf(getProtocolSendBufferSize()));
/*  87 */     socketUDT.setOption(OptionUDT.System_Receive_Buffer_Size, 
/*  88 */         Integer.valueOf(getSystemReceiveBufferSize()));
/*  89 */     socketUDT.setOption(OptionUDT.System_Send_Buffer_Size, 
/*  90 */         Integer.valueOf(getSystemSendBufferSize()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getProtocolReceiveBufferSize() {
/*  95 */     return this.protocolReceiveBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/* 101 */     if (option == UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE) {
/* 102 */       return (T)Integer.valueOf(getProtocolReceiveBufferSize());
/*     */     }
/* 104 */     if (option == UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE) {
/* 105 */       return (T)Integer.valueOf(getProtocolSendBufferSize());
/*     */     }
/* 107 */     if (option == UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE) {
/* 108 */       return (T)Integer.valueOf(getSystemReceiveBufferSize());
/*     */     }
/* 110 */     if (option == UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE) {
/* 111 */       return (T)Integer.valueOf(getSystemSendBufferSize());
/*     */     }
/* 113 */     if (option == ChannelOption.SO_RCVBUF) {
/* 114 */       return (T)Integer.valueOf(getReceiveBufferSize());
/*     */     }
/* 116 */     if (option == ChannelOption.SO_SNDBUF) {
/* 117 */       return (T)Integer.valueOf(getSendBufferSize());
/*     */     }
/* 119 */     if (option == ChannelOption.SO_REUSEADDR) {
/* 120 */       return (T)Boolean.valueOf(isReuseAddress());
/*     */     }
/* 122 */     if (option == ChannelOption.SO_LINGER) {
/* 123 */       return (T)Integer.valueOf(getSoLinger());
/*     */     }
/* 125 */     return (T)super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/* 130 */     return getOptions(super.getOptions(), new ChannelOption[] { UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE, UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE, UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE, UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getReceiveBufferSize() {
/* 138 */     return this.allocatorReceiveBufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSendBufferSize() {
/* 143 */     return this.allocatorSendBufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSoLinger() {
/* 148 */     return this.soLinger;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReuseAddress() {
/* 153 */     return this.reuseAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setProtocolReceiveBufferSize(int protocolReceiveBufferSize) {
/* 158 */     this.protocolReceiveBufferSize = protocolReceiveBufferSize;
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/* 164 */     validate(option, value);
/* 165 */     if (option == UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE) {
/* 166 */       setProtocolReceiveBufferSize(((Integer)value).intValue());
/* 167 */     } else if (option == UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE) {
/* 168 */       setProtocolSendBufferSize(((Integer)value).intValue());
/* 169 */     } else if (option == UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE) {
/* 170 */       setSystemReceiveBufferSize(((Integer)value).intValue());
/* 171 */     } else if (option == UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE) {
/* 172 */       setSystemSendBufferSize(((Integer)value).intValue());
/* 173 */     } else if (option == ChannelOption.SO_RCVBUF) {
/* 174 */       setReceiveBufferSize(((Integer)value).intValue());
/* 175 */     } else if (option == ChannelOption.SO_SNDBUF) {
/* 176 */       setSendBufferSize(((Integer)value).intValue());
/* 177 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/* 178 */       setReuseAddress(((Boolean)value).booleanValue());
/* 179 */     } else if (option == ChannelOption.SO_LINGER) {
/* 180 */       setSoLinger(((Integer)value).intValue());
/*     */     } else {
/* 182 */       return super.setOption(option, value);
/*     */     } 
/* 184 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/* 189 */     this.allocatorReceiveBufferSize = receiveBufferSize;
/* 190 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setReuseAddress(boolean reuseAddress) {
/* 195 */     this.reuseAddress = reuseAddress;
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setSendBufferSize(int sendBufferSize) {
/* 201 */     this.allocatorSendBufferSize = sendBufferSize;
/* 202 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setSoLinger(int soLinger) {
/* 207 */     this.soLinger = soLinger;
/* 208 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSystemReceiveBufferSize() {
/* 213 */     return this.systemReceiveBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setSystemSendBufferSize(int systemReceiveBufferSize) {
/* 219 */     this.systemReceiveBufferSize = systemReceiveBufferSize;
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getProtocolSendBufferSize() {
/* 225 */     return this.protocolSendBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setProtocolSendBufferSize(int protocolSendBufferSize) {
/* 231 */     this.protocolSendBufferSize = protocolSendBufferSize;
/* 232 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setSystemReceiveBufferSize(int systemSendBufferSize) {
/* 238 */     this.systemSendBufferSize = systemSendBufferSize;
/* 239 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSystemSendBufferSize() {
/* 244 */     return this.systemSendBufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 249 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 250 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public UdtChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 256 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 257 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 262 */     super.setWriteSpinCount(writeSpinCount);
/* 263 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 268 */     super.setAllocator(allocator);
/* 269 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 274 */     super.setRecvByteBufAllocator(allocator);
/* 275 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setAutoRead(boolean autoRead) {
/* 280 */     super.setAutoRead(autoRead);
/* 281 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setAutoClose(boolean autoClose) {
/* 286 */     super.setAutoClose(autoClose);
/* 287 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 292 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 293 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 298 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 299 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 304 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 305 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 310 */     super.setMessageSizeEstimator(estimator);
/* 311 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\udt\DefaultUdtChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */