/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
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
/*     */ 
/*     */ 
/*     */ public class DefaultChannelConfig
/*     */   implements ChannelConfig
/*     */ {
/*  44 */   private static final MessageSizeEstimator DEFAULT_MSG_SIZE_ESTIMATOR = DefaultMessageSizeEstimator.DEFAULT;
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_CONNECT_TIMEOUT = 30000;
/*     */   
/*  49 */   private static final AtomicIntegerFieldUpdater<DefaultChannelConfig> AUTOREAD_UPDATER = AtomicIntegerFieldUpdater.newUpdater(DefaultChannelConfig.class, "autoRead");
/*     */   
/*  51 */   private static final AtomicReferenceFieldUpdater<DefaultChannelConfig, WriteBufferWaterMark> WATERMARK_UPDATER = AtomicReferenceFieldUpdater.newUpdater(DefaultChannelConfig.class, WriteBufferWaterMark.class, "writeBufferWaterMark");
/*     */ 
/*     */   
/*     */   protected final Channel channel;
/*     */   
/*  56 */   private volatile ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
/*     */   private volatile RecvByteBufAllocator rcvBufAllocator;
/*  58 */   private volatile MessageSizeEstimator msgSizeEstimator = DEFAULT_MSG_SIZE_ESTIMATOR;
/*     */   
/*  60 */   private volatile int connectTimeoutMillis = 30000;
/*  61 */   private volatile int writeSpinCount = 16;
/*  62 */   private volatile int autoRead = 1;
/*     */   
/*     */   private volatile boolean autoClose = true;
/*  65 */   private volatile WriteBufferWaterMark writeBufferWaterMark = WriteBufferWaterMark.DEFAULT;
/*     */   private volatile boolean pinEventExecutor = true;
/*     */   
/*     */   public DefaultChannelConfig(Channel channel) {
/*  69 */     this(channel, new AdaptiveRecvByteBufAllocator());
/*     */   }
/*     */   
/*     */   protected DefaultChannelConfig(Channel channel, RecvByteBufAllocator allocator) {
/*  73 */     setRecvByteBufAllocator(allocator, channel.metadata());
/*  74 */     this.channel = channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions() {
/*  80 */     return getOptions(null, (ChannelOption<?>[])new ChannelOption[] { ChannelOption.CONNECT_TIMEOUT_MILLIS, ChannelOption.MAX_MESSAGES_PER_READ, ChannelOption.WRITE_SPIN_COUNT, ChannelOption.ALLOCATOR, ChannelOption.AUTO_READ, ChannelOption.AUTO_CLOSE, ChannelOption.RCVBUF_ALLOCATOR, ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, ChannelOption.WRITE_BUFFER_WATER_MARK, ChannelOption.MESSAGE_SIZE_ESTIMATOR, ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<ChannelOption<?>, Object> getOptions(Map<ChannelOption<?>, Object> result, ChannelOption<?>... options) {
/*  90 */     if (result == null) {
/*  91 */       result = new IdentityHashMap<ChannelOption<?>, Object>();
/*     */     }
/*  93 */     for (ChannelOption<?> o : options) {
/*  94 */       result.put(o, getOption(o));
/*     */     }
/*  96 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setOptions(Map<ChannelOption<?>, ?> options) {
/* 102 */     if (options == null) {
/* 103 */       throw new NullPointerException("options");
/*     */     }
/*     */     
/* 106 */     boolean setAllOptions = true;
/* 107 */     for (Map.Entry<ChannelOption<?>, ?> e : options.entrySet()) {
/* 108 */       if (!setOption(e.getKey(), e.getValue())) {
/* 109 */         setAllOptions = false;
/*     */       }
/*     */     } 
/*     */     
/* 113 */     return setAllOptions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option) {
/* 119 */     if (option == null) {
/* 120 */       throw new NullPointerException("option");
/*     */     }
/*     */     
/* 123 */     if (option == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
/* 124 */       return (T)Integer.valueOf(getConnectTimeoutMillis());
/*     */     }
/* 126 */     if (option == ChannelOption.MAX_MESSAGES_PER_READ) {
/* 127 */       return (T)Integer.valueOf(getMaxMessagesPerRead());
/*     */     }
/* 129 */     if (option == ChannelOption.WRITE_SPIN_COUNT) {
/* 130 */       return (T)Integer.valueOf(getWriteSpinCount());
/*     */     }
/* 132 */     if (option == ChannelOption.ALLOCATOR) {
/* 133 */       return (T)getAllocator();
/*     */     }
/* 135 */     if (option == ChannelOption.RCVBUF_ALLOCATOR) {
/* 136 */       return (T)getRecvByteBufAllocator();
/*     */     }
/* 138 */     if (option == ChannelOption.AUTO_READ) {
/* 139 */       return (T)Boolean.valueOf(isAutoRead());
/*     */     }
/* 141 */     if (option == ChannelOption.AUTO_CLOSE) {
/* 142 */       return (T)Boolean.valueOf(isAutoClose());
/*     */     }
/* 144 */     if (option == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
/* 145 */       return (T)Integer.valueOf(getWriteBufferHighWaterMark());
/*     */     }
/* 147 */     if (option == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
/* 148 */       return (T)Integer.valueOf(getWriteBufferLowWaterMark());
/*     */     }
/* 150 */     if (option == ChannelOption.WRITE_BUFFER_WATER_MARK) {
/* 151 */       return (T)getWriteBufferWaterMark();
/*     */     }
/* 153 */     if (option == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
/* 154 */       return (T)getMessageSizeEstimator();
/*     */     }
/* 156 */     if (option == ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP) {
/* 157 */       return (T)Boolean.valueOf(getPinEventExecutorPerGroup());
/*     */     }
/* 159 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value) {
/* 165 */     validate(option, value);
/*     */     
/* 167 */     if (option == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
/* 168 */       setConnectTimeoutMillis(((Integer)value).intValue());
/* 169 */     } else if (option == ChannelOption.MAX_MESSAGES_PER_READ) {
/* 170 */       setMaxMessagesPerRead(((Integer)value).intValue());
/* 171 */     } else if (option == ChannelOption.WRITE_SPIN_COUNT) {
/* 172 */       setWriteSpinCount(((Integer)value).intValue());
/* 173 */     } else if (option == ChannelOption.ALLOCATOR) {
/* 174 */       setAllocator((ByteBufAllocator)value);
/* 175 */     } else if (option == ChannelOption.RCVBUF_ALLOCATOR) {
/* 176 */       setRecvByteBufAllocator((RecvByteBufAllocator)value);
/* 177 */     } else if (option == ChannelOption.AUTO_READ) {
/* 178 */       setAutoRead(((Boolean)value).booleanValue());
/* 179 */     } else if (option == ChannelOption.AUTO_CLOSE) {
/* 180 */       setAutoClose(((Boolean)value).booleanValue());
/* 181 */     } else if (option == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
/* 182 */       setWriteBufferHighWaterMark(((Integer)value).intValue());
/* 183 */     } else if (option == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
/* 184 */       setWriteBufferLowWaterMark(((Integer)value).intValue());
/* 185 */     } else if (option == ChannelOption.WRITE_BUFFER_WATER_MARK) {
/* 186 */       setWriteBufferWaterMark((WriteBufferWaterMark)value);
/* 187 */     } else if (option == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
/* 188 */       setMessageSizeEstimator((MessageSizeEstimator)value);
/* 189 */     } else if (option == ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP) {
/* 190 */       setPinEventExecutorPerGroup(((Boolean)value).booleanValue());
/*     */     } else {
/* 192 */       return false;
/*     */     } 
/*     */     
/* 195 */     return true;
/*     */   }
/*     */   
/*     */   protected <T> void validate(ChannelOption<T> option, T value) {
/* 199 */     if (option == null) {
/* 200 */       throw new NullPointerException("option");
/*     */     }
/* 202 */     option.validate(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getConnectTimeoutMillis() {
/* 207 */     return this.connectTimeoutMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 212 */     if (connectTimeoutMillis < 0)
/* 213 */       throw new IllegalArgumentException(String.format("connectTimeoutMillis: %d (expected: >= 0)", new Object[] {
/* 214 */               Integer.valueOf(connectTimeoutMillis)
/*     */             })); 
/* 216 */     this.connectTimeoutMillis = connectTimeoutMillis;
/* 217 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getMaxMessagesPerRead() {
/*     */     try {
/* 230 */       MaxMessagesRecvByteBufAllocator allocator = getRecvByteBufAllocator();
/* 231 */       return allocator.maxMessagesPerRead();
/* 232 */     } catch (ClassCastException e) {
/* 233 */       throw new IllegalStateException("getRecvByteBufAllocator() must return an object of type MaxMessagesRecvByteBufAllocator", e);
/*     */     } 
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
/*     */   @Deprecated
/*     */   public ChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/*     */     try {
/* 248 */       MaxMessagesRecvByteBufAllocator allocator = getRecvByteBufAllocator();
/* 249 */       allocator.maxMessagesPerRead(maxMessagesPerRead);
/* 250 */       return this;
/* 251 */     } catch (ClassCastException e) {
/* 252 */       throw new IllegalStateException("getRecvByteBufAllocator() must return an object of type MaxMessagesRecvByteBufAllocator", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWriteSpinCount() {
/* 259 */     return this.writeSpinCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelConfig setWriteSpinCount(int writeSpinCount) {
/* 264 */     if (writeSpinCount <= 0) {
/* 265 */       throw new IllegalArgumentException("writeSpinCount must be a positive integer.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 272 */     if (writeSpinCount == Integer.MAX_VALUE) {
/* 273 */       writeSpinCount--;
/*     */     }
/* 275 */     this.writeSpinCount = writeSpinCount;
/* 276 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBufAllocator getAllocator() {
/* 281 */     return this.allocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelConfig setAllocator(ByteBufAllocator allocator) {
/* 286 */     if (allocator == null) {
/* 287 */       throw new NullPointerException("allocator");
/*     */     }
/* 289 */     this.allocator = allocator;
/* 290 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends RecvByteBufAllocator> T getRecvByteBufAllocator() {
/* 296 */     return (T)this.rcvBufAllocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
/* 301 */     this.rcvBufAllocator = (RecvByteBufAllocator)ObjectUtil.checkNotNull(allocator, "allocator");
/* 302 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setRecvByteBufAllocator(RecvByteBufAllocator allocator, ChannelMetadata metadata) {
/* 312 */     if (allocator instanceof MaxMessagesRecvByteBufAllocator) {
/* 313 */       ((MaxMessagesRecvByteBufAllocator)allocator).maxMessagesPerRead(metadata.defaultMaxMessagesPerRead());
/* 314 */     } else if (allocator == null) {
/* 315 */       throw new NullPointerException("allocator");
/*     */     } 
/* 317 */     setRecvByteBufAllocator(allocator);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAutoRead() {
/* 322 */     return (this.autoRead == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelConfig setAutoRead(boolean autoRead) {
/* 327 */     boolean oldAutoRead = (AUTOREAD_UPDATER.getAndSet(this, autoRead ? 1 : 0) == 1);
/* 328 */     if (autoRead && !oldAutoRead) {
/* 329 */       this.channel.read();
/* 330 */     } else if (!autoRead && oldAutoRead) {
/* 331 */       autoReadCleared();
/*     */     } 
/* 333 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void autoReadCleared() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoClose() {
/* 344 */     return this.autoClose;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelConfig setAutoClose(boolean autoClose) {
/* 349 */     this.autoClose = autoClose;
/* 350 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWriteBufferHighWaterMark() {
/* 355 */     return this.writeBufferWaterMark.high();
/*     */   }
/*     */   
/*     */   public ChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
/*     */     WriteBufferWaterMark waterMark;
/* 360 */     if (writeBufferHighWaterMark < 0) {
/* 361 */       throw new IllegalArgumentException("writeBufferHighWaterMark must be >= 0");
/*     */     }
/*     */     
/*     */     do {
/* 365 */       waterMark = this.writeBufferWaterMark;
/* 366 */       if (writeBufferHighWaterMark < waterMark.low()) {
/* 367 */         throw new IllegalArgumentException("writeBufferHighWaterMark cannot be less than writeBufferLowWaterMark (" + waterMark
/*     */             
/* 369 */             .low() + "): " + writeBufferHighWaterMark);
/*     */       }
/*     */     }
/* 372 */     while (!WATERMARK_UPDATER.compareAndSet(this, waterMark, new WriteBufferWaterMark(waterMark
/* 373 */           .low(), writeBufferHighWaterMark, false)));
/* 374 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWriteBufferLowWaterMark() {
/* 381 */     return this.writeBufferWaterMark.low();
/*     */   }
/*     */   
/*     */   public ChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
/*     */     WriteBufferWaterMark waterMark;
/* 386 */     if (writeBufferLowWaterMark < 0) {
/* 387 */       throw new IllegalArgumentException("writeBufferLowWaterMark must be >= 0");
/*     */     }
/*     */     
/*     */     do {
/* 391 */       waterMark = this.writeBufferWaterMark;
/* 392 */       if (writeBufferLowWaterMark > waterMark.high()) {
/* 393 */         throw new IllegalArgumentException("writeBufferLowWaterMark cannot be greater than writeBufferHighWaterMark (" + waterMark
/*     */             
/* 395 */             .high() + "): " + writeBufferLowWaterMark);
/*     */       }
/*     */     }
/* 398 */     while (!WATERMARK_UPDATER.compareAndSet(this, waterMark, new WriteBufferWaterMark(writeBufferLowWaterMark, waterMark
/* 399 */           .high(), false)));
/* 400 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark) {
/* 407 */     this.writeBufferWaterMark = (WriteBufferWaterMark)ObjectUtil.checkNotNull(writeBufferWaterMark, "writeBufferWaterMark");
/* 408 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public WriteBufferWaterMark getWriteBufferWaterMark() {
/* 413 */     return this.writeBufferWaterMark;
/*     */   }
/*     */ 
/*     */   
/*     */   public MessageSizeEstimator getMessageSizeEstimator() {
/* 418 */     return this.msgSizeEstimator;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
/* 423 */     if (estimator == null) {
/* 424 */       throw new NullPointerException("estimator");
/*     */     }
/* 426 */     this.msgSizeEstimator = estimator;
/* 427 */     return this;
/*     */   }
/*     */   
/*     */   private ChannelConfig setPinEventExecutorPerGroup(boolean pinEventExecutor) {
/* 431 */     this.pinEventExecutor = pinEventExecutor;
/* 432 */     return this;
/*     */   }
/*     */   
/*     */   private boolean getPinEventExecutorPerGroup() {
/* 436 */     return this.pinEventExecutor;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\DefaultChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */