/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.util.UncheckedBooleanSupplier;
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
/*     */ public abstract class DefaultMaxMessagesRecvByteBufAllocator
/*     */   implements MaxMessagesRecvByteBufAllocator
/*     */ {
/*     */   private volatile int maxMessagesPerRead;
/*     */   private volatile boolean respectMaybeMoreData = true;
/*     */   
/*     */   public DefaultMaxMessagesRecvByteBufAllocator() {
/*  31 */     this(1);
/*     */   }
/*     */   
/*     */   public DefaultMaxMessagesRecvByteBufAllocator(int maxMessagesPerRead) {
/*  35 */     maxMessagesPerRead(maxMessagesPerRead);
/*     */   }
/*     */ 
/*     */   
/*     */   public int maxMessagesPerRead() {
/*  40 */     return this.maxMessagesPerRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public MaxMessagesRecvByteBufAllocator maxMessagesPerRead(int maxMessagesPerRead) {
/*  45 */     if (maxMessagesPerRead <= 0) {
/*  46 */       throw new IllegalArgumentException("maxMessagesPerRead: " + maxMessagesPerRead + " (expected: > 0)");
/*     */     }
/*  48 */     this.maxMessagesPerRead = maxMessagesPerRead;
/*  49 */     return this;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMaxMessagesRecvByteBufAllocator respectMaybeMoreData(boolean respectMaybeMoreData) {
/*  65 */     this.respectMaybeMoreData = respectMaybeMoreData;
/*  66 */     return this;
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
/*     */ 
/*     */   
/*     */   public final boolean respectMaybeMoreData() {
/*  81 */     return this.respectMaybeMoreData;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract class MaxMessageHandle
/*     */     implements RecvByteBufAllocator.ExtendedHandle
/*     */   {
/*     */     private ChannelConfig config;
/*     */     private int maxMessagePerRead;
/*     */     private int totalMessages;
/*     */     private int totalBytesRead;
/*     */     private int attemptedBytesRead;
/*     */     private int lastBytesRead;
/*  94 */     private final boolean respectMaybeMoreData = DefaultMaxMessagesRecvByteBufAllocator.this.respectMaybeMoreData;
/*  95 */     private final UncheckedBooleanSupplier defaultMaybeMoreSupplier = new UncheckedBooleanSupplier()
/*     */       {
/*     */         public boolean get() {
/*  98 */           return (DefaultMaxMessagesRecvByteBufAllocator.MaxMessageHandle.this.attemptedBytesRead == DefaultMaxMessagesRecvByteBufAllocator.MaxMessageHandle.this.lastBytesRead);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void reset(ChannelConfig config) {
/* 107 */       this.config = config;
/* 108 */       this.maxMessagePerRead = DefaultMaxMessagesRecvByteBufAllocator.this.maxMessagesPerRead();
/* 109 */       this.totalMessages = this.totalBytesRead = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuf allocate(ByteBufAllocator alloc) {
/* 114 */       return alloc.ioBuffer(guess());
/*     */     }
/*     */ 
/*     */     
/*     */     public final void incMessagesRead(int amt) {
/* 119 */       this.totalMessages += amt;
/*     */     }
/*     */ 
/*     */     
/*     */     public void lastBytesRead(int bytes) {
/* 124 */       this.lastBytesRead = bytes;
/* 125 */       if (bytes > 0) {
/* 126 */         this.totalBytesRead += bytes;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public final int lastBytesRead() {
/* 132 */       return this.lastBytesRead;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean continueReading() {
/* 137 */       return continueReading(this.defaultMaybeMoreSupplier);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean continueReading(UncheckedBooleanSupplier maybeMoreDataSupplier) {
/* 142 */       return (this.config.isAutoRead() && (!this.respectMaybeMoreData || maybeMoreDataSupplier
/* 143 */         .get()) && this.totalMessages < this.maxMessagePerRead && this.totalBytesRead > 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void readComplete() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public int attemptedBytesRead() {
/* 154 */       return this.attemptedBytesRead;
/*     */     }
/*     */ 
/*     */     
/*     */     public void attemptedBytesRead(int bytes) {
/* 159 */       this.attemptedBytesRead = bytes;
/*     */     }
/*     */     
/*     */     protected final int totalBytesRead() {
/* 163 */       return (this.totalBytesRead < 0) ? Integer.MAX_VALUE : this.totalBytesRead;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\DefaultMaxMessagesRecvByteBufAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */