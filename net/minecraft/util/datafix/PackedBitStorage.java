/*    */ package net.minecraft.util.datafix;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PackedBitStorage
/*    */ {
/*    */   private final long[] data;
/*    */   private final int bits;
/*    */   private final long mask;
/*    */   private final int size;
/*    */   
/*    */   public PackedBitStorage(int debug1, int debug2) {
/* 26 */     this(debug1, debug2, new long[Mth.roundUp(debug2 * debug1, 64) / 64]);
/*    */   }
/*    */   
/*    */   public PackedBitStorage(int debug1, int debug2, long[] debug3) {
/* 30 */     Validate.inclusiveBetween(1L, 32L, debug1);
/*    */     
/* 32 */     this.size = debug2;
/* 33 */     this.bits = debug1;
/* 34 */     this.data = debug3;
/* 35 */     this.mask = (1L << debug1) - 1L;
/*    */     
/* 37 */     int debug4 = Mth.roundUp(debug2 * debug1, 64) / 64;
/* 38 */     if (debug3.length != debug4) {
/* 39 */       throw new IllegalArgumentException("Invalid length given for storage, got: " + debug3.length + " but expected: " + debug4);
/*    */     }
/*    */   }
/*    */   
/*    */   public void set(int debug1, int debug2) {
/* 44 */     Validate.inclusiveBetween(0L, (this.size - 1), debug1);
/* 45 */     Validate.inclusiveBetween(0L, this.mask, debug2);
/*    */     
/* 47 */     int debug3 = debug1 * this.bits;
/* 48 */     int debug4 = debug3 >> 6;
/* 49 */     int debug5 = (debug1 + 1) * this.bits - 1 >> 6;
/* 50 */     int debug6 = debug3 ^ debug4 << 6;
/*    */     
/* 52 */     this.data[debug4] = this.data[debug4] & (this.mask << debug6 ^ 0xFFFFFFFFFFFFFFFFL) | (debug2 & this.mask) << debug6;
/* 53 */     if (debug4 != debug5) {
/* 54 */       int debug7 = 64 - debug6;
/* 55 */       int debug8 = this.bits - debug7;
/* 56 */       this.data[debug5] = this.data[debug5] >>> debug8 << debug8 | (debug2 & this.mask) >> debug7;
/*    */     } 
/*    */   }
/*    */   
/*    */   public int get(int debug1) {
/* 61 */     Validate.inclusiveBetween(0L, (this.size - 1), debug1);
/*    */     
/* 63 */     int debug2 = debug1 * this.bits;
/* 64 */     int debug3 = debug2 >> 6;
/* 65 */     int debug4 = (debug1 + 1) * this.bits - 1 >> 6;
/* 66 */     int debug5 = debug2 ^ debug3 << 6;
/*    */     
/* 68 */     if (debug3 == debug4) {
/* 69 */       return (int)(this.data[debug3] >>> debug5 & this.mask);
/*    */     }
/* 71 */     int debug6 = 64 - debug5;
/* 72 */     return (int)((this.data[debug3] >>> debug5 | this.data[debug4] << debug6) & this.mask);
/*    */   }
/*    */ 
/*    */   
/*    */   public long[] getRaw() {
/* 77 */     return this.data;
/*    */   }
/*    */   
/*    */   public int getBits() {
/* 81 */     return this.bits;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\PackedBitStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */