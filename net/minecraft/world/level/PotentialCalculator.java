/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ 
/*    */ public class PotentialCalculator
/*    */ {
/*    */   static class PointCharge {
/*    */     private final BlockPos pos;
/*    */     private final double charge;
/*    */     
/*    */     public PointCharge(BlockPos debug1, double debug2) {
/* 15 */       this.pos = debug1;
/* 16 */       this.charge = debug2;
/*    */     }
/*    */     
/*    */     public double getPotentialChange(BlockPos debug1) {
/* 20 */       double debug2 = this.pos.distSqr((Vec3i)debug1);
/* 21 */       if (debug2 == 0.0D)
/*    */       {
/* 23 */         return Double.POSITIVE_INFINITY;
/*    */       }
/* 25 */       return this.charge / Math.sqrt(debug2);
/*    */     }
/*    */   }
/*    */   
/* 29 */   private final List<PointCharge> charges = Lists.newArrayList();
/*    */   
/*    */   public void addCharge(BlockPos debug1, double debug2) {
/* 32 */     if (debug2 != 0.0D) {
/* 33 */       this.charges.add(new PointCharge(debug1, debug2));
/*    */     }
/*    */   }
/*    */   
/*    */   public double getPotentialEnergyChange(BlockPos debug1, double debug2) {
/* 38 */     if (debug2 == 0.0D) {
/* 39 */       return 0.0D;
/*    */     }
/* 41 */     double debug4 = 0.0D;
/* 42 */     for (PointCharge debug7 : this.charges) {
/* 43 */       debug4 += debug7.getPotentialChange(debug1);
/*    */     }
/* 45 */     return debug4 * debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\PotentialCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */