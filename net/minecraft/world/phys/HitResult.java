/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ public abstract class HitResult {
/*    */   protected final Vec3 location;
/*    */   
/*    */   public enum Type {
/*  7 */     MISS, BLOCK, ENTITY;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected HitResult(Vec3 debug1) {
/* 13 */     this.location = debug1;
/*    */   }
/*    */   
/*    */   public double distanceTo(Entity debug1) {
/* 17 */     double debug2 = this.location.x - debug1.getX();
/* 18 */     double debug4 = this.location.y - debug1.getY();
/* 19 */     double debug6 = this.location.z - debug1.getZ();
/* 20 */     return debug2 * debug2 + debug4 * debug4 + debug6 * debug6;
/*    */   }
/*    */   
/*    */   public abstract Type getType();
/*    */   
/*    */   public Vec3 getLocation() {
/* 26 */     return this.location;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\HitResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */