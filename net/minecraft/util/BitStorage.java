/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.function.IntConsumer;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BitStorage
/*     */ {
/*  13 */   private static final int[] MAGIC = new int[] { -1, -1, 0, Integer.MIN_VALUE, 0, 0, 1431655765, 1431655765, 0, Integer.MIN_VALUE, 0, 1, 858993459, 858993459, 0, 715827882, 715827882, 0, 613566756, 613566756, 0, Integer.MIN_VALUE, 0, 2, 477218588, 477218588, 0, 429496729, 429496729, 0, 390451572, 390451572, 0, 357913941, 357913941, 0, 330382099, 330382099, 0, 306783378, 306783378, 0, 286331153, 286331153, 0, Integer.MIN_VALUE, 0, 3, 252645135, 252645135, 0, 238609294, 238609294, 0, 226050910, 226050910, 0, 214748364, 214748364, 0, 204522252, 204522252, 0, 195225786, 195225786, 0, 186737708, 186737708, 0, 178956970, 178956970, 0, 171798691, 171798691, 0, 165191049, 165191049, 0, 159072862, 159072862, 0, 153391689, 153391689, 0, 148102320, 148102320, 0, 143165576, 143165576, 0, 138547332, 138547332, 0, Integer.MIN_VALUE, 0, 4, 130150524, 130150524, 0, 126322567, 126322567, 0, 122713351, 122713351, 0, 119304647, 119304647, 0, 116080197, 116080197, 0, 113025455, 113025455, 0, 110127366, 110127366, 0, 107374182, 107374182, 0, 104755299, 104755299, 0, 102261126, 102261126, 0, 99882960, 99882960, 0, 97612893, 97612893, 0, 95443717, 95443717, 0, 93368854, 93368854, 0, 91382282, 91382282, 0, 89478485, 89478485, 0, 87652393, 87652393, 0, 85899345, 85899345, 0, 84215045, 84215045, 0, 82595524, 82595524, 0, 81037118, 81037118, 0, 79536431, 79536431, 0, 78090314, 78090314, 0, 76695844, 76695844, 0, 75350303, 75350303, 0, 74051160, 74051160, 0, 72796055, 72796055, 0, 71582788, 71582788, 0, 70409299, 70409299, 0, 69273666, 69273666, 0, 68174084, 68174084, 0, Integer.MIN_VALUE, 0, 5 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long[] data;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int bits;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long mask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int valuesPerLong;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int divideMul;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int divideAdd;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int divideShift;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitStorage(int debug1, int debug2) {
/*  92 */     this(debug1, debug2, null);
/*     */   }
/*     */   
/*     */   public BitStorage(int debug1, int debug2, @Nullable long[] debug3) {
/*  96 */     Validate.inclusiveBetween(1L, 32L, debug1);
/*     */     
/*  98 */     this.size = debug2;
/*  99 */     this.bits = debug1;
/* 100 */     this.mask = (1L << debug1) - 1L;
/* 101 */     this.valuesPerLong = (char)(64 / debug1);
/*     */     
/* 103 */     int debug4 = 3 * (this.valuesPerLong - 1);
/* 104 */     this.divideMul = MAGIC[debug4 + 0];
/* 105 */     this.divideAdd = MAGIC[debug4 + 1];
/* 106 */     this.divideShift = MAGIC[debug4 + 2];
/*     */     
/* 108 */     int debug5 = (debug2 + this.valuesPerLong - 1) / this.valuesPerLong;
/* 109 */     if (debug3 != null) {
/* 110 */       if (debug3.length != debug5) {
/* 111 */         throw (RuntimeException)Util.pauseInIde(new RuntimeException("Invalid length given for storage, got: " + debug3.length + " but expected: " + debug5));
/*     */       }
/* 113 */       this.data = debug3;
/*     */     } else {
/* 115 */       this.data = new long[debug5];
/*     */     } 
/*     */   }
/*     */   
/*     */   private int cellIndex(int debug1) {
/* 120 */     long debug2 = Integer.toUnsignedLong(this.divideMul);
/* 121 */     long debug4 = Integer.toUnsignedLong(this.divideAdd);
/* 122 */     return (int)(debug1 * debug2 + debug4 >> 32L >> this.divideShift);
/*     */   }
/*     */   
/*     */   public int getAndSet(int debug1, int debug2) {
/* 126 */     Validate.inclusiveBetween(0L, (this.size - 1), debug1);
/* 127 */     Validate.inclusiveBetween(0L, this.mask, debug2);
/*     */     
/* 129 */     int debug3 = cellIndex(debug1);
/* 130 */     long debug4 = this.data[debug3];
/* 131 */     int debug6 = (debug1 - debug3 * this.valuesPerLong) * this.bits;
/*     */     
/* 133 */     int debug7 = (int)(debug4 >> debug6 & this.mask);
/* 134 */     this.data[debug3] = debug4 & (this.mask << debug6 ^ 0xFFFFFFFFFFFFFFFFL) | (debug2 & this.mask) << debug6;
/*     */     
/* 136 */     return debug7;
/*     */   }
/*     */   
/*     */   public void set(int debug1, int debug2) {
/* 140 */     Validate.inclusiveBetween(0L, (this.size - 1), debug1);
/* 141 */     Validate.inclusiveBetween(0L, this.mask, debug2);
/*     */     
/* 143 */     int debug3 = cellIndex(debug1);
/* 144 */     long debug4 = this.data[debug3];
/* 145 */     int debug6 = (debug1 - debug3 * this.valuesPerLong) * this.bits;
/*     */     
/* 147 */     this.data[debug3] = debug4 & (this.mask << debug6 ^ 0xFFFFFFFFFFFFFFFFL) | (debug2 & this.mask) << debug6;
/*     */   }
/*     */   
/*     */   public int get(int debug1) {
/* 151 */     Validate.inclusiveBetween(0L, (this.size - 1), debug1);
/*     */     
/* 153 */     int debug2 = cellIndex(debug1);
/* 154 */     long debug3 = this.data[debug2];
/* 155 */     int debug5 = (debug1 - debug2 * this.valuesPerLong) * this.bits;
/*     */     
/* 157 */     return (int)(debug3 >> debug5 & this.mask);
/*     */   }
/*     */   
/*     */   public long[] getRaw() {
/* 161 */     return this.data;
/*     */   }
/*     */   
/*     */   public int getSize() {
/* 165 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getAll(IntConsumer debug1) {
/* 173 */     int debug2 = 0;
/* 174 */     for (long debug6 : this.data) {
/* 175 */       for (int debug8 = 0; debug8 < this.valuesPerLong; debug8++) {
/* 176 */         debug1.accept((int)(debug6 & this.mask));
/* 177 */         debug6 >>= this.bits;
/* 178 */         if (++debug2 >= this.size)
/*     */           return; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\BitStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */