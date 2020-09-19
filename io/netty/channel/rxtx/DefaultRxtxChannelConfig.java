/*     */ package io.netty.channel.rxtx;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.PreferHeapByteBufAllocator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
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
/*     */ @Deprecated
/*     */ final class DefaultRxtxChannelConfig
/*     */   extends DefaultChannelConfig
/*     */   implements RxtxChannelConfig
/*     */ {
/*  45 */   private volatile int baudrate = 115200;
/*     */   private volatile boolean dtr;
/*     */   private volatile boolean rts;
/*  48 */   private volatile RxtxChannelConfig.Stopbits stopbits = RxtxChannelConfig.Stopbits.STOPBITS_1;
/*  49 */   private volatile RxtxChannelConfig.Databits databits = RxtxChannelConfig.Databits.DATABITS_8;
/*  50 */   private volatile RxtxChannelConfig.Paritybit paritybit = RxtxChannelConfig.Paritybit.NONE;
/*     */   private volatile int waitTime;
/*  52 */   private volatile int readTimeout = 1000;
/*     */   
/*     */   DefaultRxtxChannelConfig(RxtxChannel channel) {
/*  55 */     super((Channel)channel);
/*  56 */     setAllocator((ByteBufAllocator)new PreferHeapByteBufAllocator(getAllocator()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  61 */     return getOptions(super.getOptions(), new ChannelOption[] { RxtxChannelOption.BAUD_RATE, RxtxChannelOption.DTR, RxtxChannelOption.RTS, RxtxChannelOption.STOP_BITS, RxtxChannelOption.DATA_BITS, RxtxChannelOption.PARITY_BIT, RxtxChannelOption.WAIT_TIME });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/*  67 */     if (option == RxtxChannelOption.BAUD_RATE) {
/*  68 */       return (T)Integer.valueOf(getBaudrate());
/*     */     }
/*  70 */     if (option == RxtxChannelOption.DTR) {
/*  71 */       return (T)Boolean.valueOf(isDtr());
/*     */     }
/*  73 */     if (option == RxtxChannelOption.RTS) {
/*  74 */       return (T)Boolean.valueOf(isRts());
/*     */     }
/*  76 */     if (option == RxtxChannelOption.STOP_BITS) {
/*  77 */       return (T)getStopbits();
/*     */     }
/*  79 */     if (option == RxtxChannelOption.DATA_BITS) {
/*  80 */       return (T)getDatabits();
/*     */     }
/*  82 */     if (option == RxtxChannelOption.PARITY_BIT) {
/*  83 */       return (T)getParitybit();
/*     */     }
/*  85 */     if (option == RxtxChannelOption.WAIT_TIME) {
/*  86 */       return (T)Integer.valueOf(getWaitTimeMillis());
/*     */     }
/*  88 */     if (option == RxtxChannelOption.READ_TIMEOUT) {
/*  89 */       return (T)Integer.valueOf(getReadTimeout());
/*     */     }
/*  91 */     return (T)super.getOption(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/*  96 */     validate(option, value);
/*     */     
/*  98 */     if (option == RxtxChannelOption.BAUD_RATE) {
/*  99 */       setBaudrate(((Integer)value).intValue());
/* 100 */     } else if (option == RxtxChannelOption.DTR) {
/* 101 */       setDtr(((Boolean)value).booleanValue());
/* 102 */     } else if (option == RxtxChannelOption.RTS) {
/* 103 */       setRts(((Boolean)value).booleanValue());
/* 104 */     } else if (option == RxtxChannelOption.STOP_BITS) {
/* 105 */       setStopbits((RxtxChannelConfig.Stopbits)value);
/* 106 */     } else if (option == RxtxChannelOption.DATA_BITS) {
/* 107 */       setDatabits((RxtxChannelConfig.Databits)value);
/* 108 */     } else if (option == RxtxChannelOption.PARITY_BIT) {
/* 109 */       setParitybit((RxtxChannelConfig.Paritybit)value);
/* 110 */     } else if (option == RxtxChannelOption.WAIT_TIME) {
/* 111 */       setWaitTimeMillis(((Integer)value).intValue());
/* 112 */     } else if (option == RxtxChannelOption.READ_TIMEOUT) {
/* 113 */       setReadTimeout(((Integer)value).intValue());
/*     */     } else {
/* 115 */       return super.setOption(option, value);
/*     */     } 
/* 117 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setBaudrate(int baudrate) {
/* 122 */     this.baudrate = baudrate;
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setStopbits(RxtxChannelConfig.Stopbits stopbits) {
/* 128 */     this.stopbits = stopbits;
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setDatabits(RxtxChannelConfig.Databits databits) {
/* 134 */     this.databits = databits;
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setParitybit(RxtxChannelConfig.Paritybit paritybit) {
/* 140 */     this.paritybit = paritybit;
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBaudrate() {
/* 146 */     return this.baudrate;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig.Stopbits getStopbits() {
/* 151 */     return this.stopbits;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig.Databits getDatabits() {
/* 156 */     return this.databits;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig.Paritybit getParitybit() {
/* 161 */     return this.paritybit;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDtr() {
/* 166 */     return this.dtr;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setDtr(boolean dtr) {
/* 171 */     this.dtr = dtr;
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRts() {
/* 177 */     return this.rts;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setRts(boolean rts) {
/* 182 */     this.rts = rts;
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWaitTimeMillis() {
/* 188 */     return this.waitTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setWaitTimeMillis(int waitTimeMillis) {
/* 193 */     if (waitTimeMillis < 0) {
/* 194 */       throw new IllegalArgumentException("Wait time must be >= 0");
/*     */     }
/* 196 */     this.waitTime = waitTimeMillis;
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setReadTimeout(int readTimeout) {
/* 202 */     if (readTimeout < 0) {
/* 203 */       throw new IllegalArgumentException("readTime must be >= 0");
/*     */     }
/* 205 */     this.readTimeout = readTimeout;
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReadTimeout() {
/* 211 */     return this.readTimeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 216 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 217 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public RxtxChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/* 223 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 224 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 229 */     super.setWriteSpinCount(writeSpinCount);
/* 230 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 235 */     super.setAllocator(allocator);
/* 236 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 241 */     super.setRecvByteBufAllocator(allocator);
/* 242 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setAutoRead(boolean autoRead) {
/* 247 */     super.setAutoRead(autoRead);
/* 248 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setAutoClose(boolean autoClose) {
/* 253 */     super.setAutoClose(autoClose);
/* 254 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/* 259 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 260 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/* 265 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 266 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 271 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 272 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 277 */     super.setMessageSizeEstimator(estimator);
/* 278 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\rxtx\DefaultRxtxChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */