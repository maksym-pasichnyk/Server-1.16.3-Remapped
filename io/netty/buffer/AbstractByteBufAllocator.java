/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.ResourceLeakDetector;
/*     */ import io.netty.util.ResourceLeakTracker;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public abstract class AbstractByteBufAllocator
/*     */   implements ByteBufAllocator
/*     */ {
/*     */   static final int DEFAULT_INITIAL_CAPACITY = 256;
/*     */   static final int DEFAULT_MAX_CAPACITY = 2147483647;
/*     */   static final int DEFAULT_MAX_COMPONENTS = 16;
/*     */   static final int CALCULATE_THRESHOLD = 4194304;
/*     */   private final boolean directByDefault;
/*     */   private final ByteBuf emptyBuf;
/*     */   
/*     */   static {
/*  34 */     ResourceLeakDetector.addExclusions(AbstractByteBufAllocator.class, new String[] { "toLeakAwareBuffer" });
/*     */   }
/*     */   
/*     */   protected static ByteBuf toLeakAwareBuffer(ByteBuf buf) {
/*     */     ResourceLeakTracker<ByteBuf> leak;
/*  39 */     switch (ResourceLeakDetector.getLevel()) {
/*     */       case SIMPLE:
/*  41 */         leak = AbstractByteBuf.leakDetector.track(buf);
/*  42 */         if (leak != null) {
/*  43 */           buf = new SimpleLeakAwareByteBuf(buf, leak);
/*     */         }
/*     */         break;
/*     */       case ADVANCED:
/*     */       case PARANOID:
/*  48 */         leak = AbstractByteBuf.leakDetector.track(buf);
/*  49 */         if (leak != null) {
/*  50 */           buf = new AdvancedLeakAwareByteBuf(buf, leak);
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/*  56 */     return buf;
/*     */   }
/*     */   
/*     */   protected static CompositeByteBuf toLeakAwareBuffer(CompositeByteBuf buf) {
/*     */     ResourceLeakTracker<ByteBuf> leak;
/*  61 */     switch (ResourceLeakDetector.getLevel()) {
/*     */       case SIMPLE:
/*  63 */         leak = AbstractByteBuf.leakDetector.track(buf);
/*  64 */         if (leak != null) {
/*  65 */           buf = new SimpleLeakAwareCompositeByteBuf(buf, leak);
/*     */         }
/*     */         break;
/*     */       case ADVANCED:
/*     */       case PARANOID:
/*  70 */         leak = AbstractByteBuf.leakDetector.track(buf);
/*  71 */         if (leak != null) {
/*  72 */           buf = new AdvancedLeakAwareCompositeByteBuf(buf, leak);
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/*  78 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractByteBufAllocator() {
/*  88 */     this(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractByteBufAllocator(boolean preferDirect) {
/*  98 */     this.directByDefault = (preferDirect && PlatformDependent.hasUnsafe());
/*  99 */     this.emptyBuf = new EmptyByteBuf(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf buffer() {
/* 104 */     if (this.directByDefault) {
/* 105 */       return directBuffer();
/*     */     }
/* 107 */     return heapBuffer();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf buffer(int initialCapacity) {
/* 112 */     if (this.directByDefault) {
/* 113 */       return directBuffer(initialCapacity);
/*     */     }
/* 115 */     return heapBuffer(initialCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf buffer(int initialCapacity, int maxCapacity) {
/* 120 */     if (this.directByDefault) {
/* 121 */       return directBuffer(initialCapacity, maxCapacity);
/*     */     }
/* 123 */     return heapBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf ioBuffer() {
/* 128 */     if (PlatformDependent.hasUnsafe()) {
/* 129 */       return directBuffer(256);
/*     */     }
/* 131 */     return heapBuffer(256);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf ioBuffer(int initialCapacity) {
/* 136 */     if (PlatformDependent.hasUnsafe()) {
/* 137 */       return directBuffer(initialCapacity);
/*     */     }
/* 139 */     return heapBuffer(initialCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf ioBuffer(int initialCapacity, int maxCapacity) {
/* 144 */     if (PlatformDependent.hasUnsafe()) {
/* 145 */       return directBuffer(initialCapacity, maxCapacity);
/*     */     }
/* 147 */     return heapBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf heapBuffer() {
/* 152 */     return heapBuffer(256, 2147483647);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf heapBuffer(int initialCapacity) {
/* 157 */     return heapBuffer(initialCapacity, 2147483647);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf heapBuffer(int initialCapacity, int maxCapacity) {
/* 162 */     if (initialCapacity == 0 && maxCapacity == 0) {
/* 163 */       return this.emptyBuf;
/*     */     }
/* 165 */     validate(initialCapacity, maxCapacity);
/* 166 */     return newHeapBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf directBuffer() {
/* 171 */     return directBuffer(256, 2147483647);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf directBuffer(int initialCapacity) {
/* 176 */     return directBuffer(initialCapacity, 2147483647);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf directBuffer(int initialCapacity, int maxCapacity) {
/* 181 */     if (initialCapacity == 0 && maxCapacity == 0) {
/* 182 */       return this.emptyBuf;
/*     */     }
/* 184 */     validate(initialCapacity, maxCapacity);
/* 185 */     return newDirectBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompositeByteBuf compositeBuffer() {
/* 190 */     if (this.directByDefault) {
/* 191 */       return compositeDirectBuffer();
/*     */     }
/* 193 */     return compositeHeapBuffer();
/*     */   }
/*     */ 
/*     */   
/*     */   public CompositeByteBuf compositeBuffer(int maxNumComponents) {
/* 198 */     if (this.directByDefault) {
/* 199 */       return compositeDirectBuffer(maxNumComponents);
/*     */     }
/* 201 */     return compositeHeapBuffer(maxNumComponents);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompositeByteBuf compositeHeapBuffer() {
/* 206 */     return compositeHeapBuffer(16);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompositeByteBuf compositeHeapBuffer(int maxNumComponents) {
/* 211 */     return toLeakAwareBuffer(new CompositeByteBuf(this, false, maxNumComponents));
/*     */   }
/*     */ 
/*     */   
/*     */   public CompositeByteBuf compositeDirectBuffer() {
/* 216 */     return compositeDirectBuffer(16);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompositeByteBuf compositeDirectBuffer(int maxNumComponents) {
/* 221 */     return toLeakAwareBuffer(new CompositeByteBuf(this, true, maxNumComponents));
/*     */   }
/*     */   
/*     */   private static void validate(int initialCapacity, int maxCapacity) {
/* 225 */     if (initialCapacity < 0) {
/* 226 */       throw new IllegalArgumentException("initialCapacity: " + initialCapacity + " (expected: 0+)");
/*     */     }
/* 228 */     if (initialCapacity > maxCapacity) {
/* 229 */       throw new IllegalArgumentException(String.format("initialCapacity: %d (expected: not greater than maxCapacity(%d)", new Object[] {
/*     */               
/* 231 */               Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity)
/*     */             }));
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
/*     */   
/*     */   public String toString() {
/* 247 */     return StringUtil.simpleClassName(this) + "(directByDefault: " + this.directByDefault + ')';
/*     */   }
/*     */ 
/*     */   
/*     */   public int calculateNewCapacity(int minNewCapacity, int maxCapacity) {
/* 252 */     if (minNewCapacity < 0) {
/* 253 */       throw new IllegalArgumentException("minNewCapacity: " + minNewCapacity + " (expected: 0+)");
/*     */     }
/* 255 */     if (minNewCapacity > maxCapacity)
/* 256 */       throw new IllegalArgumentException(String.format("minNewCapacity: %d (expected: not greater than maxCapacity(%d)", new Object[] {
/*     */               
/* 258 */               Integer.valueOf(minNewCapacity), Integer.valueOf(maxCapacity)
/*     */             })); 
/* 260 */     int threshold = 4194304;
/*     */     
/* 262 */     if (minNewCapacity == 4194304) {
/* 263 */       return 4194304;
/*     */     }
/*     */ 
/*     */     
/* 267 */     if (minNewCapacity > 4194304) {
/* 268 */       int i = minNewCapacity / 4194304 * 4194304;
/* 269 */       if (i > maxCapacity - 4194304) {
/* 270 */         i = maxCapacity;
/*     */       } else {
/* 272 */         i += 4194304;
/*     */       } 
/* 274 */       return i;
/*     */     } 
/*     */ 
/*     */     
/* 278 */     int newCapacity = 64;
/* 279 */     while (newCapacity < minNewCapacity) {
/* 280 */       newCapacity <<= 1;
/*     */     }
/*     */     
/* 283 */     return Math.min(newCapacity, maxCapacity);
/*     */   }
/*     */   
/*     */   protected abstract ByteBuf newHeapBuffer(int paramInt1, int paramInt2);
/*     */   
/*     */   protected abstract ByteBuf newDirectBuffer(int paramInt1, int paramInt2);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\AbstractByteBufAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */