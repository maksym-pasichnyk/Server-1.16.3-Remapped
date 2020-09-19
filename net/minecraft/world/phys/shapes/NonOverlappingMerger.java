/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public class NonOverlappingMerger extends AbstractDoubleList implements IndexMerger {
/*    */   private final DoubleList lower;
/*    */   private final DoubleList upper;
/*    */   private final boolean swap;
/*    */   
/*    */   public NonOverlappingMerger(DoubleList debug1, DoubleList debug2, boolean debug3) {
/* 12 */     this.lower = debug1;
/* 13 */     this.upper = debug2;
/* 14 */     this.swap = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 19 */     return this.lower.size() + this.upper.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer debug1) {
/* 24 */     if (this.swap) {
/* 25 */       return forNonSwappedIndexes((debug1, debug2, debug3) -> debug0.merge(debug2, debug1, debug3));
/*    */     }
/* 27 */     return forNonSwappedIndexes(debug1);
/*    */   }
/*    */   
/*    */   private boolean forNonSwappedIndexes(IndexMerger.IndexConsumer debug1) {
/* 31 */     int debug2 = this.lower.size() - 1; int debug3;
/* 32 */     for (debug3 = 0; debug3 < debug2; debug3++) {
/* 33 */       if (!debug1.merge(debug3, -1, debug3)) {
/* 34 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 38 */     if (!debug1.merge(debug2, -1, debug2)) {
/* 39 */       return false;
/*    */     }
/*    */     
/* 42 */     for (debug3 = 0; debug3 < this.upper.size(); debug3++) {
/* 43 */       if (!debug1.merge(debug2, debug3, debug2 + 1 + debug3)) {
/* 44 */         return false;
/*    */       }
/*    */     } 
/* 47 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getDouble(int debug1) {
/* 52 */     if (debug1 < this.lower.size()) {
/* 53 */       return this.lower.getDouble(debug1);
/*    */     }
/* 55 */     return this.upper.getDouble(debug1 - this.lower.size());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public DoubleList getList() {
/* 61 */     return (DoubleList)this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\NonOverlappingMerger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */