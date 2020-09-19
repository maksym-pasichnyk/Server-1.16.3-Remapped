/*    */ package net.minecraft.world.entity.animal;
/*    */ 
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.TamableAnimal;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class ShoulderRidingEntity
/*    */   extends TamableAnimal
/*    */ {
/*    */   private int rideCooldownCounter;
/*    */   
/*    */   protected ShoulderRidingEntity(EntityType<? extends ShoulderRidingEntity> debug1, Level debug2) {
/* 15 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   public boolean setEntityOnShoulder(ServerPlayer debug1) {
/* 19 */     CompoundTag debug2 = new CompoundTag();
/* 20 */     debug2.putString("id", getEncodeId());
/* 21 */     saveWithoutId(debug2);
/*    */     
/* 23 */     if (debug1.setEntityOnShoulder(debug2)) {
/* 24 */       remove();
/* 25 */       return true;
/*    */     } 
/*    */     
/* 28 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 33 */     this.rideCooldownCounter++;
/* 34 */     super.tick();
/*    */   }
/*    */   
/*    */   public boolean canSitOnShoulder() {
/* 38 */     return (this.rideCooldownCounter > 100);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\ShoulderRidingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */