/*     */ package io.netty.channel.sctp;
/*     */ 
/*     */ import com.sun.nio.sctp.SctpServerChannel;
/*     */ import com.sun.nio.sctp.SctpStandardSocketOptions;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
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
/*     */ public class DefaultSctpServerChannelConfig
/*     */   extends DefaultChannelConfig
/*     */   implements SctpServerChannelConfig
/*     */ {
/*     */   private final SctpServerChannel javaChannel;
/*  38 */   private volatile int backlog = NetUtil.SOMAXCONN;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultSctpServerChannelConfig(SctpServerChannel channel, SctpServerChannel javaChannel) {
/*  45 */     super((Channel)channel);
/*  46 */     if (javaChannel == null) {
/*  47 */       throw new NullPointerException("javaChannel");
/*     */     }
/*  49 */     this.javaChannel = javaChannel;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  54 */     return getOptions(super
/*  55 */         .getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, SctpChannelOption.SCTP_INIT_MAXSTREAMS });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  62 */     if (option == ChannelOption.SO_RCVBUF) {
/*  63 */       return (T)Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  65 */     if (option == ChannelOption.SO_SNDBUF) {
/*  66 */       return (T)Integer.valueOf(getSendBufferSize());
/*     */     }
/*  68 */     if (option == SctpChannelOption.SCTP_INIT_MAXSTREAMS) {
/*  69 */       return (T)getInitMaxStreams();
/*     */     }
/*  71 */     return (T)super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  76 */     validate(option, value);
/*     */     
/*  78 */     if (option == ChannelOption.SO_RCVBUF) {
/*  79 */       setReceiveBufferSize(((Integer)value).intValue());
/*  80 */     } else if (option == ChannelOption.SO_SNDBUF) {
/*  81 */       setSendBufferSize(((Integer)value).intValue());
/*  82 */     } else if (option == SctpChannelOption.SCTP_INIT_MAXSTREAMS) {
/*  83 */       setInitMaxStreams((SctpStandardSocketOptions.InitMaxStreams)value);
/*     */     } else {
/*  85 */       return super.setOption(option, value);
/*     */     } 
/*     */     
/*  88 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSendBufferSize() {
/*     */     try {
/*  94 */       return ((Integer)this.javaChannel.<Integer>getOption(SctpStandardSocketOptions.SO_SNDBUF)).intValue();
/*  95 */     } catch (IOException e) {
/*  96 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setSendBufferSize(int sendBufferSize) {
/*     */     try {
/* 103 */       this.javaChannel.setOption(SctpStandardSocketOptions.SO_SNDBUF, Integer.valueOf(sendBufferSize));
/* 104 */     } catch (IOException e) {
/* 105 */       throw new ChannelException(e);
/*     */     } 
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReceiveBufferSize() {
/*     */     try {
/* 113 */       return ((Integer)this.javaChannel.<Integer>getOption(SctpStandardSocketOptions.SO_RCVBUF)).intValue();
/* 114 */     } catch (IOException e) {
/* 115 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/*     */     try {
/* 122 */       this.javaChannel.setOption(SctpStandardSocketOptions.SO_RCVBUF, Integer.valueOf(receiveBufferSize));
/* 123 */     } catch (IOException e) {
/* 124 */       throw new ChannelException(e);
/*     */     } 
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpStandardSocketOptions.InitMaxStreams getInitMaxStreams() {
/*     */     try {
/* 132 */       return this.javaChannel.<SctpStandardSocketOptions.InitMaxStreams>getOption(SctpStandardSocketOptions.SCTP_INIT_MAXSTREAMS);
/* 133 */     } catch (IOException e) {
/* 134 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setInitMaxStreams(SctpStandardSocketOptions.InitMaxStreams initMaxStreams) {
/*     */     try {
/* 141 */       this.javaChannel.setOption(SctpStandardSocketOptions.SCTP_INIT_MAXSTREAMS, initMaxStreams);
/* 142 */     } catch (IOException e) {
/* 143 */       throw new ChannelException(e);
/*     */     } 
/* 145 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBacklog() {
/* 150 */     return this.backlog;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setBacklog(int backlog) {
/* 155 */     if (backlog < 0) {
/* 156 */       throw new IllegalArgumentException("backlog: " + backlog);
/*     */     }
/* 158 */     this.backlog = backlog;
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SctpServerChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 165 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 171 */     super.setWriteSpinCount(writeSpinCount);
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 177 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 183 */     super.setAllocator(allocator);
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 189 */     super.setRecvByteBufAllocator(allocator);
/* 190 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setAutoRead(boolean autoRead) {
/* 195 */     super.setAutoRead(autoRead);
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setAutoClose(boolean autoClose) {
/* 201 */     super.setAutoClose(autoClose);
/* 202 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 207 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 208 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 213 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 219 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 225 */     super.setMessageSizeEstimator(estimator);
/* 226 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\sctp\DefaultSctpServerChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */