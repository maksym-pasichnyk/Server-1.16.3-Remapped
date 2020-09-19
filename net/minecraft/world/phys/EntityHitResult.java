/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class EntityHitResult extends HitResult {
/*    */   private final Entity entity;
/*    */   
/*    */   public EntityHitResult(Entity debug1) {
/*  9 */     this(debug1, debug1.position());
/*    */   }
/*    */   
/*    */   public EntityHitResult(Entity debug1, Vec3 debug2) {
/* 13 */     super(debug2);
/*    */     
/* 15 */     this.entity = debug1;
/*    */   }
/*    */   
/*    */   public Entity getEntity() {
/* 19 */     return this.entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public HitResult.Type getType() {
/* 24 */     return HitResult.Type.ENTITY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\EntityHitResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */