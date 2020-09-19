/*    */ package net.minecraft.world.level.block.piston;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PistonMath
/*    */ {
/*    */   public static AABB getMovementArea(AABB debug0, Direction debug1, double debug2) {
/* 15 */     double debug4 = debug2 * debug1.getAxisDirection().getStep();
/* 16 */     double debug6 = Math.min(debug4, 0.0D);
/* 17 */     double debug8 = Math.max(debug4, 0.0D);
/* 18 */     switch (debug1)
/*    */     { case WEST:
/* 20 */         return new AABB(debug0.minX + debug6, debug0.minY, debug0.minZ, debug0.minX + debug8, debug0.maxY, debug0.maxZ);
/*    */       case EAST:
/* 22 */         return new AABB(debug0.maxX + debug6, debug0.minY, debug0.minZ, debug0.maxX + debug8, debug0.maxY, debug0.maxZ);
/*    */       case DOWN:
/* 24 */         return new AABB(debug0.minX, debug0.minY + debug6, debug0.minZ, debug0.maxX, debug0.minY + debug8, debug0.maxZ);
/*    */       
/*    */       default:
/* 27 */         return new AABB(debug0.minX, debug0.maxY + debug6, debug0.minZ, debug0.maxX, debug0.maxY + debug8, debug0.maxZ);
/*    */       case NORTH:
/* 29 */         return new AABB(debug0.minX, debug0.minY, debug0.minZ + debug6, debug0.maxX, debug0.maxY, debug0.minZ + debug8);
/*    */       case SOUTH:
/* 31 */         break; }  return new AABB(debug0.minX, debug0.minY, debug0.maxZ + debug6, debug0.maxX, debug0.maxY, debug0.maxZ + debug8);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\piston\PistonMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */