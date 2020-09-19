/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import com.google.common.math.IntMath;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public final class DiscreteCubeMerger implements IndexMerger {
/*    */   private final CubePointRange result;
/*    */   private final int firstSize;
/*    */   private final int secondSize;
/*    */   private final int gcd;
/*    */   
/*    */   DiscreteCubeMerger(int debug1, int debug2) {
/* 13 */     this.result = new CubePointRange((int)Shapes.lcm(debug1, debug2));
/* 14 */     this.firstSize = debug1;
/* 15 */     this.secondSize = debug2;
/* 16 */     this.gcd = IntMath.gcd(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer debug1) {
/* 21 */     int debug2 = this.firstSize / this.gcd;
/* 22 */     int debug3 = this.secondSize / this.gcd;
/* 23 */     for (int debug4 = 0; debug4 <= this.result.size(); debug4++) {
/* 24 */       if (!debug1.merge(debug4 / debug3, debug4 / debug2, debug4)) {
/* 25 */         return false;
/*    */       }
/*    */     } 
/* 28 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public DoubleList getList() {
/* 33 */     return (DoubleList)this.result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\DiscreteCubeMerger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */