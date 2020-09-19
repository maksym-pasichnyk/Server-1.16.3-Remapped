/*     */ package io.netty.channel.sctp;
/*     */ 
/*     */ import com.sun.nio.sctp.SctpChannel;
/*     */ import com.sun.nio.sctp.SctpStandardSocketOptions;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ public class DefaultSctpChannelConfig
/*     */   extends DefaultChannelConfig
/*     */   implements SctpChannelConfig
/*     */ {
/*     */   private final SctpChannel javaChannel;
/*     */   
/*     */   public DefaultSctpChannelConfig(SctpChannel channel, SctpChannel javaChannel) {
/*  45 */     super(channel);
/*  46 */     if (javaChannel == null) {
/*  47 */       throw new NullPointerException("javaChannel");
/*     */     }
/*  49 */     this.javaChannel = javaChannel;
/*     */ 
/*     */     
/*  52 */     if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
/*     */       try {
/*  54 */         setSctpNoDelay(true);
/*  55 */       } catch (Exception exception) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  63 */     return getOptions(super
/*  64 */         .getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, SctpChannelOption.SCTP_NODELAY, SctpChannelOption.SCTP_INIT_MAXSTREAMS });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  71 */     if (option == ChannelOption.SO_RCVBUF) {
/*  72 */       return (T)Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  74 */     if (option == ChannelOption.SO_SNDBUF) {
/*  75 */       return (T)Integer.valueOf(getSendBufferSize());
/*     */     }
/*  77 */     if (option == SctpChannelOption.SCTP_NODELAY) {
/*  78 */       return (T)Boolean.valueOf(isSctpNoDelay());
/*     */     }
/*  80 */     if (option == SctpChannelOption.SCTP_INIT_MAXSTREAMS) {
/*  81 */       return (T)getInitMaxStreams();
/*     */     }
/*  83 */     return (T)super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  88 */     validate(option, value);
/*     */     
/*  90 */     if (option == ChannelOption.SO_RCVBUF) {
/*  91 */       setReceiveBufferSize(((Integer)value).intValue());
/*  92 */     } else if (option == ChannelOption.SO_SNDBUF) {
/*  93 */       setSendBufferSize(((Integer)value).intValue());
/*  94 */     } else if (option == SctpChannelOption.SCTP_NODELAY) {
/*  95 */       setSctpNoDelay(((Boolean)value).booleanValue());
/*  96 */     } else if (option == SctpChannelOption.SCTP_INIT_MAXSTREAMS) {
/*  97 */       setInitMaxStreams((SctpStandardSocketOptions.InitMaxStreams)value);
/*     */     } else {
/*  99 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSctpNoDelay() {
/*     */     try {
/* 108 */       return ((Boolean)this.javaChannel.<Boolean>getOption(SctpStandardSocketOptions.SCTP_NODELAY)).booleanValue();
/* 109 */     } catch (IOException e) {
/* 110 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setSctpNoDelay(boolean sctpNoDelay) {
/*     */     try {
/* 117 */       this.javaChannel.setOption(SctpStandardSocketOptions.SCTP_NODELAY, Boolean.valueOf(sctpNoDelay));
/* 118 */     } catch (IOException e) {
/* 119 */       throw new ChannelException(e);
/*     */     } 
/* 121 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSendBufferSize() {
/*     */     try {
/* 127 */       return ((Integer)this.javaChannel.<Integer>getOption(SctpStandardSocketOptions.SO_SNDBUF)).intValue();
/* 128 */     } catch (IOException e) {
/* 129 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setSendBufferSize(int sendBufferSize) {
/*     */     try {
/* 136 */       this.javaChannel.setOption(SctpStandardSocketOptions.SO_SNDBUF, Integer.valueOf(sendBufferSize));
/* 137 */     } catch (IOException e) {
/* 138 */       throw new ChannelException(e);
/*     */     } 
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReceiveBufferSize() {
/*     */     try {
/* 146 */       return ((Integer)this.javaChannel.<Integer>getOption(SctpStandardSocketOptions.SO_RCVBUF)).intValue();
/* 147 */     } catch (IOException e) {
/* 148 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/*     */     try {
/* 155 */       this.javaChannel.setOption(SctpStandardSocketOptions.SO_RCVBUF, Integer.valueOf(receiveBufferSize));
/* 156 */     } catch (IOException e) {
/* 157 */       throw new ChannelException(e);
/*     */     } 
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpStandardSocketOptions.InitMaxStreams getInitMaxStreams() {
/*     */     try {
/* 165 */       return this.javaChannel.<SctpStandardSocketOptions.InitMaxStreams>getOption(SctpStandardSocketOptions.SCTP_INIT_MAXSTREAMS);
/* 166 */     } catch (IOException e) {
/* 167 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setInitMaxStreams(SctpStandardSocketOptions.InitMaxStreams initMaxStreams) {
/*     */     try {
/* 174 */       this.javaChannel.setOption(SctpStandardSocketOptions.SCTP_INIT_MAXSTREAMS, initMaxStreams);
/* 175 */     } catch (IOException e) {
/* 176 */       throw new ChannelException(e);
/*     */     } 
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 183 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SctpChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 190 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 191 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 196 */     super.setWriteSpinCount(writeSpinCount);
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 202 */     super.setAllocator(allocator);
/* 203 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 208 */     super.setRecvByteBufAllocator(allocator);
/* 209 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setAutoRead(boolean autoRead) {
/* 214 */     super.setAutoRead(autoRead);
/* 215 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setAutoClose(boolean autoClose) {
/* 220 */     super.setAutoClose(autoClose);
/* 221 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 226 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 227 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 232 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 233 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 238 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 239 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 244 */     super.setMessageSizeEstimator(estimator);
/* 245 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\sctp\DefaultSctpChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */