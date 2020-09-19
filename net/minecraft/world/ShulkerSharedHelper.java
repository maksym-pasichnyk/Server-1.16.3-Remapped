/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ 
/*    */ 
/*    */ public class ShulkerSharedHelper
/*    */ {
/*    */   public static AABB openBoundingBox(BlockPos debug0, Direction debug1) {
/* 12 */     return Shapes.block().bounds().expandTowards((0.5F * debug1
/* 13 */         .getStepX()), (0.5F * debug1
/* 14 */         .getStepY()), (0.5F * debug1
/* 15 */         .getStepZ()))
/* 16 */       .contract(debug1
/* 17 */         .getStepX(), debug1
/* 18 */         .getStepY(), debug1
/* 19 */         .getStepZ())
/* 20 */       .move(debug0.relative(debug1));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\ShulkerSharedHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */