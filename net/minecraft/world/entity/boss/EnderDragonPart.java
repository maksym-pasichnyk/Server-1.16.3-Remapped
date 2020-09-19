/*    */ package net.minecraft.world.entity.boss;
/*    */ 
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ 
/*    */ public class EnderDragonPart
/*    */   extends Entity {
/*    */   public final EnderDragon parentMob;
/*    */   public final String name;
/*    */   private final EntityDimensions size;
/*    */   
/*    */   public EnderDragonPart(EnderDragon debug1, String debug2, float debug3, float debug4) {
/* 18 */     super(debug1.getType(), debug1.level);
/* 19 */     this.size = EntityDimensions.scalable(debug3, debug4);
/* 20 */     refreshDimensions();
/* 21 */     this.parentMob = debug1;
/* 22 */     this.name = debug2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void defineSynchedData() {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(CompoundTag debug1) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(CompoundTag debug1) {}
/*    */ 
/*    */   
/*    */   public boolean isPickable() {
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hurt(DamageSource debug1, float debug2) {
/* 44 */     if (isInvulnerableTo(debug1)) {
/* 45 */       return false;
/*    */     }
/* 47 */     return this.parentMob.hurt(this, debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean is(Entity debug1) {
/* 52 */     return (this == debug1 || this.parentMob == debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet<?> getAddEntityPacket() {
/* 58 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityDimensions getDimensions(Pose debug1) {
/* 63 */     return this.size;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\EnderDragonPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */