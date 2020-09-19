/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EntityDimensions {
/*    */   public final float width;
/*    */   public final float height;
/*    */   public final boolean fixed;
/*    */   
/*    */   public EntityDimensions(float debug1, float debug2, boolean debug3) {
/* 12 */     this.width = debug1;
/* 13 */     this.height = debug2;
/* 14 */     this.fixed = debug3;
/*    */   }
/*    */   
/*    */   public AABB makeBoundingBox(Vec3 debug1) {
/* 18 */     return makeBoundingBox(debug1.x, debug1.y, debug1.z);
/*    */   }
/*    */   
/*    */   public AABB makeBoundingBox(double debug1, double debug3, double debug5) {
/* 22 */     float debug7 = this.width / 2.0F;
/* 23 */     float debug8 = this.height;
/* 24 */     return new AABB(debug1 - debug7, debug3, debug5 - debug7, debug1 + debug7, debug3 + debug8, debug5 + debug7);
/*    */   }
/*    */   
/*    */   public EntityDimensions scale(float debug1) {
/* 28 */     return scale(debug1, debug1);
/*    */   }
/*    */   
/*    */   public EntityDimensions scale(float debug1, float debug2) {
/* 32 */     if (this.fixed || (debug1 == 1.0F && debug2 == 1.0F)) {
/* 33 */       return this;
/*    */     }
/* 35 */     return scalable(this.width * debug1, this.height * debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public static EntityDimensions scalable(float debug0, float debug1) {
/* 40 */     return new EntityDimensions(debug0, debug1, false);
/*    */   }
/*    */   
/*    */   public static EntityDimensions fixed(float debug0, float debug1) {
/* 44 */     return new EntityDimensions(debug0, debug1, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     return "EntityDimensions w=" + this.width + ", h=" + this.height + ", fixed=" + this.fixed;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\EntityDimensions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */