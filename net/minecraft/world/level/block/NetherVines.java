/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NetherVines
/*    */ {
/*    */   public static boolean isValidGrowthState(BlockState debug0) {
/* 12 */     return debug0.isAir();
/*    */   }
/*    */   
/*    */   public static int getBlocksToGrowWhenBonemealed(Random debug0) {
/* 16 */     double debug1 = 1.0D;
/* 17 */     int debug3 = 0;
/* 18 */     while (debug0.nextDouble() < debug1) {
/* 19 */       debug1 *= 0.826D;
/* 20 */       debug3++;
/*    */     } 
/* 22 */     return debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\NetherVines.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */