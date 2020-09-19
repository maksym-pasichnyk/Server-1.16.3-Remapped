/*    */ package net.minecraft.world.level.portal;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class PortalInfo {
/*    */   public final Vec3 pos;
/*    */   public final Vec3 speed;
/*    */   public final float yRot;
/*    */   public final float xRot;
/*    */   
/*    */   public PortalInfo(Vec3 debug1, Vec3 debug2, float debug3, float debug4) {
/* 12 */     this.pos = debug1;
/* 13 */     this.speed = debug2;
/* 14 */     this.yRot = debug3;
/* 15 */     this.xRot = debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\portal\PortalInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */