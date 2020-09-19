/*     */ package io.netty.channel;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class AdaptiveRecvByteBufAllocator
/*     */   extends DefaultMaxMessagesRecvByteBufAllocator
/*     */ {
/*     */   static final int DEFAULT_MINIMUM = 64;
/*     */   static final int DEFAULT_INITIAL = 1024;
/*     */   static final int DEFAULT_MAXIMUM = 65536;
/*     */   private static final int INDEX_INCREMENT = 4;
/*     */   private static final int INDEX_DECREMENT = 1;
/*     */   private static final int[] SIZE_TABLE;
/*     */   
/*     */   static {
/*  46 */     List<Integer> sizeTable = new ArrayList<Integer>(); int i;
/*  47 */     for (i = 16; i < 512; i += 16) {
/*  48 */       sizeTable.add(Integer.valueOf(i));
/*     */     }
/*     */     
/*  51 */     for (i = 512; i > 0; i <<= 1) {
/*  52 */       sizeTable.add(Integer.valueOf(i));
/*     */     }
/*     */     
/*  55 */     SIZE_TABLE = new int[sizeTable.size()];
/*  56 */     for (i = 0; i < SIZE_TABLE.length; i++) {
/*  57 */       SIZE_TABLE[i] = ((Integer)sizeTable.get(i)).intValue();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  65 */   public static final AdaptiveRecvByteBufAllocator DEFAULT = new AdaptiveRecvByteBufAllocator(); private final int minIndex; private final int maxIndex; private final int initial;
/*     */   
/*     */   private static int getSizeTableIndex(int size) {
/*  68 */     int mid, a, low = 0, high = SIZE_TABLE.length - 1; while (true) {
/*  69 */       if (high < low) {
/*  70 */         return low;
/*     */       }
/*  72 */       if (high == low) {
/*  73 */         return high;
/*     */       }
/*     */       
/*  76 */       mid = low + high >>> 1;
/*  77 */       a = SIZE_TABLE[mid];
/*  78 */       int b = SIZE_TABLE[mid + 1];
/*  79 */       if (size > b) {
/*  80 */         low = mid + 1; continue;
/*  81 */       }  if (size < a)
/*  82 */       { high = mid - 1; continue; }  break;
/*  83 */     }  if (size == a) {
/*  84 */       return mid;
/*     */     }
/*  86 */     return mid + 1;
/*     */   }
/*     */   
/*     */   private final class HandleImpl
/*     */     extends DefaultMaxMessagesRecvByteBufAllocator.MaxMessageHandle
/*     */   {
/*     */     private final int minIndex;
/*     */     private final int maxIndex;
/*     */     private int index;
/*     */     private int nextReceiveBufferSize;
/*     */     private boolean decreaseNow;
/*     */     
/*     */     public HandleImpl(int minIndex, int maxIndex, int initial) {
/*  99 */       this.minIndex = minIndex;
/* 100 */       this.maxIndex = maxIndex;
/*     */       
/* 102 */       this.index = AdaptiveRecvByteBufAllocator.getSizeTableIndex(initial);
/* 103 */       this.nextReceiveBufferSize = AdaptiveRecvByteBufAllocator.SIZE_TABLE[this.index];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void lastBytesRead(int bytes) {
/* 112 */       if (bytes == attemptedBytesRead()) {
/* 113 */         record(bytes);
/*     */       }
/* 115 */       super.lastBytesRead(bytes);
/*     */     }
/*     */ 
/*     */     
/*     */     public int guess() {
/* 120 */       return this.nextReceiveBufferSize;
/*     */     }
/*     */     
/*     */     private void record(int actualReadBytes) {
/* 124 */       if (actualReadBytes <= AdaptiveRecvByteBufAllocator.SIZE_TABLE[Math.max(0, this.index - 1 - 1)]) {
/* 125 */         if (this.decreaseNow) {
/* 126 */           this.index = Math.max(this.index - 1, this.minIndex);
/* 127 */           this.nextReceiveBufferSize = AdaptiveRecvByteBufAllocator.SIZE_TABLE[this.index];
/* 128 */           this.decreaseNow = false;
/*     */         } else {
/* 130 */           this.decreaseNow = true;
/*     */         } 
/* 132 */       } else if (actualReadBytes >= this.nextReceiveBufferSize) {
/* 133 */         this.index = Math.min(this.index + 4, this.maxIndex);
/* 134 */         this.nextReceiveBufferSize = AdaptiveRecvByteBufAllocator.SIZE_TABLE[this.index];
/* 135 */         this.decreaseNow = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void readComplete() {
/* 141 */       record(totalBytesRead());
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
/*     */   
/*     */   public AdaptiveRecvByteBufAllocator() {
/* 155 */     this(64, 1024, 65536);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AdaptiveRecvByteBufAllocator(int minimum, int initial, int maximum) {
/* 166 */     if (minimum <= 0) {
/* 167 */       throw new IllegalArgumentException("minimum: " + minimum);
/*     */     }
/* 169 */     if (initial < minimum) {
/* 170 */       throw new IllegalArgumentException("initial: " + initial);
/*     */     }
/* 172 */     if (maximum < initial) {
/* 173 */       throw new IllegalArgumentException("maximum: " + maximum);
/*     */     }
/*     */     
/* 176 */     int minIndex = getSizeTableIndex(minimum);
/* 177 */     if (SIZE_TABLE[minIndex] < minimum) {
/* 178 */       this.minIndex = minIndex + 1;
/*     */     } else {
/* 180 */       this.minIndex = minIndex;
/*     */     } 
/*     */     
/* 183 */     int maxIndex = getSizeTableIndex(maximum);
/* 184 */     if (SIZE_TABLE[maxIndex] > maximum) {
/* 185 */       this.maxIndex = maxIndex - 1;
/*     */     } else {
/* 187 */       this.maxIndex = maxIndex;
/*     */     } 
/*     */     
/* 190 */     this.initial = initial;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RecvByteBufAllocator.Handle newHandle() {
/* 196 */     return new HandleImpl(this.minIndex, this.maxIndex, this.initial);
/*     */   }
/*     */ 
/*     */   
/*     */   public AdaptiveRecvByteBufAllocator respectMaybeMoreData(boolean respectMaybeMoreData) {
/* 201 */     super.respectMaybeMoreData(respectMaybeMoreData);
/* 202 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\AdaptiveRecvByteBufAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */