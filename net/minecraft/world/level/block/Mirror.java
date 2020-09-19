/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.math.OctahedralGroup;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public enum Mirror
/*    */ {
/*  8 */   NONE(OctahedralGroup.IDENTITY),
/*  9 */   LEFT_RIGHT(OctahedralGroup.INVERT_Z),
/* 10 */   FRONT_BACK(OctahedralGroup.INVERT_X);
/*    */   
/*    */   private final OctahedralGroup rotation;
/*    */ 
/*    */   
/*    */   Mirror(OctahedralGroup debug3) {
/* 16 */     this.rotation = debug3;
/*    */   }
/*    */   
/*    */   public int mirror(int debug1, int debug2) {
/* 20 */     int debug3 = debug2 / 2;
/* 21 */     int debug4 = (debug1 > debug3) ? (debug1 - debug2) : debug1;
/* 22 */     switch (this) {
/*    */       case FRONT_BACK:
/* 24 */         return (debug2 - debug4) % debug2;
/*    */       case LEFT_RIGHT:
/* 26 */         return (debug3 - debug4 + debug2) % debug2;
/*    */     } 
/* 28 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public Rotation getRotation(Direction debug1) {
/* 33 */     Direction.Axis debug2 = debug1.getAxis();
/* 34 */     return ((this == LEFT_RIGHT && debug2 == Direction.Axis.Z) || (this == FRONT_BACK && debug2 == Direction.Axis.X)) ? Rotation.CLOCKWISE_180 : Rotation.NONE;
/*    */   }
/*    */   
/*    */   public Direction mirror(Direction debug1) {
/* 38 */     if (this == FRONT_BACK && debug1.getAxis() == Direction.Axis.X) {
/* 39 */       return debug1.getOpposite();
/*    */     }
/* 41 */     if (this == LEFT_RIGHT && debug1.getAxis() == Direction.Axis.Z) {
/* 42 */       return debug1.getOpposite();
/*    */     }
/* 44 */     return debug1;
/*    */   }
/*    */   
/*    */   public OctahedralGroup rotation() {
/* 48 */     return this.rotation;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\Mirror.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */