/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public class IdenticalMerger implements IndexMerger {
/*    */   private final DoubleList coords;
/*    */   
/*    */   public IdenticalMerger(DoubleList debug1) {
/*  9 */     this.coords = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer debug1) {
/* 14 */     for (int debug2 = 0; debug2 <= this.coords.size(); debug2++) {
/* 15 */       if (!debug1.merge(debug2, debug2, debug2)) {
/* 16 */         return false;
/*    */       }
/*    */     } 
/* 19 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public DoubleList getList() {
/* 24 */     return this.coords;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\IdenticalMerger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */