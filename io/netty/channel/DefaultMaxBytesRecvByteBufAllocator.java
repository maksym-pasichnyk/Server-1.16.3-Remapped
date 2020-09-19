/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.util.UncheckedBooleanSupplier;
/*     */ import java.util.AbstractMap;
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
/*     */ public class DefaultMaxBytesRecvByteBufAllocator
/*     */   implements MaxBytesRecvByteBufAllocator
/*     */ {
/*     */   private volatile int maxBytesPerRead;
/*     */   private volatile int maxBytesPerIndividualRead;
/*     */   
/*     */   private final class HandleImpl
/*     */     implements RecvByteBufAllocator.ExtendedHandle
/*     */   {
/*     */     private int individualReadMax;
/*     */     private int bytesToRead;
/*     */     
/*  38 */     private final UncheckedBooleanSupplier defaultMaybeMoreSupplier = new UncheckedBooleanSupplier()
/*     */       {
/*     */         public boolean get() {
/*  41 */           return (DefaultMaxBytesRecvByteBufAllocator.HandleImpl.this.attemptBytesRead == DefaultMaxBytesRecvByteBufAllocator.HandleImpl.this.lastBytesRead);
/*     */         }
/*     */       };
/*     */     private int lastBytesRead; private int attemptBytesRead;
/*     */     
/*     */     public ByteBuf allocate(ByteBufAllocator alloc) {
/*  47 */       return alloc.ioBuffer(guess());
/*     */     }
/*     */ 
/*     */     
/*     */     public int guess() {
/*  52 */       return Math.min(this.individualReadMax, this.bytesToRead);
/*     */     }
/*     */ 
/*     */     
/*     */     public void reset(ChannelConfig config) {
/*  57 */       this.bytesToRead = DefaultMaxBytesRecvByteBufAllocator.this.maxBytesPerRead();
/*  58 */       this.individualReadMax = DefaultMaxBytesRecvByteBufAllocator.this.maxBytesPerIndividualRead();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void incMessagesRead(int amt) {}
/*     */ 
/*     */     
/*     */     public void lastBytesRead(int bytes) {
/*  67 */       this.lastBytesRead = bytes;
/*     */ 
/*     */       
/*  70 */       this.bytesToRead -= bytes;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastBytesRead() {
/*  75 */       return this.lastBytesRead;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean continueReading() {
/*  80 */       return continueReading(this.defaultMaybeMoreSupplier);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean continueReading(UncheckedBooleanSupplier maybeMoreDataSupplier) {
/*  86 */       return (this.bytesToRead > 0 && maybeMoreDataSupplier.get());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void readComplete() {}
/*     */ 
/*     */     
/*     */     public void attemptedBytesRead(int bytes) {
/*  95 */       this.attemptBytesRead = bytes;
/*     */     }
/*     */     private HandleImpl() {}
/*     */     
/*     */     public int attemptedBytesRead() {
/* 100 */       return this.attemptBytesRead;
/*     */     }
/*     */   }
/*     */   
/*     */   public DefaultMaxBytesRecvByteBufAllocator() {
/* 105 */     this(65536, 65536);
/*     */   }
/*     */   
/*     */   public DefaultMaxBytesRecvByteBufAllocator(int maxBytesPerRead, int maxBytesPerIndividualRead) {
/* 109 */     checkMaxBytesPerReadPair(maxBytesPerRead, maxBytesPerIndividualRead);
/* 110 */     this.maxBytesPerRead = maxBytesPerRead;
/* 111 */     this.maxBytesPerIndividualRead = maxBytesPerIndividualRead;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RecvByteBufAllocator.Handle newHandle() {
/* 117 */     return new HandleImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public int maxBytesPerRead() {
/* 122 */     return this.maxBytesPerRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultMaxBytesRecvByteBufAllocator maxBytesPerRead(int maxBytesPerRead) {
/* 127 */     if (maxBytesPerRead <= 0) {
/* 128 */       throw new IllegalArgumentException("maxBytesPerRead: " + maxBytesPerRead + " (expected: > 0)");
/*     */     }
/*     */ 
/*     */     
/* 132 */     synchronized (this) {
/* 133 */       int maxBytesPerIndividualRead = maxBytesPerIndividualRead();
/* 134 */       if (maxBytesPerRead < maxBytesPerIndividualRead) {
/* 135 */         throw new IllegalArgumentException("maxBytesPerRead cannot be less than maxBytesPerIndividualRead (" + maxBytesPerIndividualRead + "): " + maxBytesPerRead);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 140 */       this.maxBytesPerRead = maxBytesPerRead;
/*     */     } 
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int maxBytesPerIndividualRead() {
/* 147 */     return this.maxBytesPerIndividualRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultMaxBytesRecvByteBufAllocator maxBytesPerIndividualRead(int maxBytesPerIndividualRead) {
/* 152 */     if (maxBytesPerIndividualRead <= 0) {
/* 153 */       throw new IllegalArgumentException("maxBytesPerIndividualRead: " + maxBytesPerIndividualRead + " (expected: > 0)");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 158 */     synchronized (this) {
/* 159 */       int maxBytesPerRead = maxBytesPerRead();
/* 160 */       if (maxBytesPerIndividualRead > maxBytesPerRead) {
/* 161 */         throw new IllegalArgumentException("maxBytesPerIndividualRead cannot be greater than maxBytesPerRead (" + maxBytesPerRead + "): " + maxBytesPerIndividualRead);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 166 */       this.maxBytesPerIndividualRead = maxBytesPerIndividualRead;
/*     */     } 
/* 168 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Map.Entry<Integer, Integer> maxBytesPerReadPair() {
/* 173 */     return new AbstractMap.SimpleEntry<Integer, Integer>(Integer.valueOf(this.maxBytesPerRead), Integer.valueOf(this.maxBytesPerIndividualRead));
/*     */   }
/*     */   
/*     */   private static void checkMaxBytesPerReadPair(int maxBytesPerRead, int maxBytesPerIndividualRead) {
/* 177 */     if (maxBytesPerRead <= 0) {
/* 178 */       throw new IllegalArgumentException("maxBytesPerRead: " + maxBytesPerRead + " (expected: > 0)");
/*     */     }
/* 180 */     if (maxBytesPerIndividualRead <= 0) {
/* 181 */       throw new IllegalArgumentException("maxBytesPerIndividualRead: " + maxBytesPerIndividualRead + " (expected: > 0)");
/*     */     }
/*     */     
/* 184 */     if (maxBytesPerRead < maxBytesPerIndividualRead) {
/* 185 */       throw new IllegalArgumentException("maxBytesPerRead cannot be less than maxBytesPerIndividualRead (" + maxBytesPerIndividualRead + "): " + maxBytesPerRead);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMaxBytesRecvByteBufAllocator maxBytesPerReadPair(int maxBytesPerRead, int maxBytesPerIndividualRead) {
/* 194 */     checkMaxBytesPerReadPair(maxBytesPerRead, maxBytesPerIndividualRead);
/*     */ 
/*     */     
/* 197 */     synchronized (this) {
/* 198 */       this.maxBytesPerRead = maxBytesPerRead;
/* 199 */       this.maxBytesPerIndividualRead = maxBytesPerIndividualRead;
/*     */     } 
/* 201 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\DefaultMaxBytesRecvByteBufAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */